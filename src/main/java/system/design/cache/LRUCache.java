package system.design.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem Statement
 * Design an LRU (Least Recently Used) Cache that:
 * <p>
 * Supports get(key) → Returns the value if present, otherwise -1.
 * Supports put(key, value) → Inserts or updates a key.
 * If the cache reaches its capacity, remove the Least Recently Used (LRU) item before inserting.
 * Both get() and put() must run in O(1) time.
 * <p>
 * Best Approach:
 * Use HashMap + Doubly Linked List
 * <p>
 * Why?
 * HashMap (O(1)) → Fast lookups.
 * Doubly Linked List (O(1)) → Fast removal & reordering.
 *
 * The easiest way to imagine the doubly linked list
 * HEAD <-> (1,10) <-> (2,20) <-> (3,30) <-> TAIL
 * HEAD and TAIL are dummy nodes to make adding/removing nodes easier in our implementation.
 */
public class LRUCache {

    private final int capacity;
    private final Map<Integer, Node> map;
    private final Node head, tail;

    // Doubly Linked List structure
    static class Node {
        int key, value;
        Node next, prev;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }

    }

    /**
     * Create Dummy head and tail nodes to make adding/removing nodes easier.
     * Head is always at the front (Most Recently Used).
     * Tail is always at the back (Least Recently Used).
     *
     * @param capacity max cache capacity
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();

        head = new Node(0, 0);
        tail = new Node(0, 0);

        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (!map.containsKey(key)) {
            return -1;
        }

        Node node = map.get(key);
        // Move the node to the front (Most Recently Used)
        moveToFront(node);
        return node.value;

    }

    public void put(int key, int value) {
        if (capacity == 0) return;

        if (map.containsKey(key)) { // update
            Node node = map.get(key); // Fetch reference to the same node stored in HashMap
            // Modify the original object
            // (because Java HashMap stores references to objects, not copies.)
            node.value = value;
            moveToFront(node);

        } else { // insert
            if (map.size() == capacity) {
                // Remove the Least Recently Used item before inserting
                removeLRUNode();
            }
            // Add new item to the front
            Node node = new Node(key, value);
            map.put(key, node);
            addNode(node);
        }

    }

    private void removeLRUNode() {
        Node node = tail.prev;
        map.remove(node.key);
        removeNode(tail.prev);
    }


    private void moveToFront(Node node) {
        removeNode(node);
        addNode(node);
    }

    /**
     * Add the node to the front (Most Recently Used)
     *
     * @param node node to add
     */
    private void addNode(Node node) {
        node.prev = head;
        node.next = head.next;

        head.next.prev = node;
        head.next = node;
    }

    /**
     * Remove the node from the list by changing the pointers/links (prev and next)
     *
     * @param node node to be removed
     */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public static void main(String[] args) {
        LRUCache cache = new LRUCache(2);
        cache.put(1, 10);
        cache.put(2, 20);
        System.out.println(cache.get(1)); // 10
        cache.put(3, 30); // Removes key 2 (Least Recently Used)
        System.out.println(cache.get(2)); // -1 (not found)
        cache.put(4, 40); // Removes key 1
        System.out.println(cache.get(1)); // -1
        System.out.println(cache.get(3)); // 30
        System.out.println(cache.get(4)); // 40

    }


}
