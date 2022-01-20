package src;


public class Node {
    int value;
    int height;
    int size;
    Node right;
    Node left;

    Node(int value) {
        this.value = value;
        this.height = 1;
        this.size = 1;
    }

    public static int getHeight(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    public static int getBalance(Node node) {
        if (node == null)
            return 0;
        return getHeight(node.left) - getHeight(node.right);
    }

    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                ", height=" + height +
                ", size=" + size +
                '}';
    }
}
