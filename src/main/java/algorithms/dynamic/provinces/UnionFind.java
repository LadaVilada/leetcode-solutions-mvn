package algorithms.dynamic.provinces;

public class UnionFind {

    // int array to keep track of the parent node of each node
    // helps identify connected components
    private int[] id;
    // keep track of the size of each component
    private int[] weight;
    // Tracks the number of disjoint sets remaining
    private int count;

    /**
     * Initialize the UnionFind data structure with n nodes
     *
     * @param n number of nodes
     */
    public UnionFind(int n) {
        id = new int[n];
        weight = new int[n];
        count = n;

        for (int i = 0; i < n; i++) {
            id[i] = i;
            weight[i] = 1;
        }
    }

    /**
     * Connect two nodes p and q keeping track of the size of each component(tree)
     * <p>
     * Uses find(x) and find(y) to determine the roots of the sets containing x and y.
     * Uses weight to attach the smaller tree under the larger tree, optimizing the structure.
     * If the two sets are merged, count is decremented.
     *
     * @param p node p
     * @param q node q
     */
    public void union(int p, int q) {
        int i = find(p);
        int j = find(q);

        if (i == j) return;

        if (weight[i] < weight[j]) {
            id[i] = j;
            weight[j] += weight[i];
        } else {
            id[j] = i;
            weight[i] += weight[j];
        }

        // Reduce the number of connected components
        count--;

    }

    /**
     * Find the corresponding root of the set containing p, traversing the id[] array structure.
     * <p>
     * Flatten the tree up by making every other node in pass point to its grandparent.
     * (partial path compression)
     * @return the corresponding root
     */
    public int find(int p) {
        while (p != id[p]) {
            // point to grandparent
            id[p] = id[id[p]];
            p = id[p];
        }
        return p;
    }


    /**
     * Find the corresponding root of the set containing p, traversing the id[] array structure.
     * <p>
     * Optional (full path compression) because it uses path compression to optimize the structure,
     * making parent[x] point directly to the root for faster future lookups.
     *
     * @param p node p
     * @return the corresponding root
     */
    public int findOpt(int p) {
        if (id[p] != p) {
            // pointing to the root
            id[p] = findOpt(id[p]);
        }
        return id[p];

    }

    public int getCount() {
        return count;
    }
}