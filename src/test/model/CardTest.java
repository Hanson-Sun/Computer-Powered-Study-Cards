package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// Tests for Card class
public class CardTest {
    Card c1;
    Card c2;
    CardSide cf1;
    CardSide cb1;
    CardSide cf2;
    CardSide cb2;

    @BeforeEach
    public void setup() {
        cf1 = new CardSide();
        cb1 = new CardSide();
        cf2 = new CardSide();
        cb2 = new CardSide();

        cf1.setContent("Front 1");
        cb1.setContent("Back 1");
        cf2.setContent("Front 2");
        cb2.setContent("Back 2");

        c1 = new Card(cf1, cb1);
        c2 = new Card(cf2, cb2);
    }

    @Test
    public void constructorTest() {
        assertFalse(c1.getState());
        assertEquals(0, c1.getWrongCount());
        assertEquals(0, c1.getCorrectCount());
        assertEquals(cf1, c1.getFront());
        assertEquals(cb1, c1.getBack());

        assertFalse(c2.getState());
        assertEquals(0, c2.getWrongCount());
        assertEquals(0, c2.getCorrectCount());
        assertEquals(cf2, c2.getFront());
        assertEquals(cb2, c2.getBack());
    }

    @Test
    public void flipTest() {
        c1.flip();
        c2.flip();
        assertTrue(c1.getState());
        assertTrue(c2.getState());
        c1.flip();
        c2.flip();
        assertFalse(c1.getState());
        assertFalse(c2.getState());
    }

    @Test
    public void getAccuracyTest() {
        assertEquals(-1, c1.getAccuracy());
        c2.incrementWrongCount();
        c2.incrementCorrectCount();
        assertEquals(0.5, c2.getAccuracy());
    }

    @Test
    public void getCardFaceTest() {
        assertEquals(cf1, c1.getCardFace());
        c1.flip();
        assertEquals(cb1, c1.getCardFace());
        c1.flip();
        assertEquals(cf1, c1.getCardFace());

        assertEquals(cf2, c2.getCardFace());
        c2.flip();
        assertEquals(cb2, c2.getCardFace());
        c2.flip();
        assertEquals(cf2, c2.getCardFace());
    }

    @Test
    public void incrementCorrectCountTest() {
        for (int i = 0; i < 50; i++) {
            assertEquals(i, c1.getCorrectCount());
            c1.incrementCorrectCount();
            assertEquals(i + 1, c1.getCorrectCount() );
        }
    }

    @Test
    public void incrementWrongCountTest() {
        for (int i = 0; i < 50; i++) {
            assertEquals(i, c1.getWrongCount());
            c1.incrementWrongCount();
            assertEquals(i + 1, c1.getWrongCount() );
        }
    }
}
