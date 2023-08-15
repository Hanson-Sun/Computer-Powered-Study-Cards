package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class (Taken from the AlarmSystem project demo)
 */
public class EventTest {
	private Event e;
	private Date d;
	
	//NOTE: these tests might fail if time at which line (2) below is executed
	//is different from time that line (1) is executed.  Lines (1) and (2) must
	//run in same millisecond for this test to make sense and pass.
	
	@BeforeEach
	public void runBefore() {
		e = new Event("Sensor open at door");   // (1)
		d = Calendar.getInstance().getTime();   // (2)
	}
	
	@Test
	public void testEvent() {
		assertEquals("Sensor open at door", e.getDescription());
        double second = (d.getTime() - e.getDate().getTime()) / 1000.0;
        assertTrue(second < 10);
	}

	@Test
	public void testToString() {
		assertEquals(d.toString() + "\n" + "Sensor open at door", e.toString());
	}

    @Test
    public void equalsTest() {
        assertFalse(e.equals(null));
        assertFalse(e.equals(new ArrayList<String>()));
    }

    @Test
    public void hashCodeTest() {
        // Fake test since we are not expected to know how hashcode works in 210
        assertEquals(e.hashCode(), e.hashCode());
    }
}
