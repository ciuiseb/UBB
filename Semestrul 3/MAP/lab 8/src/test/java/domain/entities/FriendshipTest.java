package domain.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ubb.scs.map.domain.entities.Friendship;

import static org.junit.jupiter.api.Assertions.*;

public class FriendshipTest {

    private Friendship friendship;

    @BeforeEach
    public void setUp() {
        friendship = new Friendship(1L, 2L);
    }

    // Test the constructor and toString() method
    @Test
    public void constructorAndToStringTest() {
        assertEquals("Friendship{first = 1; second = 2}", friendship.toString());
    }

    // Test equals() method
    @Test
    public void equalsTest() {
        Friendship friendship1 = new Friendship(1L, 2L);
        Friendship friendship2 = new Friendship(1L, 2L);
        Friendship friendship3 = new Friendship(2L, 1L); // different order, but same IDs

        assertNotEquals(friendship1, friendship2); // should be different since they represent different pairs
        assertNotEquals(friendship1, friendship3); // should be different as they have different IDs
        assertNotEquals(friendship1, null); // should not equal null
        assertNotEquals(friendship1, new Object()); // should not equal a different class
    }

    // Test hashCode() method
    @Test
    public void hashCodeTest() {
        Friendship friendship1 = new Friendship(1L, 2L);
        Friendship friendship2 = new Friendship(1L, 2L);
        Friendship friendship3 = new Friendship(2L, 1L); // different order

        assertEquals(friendship1.hashCode(), friendship2.hashCode()); // same values should have the same hash code
        assertNotEquals(friendship1.hashCode(), friendship3.hashCode()); // different values should have different hash codes
    }

    @AfterEach
    public void tearDown() {
        friendship = null; // Clean up after each test
    }
}
