package ubb;

public class BinarySearchTree {
    private static class Node {
        String value;
        int index; 
        Node left, right;

        Node(String value, int index) { 
            this.value = value;
            this.index = index;
        }
    }

    private Node root;
    private int nextIndex; 

    public BinarySearchTree() {
        this.root = null;
        this.nextIndex = 0; 
    }

    public void insert(String value) {
        root = insertRecursive(root, value);
    }

    private Node insertRecursive(Node node, String value) {
        if (node == null) {
            int newIndex = this.nextIndex;
            this.nextIndex++;
            return new Node(value, newIndex);
        }

        if (value.compareTo(node.value) < 0)
            node.left = insertRecursive(node.left, value);
        else if (value.compareTo(node.value) > 0)
            node.right = insertRecursive(node.right, value);
        return node;
    }

    public boolean contains(String value) {
        return containsRecursive(root, value);
    }

    private boolean containsRecursive(Node node, String value) {
        if (node == null) return false;
        if (value.equals(node.value)) return true;
        return value.compareTo(node.value) < 0
                ? containsRecursive(node.left, value)
                : containsRecursive(node.right, value);
    }


    public Integer get(String value) { 
        return getRecursive(root, value);
    }

    private Integer getRecursive(Node node, String value) { 
        if (node == null) {
            return null; 
        }

        if (value.equals(node.value)) {
            return node.index; 
        }

        return value.compareTo(node.value) < 0
                ? getRecursive(node.left, value)
                : getRecursive(node.right, value);
    }

    public void print() {
        printRecursive(root);
        System.out.println(); 
    }

    private void printRecursive(Node node) {
        if (node != null) {
            printRecursive(node.left);
            System.out.print(node.value + " " + node.index + "\n");
            printRecursive(node.right);
        }
    }
}