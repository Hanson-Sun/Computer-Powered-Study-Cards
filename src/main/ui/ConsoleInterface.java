package ui;

import model.Card;
import model.CardSet;
import model.CardSetsMenu;
import model.CardSide;
import persistence.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

// The main console user interface of the flashcard application.
public class ConsoleInterface {
    private final Scanner sc = new Scanner(System.in);
    private CardSetsMenu cardSetsMenu;
    private CardSet selectedCardSet;
    private static final String CSM_DATA = "./data/csm.cpsc";
    private static final String CARDSET_FOLDER = "./data/CardSets/";
    private static final String SUFFIX = ".cpsc";
    private static JsonUtils util;

    // EFFECTS: instantiate a new console interface with a loaded CarSetMenu
    public ConsoleInterface() {
        util = new JsonUtils(CSM_DATA, CARDSET_FOLDER, SUFFIX);
        this.cardSetsMenu = new CardSetsMenu();
        //this.cardSetsMenu = readCardSetMenu();
        // generate function can be uncommented to generate a fresh set of cardSets
        generateTestSets(this.cardSetsMenu);
        start();
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

    // The following generation functions are used to create data for fast user testing
    //EFFECTS: A function to generate cardSets to help user testing.
    //MODIFIES: csm
    private void generateTestSets(CardSetsMenu csm) {
        try {
            for (int i = 1; i <= 10; i++) {
                csm.addCardSet(generateTestCards(i));
                util.saveCardSet(generateTestCards(i));
            }
            util.saveCardSetMenu(csm);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    //EFFECTS:  A function used to generate cards in a cardSet for user testing
    private CardSet generateTestCards(int i) {
        CardSet testCardSet = new CardSet("Card set " + i);
        for (int j = 1; j < 9; j++) {
            CardSide front = new CardSide();
            CardSide back = new CardSide();
            front.setContent("Set " + i + " Front " + j);
            back.setContent("Set " + i + " Back " + j);
            Card testCard = new Card(front, back);
            testCardSet.addCard(testCard);
        }
        return testCardSet;
    }

    // EFFECTS: saves the current state of the program (the cardSetMenu + currentCardSet); throws a runTimeException
    //          if the save fails.
    private void saveState() {
        try {
            if (this.selectedCardSet != null) {
                util.saveCardSet(this.selectedCardSet);
            }
            util.saveCardSetMenu(this.cardSetsMenu);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // EFFECTS: deletes the selectedCardSet from memory and the CardSetMenu + reassigns the current cs selection to null
    // MODIFIES: this
    private void deleteCardSet(CardSet cs) {
        this.selectedCardSet = null;
        util.deleteCardSet(cs);
        saveState();
    }

    // EFFECTS: return the integer user input and "absorbs" newline character
    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("That was not an integer! Please try again");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: loads all card data into menu
    private void loadCardSet(CardSetsMenu csm) {
        for (CardSet cs : csm.getCardSets()) {
            CardSet n = readCardSet(cs.getTitle());
            csm.setCardSet(csm.getCardSets().indexOf(cs), n);
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads NEW cardSetMenu and WIPES everything --> creates a new program state
    private void loadNewCardSet() {
        for (CardSet cs : this.cardSetsMenu.getCardSets()) {
            util.deleteCardSet(cs);
        }
        this.cardSetsMenu = new CardSetsMenu();
        saveState();
        System.out.println("\nNew state loaded!\n");
    }


    // EFFECTS: print the main menu of the program.
    private void printMainMenu() {
        System.out.println("Hello, welcome to CPSC \nWhat would you like to do?");
        System.out.println("[1] View CardSet Menu (loads menu)");
        System.out.println("[2] Make New CardSet (this automatically loads existing menu)");
        System.out.println("[3] Save Changes");
        System.out.println("[4] Load All Existing CardSets (loads the full data)");
        System.out.println("[5] Load New State (opens a new program state, previous data will be lost)");
        System.out.println("[0] exit");
    }

    // EFFECTS: begin the starting menu of the program
    private void start() {
        while (true) {
            int choice;
            printMainMenu();
            choice = getIntInput();
            if (choice == 1) {
                seeCardSets();
            } else if (choice == 2) {
                makeNewCardSet();
            } else if (choice == 3) {
                saveState();
                System.out.println("All changes have been saved!\n");
            } else if (choice == 4) {
                loadCardSet(this.cardSetsMenu);
            } else if (choice == 5) {
                loadNewCardSet();
            } else if (choice == 0) {
                System.out.println("Thanks for using CPSC, bye bye!!");
                saveState();
                break;
            } else {
                System.out.println(choice + " is not a valid option, please try again");
            }
        }
    }

    // EFFECTS: lead user to create menu if there are no cardsets
    private void seeCardSetEmpty() {
        while (true) {
            System.out.println("You have no card sets, would you like to make a new one?");
            System.out.println("[1] Make a new card set");
            System.out.println("[0] go back");
            int choice = getIntInput();
            if (choice == 1) {
                makeNewCardSet();
            } else if (choice == 0) {
                saveState();
                break;
            }
        }
    }

    // MODIFIES: prints the menu for when a cardSet is selected
    private void cardSetSelectedMenu(String title) {
        System.out.println("What do you want to do with card set, " + "'" + title + "'");
        System.out.println("[1] Review card set");
        System.out.println("[2] Edit card set");
        System.out.println("[3] Delete card set");
        System.out.println("[4] View card set contents and stats");
        System.out.println("[5] Save CardSet");
        System.out.println("[0] go back");
    }

    // EFFECTS: deletes the selected cardSet from menu and all its stored data
    // MODIFIES: this, currentCardSet
    private void deleteSelectedCard(CardSet currentCardSet) {
        cardSetsMenu.deleteCardSet(currentCardSet);
        deleteCardSet(currentCardSet);
    }

    // EFFECTS: display menu to pick action for a chosen card set
    // MODIFIES: currentCardSet, this
    private void cardSetSelected(CardSet currentCardSet) {
        while (true) {
            cardSetSelectedMenu(currentCardSet.getTitle());
            currentCardSet.incrementUseCount();
            int csChoice = getIntInput();
            if (csChoice == 1) {
                reviewCardSet(currentCardSet);
            } else if (csChoice == 2) {
                editCardSet(currentCardSet);
            } else if (csChoice == 3) {
                deleteSelectedCard(currentCardSet);
                break;
            } else if (csChoice == 4) {
                seeCardStats(currentCardSet);
            } else if (csChoice == 5) {
                saveState();
                System.out.println("All changes have been saved! \n");
            } else if (csChoice == 0) {
                saveState();
                break;
            } else {
                System.out.println(csChoice + " is not a valid choice, please pick again");
            }
        }
    }

    // EFFECTS: display the stats of the cardSet, including the accuracy of each card and how much the cardSet is used
    private void seeCardStats(CardSet currentCardSet) {
        System.out.println("The cardSet '" + currentCardSet.getTitle() + "' has been selected "
                + currentCardSet.getUseCount() + " times. It is currently "
                + (currentCardSet.getAverageAccuracy() * 100) + "% accurate");
        for (int i = 1; i <= currentCardSet.getCardSetLength(); i++) {
            Card c = currentCardSet.getCardByIndex(i - 1);
            System.out.println("Card " + i + ": ");
            System.out.println(c.getFront().getContent() + " : " + c.getBack().getContent());
            int sum = (c.getCorrectCount() + c.getWrongCount());
            if (sum != 0) {
                System.out.println("Reviewed: " + (c.getCorrectCount() + c.getWrongCount()) + " times; "
                        + "Accuracy: " + (c.getAccuracy() * 100) + "%");
            } else {
                System.out.println("This Card has not been reviewed yet");
            }
        }
        System.out.println();
    }

    // EFFECTS: selects a cardSet from file
    // MODIFIES: this
    private void selectCardSet(int choice) {
        int i = choice - 1;
        this.cardSetsMenu.setCardSet(i, readCardSet(this.cardSetsMenu.getCardSet(i).getTitle()));
        this.selectedCardSet = this.cardSetsMenu.getCardSet(i);
    }

    // EFFECTS: sorts and displays the cardSets inside a cardsetmenu and allow users to select a given cardset
    // MODIFIES: this
    private void seeCardSets() {
        if (this.cardSetsMenu.getCardSets().size() == 0) {
            seeCardSetEmpty();
        } else {
            while (true) {
                this.cardSetsMenu.sortCardSet();
                int count;
                for (count = 1; count <= this.cardSetsMenu.getCardSetLength(); count++) {
                    CardSet cs = this.cardSetsMenu.getCardSet(count - 1);
                    System.out.println(count + ". " + cs.getTitle());
                }
                System.out.println("[0] go back\nWhich cardSet would you like to select and LOAD? (enter card number)");
                int choice = getIntInput();
                if (choice < count && choice >= 1) {
                    selectCardSet(choice);
                    cardSetSelected(this.selectedCardSet);
                } else if (choice == 0) {
                    saveState();
                    return;
                } else {
                    System.out.println(choice + " is not a valid choice, please pick again");
                }
            }
        }
    }

    // EFFECTS: begin process to add new cards in a new CardSet
    // MODIFIES: newCardSet
    private void addNewCardInNewCardSet(CardSet newCardSet) {
        while (true) {
            System.out.println("[1] add new card\n[0] go back");
            int cardChoice = getIntInput();
            if (cardChoice == 1) {
                makeNewCard(newCardSet);
            } else if (cardChoice == 0) {
                saveState();
                break;
            } else {
                System.out.println(cardChoice + " is not a valid option, please try again");
            }
        }
    }

    // EFFECTS: create new cardset and add it to the menu, also allow users to add new cards in the cardset
    // MODIFIES: this
    private void makeNewCardSet() {
        CardSet newCardSet;
        while (true) {
            System.out.println("Card set maker:\nWhat is the title of this card set?");
            String title = sc.nextLine();
            if (cardSetsMenu.containsCardSetWithName(title)) {
                System.out.println("A card set already has the title: " + "\"" + title + "\"\nPlease try again");
            }
            if (!CardSet.isValidName(title)) {
                System.out.println(title + ": is not a valid name, please try again");
            } else {
                newCardSet = new CardSet(title);
                this.selectedCardSet = newCardSet;
                cardSetsMenu.addCardSet(newCardSet);
                saveState();
                break;
            }
        }
        addNewCardInNewCardSet(newCardSet);
    }

    // EFFECTS: create and adds new card to a cardset
    // MODIFIES: newCardSet
    private void makeNewCard(CardSet newCardSet) {
        System.out.println("Enter the value for the front of the card");
        String frontText = sc.nextLine();
        System.out.println("Enter the value for the back of the card");
        String backText = sc.nextLine();
        CardSide front = new CardSide();
        CardSide back = new CardSide();
        front.setContent(frontText);
        back.setContent(backText);
        Card newCard = new Card(front, back);
        newCardSet.addCard(newCard);
        System.out.println("New card added");
    }

    // EFFECTS: display the review options for a cardset
    // MODIFIES: currentCardSet
    private boolean checkReviewType(CardSet currentCardSet) {
        while (true) {
            System.out.println("Do you want to show the front or the back first?");
            System.out.println("[1] front");
            System.out.println("[2] back");
            System.out.println("[0] exit review");
            int choice = getIntInput();
            if (choice == 1) {
                currentCardSet.setCardFlippedState(false);
                break;
            } else if (choice == 2) {
                currentCardSet.setCardFlippedState(true);
                break;
            } else if (choice == 0) {
                saveState();
                return false;
            } else {
                System.out.println(choice + " is not a valid option, please try again");
            }
        }
        return true;
    }

    // EFFECTS: start the review process for a cardset
    private void reviewCardSet(CardSet currentCardSet) {
        boolean cont = checkReviewType(currentCardSet);
        if (cont) {
            System.out.println("Press enter to begin");
            sc.nextLine();
            ArrayList<Card> mixedCards = currentCardSet.getMixedCards();
            for (Card c : mixedCards) {
                System.out.println("\n" + c.getCardFace().getContent() + "\nType your answer or just press enter");
                sc.nextLine();
                c.flip();
                System.out.println(c.getCardFace().getContent());
                if (verifyCardCorrectnessHelper(c)) {
                    break;
                }
            }
            saveState();
            System.out.println("Review completed");
        }
    }

    // EFFECTS: displays options for a card after review process
    //          allows user to check whether they got it correct, wrong, or want to exit
    // MODIFIES: c
    private boolean verifyCardCorrectnessHelper(Card c) {
        while (true) {
            System.out.println("Were you correct? \n[1] correct; [2] wrong; [0] exit review");
            int choice = getIntInput();
            if (choice == 1) {
                c.incrementCorrectCount();
                break;
            } else if (choice == 2) {
                c.incrementWrongCount();
                break;
            } else if (choice == 0) {
                return true;
            } else {
                System.out.println(choice + " is not valid, please try again");
            }
        }
        return false;
    }

    // EFFECTS: display options to edit a cardset:
    //           add new card, delete card, edit card, or go back
    private void editCardSet(CardSet currentCardSet) {
        while (true) {
            System.out.println("How do you want to edit the card set, " + "'" + currentCardSet.getTitle() + "'");
            System.out.println("[1] Add new card");
            System.out.println("[2] Edit Card");
            System.out.println("[3] Delete Card");
            System.out.println("[0] go back");
            int choice = getIntInput();
            if (choice == 1) {
                makeNewCard(currentCardSet);
            } else if (choice == 2) {
                editCardSetViewCards(currentCardSet);
            } else if (choice == 3) {
                deleteCardSetViewCards(currentCardSet);
            } else if (choice == 0) {
                saveState();
                break;
            } else {
                System.out.println(choice + " is not a valid input, please try again");
            }
        }
    }

    // EFFECTS: display options to view and delete the card in a cardset
    // MODIFIES: currentCardSet
    private void deleteCardSetViewCards(CardSet currentCardSet) {
        while (true) {
            ArrayList<Card> cards = currentCardSet.getCards();
            int i = 1;
            for (Card c : cards) {
                System.out.println("Card " + i + ":");
                System.out.println(c.getFront().getContent() + " : " + c.getBack().getContent());
                i++;
            }
            System.out.println("Which card do you want to delete? (enter card number to select, enter 0 to exit)");
            int choice = getIntInput();
            if (choice >= 1 && choice <= i) {
                currentCardSet.deleteCard(choice - 1);
            } else if (choice == 0) {
                saveState();
                break;
            } else {
                System.out.println(choice + " is not a valid option, please pick again");
            }
        }
    }

    // EFFECTS: display options to see cards, and pick a card to edit in a cardset
    private void editCardSetViewCards(CardSet currentCardSet) {
        while (true) {
            ArrayList<Card> cards = currentCardSet.getCards();
            int i = 1;
            for (Card c : cards) {
                System.out.println("Card " + i + ":");
                System.out.println(c.getFront().getContent() + " : " + c.getBack().getContent());
                i++;
            }
            System.out.println("Which card do you want to edit? (enter card number to select; enter 0 to exit)");
            int choice = getIntInput();
            if (choice >= 1 && choice <= i) {
                editCard(currentCardSet, choice - 1);
            } else if (choice == 0) {
                saveState();
                break;
            } else {
                System.out.println(choice + " is not a valid option, please pick again");
            }
        }
    }

    // EFFECTS: display the options to change a card at index i in a cardset
    private void editCard(CardSet c, int i) {
        Card card = c.getCardByIndex(i);
        CardSide cardFront = card.getFront();
        CardSide cardBack = card.getBack();
        System.out.println("The front of the card is: ");
        System.out.println(cardFront.getContent());
        System.out.println("Do you want to change this side?");
        changeCardSide(cardFront);
        System.out.println("The back of the card is: ");
        System.out.println(cardBack.getContent());
        System.out.println("Do you want to change this side?");
        changeCardSide(cardBack);
    }

    // EFFECTS: change the content of a cardside
    // MODIFIES: cs
    private void changeCardSide(CardSide cs) {
        while (true) {
            System.out.println("[1] yes");
            System.out.println("[2] no");
            int choice = getIntInput();
            if (choice == 1) {
                System.out.println("Enter the new content for this card face:");
                String content = sc.nextLine();
                cs.setContent(content);
                break;
            } else if (choice == 2) {
                break;
            } else {
                System.out.println(choice + " is not a valid option, please try again");
            }
        }
    }
}
