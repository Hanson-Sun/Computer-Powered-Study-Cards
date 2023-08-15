package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

// Tests for the CardSet class
public class CardSetTest {

    CardSet cs;
    CardSet csEmpty;

    @BeforeEach
    public void setup() {
        cs = generateTestCards("testset");
        csEmpty = new CardSet("empty");
    }

    // EFFECTS: generate test cardset
    private CardSet generateTestCards(String name) {
        CardSet testCardSet = new CardSet(name);
        int cardSetLength = 9;
        for (int j = 1; j < cardSetLength; j++) {
            CardSide front = new CardSide();
            CardSide back = new CardSide();
            front.setContent("Front " + j);
            back.setContent("Back " + j);
            Card testCard = new Card(front, back);
            testCardSet.addCard(testCard);
        }
        return testCardSet;
    }

    @Test
    public void CardSetConstructorTest() {
        assertEquals("empty", csEmpty.getTitle());
        assertEquals("testset", cs.getTitle());
        int i = 0;
        for (Card c : cs.getCards()) {
            i++;
            assertEquals("Front " + i, c.getFront().getContent());
            assertEquals("Back " + i, c.getBack().getContent());
        }
    }

    @Test
    public void getMixedCardTest() {
        ArrayList<Card> mixedCards;
        for (int i = 0; i < 100; i++) {
            mixedCards = cs.getMixedCards();
            assertEquals(cs.getCardSetLength(), mixedCards.size());
            for (Card c : cs.getCards()) {
                assertTrue(mixedCards.contains(c));
            }
        }
    }

    @Test
    public void setCardFlippedStateTest() {
        cs.setCardFlippedState(true);
        for (Card c : cs.getCards()) {
            assertTrue(c.getState());
        }
        cs.setCardFlippedState(false);
        for (Card c : cs.getCards()) {
            assertFalse(c.getState());
        }
    }

    @Test
    public void incrementUseCount() {
        assertEquals(0, cs.getUseCount());
        cs.incrementUseCount();
        assertEquals(1, cs.getUseCount());
        cs.incrementUseCount();
        assertEquals(2, cs.getUseCount());
    }

    @Test
    public void equalsTest() {
        assertEquals(cs, new CardSet("testset"));
        assertEquals(csEmpty, new CardSet("empty"));

        assertNotEquals(cs, new CardSet("nonexistent"));
        // Need to test not equals when a wrong object is used to cover all branches
        assertNotEquals(cs, new CardSide());
    }

    // EFFECTS: returns true if there is a card in the carset with the same front and back content
    private boolean containsCardContent(CardSet cs, String front, String back) {
        for (Card c : cs.getCards()) {
            if (c.getFront().getContent().equals(front) && c.getBack().getContent().equals(back)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns true if there is the same card object in the cardset
    private boolean containsCardContent(CardSet cs, Card card) {
        for (Card c : cs.getCards()) {
            if (c.equals(card)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void deleteCardByIndexTest() {
        assertTrue(containsCardContent(cs, "Front 1", "Back 1"));
        assertTrue(containsCardContent(cs, "Front 3", "Back 3"));
        assertTrue(containsCardContent(cs, "Front 8", "Back 8"));
        cs.deleteCard(7);
        assertFalse(containsCardContent(cs, "Front 8", "Back 8"));
        cs.deleteCard(2);
        assertFalse(containsCardContent(cs, "Front 3", "Back 3"));
        cs.deleteCard(0);
        assertFalse(containsCardContent(cs, "Front 1", "Back 1"));
    }

    @Test
    public void deleteCardByCardTest() {
        Card temp1 = cs.getCardByIndex(0);
        Card temp2 = cs.getCardByIndex(2);
        Card temp3 = cs.getCardByIndex(7);
        assertTrue(containsCardContent(cs, temp1));
        assertTrue(containsCardContent(cs, temp2));
        assertTrue(containsCardContent(cs, temp3));
        cs.deleteCard(temp3);
        assertFalse(containsCardContent(cs, temp3));
        cs.deleteCard(temp2);
        assertFalse(containsCardContent(cs, temp2));
        cs.deleteCard(temp1);
        assertFalse(containsCardContent(cs, temp1));

    }


    @Test
    public void getCardSetCompletenessTest() {
        assertEquals(0, cs.getAverageAccuracy());
        for (Card c : cs.getCards()) {
            c.incrementCorrectCount();
        }
        assertEquals(1, cs.getAverageAccuracy());
        for (Card c: cs.getCards()) {
            c.incrementWrongCount();
        }
        assertEquals(0.5, cs.getAverageAccuracy());
    }

    @Test
    public void getCardByIndexTest() {
        assertTrue(compareCardValues(cs.getCardByIndex(0), "Front 1", "Back 1"));
        assertTrue(compareCardValues(cs.getCardByIndex(2), "Front 3", "Back 3"));
        assertTrue(compareCardValues(cs.getCardByIndex(7), "Front 8", "Back 8"));
    }

    private boolean compareCardValues(Card c, String front, String back) {
        return c.getFront().getContent().equals(front) && c.getBack().getContent().equals(back);
    }

    @Test
    public void getCardSetLengthTest() {
        assertEquals(0, csEmpty.getCardSetLength());
        assertEquals(8, cs.getCardSetLength());
    }

    @Test
    public void setUseCountTest() {
        cs.setUseCount(100);
        assertEquals(100, cs.getUseCount());
        cs.setUseCount(1000);
        assertEquals(1000, cs.getUseCount());
    }

    @Test
    public void isValidNameTest() {
        assertTrue(CardSet.isValidName("bruhomoment"));
        assertTrue(CardSet.isValidName("123 oandowin 12_ -asd32 sdf"));
        assertFalse(CardSet.isValidName("12(*(79ds"));
        assertFalse(CardSet.isValidName("name!?"));
    }

}
