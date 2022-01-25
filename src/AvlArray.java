package src;

import java.lang.module.ModuleDescriptor.Builder;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class AvlArray<V> {
    Node root;
    public AvlArray() {
        this.root = null;
    }

    public AvlArray(V init) {
        this.root = new Node(init);
        this.root.size = getNodeSize(root);
    }

    public AvlArray(V... data) {
        if (data.length == 0)
            return;

        for(V value : data) {
            add(value);
        }
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    private Node balance(Node node) {
        int balance = getBalance(node);
        if (balance > 1) {
            if (getBalance(node.left) < 0)
                return doubleRightRotation(node);
            return rightRotation(node);
        }
        else if (balance < -1) {
            if (getBalance(node.right) > 0)
                return doubleLeftRotation(node);
            return leftRotation(node);
        }

        return node;
    }

    public int size() {
        return getNodeSize(this.root);
    }

    public Stream<V> stream() {
        Stream.Builder<V> builder = Stream.<V>builder();
        stream(this.root, builder);
        return builder.build();
    }

    private void stream(Node node, Stream.Builder<V> builder) {
        if (node == null)
            return;
        stream(node.left, builder);
        builder.accept(node.value);
        stream(node.right, builder);
    }

    private Node addNewNode(Node root, V value, int index) {
        if (root == null)
            return new Node(value);
        else if (index == getNodeSize(root.left)) {
            Node tmp = new Node(value);
            tmp.left = root.left;
            root.left = null;
            root.size = getNodeSize(root);
            root.height = getNodeHeight(root);
            tmp.right = balance(root);
            tmp.size = getNodeSize(tmp);
            tmp.height = getNodeHeight(tmp);
            return balance(tmp);
        }
        else if (index < getNodeSize(root.left))
            root.left = addNewNode(root.left, value, index);
        else
            root.right = addNewNode(root.right, value,index - getNodeSize(root.left) - 1);

        root.height = getNodeHeight(root);
        root.size++;
        return balance(root);
    }

    public void addAndLift(V value, int index) {
        if (this.root == null)
            this.root = new Node(value);
        else
            this.root = addNewNode(this.root, value, index);
    }

    public void add(V value) {
        addAndLift(value, size());
    }

    private Node get(Node node, int index) {
        if (index == getNodeSize(node.left))
            return node;
        else if (index < getNodeSize(node.left))
            return get(node.left, index);
        return get(node.right, index - getNodeSize(node.left) - 1);
    }

    public V get(int index) {
        if (index < 0 || index > size()-1)
            throw new IndexOutOfBoundsException(index);
        return get(this.root, index).value;
    }


    private int getNodeSize(Node node) {
        int size = 1;

        if (node == null)
            return 0;
        if (node.left != null)
            size += node.left.size;
        if (node.right != null)
            size += node.right.size;

        return size;
    }

    private int getNodeHeight(Node node) {
        int lHeight = 0, rHeight = 0;

        if (node.left != null)
            lHeight = node.left.height;

        if (node.right != null)
            rHeight = node.right.height;

        return 1 + max(lHeight, rHeight);
    }

    public void remove(int index) {
        this.root = deleteNode(this.root, index);
    }

    public void set(int index, V value) {
        if (index < 0 || index > size()-1)
            throw new IndexOutOfBoundsException(index);
        get(this.root, index).value = value;
    }

    public void forEach(Consumer<V> consumer) {
        forEach(this.root, consumer);
    }

    // going through the tree using inOrder method
    private void forEach(Node node, Consumer<V> consumer) {
        if (node == null)
            return;
        forEach(node.left, consumer);
        consumer.accept(node.value);
        forEach(node.right, consumer);
    }

    private Node deleteNode(Node node, int index) {
        if (node == null)
            return null;
        if (index == getNodeSize(node.left)) {
            return deleteNode(node);
        } else if (index < getNodeSize(node.left))
            node.left = deleteNode(node.left,  index);
        else
            node.right = deleteNode(node.right, index - getNodeSize(node.left) - 1);


        node.height = getNodeHeight(node);
        node.size = getNodeSize(node);
        return balance(node);
    }

    private Node deleteNode(Node node) {
        if (node.left == null && node.right == null)
            return null;
        else if (node.left == null)
            return node.right;
        else if (node.right == null)
            return node.left;

        Node tmp = getNextNode(node.right);
        node.value = tmp.value;
        node.right = deleteNode(node.right, 0);

        node.size = getNodeSize(node);
        node.height = getNodeHeight(node);
        return node;
    }

    private Node getNextNode(Node node) {
        if (node.left != null)
            return getNextNode(node.left);
        return node;
    }

    private Node rightRotation(Node b) {
        Node a = b.left;
        Node y = a.right;
        a.right = b;
        b.left = y;

        b.height = getNodeHeight(b);
        a.height = getNodeHeight(a);

        b.size = getNodeSize(b);
        a.size = getNodeSize(a);

        return a;
    }

    private Node leftRotation(Node b) {
        Node a = b.right;
        Node y = a.left;
        a.left = b;
        b.right = y;

        b.height = getNodeHeight(b);
        a.height = getNodeHeight(a);


        b.size = getNodeSize(b);
        a.size = getNodeSize(a);

        return a;
    }

    private Node doubleRightRotation(Node c) {
        Node a = c.left;
        Node b = a.right;
        Node y = b.left;
        Node z = b.right;

        b.left = a;
        b.right = c;
        a.right = y;
        c.left = z;

        a.height = max(getHeight(a.left), getHeight(a.right)) + 1;
        c.height = max(getHeight(c.left), getHeight(c.right)) + 1;
        b.height = max(getHeight(b.left), getHeight(b.right)) + 1;

        a.size = getNodeSize(a);
        c.size = getNodeSize(c);
        b.size = getNodeSize(b);

        return b;
    }

    private Node doubleLeftRotation(Node c) {
        Node a = c.right;
        Node b = a.left;
        Node y = b.right;
        Node z = b.left;

        b.left = c;
        c.right = z;
        b.right = a;
        a.left = y;

        a.height = max(getHeight(a.left), getHeight(a.right)) + 1;
        c.height = max(getHeight(c.left), getHeight(c.right)) + 1;
        b.height = max(getHeight(b.left), getHeight(b.right)) + 1;

        a.size = getNodeSize(a);
        c.size = getNodeSize(c);
        b.size = getNodeSize(b);

        return b;
    }

    private int getHeight(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    private class Node {
        V value;
        int height;
        int size;
        Node right;
        Node left;
    
        public Node(V value) {
            this.value = value;
            this.height = 1;
            this.size = 1;
        }
    }

    private int getBalance(Node node) {
        if (node == null)
            return 0;
        return getHeight(node.left) - getHeight(node.right);
    }
}
