package ui;

import model.*;
import model.Event;
import persistence.JsonUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

// TODO: fix edit card menu
// TODO: refactor code
// TODO: fix flow layout height issue (P4)

// Main GUI class
public class MenuUI extends JFrame {
    private CardSetsMenu csm;
    private CardSet cs;
    private JPanel currentPanel;
    private MenuBar currentMenuBar;
    private static final String CSM_DATA = "./data/csm.cpsc";
    private static final String CARDSET_FOLDER = "./data/CardSets/";
    private static final String SUFFIX = ".cpsc";
    private static final JsonUtils util = new JsonUtils(CSM_DATA, CARDSET_FOLDER, SUFFIX);
    private static final int width = 900;
    private static final int height = 700;
    private boolean load = false;

    // EFFECTS: instantiate new GUI menu with preloaded data
    public MenuUI() {
        this.csm = readCardSetMenu();
        this.currentMenuBar = new MenuBar();
        this.currentPanel = new JPanel();
        this.setPreferredSize(new Dimension(width, height));
        this.setIconImage(IconPackage.CPSC.icon.getImage());
        Object[] options = {"Load Data", "Load Later", "Leave"};
        int choice = JOptionPane.showOptionDialog(this, null, "Welcome to CPSC",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, IconPackage.CPSC_MAIN.icon,
                options, options[2]);
        if (choice == 0) {
            this.load = true;
        } else if (choice == 2) {
            System.exit(0);
        }
        displayCsmMenu();
        addWindowExitListener();
    }

    // EFFECTS: reads the cardSetMenu from file and returns it; throws a RunTimeException if it cannot be read
    private CardSetsMenu readCardSetMenu() {
        try {
            return util.readCardSetMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: read a cardSet with a given title from files; throws a RunTimeException if it cannot be read.
    private CardSet readCardSet(String title) {
        try {
            return util.readCardSet(title);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: create the JMenuBar buttons for the CardSetMenu selection
    // MODIFIES: container
    private void createCsmButtons(Container container) {
        JButton menuLoad = new MenuButton(IconPackage.LOAD.icon);
        JButton menuNewLoad = new MenuButton(IconPackage.NEW_LOAD.icon);
        JButton menuNewCs = new MenuButton(IconPackage.ADD.icon);
        JButton menuSave = new MenuButton((ImageIcon) UIManager.getIcon("FileView.floppyDriveIcon"));
        menuLoad.addActionListener(e -> {
            loadCardSetsData();
            this.load = true;
            JOptionPane.showMessageDialog(this, "Program data loaded");
        });
        menuNewLoad.addActionListener(e -> loadNewData());
        menuNewCs.addActionListener(e -> displayMakeCSMenu());
        menuSave.addActionListener(e -> saveState(true));
        container.add(menuLoad);
        container.add(menuNewLoad);
        container.add(menuNewCs);
        container.add(menuSave);
    }

    // EFFECTS: create the JMenuBar buttons for the CardSet selection
    // MODIFIES: container
    private void createCSButtons(Container container) {
        JButton menuAddNewCard = new MenuButton(IconPackage.ADD_CARD.icon);
        JButton menuReviewCs = new MenuButton(IconPackage.CARDS.icon);
        JButton menuSeeCsStats = new MenuButton(IconPackage.STATS.icon);
        JButton menuDeleteCs = new MenuButton(IconPackage.DELETE.icon);
        JButton menuBack = new MenuButton(IconPackage.LEFT_ARROW.icon);
        menuAddNewCard.addActionListener(e -> displayMakeCardMenu());
        menuReviewCs.addActionListener(e -> displayCardSetReviewMenu());
        menuSeeCsStats.addActionListener(e -> displayCardStatsMenu());
        menuDeleteCs.addActionListener(e -> deleteCardSet());
        menuBack.addActionListener(e -> displayCsmMenu());
        container.add(menuBack);
        container.add(menuAddNewCard);
        container.add(menuReviewCs);
        container.add(menuSeeCsStats);
        container.add(menuDeleteCs);

    }

    // EFFECTS: create the JMenuBar buttons for the edit card selection
    // MODIFIES: this
    private void createEditCardButtons(Card c, Container container) {
        JButton menuSaveCard = new MenuButton((ImageIcon) UIManager.getIcon("FileView.floppyDriveIcon"));
        JButton menuDeleteCard = new MenuButton(IconPackage.DELETE.icon);
        JButton menuBack = new MenuButton(IconPackage.LEFT_ARROW.icon);
        menuSaveCard.addActionListener(e -> saveCardChanges(c));
        menuDeleteCard.addActionListener(e -> deleteCard(c));
        menuBack.addActionListener(e -> displayCSMenu(this.cs.getTitle()));
        container.add(menuBack);
        container.add(menuSaveCard);
        container.add(menuDeleteCard);
    }

    // EFFECTS: create the JMenuBar buttons for the create new cs selection
    // MODIFIES: container
    private void createMakeCsButtons(Container container, Container textField) {
        JButton menuSave = new MenuButton((ImageIcon) UIManager.getIcon("FileView.floppyDriveIcon"));
        JButton menuCancel = new MenuButton(IconPackage.CANCEL.icon);
        menuSave.addActionListener(e -> saveNewCs(textField));
        menuCancel.addActionListener(e -> displayCsmMenu());
        container.add(menuSave);
        container.add(menuCancel);
    }

    // EFFECTS: create the JMenuBar buttons for the make new card selection
    // MODIFIES: container
    private void createMakeCardButtons(Container container) {
        JButton menuNewCs = new MenuButton(IconPackage.ADD_CARD.icon);
        JButton menuCancel = new MenuButton(IconPackage.CANCEL.icon);
        menuNewCs.addActionListener(e -> addNewCard());
        menuCancel.addActionListener(e -> displayCSMenu(this.cs.getTitle()));
        container.add(menuNewCs);
        container.add(menuCancel);
    }

    // EFFECTS: adds newly created card
    // MODIFIES: this
    private void addNewCard() {
        Component[] components = this.currentPanel.getComponents();
        ArrayList<JTextArea> panes = new ArrayList<>();
        for (Component comp : components) {
            if (comp instanceof JTextArea) {
                panes.add((JTextArea) comp);
            }
        }
        JTextArea front = panes.get(0);
        JTextArea back = panes.get(1);
        CardSide cfront = new CardSide();
        CardSide cback = new CardSide();
        cfront.setContent(front.getText());
        cback.setContent(back.getText());
        Card newCard = new Card(cfront, cback);
        this.cs.addCard(newCard);
        saveState(false);
        displayCSMenu(this.cs.getTitle());
        JOptionPane.showMessageDialog(this, "New card has been added!");
    }

    // EFFECTS: saves new card set
    // MODIFIES: this
    private void saveNewCs(Container x) {
        JTextField textField = (JTextField) x.getComponent(1);
        String text = textField.getText();
        CardSet newCardSet = new CardSet(text);
        if (!CardSet.isValidName(text) || this.csm.containsCardSetWithName(text)) {
            if (this.csm.containsCardSetWithName(text)) {
                JOptionPane.showMessageDialog(this,
                        text + ": This name already exists!",  "CardSet Title Duplicate",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        text + ": is not a valid name, please try again", "CardSet Title Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            this.cs = newCardSet;
            this.csm.addCardSet(newCardSet);
            saveState(false);
            displayCsmMenu();
            loadCardSetsData();
            JOptionPane.showMessageDialog(this, "A new cardSet '" + text + "' has been created!");
        }
    }

    // EFFECTS: deletes card
    // MODIFIES: this, c
    private void deleteCard(Card c) {
        this.cs.deleteCard(c);
        saveState(true);
        displayCSMenu(this.cs.getTitle());
    }

    // EFFECTS: save changes to card
    // MODIFIES: this, c
    private void saveCardChanges(Card c) {
        Component[] components = this.currentPanel.getComponents();
        ArrayList<JTextArea> panes = new ArrayList<>();
        for (Component comp : components) {
            if (comp instanceof JTextArea) {
                panes.add((JTextArea) comp);
            }
        }
        JTextArea front = panes.get(0);
        JTextArea back = panes.get(1);
        c.getFront().setContent(front.getText());
        c.getBack().setContent(back.getText());
        saveState(true);
    }

    // EFFECTS: delete current card set
    // MODIFIES: this
    private void deleteCardSet() {
        this.csm.deleteCardSet(this.cs);
        util.deleteCardSet(this.cs);
        this.cs = null;
        saveState(false);
        displayCsmMenu();
        JOptionPane.showMessageDialog(this, "Card Set has been deleted!");
    }

    // EFFECTS: display CardSetMenu menu
    // MODIFIES: this
    private void displayCsmMenu() {
        this.currentPanel = new JPanel();
        this.currentPanel.setPreferredSize(new Dimension(200, height * 2));
        this.currentMenuBar = new MenuBar();
        clearFrame();
        this.currentPanel.setBorder(BorderFactory.createEmptyBorder());
        this.currentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        createCsmButtons(this.currentMenuBar);
        if (this.load) {
            loadCardSetsData();
        }
        refresh("CPSC - Main Menu");

    }

    // EFFECTS: display CardSet menu
    // MODIFIES: this
    private void displayCSMenu(String name) {
        this.currentPanel = new JPanel();
        this.currentMenuBar = new MenuBar();
        this.currentPanel.setPreferredSize(new Dimension(200, height * 2));
        this.cs = readCardSet(name);
        clearFrame();
        this.currentPanel.setBorder(BorderFactory.createEmptyBorder());
        // TODO: maybe implement grid bag layout
        this.currentPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        loadCardSetData(name);
        createCSButtons(this.currentMenuBar);
        refresh("CPSC - CardSet '" + name + "'");
    }

    // TODO: refactor to use JOptionPane
    // EFFECTS: display make CardSet menu
    // MODIFIES: this
    private void displayMakeCSMenu() {
        this.currentPanel = new JPanel();
        this.currentMenuBar = new MenuBar();
        clearFrame();
        this.currentPanel.setBorder(BorderFactory.createEmptyBorder());
        this.currentPanel.setLayout(new FlowLayout());
        loadCreateCsMenu();
        createMakeCsButtons(this.currentMenuBar, this.currentPanel);
        refresh("CPSC - Make new CardSet");
    }

    // EFFECTS: display CardSet stats in a pop-up window
    private void displayCardStatsMenu() {
        String text = ("The cardSet '" + this.cs.getTitle() + "' has been selected "
                + this.cs.getUseCount() + " times. \nIt is currently "
                + CardPane.round((this.cs.getAverageAccuracy() * 100),2) + "% accurate");
        JOptionPane.showMessageDialog(this, text);
    }

    // EFFECTS: display CardSet review menu
    // MODIFIES: this
    private void displayCardSetReviewMenu() {
        this.cs.setCardFlippedState(false);
        this.currentPanel = new ReviewCardSetUI(this.cs);
        this.currentMenuBar = new MenuBar();
        clearFrame();
        this.currentPanel.setBorder(BorderFactory.createEmptyBorder());
        this.currentPanel.setLayout(new BoxLayout(this.currentPanel, BoxLayout.Y_AXIS));
        createReviewCsButtons(this.currentMenuBar);
        refresh("Reviewing: '" + this.cs.getTitle() + "'");
    }


    // EFFECTS: create buttons for review CardSet
    private void createReviewCsButtons(Container container) {
        JButton menuExit = new MenuButton(IconPackage.EXIT.icon);
        menuExit.addActionListener(e -> {
            saveState(false);
            displayCSMenu(this.cs.getTitle());
        });
        container.add(menuExit);
    }

    // TODO: refactor to use JOptionPane
    // EFFECTS: load UI elements for creating new cs
    // MODIFIES: this
    private void loadCreateCsMenu() {
        JTextField input = new JTextField();
        input.setBorder(BorderFactory.createLineBorder(Color.black));
        input.setPreferredSize(new Dimension(200, 20));
        this.currentPanel.add(new JLabel("New CardSet title: "));
        this.currentPanel.add(input);
        this.currentPanel.revalidate();
        this.currentPanel.repaint();
    }

    // EFFECTS: load UI interface for making new card
    // MODIFIES: this
    private void displayMakeCardMenu() {
        this.currentPanel = new JPanel();
        this.currentMenuBar = new MenuBar();
        clearFrame();
        this.currentPanel.setBorder(BorderFactory.createEmptyBorder());
        this.currentPanel.setLayout(new BoxLayout(this.currentPanel,BoxLayout.Y_AXIS));
        loadMakeCardMenu();
        createMakeCardButtons(this.currentMenuBar);
        refresh("CPSC - Create New Card");
    }

    // EFFECTS: load UI elements for make new card interface
    // MODIFIES: this
    private void loadMakeCardMenu() {
        Container front = new CardEditPane("");
        Container back = new CardEditPane("");
        this.currentPanel.add(new JLabel("Front of Card"));
        this.currentPanel.add(front);
        this.currentPanel.add(new JLabel("Back of Card"));
        this.currentPanel.add(back);
    }


    // EFFECTS: load the UI interface for editing cards
    // MODIFIES: this
    private void displayEditCardMenu(Card c) {
        this.currentPanel = new JPanel();
        this.currentMenuBar = new MenuBar();
        clearFrame();
        this.currentPanel.setBorder(BorderFactory.createEmptyBorder());
        this.currentPanel.setLayout(new BoxLayout(this.currentPanel,BoxLayout.Y_AXIS));
        createEditCardButtons(c, this.currentMenuBar);
        loadEditCardInterface(c);
        refresh("CPSC - Edit Card");
    }

    // EFFECTS: loads UI elements for editing cards
    // MODIFIES: this
    private void loadEditCardInterface(Card c) {
        Container front = new CardEditPane(c.getFront().getContent());
        Container back = new CardEditPane(c.getBack().getContent());
        this.currentPanel.add(new JLabel("Front of Card"));
        this.currentPanel.add(front);
        this.currentPanel.add(new JLabel("Back of Card"));
        this.currentPanel.add(back);
    }

    // EFFECTS: loads UI elements that display each CardSet
    // MODIFIES: this
    private void loadCardSetsData() {
        this.csm = readCardSetMenu();
        this.currentPanel.removeAll();
        this.csm.sortCardSet();
        for (CardSet cs: this.csm.getCardSets()) {
            CardSetButton csb = new CardSetButton(cs.getTitle());
            addOpenCardSetListener(csb, cs.getTitle());
            this.currentPanel.add(csb);
        }
        this.pack();
        this.currentPanel.revalidate();
        this.currentPanel.repaint();
    }

    // EFFECTS: load UI elements for each card in a cardset
    // MODIFIES: this
    private void loadCardSetData(String name) {
        this.cs = readCardSet(name);
        this.currentPanel.removeAll();
        for (Card c : this.cs.getCards()) {
            CardPane csb = new CardPane(c);
            addOpenCardListener(csb, c);
            this.currentPanel.add(csb);
        }
        this.currentPanel.revalidate();
        this.currentPanel.repaint();
    }

    // EFFECTS: add event listener that opens cardset
    // MODIFIES: btn
    private void addOpenCardSetListener(JButton btn, String name) {
        btn.addActionListener(e -> {
            displayCSMenu(name);
            cs.incrementUseCount();
            csm.getCardSet(name).incrementUseCount();
            saveState(false);
        });
    }

    // EFFECTS: add mouse listener for selecting a card
    // MODIFIES: this, pane, c
    private void addOpenCardListener(CardPane pane, Card c) {
        pane.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayEditCardMenu(c);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pane.clickColorChange();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pane.clickReleaseColorChange();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pane.hoverColorChange();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pane.hoverColorChangeBack();
            }
        });
    }

    // EFFECTS: clears frame
    // MODIFIES: this
    private void clearFrame() {
        this.getContentPane().removeAll();
        this.repaint();
    }

    // EFFECTS: loads new program state
    // MODIFIES: this
    private void loadNewData() {
        int reply = JOptionPane.showConfirmDialog(
                this,
                "Are you certain you want to load new state? (All previous data will be lost)",
                "Load New State Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            for (CardSet cs : this.csm.getCardSets()) {
                util.deleteCardSet(cs);
            }
            this.csm = new CardSetsMenu();
            saveState(false);
            JOptionPane.showMessageDialog(this, "New state loaded! All data has been wiped!");
            loadCardSetsData();
        }
        this.load = true;
    }

    // EFFECTS: saves the current state of the program (the cardSetMenu + currentCardSet); throws a runTimeException
    //          if the save fails.
    private void saveState(Boolean message) {
        try {
            if (this.cs != null) {
                util.saveCardSet(this.cs);
            }
            util.saveCardSetMenu(this.csm);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (message) {
            JOptionPane.showMessageDialog(this, "Program state has been saved!");
        }
    }

    // EFFECTS: refresh the current panel and adds new changes
    // MODIFIES: this
    private void refresh(String name) {
        this.setJMenuBar(this.currentMenuBar);
        this.add(new JScrollPane(this.currentPanel,
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
                BorderLayout.CENTER);
        this.setDefaultCloseOperation(exit());
        this.setTitle(name);
        this.setPreferredSize(new Dimension(width, height));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    // EFFECTS: saves state, prints event logs, and exits the program
    private int exit() {
        saveState(false);
        return WindowConstants.EXIT_ON_CLOSE;
    }


    // EFFECTS: adds window listener to log events on close
    // MODIFIES: this
    private void addWindowExitListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event next : EventLog.getInstance()) {
                    System.out.println(next.toString());
                }
            }
        });
    }

}
