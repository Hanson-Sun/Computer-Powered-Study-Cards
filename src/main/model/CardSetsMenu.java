package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import java.util.ArrayList;


// CardSetsMenu is a collection of cardSets. It stores and organizes multiple flashcard sets, and it behaves
// as the main menu for the user.
public class CardSetsMenu implements Writable {
    private ArrayList<CardSet> cardSets;

    // MODIFIES: this
    // EFFECTS: instantiate new cardSets menu with empty cardSets
    public CardSetsMenu() {
        this.cardSets = new ArrayList<>();
    }

    // REQUIRES: the cardSet title does not already exist in the list of cardSets
    // MODIFIES: this
    // EFFECTS: adds a cardSet to the existing cardSet menu
    public void addCardSet(CardSet cardSet) {
        this.cardSets.add(cardSet);
        EventLog.getInstance().logEvent(new Event("CardSet '" + cardSet.getTitle() + "' has been added."));
    }

    // MODIFIES: this
    // EFFECTS: sorts the list of cardSets in the order of most commonly used (greatest usecount)
    public void sortCardSet() {
        this.cardSets.sort((cs1, cs2) -> Integer.compare(-cs1.getUseCount(), -cs2.getUseCount()));
    }

    // EFFECTS: returns true if name already exists in the list of cardSets
    public Boolean containsCardSetWithName(String name) {
        return cardSets.contains(new CardSet(name));
    }

    // REQUIRES: i must be a valid index of cardSets (0 <= i <= getCardSetLength()-1)
    // EFFECTS: get cardSet in cardMenu with index i
    public CardSet getCardSet(int i) {
        return cardSets.get(i);
    }

    // REQUIRES: cardSet title must be valid and exists in the menu
    // EFFECTS: get cardSet in cardMenu with title, if name does not match any cardSets, return null
    public CardSet getCardSet(String title) {
        for (CardSet cs: this.cardSets) {
            if (cs.getTitle().equals(title)) {
                return cs;
            }
        }
        return null;
    }

    // REQUIRES: i must be an index of menu (0 <= i <= getCardSetLength()-1)
    // MODIFIES: this
    // EFFECTS: remove cardset in menu by index
    public void deleteCardSet(int i) {
        EventLog.getInstance().logEvent(new Event("CardSet '"
                + this.cardSets.get(i).getTitle() + "' has been removed."));
        this.cardSets.remove(i);
    }

    // REQUIRES: cs must be a member of the menu
    // MODIFIES: this
    // EFFFECTS: removes cardet cs from menu
    public void deleteCardSet(CardSet cs) {
        EventLog.getInstance().logEvent(new Event("CardSet '" + cs.getTitle() + "' has been removed."));
        this.cardSets.remove(cs);
    }

    // EFFECTS: return the size of the menu
    public int getCardSetLength() {
        return this.cardSets.size();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("cardSets", convertCardSetsToJson());
        return json;
    }

    // EFFECTS: convert a list of cardSets into a json array
    private JSONArray convertCardSetsToJson() {
        JSONArray jsonArray = new JSONArray();
        sortCardSet();
        for (CardSet cs : this.cardSets) {
            jsonArray.put(cs.toSimpleJson());
        }
        return jsonArray;
    }

    // simple getters and setters
    public void setCardSets(ArrayList<CardSet> cardSet) {
        this.cardSets = cardSet;
    }

    public void setCardSet(int i, CardSet cardSet) {
        this.cardSets.set(i, cardSet);
    }

    public ArrayList<CardSet> getCardSets() {
        return this.cardSets;
    }
}
