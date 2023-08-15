package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Random;

// CardSet represents a set of two-sided cards. It is essentially a unique set of flashcards with a title.
// It also contains how many times this cardSet has been selected.
public class CardSet implements Writable {

    private final ArrayList<Card> cards;
    private final String title;
    private int useCount;

    // MODIFIES: this
    // EFFECTS: instantiate new CardSet object
    public CardSet(String title) {
        this.title = title;
        this.cards = new ArrayList<>();
        this.useCount = 0;
    }

    // EFFECT: return a CardSet with the cards in a random order
    public ArrayList<Card> getMixedCards() {
        ArrayList<Card> cardsCopy = new ArrayList<>(cards);
        Random rand = new Random();
        for (int i = 0; i < cardsCopy.size(); i++) {
            int randomIndexToSwap = rand.nextInt(cardsCopy.size());
            Card temp = cardsCopy.get(randomIndexToSwap);
            cardsCopy.set(randomIndexToSwap, cardsCopy.get(i));
            cardsCopy.set(i, temp);
        }
        return cardsCopy;
    }

    // TODO: create a sorting algorithm that utilize correctness to have a targeted study mode
    //  (a variation of the SuperMemo algorithm) --> for next phase !!
    // EFFECTS: return a weighted sorting of each card based on the correctness
    // public ArrayList<Card> getWeightedMixedCards() {}

    // MODIFIES: this
    // EFFECT: sets the card state of all cards in the cardSet to the specified state
    public void setCardFlippedState(Boolean state) {
        for (Card c : cards) {
            c.setState(state);
        }
    }

    // MODIFIES: this
    // EFFECT: increase use count by one
    public void incrementUseCount() {
        this.useCount += 1;
    }

    // TODO: this function might change with the implementation of the SuperMemo Algorithm
    // EFFECTS: returns the overall accuracy of the cardset (average of the accuracy of each reviewed card)
    // A card is considered to have 0 accuracy if it is not reviewed.
    public double getAverageAccuracy() {
        double sum = 0;
        for (Card c : this.cards) {
            if (c.getAccuracy() != (double) -1) {
                sum += c.getAccuracy();
            }
        }
        return sum / this.getCardSetLength();
    }

    // EFFECT: overrides equals method from super class, such that it compares equality of
    // cardsets by comparing titles only
    @Override
    public boolean equals(Object that) {
        if (that instanceof CardSet) {
            return this.title.equals(((CardSet) that).getTitle());
        }
        return false;
    }

    // EFFECTS: returns true if the name is a valid file name
    public static Boolean isValidName(String name) {
        String pattern = "^[a-zA-Z0-9._ -]+";
        return name.matches(pattern);
    }

    // MODIFIES: this
    // EFFECTS: adds a card to the cardset
    public void addCard(Card c) {
        EventLog.getInstance().logEvent(new Event("New Card '" + c + "' added to CardSet '" + title + "'"));
        cards.add(c);
    }

    // MODIFIES: this
    // EFFECTS: delete card at index i from cardset
    public void deleteCard(int i) {
        EventLog.getInstance().logEvent(new Event("Card " + i + " has been deleted"));
        this.cards.remove(i);
    }

    // MODIFIES: this
    // EFFECTS: delete card c from cardset
    public void deleteCard(Card c) {
        EventLog.getInstance().logEvent(new Event("Card " + this.cards.indexOf(c) + " has been deleted"));
        this.cards.remove(c);
    }

    // EFFECTS: gets card in cardset of index i
    public Card getCardByIndex(int i) {
        return cards.get(i);
    }

    // EFFECTS: return the amount of cards in the cardset
    public int getCardSetLength() {
        return cards.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("usecount", useCount);
        json.put("cards", cardsToJson());
        return json;
    }

    // EFFECTS: convert a CardSet into a simple json object (empty cards), for CardSetMenu storage
    public JSONObject toSimpleJson() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("usecount", useCount);
        json.put("cards", new JSONArray());
        return json;
    }

    // EFFECTS: converts a list of cards in a json array
    private JSONArray cardsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Card c : cards) {
            jsonArray.put(c.toJson());
        }
        return jsonArray;
    }

    // simple getters and setters
    public int getUseCount() {
        return this.useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public String getTitle() {
        return title;
    }
}
