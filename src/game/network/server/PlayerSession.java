/**
 * Represents one authenticated player's server-side session.
 *
 * The session owns the player's controller and caches temporary generated
 * villages so the client may explore then choose one to attack.
 */
package game.network.server;

import game.combat.AttackResult;
import game.combat.VillageDefenseTestResult;
import game.controller.GameController;
import game.buildings.BuildingType;
import game.exceptions.GameException;
import game.inhabitants.InhabitantType;
import game.model.Army;
import game.model.ResourceType;
import game.model.Village;
import game.network.dto.ArmySummaryDto;
import game.network.dto.AttackResultDto;
import game.network.dto.VillageDefenseTestResultDto;
import game.network.dto.VillageOptionDto;
import game.network.dto.VillageStatusDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerSession {
    private final String username;
    private final GameController controller;
    private List<Village> exploredVillages;

    public PlayerSession(String username, Path savePath) throws IOException {
        this.username = username;
        this.controller = GameController.create(savePath, username);
        this.exploredVillages = new ArrayList<>();

        if (Files.exists(savePath)) {
            controller.loadGame();
        } else {
            controller.newGame();
        }
    }

    public String getUsername() {
        return username;
    }

    /**
     * Advances this player's game by one server-side tick.
     */
    public synchronized void advanceOneServerTick() {
        controller.advanceTime(1);
    }

    /**
     * Builds a DTO describing the current village status for the client.
     *
     * @return the village status DTO
     */
    public synchronized VillageStatusDto getVillageStatus() {
        Village village = controller.getPlayer().getVillage();
        Map<String, Integer> resources = new LinkedHashMap<>();
        for (ResourceType type : ResourceType.values()) {
            resources.put(type.name(), village.getResources().get(type));
        }

        List<String> buildings = new ArrayList<>();
        for (int i = 0; i < village.getBuildings().size(); i++) {
            buildings.add("[" + i + "] " + village.getBuildings().get(i));
        }

        List<String> inhabitants = new ArrayList<>();
        for (int i = 0; i < village.getInhabitants().size(); i++) {
            inhabitants.add("[" + i + "] " + village.getInhabitants().get(i));
        }

        return new VillageStatusDto(
                village.getName(),
                village.getTrophies(),
                village.getCurrentPopulation(),
                village.getPopulationCap(),
                village.isGuardActive(),
                village.getGuardRemainingHours(),
                resources,
                buildings,
                inhabitants,
                village.getArmy().size(),
                village.getArmy().calculateAttackScore(),
                village.calculateDefenseScore(),
                controller.getTime()
        );
    }

    /**
     * Generates compatible villages and caches them for a later attack choice.
     *
     * @return the generated village options
     */
    public synchronized List<VillageOptionDto> exploreVillages() {
        exploredVillages = controller.exploreVillages();
        List<VillageOptionDto> options = new ArrayList<>();
        for (int i = 0; i < exploredVillages.size(); i++) {
            Village village = exploredVillages.get(i);
            options.add(new VillageOptionDto(
                    i,
                    village.getName(),
                    village.calculateDefenseScore(),
                    village.getArmy().calculateAttackScore(),
                    village.getResources().snapshot().toString(),
                    village.isGuardActive()
            ));
        }
        return options;
    }

    /**
     * Attacks one of the previously explored villages.
     *
     * @param index the cached village option index
     * @return the serializable attack result
     * @throws GameException if the index is invalid or attack fails
     */
    public synchronized AttackResultDto attackExploredVillage(int index) throws GameException {
        if (index < 0 || index >= exploredVillages.size()) {
            throw new GameException("Invalid explored village index.");
        }
        AttackResult result = controller.attack(exploredVillages.get(index));
        return new AttackResultDto(
                result.isSuccess(),
                result.getLootGold(),
                result.getLootIron(),
                result.getLootWood(),
                result.getTrophiesChange()
        );
    }

    /**
     * Generates a detached army for testing the player's village.
     *
     * @return the generated army summary
     */
    public synchronized ArmySummaryDto generateTestingArmy() {
        Army army = controller.generateTestingArmy();
        List<String> units = new ArrayList<>();
        army.getUnits().forEach(unit -> units.add(unit.toString() + " DMG=" + unit.getAttackDamage() + " HP=" + unit.getHitPoints()));
        return new ArmySummaryDto(army.size(), army.calculateAttackScore(), units);
    }

    /**
     * Tests the village defense against generated compatible armies.
     *
     * @param rounds the number of test attacks to run
     * @return the summarized defense test result
     */
    public synchronized VillageDefenseTestResultDto testVillage(int rounds) {
        VillageDefenseTestResult result = controller.testVillageDefense(rounds);
        return new VillageDefenseTestResultDto(
                result.getTotalTests(),
                result.getSuccessfulDefenses(),
                result.getFailedDefenses(),
                result.getDefenseSuccessRate(),
                result.getRoundSummaries()
        );
    }

    /**
     * Saves the current server-side session.
     *
     * @throws IOException if the save file cannot be written
     */
    public synchronized void saveGame() throws IOException {
        controller.saveGame();
    }

    /**
     * Builds a requested structure for this player.
     *
     * @param type the building type to construct
     * @throws GameException if construction fails
     */
    public synchronized void build(BuildingType type) throws GameException {
        controller.build(type);
    }

    /**
     * Trains a requested inhabitant or army unit for this player.
     *
     * @param type the inhabitant type to train
     * @throws GameException if training fails
     */
    public synchronized void train(InhabitantType type) throws GameException {
        controller.train(type);
    }

    /**
     * Upgrades one building in the player's village.
     *
     * @param index the building index
     * @throws GameException if the upgrade fails
     */
    public synchronized void upgradeBuilding(int index) throws GameException {
        controller.upgradeBuilding(index);
    }

    /**
     * Upgrades one inhabitant in the player's village.
     *
     * @param index the inhabitant index
     * @throws GameException if the upgrade fails
     */
    public synchronized void upgradeInhabitant(int index) throws GameException {
        controller.upgradeInhabitant(index);
    }

    /**
     * Loads the saved server-side session from disk.
     *
     * @return true if a save file was loaded
     * @throws IOException if the save file cannot be read
     */
    public synchronized boolean loadGame() throws IOException {
        exploredVillages = new ArrayList<>();
        return controller.loadGame();
    }

    /**
     * Starts a fresh server-side session for this player.
     */
    public synchronized void newGame() {
        exploredVillages = new ArrayList<>();
        controller.newGame();
    }

    /**
     * Returns and clears pending game event messages.
     *
     * @return pending event messages
     */
    public synchronized List<String> consumeEvents() {
        return controller.consumeEvents();
    }

    /**
     * Advances this player's game by the requested number of hours.
     *
     * @param hours the number of hours to advance
     */
    public synchronized void advanceTime(int hours) {
        controller.advanceTime(hours);
    }
}
