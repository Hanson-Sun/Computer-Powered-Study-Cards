package model;

import org.json.JSONObject;
import persistence.Writable;

// Card represents a two-sided flash card. Each card contains information of each side, whether it is flipped or not,
// and how often the user reviews this card correctly.
public class Card implements Writable {
    private final CardSide front;
    private final CardSide back;
    private boolean state;
    // TODO: utilize correctness to have a targeted study mode (a variation of the SuperMemo algorithm)
    private int correctCount;
    private int wrongCount;

    // MODIFIES: this
    // EFFECTS: instantiate new Card object
    public Card(CardSide front, CardSide back) {
        this.front = front;
        this.back = back;
        this.state = false;
        this.correctCount = 0;
        this.wrongCount = 0;
    }

    // MODIFIES: this
    // EFFECTS: flips the card state; false -> true and vice versa
    public void flip() {
        this.state = !this.state;
    }

    // EFFECTS: returns which face is facing up (front if state  = false. back if state = true)
    public CardSide getCardFace() {
        if (!this.state) {
            return this.front;
        } else {
            return this.back;
        }
    }

    // EFFECTS: returns the decimal percentage of the accuracy of this card. If card has not been reviewed, return -1
    public double getAccuracy() {
        double sum = this.correctCount + this.wrongCount;
        if (sum != 0) {
            return (double) this.correctCount / sum;
        }
        return -1;
    }

    // MODIFIES: this
    // EFFECTS: increase correct count by 1
    public void incrementCorrectCount() {
        this.correctCount += 1;
    }

    // MODIFIES: this
    // EFFECTS: increase wrong count by 1
    public void incrementWrongCount() {
        this.wrongCount += 1;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("state", this.state);
        json.put("correctCount", this.correctCount);
        json.put("wrongCount", this.wrongCount);
        json.put("front", front.toJson());
        json.put("back", back.toJson());
        return json;
    }

    // simple getters and setters
    public CardSide getFront() {
        return front;
    }

    public int getCorrectCount() {
        return this.correctCount;
    }

    public int getWrongCount() {
        return this.wrongCount;
    }

    public void setCorrectCount(Integer n) {
        this.correctCount = n;
    }

    public void setWrongCount(Integer n) {
        this.wrongCount = n;
    }

    public CardSide getBack() {
        return back;
    }

    public boolean getState() {
        return state;
    }

    public void setState(Boolean s) {
        this.state = s;
    }
}
