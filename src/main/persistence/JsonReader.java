package persistence;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.*;
import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
// referenced the WorkRoom application example
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads source file for cardSetMenu
    public CardSetsMenu readCardSetMenu() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCardSetMenu(jsonObject);
    }

    // EFFECTS: reads source file for a cardSet
    public CardSet readCardSet() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCardSet(jsonObject);
    }

    // EFFECTS: parse cardSetMenu file as a JSON object
    private CardSetsMenu parseCardSetMenu(JSONObject jsonObject) {
        CardSetsMenu csm = new CardSetsMenu();
        JSONArray cardSets = jsonObject.getJSONArray("cardSets");
        addCardSets(csm, cardSets);
        //Optional logger: EventLog.getInstance().logEvent(new Event("CardSetMenu loaded from saved data."));
        return csm;
    }

    // EFFECTS: parse CardSet file as a JSON object
    private CardSet parseCardSet(JSONObject jsonObject) {
        String title = jsonObject.getString("title");
        CardSet cs = new CardSet(title);
        cs.setUseCount(jsonObject.getInt("usecount"));
        JSONArray cards = jsonObject.getJSONArray("cards");
        addCards(cs, cards);
        // Optional Logger: EventLog.getInstance().logEvent(new Event("CardSet '"
        // + cs.getTitle() + "' loaded from saved data."));
        return cs;
    }

    // MODIFIES: cs
    // EFFECTS: convert the JSON data of a list of cards in a card object and store it in a cardSet
    private void addCards(CardSet cs, JSONArray cards) {
        for (Object json : cards) {
            JSONObject card = (JSONObject) json;
            addCard(cs, card);
        }
    }

    // MODIFIES: cs
    // EFFECTS: convert the JSON data of a card in a card object and store it in a cardSet
    private void addCard(CardSet cs, JSONObject card) {
        int correctCount = card.getInt("correctCount");
        int wrongCount = card.getInt("wrongCount");
        boolean state = card.getBoolean("state");
        CardSide front = getCardSide(card.getJSONObject("front"));
        CardSide back = getCardSide(card.getJSONObject("back"));
        Card c = new Card(front, back);
        c.setState(state);
        c.setCorrectCount(correctCount);
        c.setWrongCount(wrongCount);
        cs.getCards().add(c);
    }

    // EFFECTS: converts the JSON data of a cardSide into the cardSide object
    private CardSide getCardSide(JSONObject json) {
        String content = json.getString("content");
        int br = json.getJSONObject("backgroundColor").getInt("red");
        int bg = json.getJSONObject("backgroundColor").getInt("green");
        int bb = json.getJSONObject("backgroundColor").getInt("blue");
        int fr = json.getJSONObject("fontColor").getInt("red");
        int fg = json.getJSONObject("fontColor").getInt("green");
        int fb = json.getJSONObject("fontColor").getInt("blue");
        Color backGround = new Color(br, bg, bb);
        Color fontColor = new Color(fr, fg, fb);
        CardSide cardSide = new CardSide();
        cardSide.setContent(content);
        cardSide.setBackgroundColor(backGround);
        cardSide.setFontColor(fontColor);
        return cardSide;

    }

    // MODIFIES: csm
    // EFFECTS: convert the JSON data of a list of cardSets and add it to a cardSetMenu
    private void addCardSets(CardSetsMenu csm, JSONArray cardSets) {
        for (Object json : cardSets) {
            JSONObject cardSet = (JSONObject) json;
            addCardSet(csm, cardSet);
        }
    }

    // MODIFIES: csm
    // EFFECTS: convert the JSON data of a cardSet and add it to a cardSetMenu
    private void addCardSet(CardSetsMenu csm, JSONObject cardSet) {
        String title = cardSet.getString("title");
        int useCount = cardSet.getInt("usecount");
        CardSet cs = new CardSet(title);
        cs.setUseCount(useCount);
        csm.getCardSets().add(cs);
    }


    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }
}