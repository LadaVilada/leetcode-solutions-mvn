package system.design.graph.weighted;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static system.design.graph.GraphUtils.*;

public class WeightedGraph {

    private Map<Integer, List<int[]>> adjList;

    public WeightedGraph() {
        this.adjList = new HashMap<>();
    }

    public Map<Integer, List<int[]>> getAdjList() {
        return adjList;
    }

    private void addEdge(int start, int dest, int weight) {
        adjList.computeIfAbsent(start, k -> new ArrayList<>()).add(new int[]{dest, weight});
        adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(new int[]{start, weight});
    }

    public static void main(String[] args) {
        // Test the WeightedGraph class
        WeightedGraph graph = new WeightedGraph();
        graph.addEdge(0, 1, 4);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 1, 2);
        graph.addEdge(2, 3, 5);

        int source = 0;
        Map<Integer, VertexDataPair<Integer, Integer>> shortestPaths = dijkstra(graph, source);
//        System.out.println("Shortest path: " + shortestPaths);
        System.out.println(shortestPaths.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .toList());

        int target = 3;
        System.out.println("Shortest path to " + target + ": ");
        printList(getShortestPath(shortestPaths, target));
    }

}
