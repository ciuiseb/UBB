package domain.graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ubb.scs.map.domain.entities.SameClassPair;
import ubb.scs.map.domain.exceptions.DomainException;
import ubb.scs.map.domain.graph.UndirectedGraph;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UndirectedGraphTest {

    private UndirectedGraph<String> graph;

    @BeforeEach
    public void setUp() {
        graph = new UndirectedGraph<>();
    }

    // addEdge() tests
    @Test
    public void addEdgeTestSuccess() {
        graph.addEdge("A", "B");
        assertEquals(1, graph.getAdjacentNodes("A").size());
        assertEquals(1, graph.getAdjacentNodes("B").size());
        assertEquals("B", graph.getAdjacentNodes("A").get(0));
        assertEquals("A", graph.getAdjacentNodes("B").get(0));
    }

    @Test
    public void addEdgeTestAlreadyExists() {
        graph.addEdge("A", "B");
        DomainException exception = assertThrows(DomainException.class, () -> graph.addEdge("A", "B"));
        assertEquals("Node already exists", exception.getMessage());
    }

    @Test
    public void addEdgeTestNodeNotExists() {
        graph.addEdge("A", "B");
        DomainException exception = assertThrows(DomainException.class, () -> graph.addEdge("A", "B"));
        assertEquals("Node already exists", exception.getMessage());
    }

    // removeEdge() tests
    @Test
    public void removeEdgeTestSuccess() {
        graph.addEdge("A", "B");
        SameClassPair<String> removed = graph.removeEdge("A", "B");
        assertEquals("A", removed.getFirst());
        assertEquals("B", removed.getSecond());
        assertEquals(0, graph.getAdjacentNodes("A").size());
        assertEquals(0, graph.getAdjacentNodes("B").size());
    }

    @Test
    public void removeEdgeTestInvalidEdge() {
        graph.addEdge("A", "B");
        DomainException exception = assertThrows(DomainException.class, () -> graph.removeEdge("A", "C"));
        assertEquals("Invalid nodes", exception.getMessage());
    }

    @Test
    public void removeEdgeTestEmptyGraph() {
        DomainException exception = assertThrows(DomainException.class, () -> graph.removeEdge("A", "B"));
        assertEquals("Community is empty", exception.getMessage());
    }

    // getConnexComponents() tests
    @Test
    public void getConnexComponentsTestSingleComponent() {
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        ArrayList<ArrayList<String>> components = graph.getConnexComponents();
        assertEquals(1, components.size());
        assertEquals(new ArrayList<>(Arrays.asList("A", "B", "C")), components.get(0));
    }

    @Test
    public void getConnexComponentsTestMultipleComponents() {
        graph.addEdge("A", "B");
        graph.addEdge("C", "D");
        ArrayList<ArrayList<String>> components = graph.getConnexComponents();
        assertEquals(2, components.size());
        assertEquals(new ArrayList<>(Arrays.asList("A", "B")), components.get(0));
        assertEquals(new ArrayList<>(Arrays.asList("C", "D")), components.get(1));
    }

    @AfterEach
    public void tearDown() {
    }
}
