package system.design.queue;

import java.util.Arrays;

/**
 * The ArrayQueue class implements a circular queue using an array.
 * It efficiently handles FIFO (First-In-First-Out) operations without unnecessary shifting of elements.
 * <p>
 * Key Features
 * <p>
 * Fixed Size: Uses a pre-defined array for storage.
 * Circular Behavior: Uses modulo arithmetic (% capacity) to wrap around when reaching the end of the array.
 * Two Pointers (front and rear):
 * front: Points to the first element (oldest).
 * rear: Points to the last inserted element.
 * <p>
 * Efficient O(1) Operations:
 * enqueue() (Insert at rear).
 * poll() (Remove from front).
 * peek() (Retrieve front element without removal).
 * <p>
 * Since an array-based queue relies on a fixed-size array,
 * removing elements can be handled in different ways, depending on how you
 * define "removal."
 * <p>
 * 1. Logical Removal (Front Pointer Shift) – Most Efficient
 * Works well with circular queues
 * Old values remain in memory, but they are ignored.
 * <p>
 * 2. Overwrite Removed Elements with a Default Value (e.g., 0 or null)
 * Can be confusing if 0 is a valid queue value.
 * Useful if working with objects (null allows garbage collection).
 * Helps with debugging and visualization.
 * <p>
 * Physically Shift Elements (Expensive ❌) (O(n)) for large queues.
 * <p>
 * 3. Use a List<Integer> Instead of an Array
 * Remove elements dynamically using remove(0), shifting elements internally.
 * No need to track front or rear.
 * Not recommended for large queues, remove(0) is O(n) (shifts all elements left).
 */
public class ArrayQueue<T> {

    private T[] queue;
    private int head;
    private int tail;
    private int size;
    private final int capacity;

    /**
     * Constructor initializes an empty circular queue with fixed capacity.
     *
     * @param capacity the maximum number of elements the queue can hold.
     */
    @SuppressWarnings("unchecked")
    public ArrayQueue(int capacity) {
        this.capacity = capacity;
        this.queue = (T[]) new Object[capacity];
        head = 0;
        tail = -1;
        size = 0;
    }

    /**
     * Pass a Class Type Token
     * Another FAANG-accepted way is to pass a Class object
     * in the constructor and create an array dynamically.
     *
     * @param clazz    ClassType Token
     * @param capacity the maximum number of elements the queue can hold.
     */
    public ArrayQueue(Class<T> clazz, int capacity) {
        this.capacity = capacity;
        this.queue = (T[]) java.lang.reflect.Array.newInstance(clazz, capacity); // Safe Reflection-Based Array

        head = 0;
        tail = -1;
        size = 0;
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
     * Enqueues (adds) an element to the rear of the queue.
     *
     * @param item element to be added.
     * @throws IllegalStateException if the queue is full.
     */
    public void enqueue(T item) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full. Cannot enqueue " + item);
        }

        // Circular increment
        tail = (tail + 1) % capacity;
        queue[tail] = item;
        size++;
    }

    /**
     * Dequeues (removes) the front element of the queue.
     *
     * @return the removed element.
     * @throws EmptyQueueException if the queue is empty.
     */
    public T poll() {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty. Cannot poll.");
        }

        T item = queue[head];
        head = (head + 1) % capacity;
        size--;

        // Reset front and rear when queue becomes empty
        if (isEmpty()) {
            head = 0;
            tail = -1;
        }

        return item;
    }

    /**
     * Retrieves the front element without removing it.
     *
     * @return the front element.
     * @throws EmptyQueueException if the queue is empty.
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is empty. Cannot peek.");
        }
        return queue[head];
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;

    }

    /**
     * Checks if the queue is full.
     *
     * @return true if full, false otherwise.
     */
    private boolean isFull() {
        return size == capacity;
    }

//    public int[] getFullQueue() {
//        return queue;
//    }

    /**
     * Returns the current elements in the queue.
     *
     * @return an array containing only active queue elements.
     */
    public T[] getQueue() {
        T[] activeQueue = (T[]) new Object[size];
        for (int i = 0; i < size; i++) {
            activeQueue[i] = queue[(head + i) % capacity];
        }
        return activeQueue;
    }

    /**
     * Prints the queue elements.
     */
    public void printQueue() {
        if (isEmpty()) {
            System.out.println("Queue is empty.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (T item : getQueue()) {
            sb.append(item).append(" ");
        }
        sb.append(" ]");
        System.out.println(sb);
    }

    public static void main(String[] args) {
//        ArrayQueue<Integer> intQueue = new ArrayQueue<>(Integer.class, 3);
        ArrayQueue arrayQueue = new ArrayQueue(3);
        arrayQueue.enqueue(1);
        arrayQueue.enqueue(2);
        arrayQueue.enqueue(3);
        arrayQueue.printQueue();

        System.out.println(arrayQueue.peek()); // return 1
        arrayQueue.poll(); // dequeue/remove 1
        System.out.println(arrayQueue.peek()); // return 2
        arrayQueue.poll(); // dequeue/remove 2
        arrayQueue.printQueue();
    }
}
