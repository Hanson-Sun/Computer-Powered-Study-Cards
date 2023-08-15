package persistence;

import model.CardSet;
import model.CardSetsMenu;

import java.io.File;
import java.io.IOException;

public class JsonUtils {
    private final String csmData;
    private final String cardsetFolder;
    private final String suffix;

    // EFFECTS: constructs new JsonUtils object with path information
    public JsonUtils(String data, String folder, String suffix) {
        this.csmData = data;
        this.cardsetFolder = folder;
        this.suffix = suffix;
    }

    // EFFECTS: saves the CardSetMenu to file
    public void saveCardSetMenu(CardSetsMenu csm) throws IOException {
        JsonWriter jsonWriter = new JsonWriter(this.csmData);
        jsonWriter.open();
        jsonWriter.write(csm);
        jsonWriter.close();
    }

    // EFFECTS: saves the cardSet to file
    public void saveCardSet(CardSet cs) throws IOException {
        String title = cs.getTitle();
        String fileName = cardsetFolder + title + suffix;
        JsonWriter jsonWriter = new JsonWriter(fileName);
        jsonWriter.open();
        jsonWriter.write(cs);
        jsonWriter.close();

    }

    // EFFECTS: read card set menu from corresponding file
    public CardSetsMenu readCardSetMenu() throws IOException {
        JsonReader jsonReader = new JsonReader(csmData);
        if ((new File(csmData)).createNewFile()) {
            return new CardSetsMenu();
        } else {
            return jsonReader.readCardSetMenu();
        }
    }

    // EFFECTS: read cardset from a corresponding file with the same title
    public CardSet readCardSet(String title) throws IOException {
        String fileName = cardsetFolder + title + suffix;
        JsonReader jsonReader = new JsonReader(fileName);
        if ((new File(csmData)).createNewFile()) {
            return new CardSet(title);
        } else {
            return jsonReader.readCardSet();
        }
    }

    // EFFECTS: delete the stored info of a cardset
    public void deleteCardSet(CardSet cs) {
        File f = new File(cardsetFolder + cs.getTitle() + suffix);
        f.delete();
    }
}
