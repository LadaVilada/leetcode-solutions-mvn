package system.design.linkedList;

import java.util.PriorityQueue;
import java.util.TreeSet;

public class PairRemoval {
    private class Node {

        int sum;
        int index; // starting index of the pair
        int first;
        int second;

        Node prev;    // previous Node in the list
        Node next;    // next Node in the list

        // Each Node represents a pair: [first, second]
        public Node(int index, int first, int second) {
            this.index = index;
            this.first = first;
            this.second = second;
            this.sum = first + second;
        }
    }

    private class DoublyLinkedList {
        Node head;
        Node tail;

        public DoublyLinkedList(int[] nums) {
            head = new Node(-1, Integer.MIN_VALUE, Integer.MIN_VALUE);
            tail = new Node(-1, Integer.MAX_VALUE, Integer.MAX_VALUE);

            head.next = tail;
            tail.prev = head;

            build(nums);
        }

        private void build(int[] nums) {
            Node prev = head;
            for (int i = 0; i < nums.length - 1; i++) {
                Node node = new Node(i, nums[i], nums[i + 1]);
                prev.next = node;
                node.prev = prev;
                prev = node;
            }

            prev.next = tail;
            tail.prev = prev;
        }

        // Remove a node from the list
        public void remove(Node node) {
            System.out.println(node.index + "/" + node.first + "/" + node.next + "/" + node.prev);
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        // Merge left node and right node into one node
        public Node mergeNodes(Node node) {
            System.out.println("mergeNodes:" + node.index + "/" + node.first + "/" + node.next + "/" + node.prev);
            if (node == null || node.next == null) return node;

            if (node.next == tail && node.prev != null && node.prev != head) {
                node.prev.second = node.sum;
                node.prev.sum = node.prev.first + node.prev.second;
                return node;
            }

            Node nextPair = node.next;
            if (nextPair == tail) {
                return node;
            }

            node.first = node.sum;
            node.second = nextPair.second;
            node.sum = node.first + node.second;

            remove(nextPair);

            // update prev node second value in a pair
//            if (node.prev != head) {
            if (node.prev != null && node.prev != head) {
                node.prev.second = node.first;
                node.prev.sum = node.prev.first + node.prev.second;
            }

            return node;

        }
    }

    private void updateHeapAfterMerge(Node node, PriorityQueue<Node> heap, boolean hasPrev) {
        heap.offer(node);
        if (hasPrev) {
            heap.offer(node.prev);
        }
    }

    private void detectCycle(Node start, Node end) {
        Node slow = start;
        Node fast = start;

        while (fast != null && fast.next != null && fast != end && fast.next != end) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                System.out.println("⚡ Cycle detected in linked list at node index: " + slow.index);
                throw new RuntimeException("Linked list has become cyclic!");
            }
        }
    }


    public int minimumPairRemoval(int[] nums) {
        if (nums.length <= 1) return 0;
        DoublyLinkedList dll = new DoublyLinkedList(nums);

        TreeSet<Node> set = new TreeSet<>((a, b) -> {
            if (a == b) return 0;
            if (a == null) return 1; // null считается "больше", чтобы не падать
            if (b == null) return -1;

            if (a.sum != b.sum) {
                return Integer.compare(a.sum, b.sum);
            } else {
                return Integer.compare(a.index, b.index);
            }
        });

        Node current = dll.head.next;
        while (current != dll.tail) {
            if (current.first > current.second) {
                set.add(current);
            }
            current = current.next;
        }

        int operations = 0;

        while (!set.isEmpty() && set.size() > 1) {
            Node node = set.pollFirst();

            // Удаляем старых соседей
//            if (node.next != dll.tail) {
            if (node.prev != null && node.prev != dll.head && node.prev.first > node.prev.second) {
                set.remove(node.next);
            }
            if (node != null && node.first > node.second) {
                set.remove(node);
            }

            detectCycle(dll.head, dll.tail);

            Node updatedNode = dll.mergeNodes(node);

            // Добавляем новых соседей
            if (updatedNode.next != dll.tail) {
                set.add(updatedNode);
            }
            set.add(updatedNode.prev);

            operations++;
        }

        return operations;
    }

    public static void main(String[] args) {
//        System.out.println(new PairRemoval().minimumPairRemoval(new int[]{5, 2, 3, 1}));
//        System.out.println(new PairRemoval().minimumPairRemoval(new int[]{1, 2, 2}));
        System.out.println(new PairRemoval().minimumPairRemoval(new int[]{2, 2, -1, 3, -2, 2, 1, 1, 1, 0, -1}));
    }
}
