package algorithms.dynamic.friends;

import algorithms.dynamic.provinces.WeightedQuickUnionUF;

/**
 * Given a social network containing
 * n members and a log file containing
 * m timestamps at which times pairs of members formed friendships
 */
public class Friends {

    public static void main(String[] args) {
        int[][] isConnected = {
                {1, 0, 1},
                {2, 1, 2},
                {3, 3, 4},
                {4, 2, 3},
                {5, 4, 5},
                {6, 0, 5}};

        Friends solution = new Friends();
        int result = solution.findEarliestTime(isConnected);
        System.out.println("Earliest time at which all members are connected: " + result);

    }

    private int findEarliestTime(int[][] isConnected) {
        int n = isConnected.length;
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(n);

        for (int[] ints : isConnected) {
            // connect friends
            uf.union(ints[1], ints[2]);
            if (uf.getCount() == 1) {
                return ints[0];
            }

        }
        return -1; // Not all members are connected
    }
}
