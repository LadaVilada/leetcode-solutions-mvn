package system.design.linkedList;

/**
 * Given the head of a singly linked list, reverse the list and return its new head.
 * <p>
 * Optimal approach: Iterative O(n) time O(1) space
 * reverse the list in-place
 */
public class LinkedListUtils {

    public class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    public class DoublyNode<T> {
        T data;
        DoublyNode<T> next;
        DoublyNode<T> prev;

        DoublyNode(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    /**
     * Reverse the list using constant space (O(1))
     *
     * What is the time complexity of your solution? (O(n) for both iterative and recursive approaches.)
     * @param head
     * @return
     * @param <T>
     */
    public static <T> Node<T> reverseList(Node<T> head) {
        Node<T> current = head;
        Node<T> prev = null;
        Node<T> next;

        while (current != null) {
            next = current.next; //save the next node
            current.next = prev; //reverse the pointer
            prev = current; // move prev forward
            current = next; // move current forward
        }

        return prev; // prev is now the head of reversed list
    }

    /**
     * Memory complexity is O(n) due to recursion stack (can be improved using tail recursion).
     *
     * What is the time complexity of your solution? (O(n) for both iterative and recursive approaches.)
     * @param head
     * @return
     * @param <T>
     */
    public static <T> Node<T> reverseListRecursive(Node<T> head) {
        if (head == null || head.next == null) {
            return head;
        }

        Node<T> reversedListHead = reverseListRecursive(head.next);
        head.next.next = head;
        head.next = null;

        return reversedListHead;
    }

    public static <T> DoublyNode<T> reverseList(DoublyNode<T> head) {
        DoublyNode<T> current = head;
        DoublyNode<T> tmp = null;

        while(current != null) {
            tmp = current.prev; // store the previous node
            current.prev = current.next; // swap previous and next
            current.next = tmp;
            current = current.prev;
        }

        //`tmp` is now the second last node before null, so return `tmp.prev`
        // If the list had only one node, it safely returns head.
        return (tmp != null) ? tmp.prev : head;

    }

    /**
     * Time & Space Complexity
     * Time Complexity: O(n) (Each node is visited once)
     * Space Complexity: O(n) (Recursive stack)
     * @param head
     * @return
     * @param <T>
     */
    public static <T> DoublyNode<T> reverseListRecursive(DoublyNode<T> head) {
        if (head == null || head.next == null) {
            return head; // Base case: Last node becomes the new head
        }

        DoublyNode<T> newHead = reverseListRecursive(head.next); // Recur to next node

        head.next.next = head; // Reverse link
        head.prev = head.next;
        head.next = null; // Prevent cycle

        return newHead; // Return new head of reversed list
    }



}
