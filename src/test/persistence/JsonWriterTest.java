package persistence;

import model.Card;
import model.CardSet;
import model.CardSetsMenu;
import model.CardSide;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    private final String path = "./data/tests/";

    @Test
    public void invalidCardSetMenuWriteTest() {
        try {
            CardSetsMenu csm = new CardSetsMenu();
            JsonWriter writer = new JsonWriter(path + "0illegal:fileName.cpsc");
            writer.open();
        } catch (IOException e) {
            fail("Invalid file name");
        }
    }

    @Test
    public void emptyCardSetMenuWriteTest() {
        try {
            CardSetsMenu csm = new CardSetsMenu();
            JsonWriter writer = new JsonWriter(path + "emptycsmwrite.cpsc");
            writer.open();
            writer.write(csm);
            writer.close();

            JsonReader reader = new JsonReader(path + "emptycsmwrite.cpsc");
            csm = reader.readCardSetMenu();
            assertEquals(0, csm.getCardSetLength());
        } catch (IOException e) {
            fail("Illegal File Name!");
        }
    }

    @Test
    public void cardSetMenuWriteTest() {
        try {
            CardSetsMenu csm = new CardSetsMenu();
            generateTestSets(csm);
            JsonWriter writer = new JsonWriter(path + "csmwrite.cpsc");
            writer.open();
            writer.write(csm);
            writer.close();

            JsonReader reader = new JsonReader(path + "csmwrite.cpsc");
            csm = reader.readCardSetMenu();
            int i = 1;
            assertEquals(10, csm.getCardSetLength());
            for (CardSet cs : csm.getCardSets()) {
                assertEquals(0, cs.getCardSetLength());
                assertEquals(0, cs.getUseCount());
                assertEquals("Card set " + i, cs.getTitle());
                i++;
            }
        } catch (IOException e) {
            fail("Illegal File Name!");
        }
    }

    @Test
    public void invalidCardSetWriteTest() {
        try {
            CardSet cs = new CardSet("test");
            JsonWriter writer = new JsonWriter(path + "0illegal:file::asdName.cpsc");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void emptyCardSetWriteTest() {
        try {
            CardSet cs = new CardSet("test");
            JsonWriter writer = new JsonWriter(path + "emptycswrite.cpsc");
            writer.open();
            writer.write(cs);
            writer.close();

            JsonReader reader = new JsonReader(path + "emptycswrite.cpsc");
            cs = reader.readCardSet();
            assertEquals(0, cs.getCardSetLength());
            assertEquals(0, cs.getUseCount());
        } catch (IOException e) {
            fail("Illegal File Name!");
        }
    }

    @Test
    public void CardSetWriteTest() {
        try {
            CardSet cs = generateTestCards(1);
            JsonWriter writer = new JsonWriter(path + "cswrite.cpsc");
            writer.open();
            writer.write(cs);
            writer.close();

            JsonReader reader = new JsonReader(path + "cswrite.cpsc");
            cs = reader.readCardSet();
            assertEquals(8, cs.getCardSetLength());
            assertEquals(0, cs.getUseCount());
            int i = 1;
            for (Card c : cs.getCards()) {
                assertEquals(0, c.getCorrectCount());
                assertEquals(0, c.getCorrectCount());
                assertFalse(c.getState());
                assertEquals("Set 1 Front " + i, c.getFront().getContent());
                assertEquals("Set 1 Back " + i, c.getBack().getContent());
                assertEquals(new Color(0,0,0), c.getFront().getFontColor());
                assertEquals(new Color(255,255,255), c.getFront().getBackgroundColor());
                assertEquals(new Color(0,0,0), c.getBack().getFontColor());
                assertEquals(new Color(255,255,255), c.getBack().getBackgroundColor());
                i++;
            }
        } catch (IOException e) {
            fail("Illegal File Name!");
        }
    }

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
}
