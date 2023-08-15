package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

// Tests for the CardSetsMenu class
public class CardSetsMenuTest {
    private CardSetsMenu csm;
    private CardSetsMenu csmEmpty;

    //EFFECTS: A function to generate cardsets to help user testing.
    private void generateTestSets(CardSetsMenu csm){
        for (int i = 1; i <= 10; i++) {
            csm.addCardSet(generateTestCards(i));
        }
    }

    //EFFECTS:  A function used to generate cards in a cardset for user testing
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

    @BeforeEach
    public void setup() {
        csm = new CardSetsMenu();
        generateTestSets(csm);
        csmEmpty = new CardSetsMenu();
    }

    @Test
    public void constructorTest() {
        int i = 0;
        for (CardSet cs : csm.getCardSets()) {
            i++;
            assertEquals("Card set " + i, cs.getTitle());
        }
        assertEquals(new ArrayList<CardSet>(),csmEmpty.getCardSets());
    }

    @Test
    public void addCardSetTest() {
        CardSet cs = new CardSet("test1");
        csm.addCardSet(cs);
        int cardSetMenuLength = 10;
        assertEquals(cardSetMenuLength + 1, csm.getCardSetLength());
        assertTrue(csm.getCardSets().contains(cs));

        csmEmpty.addCardSet(cs);
        assertEquals(1, csmEmpty.getCardSetLength());
        assertEquals(1, csmEmpty.getCardSetLength());
        assertTrue(csmEmpty.getCardSets().contains(cs));
    }

    @Test
    public void sortCardSetTest() {
        String cardName1 = "Card set 6";
        String cardName2 = "Card set 4";

        csmEmpty.sortCardSet();
        assertEquals(new ArrayList<>(), csmEmpty.getCardSets());

        csm.getCardSet(cardName1).incrementUseCount();
        csm.sortCardSet();
        assertEquals(new CardSet(cardName1), csm.getCardSet(0));

        csm.getCardSet(cardName2).incrementUseCount();
        csm.getCardSet(cardName2).incrementUseCount();
        csm.sortCardSet();
        assertEquals(new CardSet(cardName2), csm.getCardSet(0));
    }

    @Test
    public void containsCardSetWithNameTest() {
        assertFalse(csm.containsCardSetWithName("kjaskdhj"));
        assertFalse(csmEmpty.containsCardSetWithName("title"));
        int i = 0;
        for (CardSet cs : csm.getCardSets()) {
            i++;
            assertEquals(true, csm.containsCardSetWithName("Card set " + i));
        }
    }

    @Test
    public void getCardSetByIndexTest() {
        for (int i = 0; i < csm.getCardSetLength(); i++) {
            assertEquals(new CardSet("Card set " + (i + 1)), csm.getCardSet(i));
        }
    }

    @Test
    public void getCardSetByTitleTest() {
        assertNull(csm.getCardSet("askdjhkjahsd"));
        int i = 0;
        for (CardSet cs : csm.getCardSets()) {
            i++;
            assertEquals(cs, csm.getCardSet("Card set " + i));
        }
    }

    @Test
    public void deleteCardSetByIndex() {
        assertTrue(csm.containsCardSetWithName("Card set 10"));
        assertTrue(csm.containsCardSetWithName("Card set 1"));
        assertTrue(csm.containsCardSetWithName("Card set 4"));

        csm.deleteCardSet(csm.getCardSetLength() - 1);
        assertFalse( csm.containsCardSetWithName("Card set 10"));

        csm.deleteCardSet(0);
        assertFalse( csm.containsCardSetWithName("Card set 1"));

        csm.deleteCardSet(2);
        assertFalse( csm.containsCardSetWithName("Card set 4"));
    }

    @Test
    public void deleteCardSetByObject() {
        assertTrue(csm.containsCardSetWithName("Card set 10"));
        assertTrue(csm.containsCardSetWithName("Card set 1"));
        assertTrue(csm.containsCardSetWithName("Card set 4"));
        csm.deleteCardSet(new CardSet("Card set 10"));
        assertFalse( csm.containsCardSetWithName("Card set 10"));
        csm.deleteCardSet(new CardSet("Card set 1"));
        assertFalse( csm.containsCardSetWithName("Card set 1"));
        csm.deleteCardSet(new CardSet("Card set 4"));
        assertFalse( csm.containsCardSetWithName("Card set 4"));
    }

    @Test
    public void getCardSetLengthTest() {
        int ogSize = csm.getCardSets().size();
        assertEquals(ogSize, csm.getCardSetLength());
        assertEquals(0,csmEmpty.getCardSetLength());
        csm.deleteCardSet(1);
        assertEquals(ogSize - 1, csm.getCardSetLength());
    }

    @Test
    public void setCardSetsTest() {
        ArrayList<CardSet> css = new ArrayList<CardSet>();
        css.add(new CardSet("cs1"));
        css.add(new CardSet("cs2"));
        css.add(new CardSet("cs3"));
        csm.setCardSets(css);
        assertEquals(css, csm.getCardSets());
    }

    @Test
    public void setCardSetByIndexTest() {
        int i = 1;
        assertEquals(csm.getCardSet(i - 1).getTitle(), "Card set " + i);
        CardSet cs = new CardSet("test");
        csm.setCardSet(i - 1, cs);
        assertEquals(csm.getCardSet(i - 1), cs);
    }
}
