package algorithms.dynamic.percolations;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * The Percolation class represents a percolation data type.
 * /n
 * It supports the classic checking of whether a system percolates, cell is open, cell is full (means the path from the cell to the top is open),
 * and the number of open sites.
 * /n
 * Corner cases.
 * By convention, the row and column indices are integers between 1 and n, where (1, 1) is the upper-left site:
 * Throw an IllegalArgumentException if any argument to open(), isOpen(), or isFull() is outside its prescribed range.
 * Throw an IllegalArgumentException in the constructor if n ≤ 0.
 * /n
 * Why use two union-find structures?
 * This means that once the system percolates, the virtual top and bottom are connected.
 * Then, isFull() will mistakenly return true for open bottom sites that are only connected to the bottom (not actually full), because they are now connected to the top through the virtual bottom. This pollutes the result of percolates() in subsequent tests.
 */
public class Percolation {

    private final WeightedQuickUnionUF uf; // for percolation checks only
    private final WeightedQuickUnionUF ufFullness; // for isFull checks
    private final int virtualTop;
    private final int virtualBottom;
    private final boolean[][] open;
    // Keep a counter that increments each time you open a site.
    private int openSites;
    private final int n; // size of the grid

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size must be greater than 0");
        }
        this.n = n;
        // Initialize the grid and union-find structure WeightedQuickUnionUF
        // Use a 2D array to represent the grid
        open = new boolean[n][n];
        // Use WeightedQuickUnionUF to manage connected components
        uf = new WeightedQuickUnionUF(n * n + 2); // plus virtual top and bottom
        ufFullness = new WeightedQuickUnionUF(n * n + 1); // only virtual top
        virtualTop = 0;
        virtualBottom = n * n + 1;
        openSites = 0;

    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        // validate indices
        validateIndices(row, col);

        // keep indices 0-indexed for internal processing
        int i = row - 1;
        int j = col - 1;

        if (!open[i][j]) {
            // mark the site as open
            open[i][j] = true;
            openSites++;

            // convert (row, col) to 1D index
            int index = convertTo1D(i, j);

            // If it’s in the top row, connect to virtual top node.
            if (i == 0) {
                uf.union(index, virtualTop);
                ufFullness.union(index, virtualTop);
            }

            // If it’s in the bottom row, connect to virtual bottom node.
            if (i == n - 1) {
                uf.union(index, virtualBottom);
            }

            // Connect this site to any open neighboring sites (up, down, left, right).
            // up
            if (i > 0 && isOpen(row - 1, col)) {
                uf.union(index, convertTo1D(i - 1, j));
                ufFullness.union(index, convertTo1D(i - 1, j));
            }

            // down
            if (i < n - 1 && isOpen(row + 1, col)) {
                uf.union(index, convertTo1D(i + 1, j));
                ufFullness.union(index, convertTo1D(i + 1, j));
            }
            // left
            if (j > 0 && isOpen(row, col - 1)) {
                uf.union(index, convertTo1D(i, j - 1));
                ufFullness.union(index, convertTo1D(i, j - 1));
            }
            // right
            if (j < n - 1 && isOpen(row, col + 1)) {
                uf.union(index, convertTo1D(i, j + 1));
                ufFullness.union(index, convertTo1D(i, j + 1));
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        // validate indices
        validateIndices(row, col);
        return open[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return isOpen(row, col) && ufFullness.find(convertTo1D(row - 1, col - 1)) == uf.find(virtualTop);

    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    private void validateIndices(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException("row and col must be between 1 and n");
        }
    }

    private int convertTo1D(int row, int col) {
        return row * n + col;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(virtualTop) == uf.find(virtualBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        // Test 1: 1x1 grid should percolate after opening the only site
        Percolation perc1 = new Percolation(1);
        perc1.open(1, 1);
        System.out.println("Test 1 (1x1 grid percolates): " + perc1.percolates()); // Expected: true

        // Test 2: 3x3 grid does NOT percolate when no sites are open
        Percolation perc2 = new Percolation(3);
        System.out.println("Test 2 (3x3 grid without opens): " + perc2.percolates()); // Expected: false

        // Test 3: 3x3 vertical percolation
        Percolation perc3 = new Percolation(3);
        perc3.open(1, 2);
        perc3.open(2, 2);
        perc3.open(3, 2);
        System.out.println("Test 3 (3x3 vertical percolation): " + perc3.percolates()); // Expected: true

        // Test 4: Diagonal open should NOT percolate
        Percolation perc4 = new Percolation(3);
        perc4.open(1, 1);
        perc4.open(2, 2);
        perc4.open(3, 3);
        System.out.println("Test 4 (3x3 diagonal open): " + perc4.percolates()); // Expected: false

        // Test 5: isFull check
        Percolation perc5 = new Percolation(3);
        perc5.open(1, 1);
        perc5.open(2, 1);
        perc5.open(2, 2);
        System.out.println("Test 5.1 (isFull 2,1): " + perc5.isFull(2, 1)); // Expected: true
        System.out.println("Test 5.2 (isFull 2,2): " + perc5.isFull(2, 2)); // Expected: false
    }


}
