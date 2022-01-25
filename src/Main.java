package src;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");

        AvlArray<Integer> arr = new AvlArray<>(1, 2, 3, 4, 4, 5);
        arr.stream()
            .map(v -> v*v)
            .forEach(System.out::println);
    }
}

