package system.design.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UndirectedGraph {

    private int[][] adjMatrix;

    private Map<Integer, List<Integer>> adjList;

    public UndirectedGraph(int vertices) {
        this.adjMatrix = new int[vertices][vertices];
    }

    public UndirectedGraph() {
        this.adjList = new HashMap<>();
    }

    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    public Map<Integer, List<Integer>> getAdjList() {
        return adjList;
    }

    /**
     * Adds an edge to the edges matrix to the representation of the graph.
     *
     * @param edges    the edges matrix
     * @return adjacency list graph representation
     */
    public UndirectedGraph convertEdgeListToAdjList(int[][] edges) {
        UndirectedGraph graph = new UndirectedGraph();

        for (int[] edge : edges) {
            graph.addGraphEdgeToList(edge[0], edge[1]);
        }

        return graph;

    }

    public void addGraphEdgeToList(int start, int dest) {
        adjList.computeIfAbsent(start, k -> new ArrayList<>()).add(dest);
        adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(start);
    }

    public static void main(String[] args) {
        int vertices = 5;
        Integer[][] edges = {{0, 1}, {1, 2}, {3, 4}};
        UndirectedGraph graph = new UndirectedGraph(vertices);

        // Test both graph representations: list and matrix
        int[][] adjMatrix = GraphUtils.createGraphAdjMatrix(edges, vertices);
        Map<Integer, List<Integer>> adjList = GraphUtils.createGraphAdjList(edges);

        // Print the graph
        System.out.println("Adjacency Matrix:");
        GraphUtils.printGraph(adjMatrix);
        System.out.println("Adjacency List:");
        GraphUtils.printGraph(adjList);

        // Test connected components in a graph
        List<List<Integer>> components = GraphUtils.findConnectedComponents(edges, vertices);

        System.out.println("Connected Components:");
        for (List<Integer> component : components) {
            System.out.println(component);
        }
    }

}
