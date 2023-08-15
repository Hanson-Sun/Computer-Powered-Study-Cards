package ui;

import javax.swing.*;

// Enum that holds all the icon assets
public enum IconPackage {

    LEFT_ARROW(new ImageIcon("./data/assets/arrow.png", "Back")),
    CPSC_MAIN(new ImageIcon("./data/assets/logo.PNG", "Logo")),
    RIGHT_ARROW(new ImageIcon("./data/assets/right-arrow.png", "Next")),
    ADD_CARD(new ImageIcon("./data/assets/credit-cards.png", "Add card")),
    ADD(new ImageIcon("./data/assets/add-file.png", "Add card Set")),
    LOAD(new ImageIcon("./data/assets/download.png", "Load data")),
    NEW_LOAD(new ImageIcon("./data/assets/add-folder.png", "load new data")),
    CANCEL(new ImageIcon("./data/assets/cancel.png", "cancel")),
    CHECK(new ImageIcon("./data/assets/check-mark.png", "Check")),
    CROSS(new ImageIcon("./data/assets/incorrect.png", "Cross")),
    DELETE(new ImageIcon("./data/assets/delete.png", "Delete")),
    EXIT(new ImageIcon("./data/assets/logout.png", "Exit")),
    FLIP(new ImageIcon("./data/assets/repeat.png", "Flip")),
    CPSC(new ImageIcon("./data/assets/brain.png", "CPSC")),
    CARDS(new ImageIcon("./data/assets/flash-card.png", "flash cards")),
    STATS(new ImageIcon("./data/assets/statistic.png", "Stats"));

    public final ImageIcon icon;

    // EFFECTS: sets the icon value of each enum
    private IconPackage(ImageIcon icon) {
        this.icon = icon;
    }

}
