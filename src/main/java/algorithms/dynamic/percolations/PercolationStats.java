package algorithms.dynamic.percolations;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Monte Carlo simulation.
 * The PercolationStats class estimates the percolation threshold using a Monte Carlo simulation. That means it's inherently randomized.
 * To estimate the percolation threshold, consider the following computational experiment:
 * Initialize all sites to be blocked.
 * Repeat the following until the system percolates:
 * Choose a site uniformly at random among all blocked sites.
 * Open the site.
 * The fraction of sites that are opened when the system percolates provides an estimate of the percolation threshold.
 * Throw an IllegalArgumentException in the constructor if either n ≤ 0 or trials ≤ 0.
 */
public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;
    private final double[] thresholds;
    private final int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must be greater than 0");
        }
        this.thresholds = new double[trials];
        this.trials = trials;

        for (int t = 0; t < trials; t++) {
            Percolation perc = new Percolation(n);
            // Repeatedly open random blocked sites until the system percolates.
            while (!perc.percolates()) {
                // Returns a random integer uniformly in [a, b).
                int row = StdRandom.uniformInt(1, n + 1);
                int col = StdRandom.uniformInt(1, n + 1);
                perc.open(row, col);
            }
            // Store the fraction of open sites when percolation occurs
            double threshold = (double) perc.numberOfOpenSites() / (n * n);
            // Store threshold in an array or list for further calculations
            thresholds[t] = threshold;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        // sum of all values in array divided by the number of trials
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        // standard deviation of all values in array
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        // mean - (1.96 * stddev) / sqrt(trials)
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        // mean + (1.96 * stddev) / sqrt(trials)
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);

        System.out.println("mean = " + percolationStats.mean());
        System.out.println("stddev = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");

    }
}
