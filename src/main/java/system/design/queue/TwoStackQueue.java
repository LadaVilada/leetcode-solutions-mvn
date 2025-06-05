package system.design.queue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Simulate a FIFO Queue behavior using two LIFO Stacks
 * <p>
 * s1 acts as the input stack (where we push elements).
 * s2 acts as the output stack (where we reverse and pop elements in FIFO order).
 * When s2 is empty, we move all elements from s1 to s2, which preserves order.
 * <p>
 * Note: Use Deque Instead of Stack
 * Reason: Stack is a legacy class in Java (extends Vector), which is synchronized and slower.
 * Best Practice: Use Deque (ArrayDeque) for better performance.
 */
public class TwoStackQueue<T> {

    private Deque<T> inputStack;
    private Deque<T> outputStack;

    public TwoStackQueue() {
        inputStack = new ArrayDeque<>();
        outputStack = new ArrayDeque<>();
    }

    /**
     * Custom exception for empty queue operations.
     * BUT FAANG interviews often prefer using Java’s built-in exceptions for readability.
     */
    public static class EmptyQueueException extends RuntimeException {
        public EmptyQueueException(String message) {
            super(message);
        }
    }

    public void enqueue(T item) {
        inputStack.push(item);
    }

    public T dequeue() {
        shiftStacks();

        if (outputStack.isEmpty()) {
            throw new NoSuchElementException("Queue is empty. Can not dequeue.");
        }

        return outputStack.pop();
    }

    /**
     * Returns the front of the queue without removing it.
     *
     * @return the front element.
     */
    public T peek() {
        shiftStacks();

        if (outputStack.isEmpty()) {
            throw new NoSuchElementException("Queue is empty. Can not peek an element.");
        }

        return outputStack.peek();

    }

    /**
     * Transfers elements from inputStack to outputStack only when needed
     * If and only id outputStack is empty
     */
    private void shiftStacks() {
        if (outputStack.isEmpty()) {
            while (!inputStack.isEmpty()) {
                outputStack.push(inputStack.pop());
            }
        }
    }

    public boolean isEmpty() {
        return inputStack.isEmpty() && outputStack.isEmpty();
    }

    public int getSize() {
        return inputStack.size() + outputStack.size();
    }

    public static void main(String[] args) {
        TwoStackQueue<Integer> queue = new TwoStackQueue<>();

        queue.enqueue(10);
        queue.enqueue(20);
        queue.enqueue(30);

        System.out.println(queue.dequeue()); // 10
        System.out.println(queue.dequeue()); // 20
        System.out.println(queue.dequeue()); // 30

        System.out.println(queue.isEmpty()); // true

        try {
            System.out.println(queue.dequeue()); // throws exception
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }


}
