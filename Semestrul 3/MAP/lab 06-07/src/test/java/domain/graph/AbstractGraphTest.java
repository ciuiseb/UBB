package ubb.scs.map.domain.graph;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ubb.scs.map.domain.entities.Tuple;
import ubb.scs.map.domain.exceptions.DomainException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbstractGraphTest {

    private AbstractGraph<String> graph;

    @BeforeEach
    public void setUp() {
        graph = new UndirectedGraph<>(); // Assuming ConcreteGraph extends AbstractGraph
    }

    // addIsolatedNode() tests
    @Test
    public void addIsolatedNodeTestSuccess() {
        graph.addIsolatedNode("A");
        assertEquals(1, graph.getNodes().size());
        assertEquals(0, graph.getAdjacentNodes("A").size());
    }

    @Test
    public void addIsolatedNodeTestAlreadyExists() {
        graph.addIsolatedNode("A");
        DomainException exception = assertThrows(DomainException.class, () -> graph.addIsolatedNode("A"));
        assertEquals("Node already exists!", exception.getMessage());
    }

    // addEdge() tests
    @Test
    public void addEdgeTestSuccess() {
        graph.addIsolatedNode("A");
        graph.addIsolatedNode("B");
        graph.addEdge("A", "B");
        assertEquals(1, graph.getAdjacentNodes("A").size());
        assertEquals(1, graph.getAdjacentNodes("B").size());
    }


    // removeEdge() tests
    @Test
    public void removeEdgeTestSuccess() {
        graph.addIsolatedNode("A");
        graph.addIsolatedNode("B");
        graph.addEdge("A", "B");
        Tuple<String> removed = graph.removeEdge("A", "B");
        assertEquals("A", removed.getFirst());
        assertEquals("B", removed.getSecond());
        assertEquals(0, graph.getAdjacentNodes("A").size());
    }


    // getAdjacentNodes() tests
    @Test
    public void getAdjacentNodesTestNodeDoesNotExist() {
        DomainException exception = assertThrows(DomainException.class, () -> graph.getAdjacentNodes("A"));
        assertEquals("Node doesnt exist!", exception.getMessage());
    }

    @AfterEach
    public void tearDown() {
        // Reset or cleanup if needed
    }
}
