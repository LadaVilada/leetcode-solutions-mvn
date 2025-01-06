package algorithms.dynamic.provinces;

public class UnionFind {

    private int[] id;
    private int[] weight;
    private int count;

    public UnionFind(int n) {
        id = new int[n];
        weight = new int[n];
        count = n;

        for (int i = 0; i < n; i++) {
            id[i] = i;
            weight[i] = 1;
        }
    }

    public void union (int p, int q) {
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

        count--;

    }

    /**
     * Find the corresponding node root
     * Flatten the tree up by making every other node in pass
     * point to its grandparent.
     */
    public int find(int p) {
        while (p != id[p]) {
            id[p] = id[id[p]];
            p = id[p];
        }
        return p;
    }

    public int getCount() {
        return count;
    }
}