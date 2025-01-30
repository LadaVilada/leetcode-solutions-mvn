package algorithms.dynamic.redundantConnection;

import algorithms.dynamic.provinces.WeightedQuickUnionUF;

import java.util.Arrays;

/**
 * 684. Redundant Connection
 * <p>
 * In this problem, a tree is an undirected graph that is connected and has no cycles.
 * <p>
 * You are given a graph that started as a tree with n nodes labeled from 1 to n, with one additional edge added. The added edge has two different vertices chosen from 1 to n, and was not an edge that already existed. The graph is represented as an array edges of length n where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the graph.
 * <p>
 * Return an edge that can be removed so that the resulting graph is a tree of n nodes. If there are multiple answers, return the answer that occurs last in the input.
 */
public class RedundantConnection {

    public int[] findRedundantConnection(int[][] edges) {

        int n = edges.length;
        // n + 1 because the nodes are 1-indexed by the problem
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(n + 1);
        int[] result = new int[2];

        for (int i = 0; i < n; i++) {
            System.out.println("pair = " + i + ": " + edges[i][0] + " " + edges[i][1]);
            if (uf.find(edges[i][0]) == uf.find(edges[i][1])) {
                // redundant pair found
                System.out.println("redundant pair found");
                result[0] = edges[i][0];
                result[1] = edges[i][1];
            } else {
                // connect two nodes
                uf.union(edges[i][0], edges[i][1]);
            }

        }

        return result;
    }

    public static void main(String[] args) {
        int[][] edges = {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};

        RedundantConnection connection = new RedundantConnection();
        int[] result = connection.findRedundantConnection(edges);
        System.out.println("An edge that can be removed: " + Arrays.toString(result));

    }
}
