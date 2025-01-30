package system.design.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * LFU (Least Frequently Used) is a cache eviction policy that removes the least
 * frequently accessed item when the cache reaches its capacity.
 * <p>
 * LFU Cache Requirements
 * <p>
 * get(key): Retrieves a value and increments its frequency count.
 * put(key, value):
 * If the cache is full, remove the least frequently used key.
 * Insert the new (key, value) with frequency = 1.
 * O(1) Time Complexity for get() and put().
 * <p>
 * <p>
 * Best Approach: HashMap + Min Frequency List (LinkedHashSet)
 * HashMap<K, Node> → Stores key-value pairs with frequency.
 * <p>
 * HashMap<frequency: int, LinkedHashSet> → Maps frequency to a (LinkedHashSet) - doubly linked list -
 * (stores all keys with the same frequency).
 * <p>
 * minFreq variable → Keeps track of the lowest frequency.
 */
public class LFUCache {

    private int minFreq = 0;
    private int capacity;
    private Map<Integer, Node> cache;
    private Map<Integer, LinkedHashSet<Node>> freqMap;


    private class Node {
        int key, value;
        int freq;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    /**
     * Example
     * <p>
     * freqMap: frequency -> Set of Nodes with that frequency
     * 1 → { (2,20) (1,10)}
     * get(1)
     * 1 → { (2,20) }
     * 2 → { (1,10) }
     *
     * @param key the key
     * @return the value stored for that key
     */
    public int get(int key) {
        Node node = cache.get(key);

        if (node == null) {
            return -1;
        }

        //Increment the frequency of the node,
        incrementNodeFreq(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        if (cache.containsKey(key)) { // update
            Node node = cache.get(key);
            node.value = value;
            incrementNodeFreq(node);

        } else { //insert new node that fits regarding capacity
            if (cache.size() == capacity) {
                // Remove the Least Frequently Used item before inserting
                evictLFU();
            }

            Node newNode = new Node(key, value);
            cache.put(key, newNode);
            // Insert the new Node (key, value) with frequency = 1
            freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(newNode);
            minFreq = 1;

        }
    }

    private void incrementNodeFreq(Node node) {
        // Remove it from the current frequency list
        int oldFreq = node.freq;
        freqMap.get(oldFreq).remove(node);

        if (freqMap.get(oldFreq).isEmpty()) {
            freqMap.remove(oldFreq);
            if (minFreq == oldFreq) {
                minFreq++;
            }
        }

        node.freq++;
        //increment the frequency of the node
        freqMap.computeIfAbsent(node.freq, k -> new LinkedHashSet<>()).add(node);
    }

    private void evictLFU() {
        Node evict = freqMap.get(minFreq).iterator().next();
        // Remove the first node from the set
        freqMap.get(minFreq).remove(evict);
        if (freqMap.get(minFreq).isEmpty()) freqMap.remove(minFreq);
        cache.remove(evict.key);
    }

    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2);
        cache.put(1, 10);
        cache.put(2, 20);
        System.out.println(cache.get(1)); // 10
        cache.put(3, 30); // Removes key 2 (LFU)
        System.out.println(cache.get(2)); // -1 (not found)
        System.out.println(cache.get(3)); // 30

    }


}
