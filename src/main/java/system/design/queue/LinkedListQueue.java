package system.design.queue;

/**
 * Linked list-based queue implementation
 * <p>
 * Class Design:
 * Use a Node class to represent queue elements.
 * The LinkedListQueue class should have enqueue(), poll(), and peek() methods.
 * Implement isEmpty() and getSize() for completeness.
 * <p>
 * Efficiency:
 * Both enqueue() and poll() should be O(1) (constant time).
 * Avoid unnecessary memory usage.
 * <p>
 * Edge Cases to Handle:
 * Empty queue polling: poll() should throw an exception or return a sentinel value.
 * Queue with one element: Ensure head and tail are both reset properly after dequeuing.
 *
 * @param <T>
 */
public class LinkedListQueue<T> {

    // Head (front) and Tail (rear) pointers
    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Node class to represent individual queue elements.
     */
    private static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    /**
     * Custom exception for empty queue operations.
     */
    public static class EmptyQueueException extends RuntimeException {
        public EmptyQueueException(String message) {
            super(message);
        }
    }

    /**
     * Constructs an empty queue.
     */
    public LinkedListQueue() {
        this.head = this.tail = null;
        this.size = 0;

    }

    /**
     * Adds element (new node) to the end of the queue (tail)
     *
     * @param data element to be added
     */
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    /**
     * Empty queue polling: poll() should throw an exception or return a sentinel value.
     * Queue with one element: Ensure head and tail are both reset properly after dequeuing
     *
     * @return return the front element and remove it from the queue
     * @throws EmptyQueueException if the queue is empty.
     */
    public T poll() {
        if (isEmpty()) {
//            return null; // Instead of throwing an exception
            throw new EmptyQueueException("Queue is empty. Cannot poll.");
        }

        T data = head.data;
        // Move the head forward
        head = head.next;

        if (isEmpty()) {
            tail = null;
        }
        size--;

        return data;
    }

    /**
     * Return the front element without removing it
     *
     * @return the front element
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty. Cannot peek.");
        }
        return head.data;

    }

    /**
     * Check if the queue is empty
     *
     * @return true if the queue is empty, false otherwise
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Get the size of the queue
     *
     * @return size of the queue
     */
    public int getSize() {
        return size;
    }

    private void printQueue() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        Node<T> tmp = head;
        while (tmp != null) {
            sb.append(tmp.data).append(" --> ");
            tmp = tmp.next;
        }
        sb.append("null");
        System.out.println(sb);
    }

    public static void main(String[] args) {
        LinkedListQueue<Integer> queue = new LinkedListQueue<>();

        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);
        queue.printQueue(); // 10 -> 20 -> 30 -> null

        System.out.println("Polled: " + queue.poll()); // 10
        queue.printQueue(); // 20 -> 30 -> null

        queue.enqueue(40);
        queue.printQueue(); // 20 -> 30 -> 40 -> null

        System.out.println("Peek: " + queue.peek()); // 20
        System.out.println("Queue Size: " + queue.getSize()); // 3
    }
}
