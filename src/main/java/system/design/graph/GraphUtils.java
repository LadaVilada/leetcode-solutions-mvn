package system.design.graph;

import system.design.graph.weighted.WeightedGraph;

import java.util.*;

public class GraphUtils {

    /**
     * Depth First Search (DFS) → Traverse a graph by exploring
     * as far as possible along each branch before backtracking.
     * <p>
     * NOTE: f the graph has a large number of nodes (V > 10^5),
     * recursion can cause StackOverflowError.
     */
    public static void dfsRecursive(Integer node, Map<Integer, List<Integer>> adjList,
                                    List<Integer> component, boolean[] visited) {
        visited[node] = true;
        component.add(node);

        for (int neighbor : adjList.getOrDefault(node, new ArrayList<>())) {
            if (!visited[neighbor]) {
                dfsRecursive(neighbor, adjList, component, visited);
            }
        }
    }

    /**
     * Depth First Search (DFS) → Traverse a graph by exploring
     * Use an iterative DFS with an explicit stack.
     *
     * @param node
     * @param adjList
     * @param component
     * @param visited
     */
    public static void dfsIterative(Integer node, Map<Integer, List<Integer>> adjList,
                                    List<Integer> component, Set<Integer> visited) {
        Stack<Integer> stack = new Stack<>();
        stack.push(node);

        while (!stack.isEmpty()) {
            int current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);
                component.add(current);
                for (int neighbor : adjList.computeIfAbsent(current, k -> new ArrayList<>())) {
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);
                    }
                }
            }
        }
    }

    /**
     * Connected Components → A group of nodes connected directly or indirectly.
     *
     * @param edges    an array of edges (which connect the nodes)
     * @param vertices number of nodes
     */
    public static List<List<Integer>> findConnectedComponents(Integer[][] edges, int vertices) {
        // Create Adjacency List: graph representation
        Map<Integer, List<Integer>> adjList = createGraphAdjList(edges);

        // Connected components list
        List<List<Integer>> components = new ArrayList<>();

        // Create a visited set to keep track of visited nodes
        Set<Integer> visited = new HashSet<>();
        for (Integer node : adjList.keySet()) {
            if (!visited.contains(node)) {
                List<Integer> component = new ArrayList<>();
                dfsIterative(node, adjList, component, visited);
                components.add(component);
            }
        }

        return components;
    }

    public static <T> Map<T, List<T>> createGraphAdjList(T[][] edges) {
        Map<T, List<T>> adjList = new HashMap<>();
        for (T[] edge : edges) {
            addGraphEdgeToList(adjList, edge[0], edge[1]);
        }
        return adjList;
    }

    public static int[][] createGraphAdjMatrix(Integer[][] edges, int vertices) {
        int[][] adjMatrix = new int[vertices][vertices];
        for (Integer[] edge : edges) {
            int start = edge[0];
            int dest = edge[1];
            addGraphEdgeToMatrix(adjMatrix, start, dest, 1);
        }
        return adjMatrix;
    }

    public static void addGraphEdgeToMatrix(int[][] edges, int start, int dest, int weight) {
        if (start < 0 || dest < 0 || start >= edges.length || dest >= edges.length) {
            throw new IllegalArgumentException("Invalid graph node indices.");
        }

        edges[start][dest] = weight; // Directed Graph
        edges[dest][start] = weight; // Uncomment for Undirected Graph

    }

    private static <T> void addGraphEdgeToList(Map<T, List<T>> adjList, T start, T dest) {
        if (start == null || dest == null) {
            throw new IllegalArgumentException("Graph nodes cannot be null.");
        }

        adjList.computeIfAbsent(start, k -> new ArrayList<>()).add(dest);
        adjList.computeIfAbsent(dest, k -> new ArrayList<>()).add(start); // if undirected graph
    }

    public static void printGraph(Map<Integer, List<Integer>> adjList) {
        for (Map.Entry<Integer, List<Integer>> entry : adjList.entrySet()) {
            System.out.print("Vertex:" + entry.getKey() + " ");
            System.out.print("Edges:" + entry.getValue());
            System.out.println();
        }

    }

    public static void printGraph(int[][] adjMatrix) {
        for (int[] row : adjMatrix) {
            for (int col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
    }

    public static class VertexDataPair<T, U> {
        T path;
        U prev;

        public VertexDataPair(T path, U prev) {
            this.path = path;
            this.prev = prev;
        }

        public T getPath() {
            return path;
        }

        public U getPrev() {
            return prev;
        }

        @Override
        public String toString() {
            return "VertexDataPair{" +
                    "shortest path=" + path +
                    ", prev node =" + prev +
                    '}';
        }
    }

    public static Map<Integer, VertexDataPair<Integer, Integer>> dijkstra(WeightedGraph graph, int source) {
//        Set<Integer> visited = new HashSet<>();

        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(a -> a[1])); // a[1] is the edge weight

        // key: vertex (node), value: shortest weight>
        // TODO: change to VertexDataPair
        Map<Integer, Integer> vertexData = new HashMap<>();
        Map<Integer, VertexDataPair<Integer, Integer>> vertexDataPairs = new HashMap<>();

        minHeap.offer(new int[]{source, 0});
//        vertexData.put(source, 0);
        vertexDataPairs.put(source, new VertexDataPair<>(0, -1));

        while (!minHeap.isEmpty()) {
            int [] current = minHeap.poll();

            for (var neighbor : graph.getAdjList().getOrDefault(current[0], new ArrayList<>())) {
                int newWeight = neighbor[1] + current[1];


//                if (vertexData.getOrDefault(neighbor[0], Integer.MAX_VALUE) > newWeight) {
                if (vertexDataPairs.getOrDefault(neighbor[0],
                        new VertexDataPair<>(Integer.MAX_VALUE, -1)).getPath()
                        > newWeight) {
//                    vertexData.put(neighbor[0], newWeight);
                    vertexDataPairs.put(neighbor[0], new VertexDataPair<>(newWeight, current[0]));
                    minHeap.offer(new int[]{neighbor[0], newWeight});
                }
            }

        }

        return vertexDataPairs;
    }

    public static List<Integer> getShortestPath(Map<Integer, VertexDataPair<Integer, Integer>> vertexData, int target) {
        List<Integer> pathList = new ArrayList<>();

        for (Integer vertex = target; vertex != -1;
             vertex = vertexData.get(vertex).getPrev()) {
            pathList.add(vertex);
        }

        Collections.reverse(pathList);
        return pathList;
    }

    public static void printList(List<Integer> list) {
        for (Integer node : list) {
            System.out.print(node + " -> ");
        }
        System.out.println();
    }

}
