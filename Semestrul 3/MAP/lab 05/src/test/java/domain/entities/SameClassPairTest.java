package domain.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ubb.scs.map.domain.entities.SameClassPair;

import static org.junit.jupiter.api.Assertions.*;

public class SameClassPairTest {

    private SameClassPair<Long> pair;

    @BeforeEach
    public void setUp() {
        pair = new SameClassPair<>(1L, 2L);
    }

    // Test the constructor and getter methods
    @Test
    public void constructorAndGettersTest() {
        assertEquals(1L, pair.getFirst());
        assertEquals(2L, pair.getSecond());
    }

    // Test equals() method
    @Test
    public void equalsTest() {
        SameClassPair<Long> pair1 = new SameClassPair<>(1L, 2L);
        SameClassPair<Long> pair2 = new SameClassPair<>(1L, 2L);
        SameClassPair<Long> pair3 = new SameClassPair<>(2L, 1L);

        assertEquals(pair1, pair2); // same values should be equal
        assertNotEquals(pair1, pair3); // different values should not be equal
        assertNotEquals(pair1, null); // should not equal null
        assertNotEquals(pair1, new Object()); // should not equal a different class
    }

    // Test hashCode() method
    @Test
    public void hashCodeTest() {
        SameClassPair<Long> pair1 = new SameClassPair<>(1L, 2L);
        SameClassPair<Long> pair2 = new SameClassPair<>(1L, 2L);
        SameClassPair<Long> pair3 = new SameClassPair<>(2L, 1L);

        assertEquals(pair1.hashCode(), pair2.hashCode()); // same values should have same hash code
        assertNotEquals(pair1.hashCode(), pair3.hashCode()); // different values should have different hash codes
    }

    @AfterEach
    public void tearDown() {
        pair = null; // Clean up after each test
    }
}