package ui;

import model.Card;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

// Styled JTextPane for diplaying cards in the CardSet Menu
public class CardPane extends JTextPane {
    private final String htmlHead = "<html><body style='text-align:center;'>";
    private Card card;
    private static int width = 250;
    private static int height = 175;
    private Color background;

    // EFFECTS: initialize new CardPane with style and displays content info
    public CardPane(Card c) {
        super();
        this.card = c;
        this.background = Color.white;
        this.setBackground(this.background);
        String front = html2String(c.getFront().getContent());
        String back = html2String(c.getBack().getContent());
        String text = htmlHead
                + "<h2>" + htmlify(front, "color:blue; font-weight: bold;") + "</h2>" + "<br/>"
                + htmlify(back, "color:black;") + "<br/>" + "<br>"
                + getStats() + "</body></html>";
        this.setContentType("text/html");
        this.setText(text);
        this.setEditable(false);
        this.setPreferredSize(new Dimension(width, height));
        this.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        this.setMaximumSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
    }

    // EFFECTS: return html span version of a text string with a given style
    private String htmlify(String text, String style) {
        if (text.length() > 25) {
            text = text.substring(0, 25 + (Integer.MAX_VALUE ^ Integer.MIN_VALUE)) + "...";
        }
        return "<span style='" + style + "'>" + text + "</span>";
    }

    // EFFECTS: convert html to string
    private String html2String(String text) {
        return text.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
    }

    // EFFECTS: return statistics of the card as a string
    private String getStats() {
        int sum = (card.getCorrectCount() + card.getWrongCount());
        if (sum != 0) {
            return ("Reviewed: " + (card.getCorrectCount() + card.getWrongCount()) + " times; "
                    + "Accuracy: " + round((card.getAccuracy() * 100), 2) + "%");
        } else {
            return ("This Card has not been reviewed yet");
        }
    }

    // EFFECTS: round a double to specified decimal place
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    // EFFECTS: change border of the CardPane to have lower bezel
    // MODIFIES: this
    public void hoverColorChange() {
        this.setEditable(true);
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray,
                Color.black, Color.gray, Color.lightGray));
        this.setEditable(false);
    }

    // EFFECTS: change  border of the CardPane to have higher bezel
    // MODIFIES: this
    public void hoverColorChangeBack() {
        this.setEditable(true);
        this.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        this.setEditable(false);
    }

    // EFFECTS: change the colour of the CardPane to light blue
    // MODIFIES: this
    public void clickColorChange() {
        this.setEditable(true);
        this.setBackground(new Color(204,229,255));
        this.setEditable(false);
    }

    // EFFECTS: change the colour of the CardPane to white
    // MODIFIES: this
    public void clickReleaseColorChange() {
        this.setEditable(true);
        this.setBackground(this.background);
        this.setEditable(false);
    }
}
