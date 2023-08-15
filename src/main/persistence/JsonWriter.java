package persistence;

import model.CardSet;
import model.CardSetsMenu;
import org.json.JSONObject;
import java.io.*;

// Represents a writer that writes JSON representation of workroom to file
// referenced the WorkRoom application example
public class JsonWriter {

    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws IOException if destination file has invalid name
    public void open() throws IOException {
        (new File(destination)).createNewFile();
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of CardSetMenu to file
    public void write(CardSetsMenu csm) {
        JSONObject json = csm.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of CardSet to file
    public void write(CardSet cs) {
        JSONObject json = cs.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

}