package ui;

import javax.swing.*;
import java.awt.*;

// Styled JTextArea for editing cards
public class CardEditPane extends JTextArea {
    private String content;
    private static int width = 500;
    private static int height = 250;

    // EFFECTS: instantiate new CardEditPane with styling
    public CardEditPane(String content) {
        super();
        this.content = content;
        this.setText(content);
        this.setEditable(true);
        this.setPreferredSize(new Dimension(width, height));
        this.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        this.setMaximumSize(new Dimension(width, height + 50));
        this.setMinimumSize(new Dimension(width / 2, height / 2));
        this.setWrapStyleWord(true);
        this.setLineWrap(true);
    }
}
