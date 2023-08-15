package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.*;

// CardSide is a single side of a two-sided flashcard. Each card side will contain graphical information of
// how the card side will be displayed, this will be implemented later on in the gui stage.
public class CardSide implements Writable {
    // TODO: create image and text editing interface during GUI stage
    private String content;
    private Color fontColor;
    private Color backgroundColor;

    // MODIFIES: this
    // EFFECTS: insantiate new cardside with no content, black text color, and white background
    public CardSide() {
        this.content = "";
        this.fontColor = new Color(0,0,0);
        this.backgroundColor = new Color(255,255,255);
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("content", content);
        json.put("fontColor", colorToJson(fontColor));
        json.put("backgroundColor", colorToJson(backgroundColor));
        return json;
    }

    // EFFECTS: convert color object int a json type
    public JSONObject colorToJson(Color c) {
        JSONObject json = new JSONObject();
        json.put("red", c.getRed());
        json.put("green", c.getGreen());
        json.put("blue", c.getBlue());
        return json;
    }

    // simple getters and setters
    public void setContent(String c) {
        this.content = c;
    }

    public void setFontColor(Color c) {
        this.fontColor = c;
    }

    public void setBackgroundColor(Color c) {
        this.backgroundColor = c;
    }

    public String getContent() {
        return content;
    }

    public Color getFontColor() {
        return this.fontColor;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

}
