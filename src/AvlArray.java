package src;

public class AvlArray {
    Node root;
    public AvlArray() {
        this.root = null;
    }

    public AvlArray(Node root) {
        this.root = root;
        this.root.size = getNodeSize(root);
    }

    public int max(int a, int b) {
        return a > b ? a : b;
    }

    public void inOrder(Node node) {
        if (node == null)
            return;
        inOrder(node.left);
        System.out.print(node.value + " ");
        inOrder(node.right);
    }

    private Node balance(Node node) {
        int balance = Node.getBalance(node);
        if (balance > 1) {
            if (Node.getBalance(node.left) < 0)
                return doubleRightRotation(node);
            return rightRotation(node);
        }
        else if (balance < -1) {
            if (Node.getBalance(node.right) > 0)
                return doubleLeftRotation(node);
            return leftRotation(node);
        }

        return node;
    }

    private Node addNewNode(Node root, int value, int index) {
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

    public void addAndLift(int value, int index) {
        if (this.root == null)
            this.root = new Node(value);
        else
            this.root = addNewNode(this.root, value, index);
    }

    private Node get(Node node, int index) {
        if (index == getNodeSize(node.left))
            return node;
        else if (index < getNodeSize(node.left))
            return get(node.left, index);
        return get(node.right, index - getNodeSize(node.left) - 1);
    }

    public Node get(int index) {
        return get(this.root, index);
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

        a.height = max(Node.getHeight(a.left), Node.getHeight(a.right)) + 1;
        c.height = max(Node.getHeight(c.left), Node.getHeight(c.right)) + 1;
        b.height = max(Node.getHeight(b.left), Node.getHeight(b.right)) + 1;

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

        a.height = max(Node.getHeight(a.left), Node.getHeight(a.right)) + 1;
        c.height = max(Node.getHeight(c.left), Node.getHeight(c.right)) + 1;
        b.height = max(Node.getHeight(b.left), Node.getHeight(b.right)) + 1;

        a.size = getNodeSize(a);
        c.size = getNodeSize(c);
        b.size = getNodeSize(b);

        return b;
    }
}
