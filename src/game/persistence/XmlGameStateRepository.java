/**
 * XML-based implementation of the game state repository.
 *
 * This class writes the current session to an XML file and restores
 * the complete session back from that file when loaded.
 */
package game.persistence;

import game.buildings.Building;
import game.buildings.BuildingType;
import game.factory.BuildingFactory;
import game.factory.InhabitantFactory;
import game.inhabitants.ArmyUnit;
import game.inhabitants.Inhabitant;
import game.inhabitants.InhabitantType;
import game.inhabitants.Worker;
import game.model.ResourceStorage;
import game.model.ResourceType;
import game.model.Village;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class XmlGameStateRepository implements GameStateRepository {
    private final Path storagePath;
    private final BuildingFactory buildingFactory;
    private final InhabitantFactory inhabitantFactory;

    public XmlGameStateRepository(Path storagePath, BuildingFactory buildingFactory, InhabitantFactory inhabitantFactory) {
        this.storagePath = storagePath;
        this.buildingFactory = buildingFactory;
        this.inhabitantFactory = inhabitantFactory;
    }

    /**
     * Loads the saved game from the configured XML file.
     *
     * The method recreates the village, buildings, inhabitants,
     * resources, time, and counters that define the full session.
     *
     * @return the saved game state if the file exists
     * @throws IOException if the file cannot be read
     */
    @Override
    public Optional<GameState> load() throws IOException {
        if (!Files.exists(storagePath)) {
            return Optional.empty();
        }

        try {
            DocumentBuilder builder = createDocumentBuilder();
            Document document = builder.parse(Files.newInputStream(storagePath));
            Element root = document.getDocumentElement();

            String username = getChildText(root, "username");
            int timeHours = Integer.parseInt(getChildText(root, "timeHours"));
            int nextId = Integer.parseInt(getChildText(root, "nextInhabitantId"));

            Element villageElement = getFirstChild(root, "village");
            Village village = new Village(
                    Integer.parseInt(villageElement.getAttribute("id")),
                    villageElement.getAttribute("name")
            );
            village.setTrophies(Integer.parseInt(villageElement.getAttribute("trophies")));
            village.activateGuard(Integer.parseInt(villageElement.getAttribute("guardRemainingHours")));

            loadBuildings(village, getFirstChild(villageElement, "buildings"));
            loadResources(village.getResources(), getFirstChild(villageElement, "resources"));
            loadInhabitants(village, getFirstChild(villageElement, "inhabitants"));

            return Optional.of(new GameState(username, village, timeHours, nextId));
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("Unable to parse XML save file.", e);
        }
    }

    /**
     * Saves the current game state to the configured XML file.
     *
     * @param state the in-memory game state snapshot
     * @throws IOException if the file cannot be written
     */
    @Override
    public void save(GameState state) throws IOException {
        try {
            DocumentBuilder builder = createDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("gameState");
            document.appendChild(root);

            appendTextElement(document, root, "username", state.getUsername());
            appendTextElement(document, root, "timeHours", String.valueOf(state.getTimeHours()));
            appendTextElement(document, root, "nextInhabitantId", String.valueOf(state.getNextInhabitantId()));

            Element villageElement = document.createElement("village");
            Village village = state.getVillage();
            villageElement.setAttribute("id", String.valueOf(village.getVillageId()));
            villageElement.setAttribute("name", village.getName());
            villageElement.setAttribute("trophies", String.valueOf(village.getTrophies()));
            villageElement.setAttribute("guardRemainingHours", String.valueOf(village.getGuardRemainingHours()));
            root.appendChild(villageElement);

            writeResources(document, villageElement, village);
            writeBuildings(document, villageElement, village);
            writeInhabitants(document, villageElement, village);

            Files.createDirectories(storagePath.getParent());
            Files.writeString(storagePath, toXml(document), StandardCharsets.UTF_8);
        } catch (ParserConfigurationException | TransformerException e) {
            throw new IOException("Unable to save XML game state.", e);
        }
    }

    private void loadBuildings(Village village, Element buildingsElement) {
        NodeList buildings = buildingsElement.getElementsByTagName("building");
        for (int i = 0; i < buildings.getLength(); i++) {
            Element buildingElement = (Element) buildings.item(i);
            BuildingType type = BuildingType.valueOf(buildingElement.getAttribute("type"));
            Building building = buildingFactory.create(type);
            building.restoreState(
                    Integer.parseInt(buildingElement.getAttribute("level")),
                    Integer.parseInt(buildingElement.getAttribute("hitPoints"))
            );
            village.addBuilding(building);
        }
    }

    /**
     * Restores stored resource values and capacities from the XML data.
     *
     * @param storage the resource storage being reconstructed
     * @param resourcesElement the parsed XML resources section
     */
    private void loadResources(ResourceStorage storage, Element resourcesElement) {
        NodeList resources = resourcesElement.getElementsByTagName("resource");
        for (int i = 0; i < resources.getLength(); i++) {
            Element resourceElement = (Element) resources.item(i);
            ResourceType type = ResourceType.valueOf(resourceElement.getAttribute("type"));
            storage.setCapacity(type, Integer.parseInt(resourceElement.getAttribute("capacity")));
            storage.set(type, Integer.parseInt(resourceElement.getAttribute("amount")));
        }
    }

    private void loadInhabitants(Village village, Element inhabitantsElement) {
        NodeList inhabitants = inhabitantsElement.getElementsByTagName("inhabitant");
        for (int i = 0; i < inhabitants.getLength(); i++) {
            Element inhabitantElement = (Element) inhabitants.item(i);
            InhabitantType type = InhabitantType.valueOf(inhabitantElement.getAttribute("type"));
            Inhabitant inhabitant = inhabitantFactory.create(type, Integer.parseInt(inhabitantElement.getAttribute("id")));

            if (inhabitant instanceof ArmyUnit armyUnit) {
                armyUnit.restoreState(
                        Integer.parseInt(inhabitantElement.getAttribute("level")),
                        Integer.parseInt(inhabitantElement.getAttribute("hitPoints"))
                );
                village.getArmy().addUnit(armyUnit);
            } else {
                inhabitant.restoreState(Integer.parseInt(inhabitantElement.getAttribute("level")));
                if (inhabitant instanceof Worker worker
                        && !Boolean.parseBoolean(inhabitantElement.getAttribute("available"))) {
                    worker.assignWork();
                }
            }
            village.addInhabitant(inhabitant);
        }
    }

    private void writeResources(Document document, Element villageElement, Village village) {
        Element resourcesElement = document.createElement("resources");
        villageElement.appendChild(resourcesElement);

        ResourceStorage storage = village.getResources();
        for (ResourceType type : ResourceType.values()) {
            Element resourceElement = document.createElement("resource");
            resourceElement.setAttribute("type", type.name());
            resourceElement.setAttribute("amount", String.valueOf(storage.get(type)));
            resourceElement.setAttribute("capacity", String.valueOf(storage.getCapacity(type)));
            resourcesElement.appendChild(resourceElement);
        }
    }

    private void writeBuildings(Document document, Element villageElement, Village village) {
        Element buildingsElement = document.createElement("buildings");
        villageElement.appendChild(buildingsElement);

        for (Building building : village.getBuildings()) {
            Element buildingElement = document.createElement("building");
            buildingElement.setAttribute("type", BuildingType.fromBuilding(building).name());
            buildingElement.setAttribute("level", String.valueOf(building.getLevel()));
            buildingElement.setAttribute("hitPoints", String.valueOf(building.getHitPoints()));
            buildingsElement.appendChild(buildingElement);
        }
    }

    private void writeInhabitants(Document document, Element villageElement, Village village) {
        Element inhabitantsElement = document.createElement("inhabitants");
        villageElement.appendChild(inhabitantsElement);

        for (Inhabitant inhabitant : village.getInhabitants()) {
            Element inhabitantElement = document.createElement("inhabitant");
            inhabitantElement.setAttribute("type", InhabitantType.fromInhabitant(inhabitant).name());
            inhabitantElement.setAttribute("id", String.valueOf(inhabitant.getId()));
            inhabitantElement.setAttribute("level", String.valueOf(inhabitant.getLevel()));

            if (inhabitant instanceof Worker worker) {
                inhabitantElement.setAttribute("available", String.valueOf(worker.isAvailable()));
            }
            if (inhabitant instanceof ArmyUnit armyUnit) {
                inhabitantElement.setAttribute("hitPoints", String.valueOf(armyUnit.getHitPoints()));
            }
            inhabitantsElement.appendChild(inhabitantElement);
        }
    }

    private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder();
    }

    private void appendTextElement(Document document, Element parent, String tagName, String value) {
        Element element = document.createElement(tagName);
        element.setTextContent(value);
        parent.appendChild(element);
    }

    private Element getFirstChild(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return (Element) nodes.item(0);
    }

    private String getChildText(Element parent, String tagName) {
        return getFirstChild(parent, tagName).getTextContent();
    }

    private String toXml(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }
}
