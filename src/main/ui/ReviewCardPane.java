package ui;

import model.Card;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Styled JTextPane to display each card face during review
public class ReviewCardPane extends JTextPane {
    private Card card;
    private String content;
    private final String htmlHead = "<html><body style='text-align:center;'><br/><br/>";

    // EFFECTS: instantiate new ReviewCardPane with styling
    public ReviewCardPane(Card c) {
        super();
        this.card = c;
        this.setContentType("text/html");
        getCurrentCardContent();
        this.setEditable(false);
        this.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        this.setMaximumSize(new Dimension(600, 800));
        this.setMinimumSize(new Dimension(100, 100));
        this.setPreferredSize(new Dimension(400, 350));
        this.addListener();
    }

    // EFFECTS: gets and sets the current card contents
    // MODIFIES: this
    private void getCurrentCardContent() {
        this.content = this.card.getCardFace().getContent();
        this.content = htmlHead + "<h2>" + this.content + "</h2>" + "</body></html>";
        this.setText(content);
    }

    // EFFECTS: flip card and update
    // MODIFIES: this
    private void flipCard() {
        this.card.flip();
        getCurrentCardContent();
        this.revalidate();
        this.repaint();
    }

    // EFFECTS: change bevel border to lowered
    // MODFIES: this
    public void hoverColorChange() {
        this.setEditable(true);
        this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray,
                Color.black, Color.gray, Color.lightGray));
        this.setEditable(false);
    }

    // EFFECTS: change border to simple line border
    // MODIFIES: this
    public void hoverColorChangeBack() {
        this.setEditable(true);
        this.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        this.setEditable(false);
    }

    // EFFECTS: change background colour to light blue
    // MODIFIES: this
    public void clickColorChange() {
        this.setEditable(true);
        this.setBackground(new Color(204,229,255));
        this.setEditable(false);
    }

    // EFFECTS: change background colour to white
    // MODIFIES: this
    public void clickReleaseColorChange() {
        this.setEditable(true);
        this.setBackground(Color.white);
        this.setEditable(false);
    }

    // EFFECTS: add mouse listener
    // EFFECTS: this
    private void addListener() {
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                flipCard();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                clickColorChange();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                clickReleaseColorChange();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hoverColorChange();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverColorChangeBack();
            }
        });
    }

    // EFFECTS: sets the current card
    // MODIFIES: this
    public void setCard(Card card) {
        this.card = card;
    }

    // EFFECTS: refresh the card display
    // MODIFIES: this
    public void refreshCard() {
        getCurrentCardContent();
        this.revalidate();
        this.repaint();
    }

}
