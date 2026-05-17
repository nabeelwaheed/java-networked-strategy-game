/**
 * Controller layer for the Village War Strategy Game.
 *
 * GameController acts as the middle layer between the ConsoleUI
 * and the GameEngine. It exposes game actions to the view while
 * also coordinating persistence operations such as starting a new
 * game, loading a saved game, and saving the current session.
 */
package game.controller;

import game.adapter.AttackOutcomeCalculator;
import game.adapter.ChallengeDecisionAttackAdapter;
import game.buildings.BuildingType;
import game.combat.AttackResult;
import game.combat.VillageDefenseTestResult;
import game.engine.GameEngine;
import game.exceptions.GameException;
import game.factory.BuildingFactory;
import game.factory.DefaultBuildingFactory;
import game.factory.DefaultInhabitantFactory;
import game.factory.InhabitantFactory;
import game.inhabitants.InhabitantType;
import game.model.Army;
import game.model.Player;
import game.model.Village;
import game.persistence.GameState;
import game.persistence.GameStateRepository;
import game.persistence.XmlGameStateRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.util.List;

public class GameController {
    private GameEngine engine;
    private final GameStateRepository repository;
    private final String defaultUsername;
    private final BuildingFactory buildingFactory;
    private final InhabitantFactory inhabitantFactory;
    private final AttackOutcomeCalculator attackCalculator;
    private final Path saveFile;

    public GameController(GameEngine engine,
                          GameStateRepository repository,
                          String defaultUsername,
                          BuildingFactory buildingFactory,
                          InhabitantFactory inhabitantFactory,
                          AttackOutcomeCalculator attackCalculator,
                          Path saveFile) {
        this.engine = engine;
        this.repository = repository;
        this.defaultUsername = defaultUsername;
        this.buildingFactory = buildingFactory;
        this.inhabitantFactory = inhabitantFactory;
        this.attackCalculator = attackCalculator;
        this.saveFile = saveFile;
    }

    /**
     * Creates a controller with all required Assignment 3 components.
     *
     * This method initializes the factories, the attack adapter,
     * the XML repository, and a default in-memory game session.
     *
     * @param saveFile the XML file used for persistence
     * @param defaultUsername the username used for a new session
     * @return a fully configured controller
     * @throws IOException if persistence setup fails
     */
    public static GameController create(Path saveFile, String defaultUsername) throws IOException {
        BuildingFactory buildingFactory = new DefaultBuildingFactory();
        InhabitantFactory inhabitantFactory = new DefaultInhabitantFactory();
        AttackOutcomeCalculator attackCalculator = new ChallengeDecisionAttackAdapter();
        GameStateRepository repository = new XmlGameStateRepository(saveFile, buildingFactory, inhabitantFactory);
        GameEngine engine = new GameEngine(defaultUsername, buildingFactory, inhabitantFactory, attackCalculator);
        return new GameController(engine, repository, defaultUsername, buildingFactory, inhabitantFactory, attackCalculator, saveFile);
    }

    public Player getPlayer() {
        return engine.getPlayer();
    }

    public int getTime() {
        return engine.getTime();
    }

    public List<String> consumeEvents() {
        return engine.consumeEvents();
    }

    public void advanceTime(int hours) {
        engine.advanceTime(hours);
    }

    public void build(BuildingType type) throws GameException {
        engine.build(type);
    }

    public void train(InhabitantType type) throws GameException {
        engine.train(type);
    }

    public void upgradeBuilding(int index) throws GameException {
        engine.upgradeBuilding(index);
    }

    public void upgradeInhabitant(int index) throws GameException {
        engine.upgradeInhabitant(index);
    }

    public List<Village> exploreVillages() {
        return engine.exploreVillages();
    }

    public AttackResult attack(Village village) throws GameException {
        return engine.attack(village);
    }

    public Army generateTestingArmy() {
        return engine.generateTestingArmy();
    }

    public VillageDefenseTestResult testVillageDefense(int numberOfTests) {
        return engine.testVillageDefense(numberOfTests);
    }

    public void saveGame() throws IOException {
        repository.save(engine.snapshot());
    }

    /**
     * Loads a previously saved game state and replaces the
     * current in-memory session.
     *
     * @return true if a saved game was loaded, otherwise false
     * @throws IOException if the save file cannot be read
     */
    public boolean loadGame() throws IOException {
        return repository.load()
                .map(state -> {
                    engine = new GameEngine(state, buildingFactory, inhabitantFactory, attackCalculator);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Starts a fresh game session using the default username.
     *
     * The current in-memory game state is discarded, but any
     * saved file remains untouched until the user saves again.
     */
    public void newGame() {
        engine = new GameEngine(defaultUsername, buildingFactory, inhabitantFactory, attackCalculator);
    }

    public boolean hasSavedGame() {
        return Files.exists(saveFile);
    }
}
