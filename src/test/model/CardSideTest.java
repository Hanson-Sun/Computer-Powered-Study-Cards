package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.*;
import static org.junit.jupiter.api.Assertions.*;

// Tests for the CardSide Class
public class CardSideTest {
    CardSide cs1;
    CardSide cs2;

    @BeforeEach
    public void setup() {
        cs1 = new CardSide();
        cs2 = new CardSide();
        cs1.setContent("front");
        cs2.setContent("back");
    }

    @Test
    public void constructorTest() {
        assertEquals("front", cs1.getContent());
        assertEquals("back", cs2.getContent());
        assertEquals(new Color(0, 0, 0), cs1.getFontColor());
        assertEquals(new Color(255, 255, 255), cs1.getBackgroundColor());
        assertEquals(new Color(0, 0, 0), cs2.getFontColor());
        assertEquals(new Color(255, 255, 255), cs2.getBackgroundColor());
    }

    @Test
    public void setFontColorTest() {
        Color c1 = new Color(0, 34, 2);
        cs1.setFontColor(c1);
        assertEquals(c1, cs1.getFontColor());

        Color c2 = new Color(0, 0, 200);
        cs2.setFontColor(c2);
        assertEquals(c2, cs2.getFontColor());

    }

    @Test
    public void setBackgroundColorTest() {
        Color c1 = new Color(0, 34, 2);
        cs1.setBackgroundColor(c1);
        assertEquals(c1, cs1.getBackgroundColor());

        Color c2 = new Color(0, 0, 200);
        cs2.setBackgroundColor(c2);
        assertEquals(c2, cs2.getBackgroundColor());
    }
}

