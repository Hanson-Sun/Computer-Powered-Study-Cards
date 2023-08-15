package persistence;

import model.Card;
import model.CardSet;
import model.CardSetsMenu;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    private final String path = "./data/tests/";

    @Test
    public void readCardSetMenuTest() {
        JsonReader reader = new JsonReader(path + "csm.cpsc");
        try {
            CardSetsMenu csm = reader.readCardSetMenu();
            assertEquals(10, csm.getCardSetLength());
            int i = 1;
            for (CardSet cs : csm.getCardSets()) {
                assertEquals("Card set " + i, cs.getTitle());
                assertEquals(0, cs.getUseCount());
                assertEquals(new ArrayList<Card>(), cs.getCards());
                i++;
            }
        } catch (IOException e) {
            fail("Could not read file");
        }
    }

    @Test
    public void nonExistentReadCardSetMenuTest() {
        JsonReader reader = new JsonReader(path + "noSuchFile.cpsc");
        try {
            CardSetsMenu csm = reader.readCardSetMenu();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void readCardSetTest() {
        JsonReader reader = new JsonReader(path + "cardset.cpsc");
        try {
            CardSet cs = reader.readCardSet();
            assertEquals("Card set 1", cs.getTitle());
            assertEquals(8, cs.getCardSetLength());
            assertEquals(0, cs.getUseCount());
            int i = 1;
            for (Card c : cs.getCards()) {
                assertEquals(1, c.getCorrectCount());
                assertEquals(0, c.getWrongCount());
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
            fail("Could not read file");
        }
    }

    @Test
    public void nonExistentReadCardSetTest() {
        JsonReader reader = new JsonReader(path + "noSuchFile.cpsc");
        try {
            CardSet cs = reader.readCardSet();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }
}
