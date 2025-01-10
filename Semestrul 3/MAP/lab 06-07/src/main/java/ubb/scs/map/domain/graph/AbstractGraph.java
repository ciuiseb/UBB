package ubb.scs.map.domain.graph;

import ubb.scs.map.domain.entities.Entity;
import ubb.scs.map.domain.entities.Tuple;
import ubb.scs.map.domain.exceptions.DomainException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public abstract class AbstractGraph<E> extends Entity<E> {
    protected HashMap<E, ArrayList<E>> adjacencyList = new HashMap<>();

    /**
     * @return the set of nodes
     */
    public Set<E> getNodes() {
        return adjacencyList.keySet();
    }

    /**
     * @param node      - the node to be added
     * @param otherNode - the node to be connected to
     * @throws DomainException if the node is already connected to otherNode
     */
    public abstract void addEdge(E node, E otherNode);

    /**
     * @param node      - the node to be removed
     * @param otherNode - the node to be disconnected from
     * @return the pair of nodes that were disconnected
     * @throws DomainException if the node is not connected to otherNode
     */
    public abstract Tuple<E> removeEdge(E node, E otherNode);

    public void addIsolatedNode(E node) {
        if (isNode(node)) {
            throw new DomainException("Node already exists!");
        }
        adjacencyList.computeIfAbsent(node, k -> new ArrayList<>());
    }

    /**
     * List of the adjacent nodes of a node
     *
     * @param node - the node to be checked
     *             must not be null
     *             must be a node
     * @return the list of adjacent nodes
     * @throws DomainException if the node doesn't exist
     */
    public ArrayList<E> getAdjacentNodes(E node) {
        if (!isNode(node)) {
            throw new DomainException("Node doesnt exist!");
        }
        return adjacencyList.get(node);
    }

    public boolean isNode(E node) {
        return getNodes().contains(node);
    }

    public boolean isEdge(E node, E otherNode) {
        if (adjacencyList.containsKey(node)) {
            return adjacencyList.get(node).contains(otherNode);
        }
        return false;
    }
}
