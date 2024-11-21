package ubb.scs.map.domain.graph;

import ubb.scs.map.domain.entities.SameClassPair;
import ubb.scs.map.domain.exceptions.DomainException;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UndirectedGraph<E> extends AbstractGraph<E> {
    /**
     * Adds an edge between two nodes
     *
     * @param node      - the first node
     * @param otherNode - the second node
     * @throws DomainException if the edge already exists
     */
    @Override
    public void addEdge(E node, E otherNode) {
        if (isEdge(node, otherNode)) {
            throw new DomainException("Node already exists");
        }
        adjacencyList.computeIfAbsent(node, k -> new ArrayList<>()).add(otherNode);
        adjacencyList.computeIfAbsent(otherNode, k -> new ArrayList<>()).add(node);
    }

    /**
     * Removes an edge between two nodes
     *
     * @param node      - the first node
     *                  must not be null
     * @param otherNode - the second node
     *                  must not be null
     * @return the pair of nodes that were disconnected
     * @throws DomainException if the edge doesn't exist
     * @throws DomainException if the otherNode doesn't exist
     * @throws DomainException if the adjacencyList is empty
     */


    @Override
    public SameClassPair<E> removeEdge(E node, E otherNode) {
        if (adjacencyList.isEmpty()) {
            throw new DomainException("Community is empty");
        }
        if (!isNode(node) || !isNode(otherNode)) {
            throw new DomainException("Invalid nodes");
        }
        if (!isEdge(node, otherNode)) {
            throw new DomainException("Invalid edge");
        }
        adjacencyList.get(node).remove(otherNode);
        adjacencyList.get(otherNode).remove(node);

        return new SameClassPair<>(node, otherNode);
    }

    /**
     * Returns the connected components of the graph
     *
     * @return a list of connected components
     */
    public ArrayList<ArrayList<E>> getConnexComponents() {
        ArrayList<ArrayList<E>> components = new ArrayList<>();
        Set<E> visited = new HashSet<>();

        for (E node : adjacencyList.keySet()) {
            if (!visited.contains(node)) {
                ArrayList<E> component = new ArrayList<>();
                dfs(node, visited, component);
                components.add(component);
            }
        }
        return components;
    }

    private void dfs(E node, Set<E> visited, ArrayList<E> component) {
        visited.add(node);
        component.add(node);
        for (E neighbor : getAdjacentNodes(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, visited, component);
            }
        }
    }
}
