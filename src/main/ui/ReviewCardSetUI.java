package ui;

import model.Card;
import model.CardSet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

// Display review UI
public class ReviewCardSetUI extends JPanel {
    private JButton right;
    private JButton wrong;
    private JButton next;
    private JButton back;
    private JButton flipped;
    private int index;
    private CardSet cs;
    private boolean isChecked = false;
    private ArrayList<Card> mixed;

    // EFFECTS: initialize ReviewCardSetUI
    public ReviewCardSetUI(CardSet cs) {
        super();
        this.cs = cs;
        this.index = 0;
        this.mixed = this.cs.getMixedCards();
        loadReviewCsMenu();
    }

    // EFFECTS: load first card of an arraylist of cards
    // MODIFIES: this
    private void loadReviewCsMenu() {
        try {
            Card card = mixed.get(index);
            ReviewCardPane cardPane = new ReviewCardPane(card);
            this.add(cardPane);
            this.revalidate();
            this.repaint();
            this.add(createReviewButtons(cardPane));
        } catch (IndexOutOfBoundsException e) {
            this.cs.setCardFlippedState(false);
            JOptionPane.showMessageDialog(this, "Review Complete");
        }
    }

    // EFFECTS: refresh review card ui and loads next card
    // MODIFES: this, pane, mixed
    private void refreshReviewCard(ReviewCardPane pane) {
        try {
            Card c = mixed.get(index);
            pane.setCard(c);
            pane.refreshCard();
            this.revalidate();
            this.repaint();
        } catch (IndexOutOfBoundsException e) {
            this.cs.setCardFlippedState(false);
            JOptionPane.showMessageDialog(this, "Review Complete");
        }
    }

    // EFFECTS: create buttons for review card ui
    // MODIFIES: this
    private JPanel createReviewButtons(ReviewCardPane pane) {
        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        next = new JButton(IconPackage.RIGHT_ARROW.icon);
        flipped = new JButton(IconPackage.FLIP.icon);
        wrong = new JButton(IconPackage.CROSS.icon);
        right = new JButton(IconPackage.CHECK.icon);
        back = new JButton(IconPackage.LEFT_ARROW.icon);
        nextButtonAddListener(next, pane);
        flipButtonAddListener(flipped, pane);
        wrongButtonAddListener(wrong, pane);
        rightButtonAddListener(right, pane);
        backButtonAddListener(back, pane);
        buttons.add(back);
        buttons.add(flipped);
        buttons.add(wrong);
        buttons.add(right);
        buttons.add(next);
        return buttons;
    }

    // EFFECTS: create action listener for back button
    // MODIFIES: back, this
    private void backButtonAddListener(JButton back, ReviewCardPane pane) {
        back.addActionListener(e -> {
            if (0 < index) {
                this.isChecked = false;
                index = index - 1;
                disableButtons();
            } else {
                JOptionPane.showMessageDialog(this, "You reached the beginning!");
            }
            refreshReviewCard(pane);
        });
    }

    // EFFECTS: adds eventlistener that moves to the next card in review ui
    // MODIFIES: next, this
    private void nextButtonAddListener(JButton next, ReviewCardPane pane) {
        next.addActionListener(e -> {
            if (index < mixed.size() - 1) {
                this.isChecked = false;
                index++;
                disableButtons();
            } else {
                this.isChecked = false;
                mixed.addAll(this.cs.getMixedCards());
                this.index = 0;
                JOptionPane.showMessageDialog(this, "Review complete!");
            }
            refreshReviewCard(pane);
        });
    }

    // EFFECTS: adds eventlistener that flips the current card in review ui
    // MODIFIES: flipped, this
    private void flipButtonAddListener(JButton flipped, ReviewCardPane pane) {
        flipped.addActionListener(e -> {
            if (mixed.size() != 0) {
                mixed.get(index).flip();
            } else {
                mixed.addAll(this.cs.getMixedCards());
            }
            refreshReviewCard(pane);
        });
    }

    // EFFECTS: adds event listener that increments wrong count in review ui
    // MODIFIES: wrong, this
    private void wrongButtonAddListener(JButton wrong, ReviewCardPane pane) {
        wrong.addActionListener(e -> {
            if (mixed.size() != 0) {
                if (!isChecked) {
                    mixed.get(index).incrementWrongCount();
                    isChecked = true;
                    disableButtons();
                }
            }
            refreshReviewCard(pane);
        });
    }

    // EFFECTS: adds event listener that increments correct count in review ui
    // MODIFIES: right, this
    private void rightButtonAddListener(JButton right, ReviewCardPane pane) {
        right.addActionListener(e -> {
            if (mixed.size() != 0) {
                if (!isChecked) {
                    mixed.get(index).incrementCorrectCount();
                    isChecked = true;
                    disableButtons();
                }
            }
            refreshReviewCard(pane);
        });
    }

    // EFFECTS: disable correctness buttons after selection based on isChecked
    // MODIFIES: this
    private void disableButtons() {
        this.right.setEnabled(!this.isChecked);
        this.wrong.setEnabled(!this.isChecked);
    }
}
