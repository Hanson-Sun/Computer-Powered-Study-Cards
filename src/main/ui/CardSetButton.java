package ui;

import javax.swing.*;
import java.awt.*;

// Styled JButton for each CardSet in the CardSetMenu display
public class CardSetButton extends JButton {
    private static int width = 200;
    private static int height = 130;

    // EFFECTS: instantiate new CardSetButton with styling
    public CardSetButton(String content) {
        super(content);
        this.setForeground(Color.BLACK);
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(width, height));
        this.setFont(new Font("Arial", Font.BOLD, 25));
        this.setSize(new Dimension(width, height));
    }
}
