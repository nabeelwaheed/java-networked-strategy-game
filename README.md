# Networked Village War Strategy Game

A console-based Java strategy game built as an advanced object-oriented programming project. The game started as a local village simulator and was progressively redesigned into a networked client/server application with MVC structure, design patterns, XML persistence, authentication, sockets, and a multi-threaded server.

The player develops a village by constructing buildings, training inhabitants and army units, upgrading structures, generating enemy villages, attacking for loot, and testing village defenses against generated armies.

## Project Highlights

- Pure Java implementation with no external dependencies.
- Client/server architecture using TCP sockets.
- Multi-threaded server supporting multiple clients.
- Serialized request/response protocol between client and server.
- Login handshake with server-side credential validation.
- MVC architecture separating UI, controller, and model responsibilities.
- Factory pattern for building and inhabitant creation.
- Adapter pattern for integrating the provided `ChallengeDecision` combat API.
- XML persistence with an XSD schema.
- Detached army generation and village defense testing.
- Command-line runnable without an IDE.

## Gameplay Features

The game supports the core strategy loop expected from a village war simulator:

- Build village structures such as farms, mines, storage buildings, archer towers, and cannons.
- Train workers, miners, collectors, soldiers, archers, knights, and catapults.
- Upgrade buildings and inhabitants up to level limits.
- Generate compatible enemy villages for attack exploration.
- Attack selected villages and gain loot/trophies.
- Generate detached test armies.
- Run village defense tests and receive a success score.
- Save and load player progress through XML persistence.
- Advance simulated game time.
- Receive random enemy attacks when the village is outside guard protection.

## Architecture

The final version is organized as a networked MVC-style application.

```text
Client Console UI
    |
    | serialized GameRequest / GameResponse over TCP
    v
Game Server
    |
    | authenticated PlayerSession
    v
GameController
    |
    v
GameEngine + Domain Model
```

### Client

The client is responsible for player interaction only. It reads console input, sends requests to the server, and displays responses.

Important classes:

- `game.network.client.GameClientMain`
- `game.network.client.NetworkGameClient`
- `game.network.client.NetworkConsoleUI`

### Server

The server owns authentication, player sessions, simulation, persistence, and game state. It accepts many client connections and delegates game actions to worker threads.

Important classes:

- `game.network.server.GameServerMain`
- `game.network.server.ClientHandler`
- `game.network.server.SessionManager`
- `game.network.server.PlayerSession`
- `game.network.server.FileUserDatabase`

### Model

The model contains the actual game objects and rules:

- `game.model.Village`
- `game.model.Player`
- `game.model.Army`
- `game.model.ResourceStorage`
- `game.buildings.*`
- `game.inhabitants.*`
- `game.engine.GameEngine`

## Advanced OOP Concepts

This project intentionally uses several object-oriented design concepts:

- Encapsulation: village state, resources, sessions, and protocol messages are managed through dedicated classes.
- Inheritance: buildings and inhabitants share behavior through abstract base classes.
- Polymorphism: combat units, buildings, and upgradeable entities are handled through common parent types and interfaces.
- Interfaces: `Upgradeable`, `BuildingFactory`, `InhabitantFactory`, `AttackOutcomeCalculator`, `GameStateRepository`, and `UserDatabase` define contracts.
- Enumerations: `BuildingType`, `InhabitantType`, `ResourceType`, and `RequestType` make supported options explicit.
- Generics: used by the provided `ChallengeDecision` package and collection-based models.
- DTOs: network responses use serializable transfer objects to decouple server internals from client display logic.

## Design Patterns

### MVC

The project uses MVC to separate presentation, control flow, and game state.

- Model: `Village`, `Army`, `ResourceStorage`, buildings, inhabitants, and `GameEngine`
- View: `ConsoleUI` for local play and `NetworkConsoleUI` for networked play
- Controller: `GameController`

This separation made it easier to move from a local console game to a client/server version because the UI could be replaced without rewriting the game model.

### Factory Pattern

Factories centralize object creation:

- `BuildingFactory`
- `DefaultBuildingFactory`
- `InhabitantFactory`
- `DefaultInhabitantFactory`

Instead of scattering `new Farm()`, `new Soldier()`, or `new Cannon()` throughout the project, the engine, village generator, and persistence layer ask factories to create objects. This improves maintainability and makes it easier to add new buildings or inhabitants later.

### Adapter Pattern

The provided `ChallengeDecision` package uses its own attack API. The game uses an adapter to translate between the game's model and that external API.

Important classes:

- `AttackOutcomeCalculator`
- `AttackResolution`
- `ChallengeDecisionAttackAdapter`

The engine depends on `AttackOutcomeCalculator`, not directly on `ChallengeDecision`. This keeps the external package isolated and makes the combat system easier to replace or extend.

### Repository Pattern

Persistence is abstracted through:

- `GameStateRepository`
- `XmlGameStateRepository`

The game controller saves and loads through the repository interface, while XML-specific parsing and writing stay inside one persistence class.

## Networking

The networked version uses TCP sockets.

TCP was chosen because the game needs reliable, ordered communication. Requests such as building, saving, attacking, and upgrading should not be dropped or processed out of order.

The server listens for client connections using `ServerSocket`, and clients connect using `Socket`.

Default port:

```text
5050
```

## Client/Server Protocol

The client and server communicate using serialized Java objects.

Protocol classes:

- `GameRequest`
- `GameResponse`
- `RequestType`

A request contains a command type and optional string parameters. A response contains success/failure status, a message, optional payload data, and game event messages.

Supported request types include:

- `AUTHENTICATE`
- `SHOW_STATUS`
- `BUILD`
- `TRAIN`
- `UPGRADE_BUILDING`
- `UPGRADE_INHABITANT`
- `EXPLORE_VILLAGES`
- `ATTACK_EXPLORED_VILLAGE`
- `ADVANCE_TIME`
- `SAVE_GAME`
- `LOAD_GAME`
- `NEW_GAME`
- `GENERATE_TEST_ARMY`
- `TEST_VILLAGE`
- `DISCONNECT`

## Authentication

The client must log in before accessing the game. The server checks credentials against a simple file-backed database:

```text
server-data/users.db
```

Default users:

```text
PlayerOne=password123
Tester=test123
VillageAdmin=admin123
```

This satisfies the assignment requirement for a handshake-like authentication protocol using text-based comparison.

## Multi-threaded Server

The server uses multiple executor services:

- A cached thread pool for multiple client connections.
- A fixed thread pool for simulation work such as attacks, village generation, and defense testing.
- A scheduled executor for server-side time ticks.

This allows one server to serve multiple clients and continue advancing active sessions over time.

Important server behavior:

- Each connected client is handled by a `ClientHandler`.
- Each authenticated player gets a `PlayerSession`.
- Sessions are managed by `SessionManager`.
- Session methods are synchronized to protect player state during concurrent access.

## Persistence

The game saves progress using XML.

Important files/classes:

- `XmlGameStateRepository`
- `GameState`
- `GameStateRepository`
- `src/game/data/village-state.xsd`

Saved server-side player states are stored under:

```text
server-data/saves/
```

The XML save stores:

- username
- elapsed game time
- next inhabitant ID
- village name and ID
- trophies
- guard timer
- resource amounts and capacities
- buildings with level and hit points
- inhabitants with type, ID, level, and unit state

## Project Structure

```text
src/
  ChallengeDecision/          Provided combat decision API
  game/
    adapter/                  Adapter around ChallengeDecision
    buildings/                Building hierarchy
    combat/                   Combat result models
    controller/               MVC controller
    data/                     XML schema
    engine/                   Game engine, time, generation logic
    exceptions/               Game-specific exceptions
    factory/                  Building and inhabitant factories
    inhabitants/              Inhabitant and army unit hierarchy
    model/                    Core domain model
    network/
      client/                 Network client and console UI
      dto/                    Serializable network DTOs
      protocol/               Request/response protocol
      server/                 TCP server and session management
    persistence/              XML save/load support
    ui/                       Local console UI
```

## Running the Project

These commands assume you are in the project root directory, the folder that contains `src`.

### macOS / Linux

Compile:

```bash
javac -d out $(find src -name "*.java")
```

Start the server:

```bash
java -cp out game.network.server.GameServerMain 5050
```

Start the client in a second terminal:

```bash
java -cp out game.network.client.GameClientMain 127.0.0.1 5050
```

Optional local, non-networked version:

```bash
java -cp out game.Main
```

### Windows PowerShell

Compile:

```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

Start the server:

```powershell
java -cp out game.network.server.GameServerMain 5050
```

Start the client:

```powershell
java -cp out game.network.client.GameClientMain 127.0.0.1 5050
```

Optional local, non-networked version:

```powershell
java -cp out game.Main
```

## Example Login

When the client starts, use one of the default credentials:

```text
Username: PlayerOne
Password: password123
```

After authentication, the networked menu allows the player to view the village, build, train, upgrade, explore, attack, save, load, generate testing armies, and test village defenses.

## Why This Project Matters

This project demonstrates more than a simple console game. It shows how a growing object-oriented system can evolve through multiple architectural stages:

1. A local domain model with buildings, resources, inhabitants, and combat.
2. MVC separation for better structure.
3. Factory and Adapter patterns for extensibility.
4. XML persistence for durable state.
5. TCP client/server networking.
6. Authentication and session management.
7. Multi-threaded server-side simulation.

The result is a compact but complete Java system that connects advanced OOP concepts to real application architecture.
