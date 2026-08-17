class BinarySearchTree:
    class _Node:
        def __init__(self, value, index):
            self.value = value
            self.index = index
            self.left = None
            self.right = None

    def __init__(self):
        self.root = None
        self.next_index = 0

    def insert(self, value):
        self.root = self._insert_recursive(self.root, value)

    def _insert_recursive(self, node, value):
        if node is None:
            new_index = self.next_index
            self.next_index += 1
            return self._Node(value, new_index)

        if value < node.value:
            node.left = self._insert_recursive(node.left, value)
        elif value > node.value:
            node.right = self._insert_recursive(node.right, value)
        return node

    def contains(self, value):
        return self._contains_recursive(self.root, value)

    def _contains_recursive(self, node, value):
        if node is None:
            return False
        if value == node.value:
            return True

        return self._contains_recursive(node.left, value) if value < node.value \
            else self._contains_recursive(node.right, value)

    def get(self, value):
        return self._get_recursive(self.root, value)

    def _get_recursive(self, node, value):
        if node is None:
            return None
        if value == node.value:
            return node.index

        return self._get_recursive(node.left, value) if value < node.value \
            else self._get_recursive(node.right, value)

    def print_tree(self):
        self._print_recursive(self.root)
        print()

    def _print_recursive(self, node):
        if node is not None:
            self._print_recursive(node.right)
            print(f"{node.index}: {node.value} ")
            self._print_recursive(node.left)

