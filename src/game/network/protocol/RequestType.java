/**
 * Enumeration of all commands supported by  network protocol.
 */
package game.network.protocol;

public enum RequestType {
    AUTHENTICATE,
    SHOW_STATUS,
    BUILD,
    TRAIN,
    UPGRADE_BUILDING,
    UPGRADE_INHABITANT,
    EXPLORE_VILLAGES,
    ATTACK_EXPLORED_VILLAGE,
    ADVANCE_TIME,
    SAVE_GAME,
    LOAD_GAME,
    NEW_GAME,
    GENERATE_TEST_ARMY,
    TEST_VILLAGE,
    DISCONNECT
}
