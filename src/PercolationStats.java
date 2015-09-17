import java.security.InvalidParameterException;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
	
	private int numExperiments;
	private int grid_size;
	private Percolation percolation;
	private double[] expResults;
	
	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T)
	{	
		if (N <= 0 || T <= 0)
			throw new IllegalArgumentException("N and T must be greater than 0.");
		
		numExperiments = T;
		expResults = new double[numExperiments];
		grid_size = N;
		int row, col;
		
		for (int i=0; i < numExperiments; i++)
		{
			percolation = new Percolation(grid_size);
			int numOpened = 0;
			while (!percolation.percolates())
			{
				// choose site at random (from 1 (inclusive) to N+1 (exclusive))
				row = StdRandom.uniform(1, grid_size+1);
				col = StdRandom.uniform(1, grid_size+1);
				
				if (!percolation.isOpen(row, col))
				{
					percolation.open(row, col);
					numOpened++;
				}
			}
			
			// get fraction of sites opened when grid percolates
			double expResult = (double) numOpened / (grid_size * grid_size);
			expResults[i] = expResult;
		}
		
	}
	
	// sample mean of percolation threshold
	public double mean()          
	{
		return StdStats.mean(expResults);
	}
	
	// sample standard deviation of percolation threshold
	public double stddev()    
	{
		return StdStats.stddev(expResults);
	}
	
	// low endpoint of 95% confidence interval
	public double confidenceLo()   
	{
		return mean() - ((1.96 * stddev()) / Math.sqrt(numExperiments));
	}
	
	// high endpoint of 95% confidence interval
	public double confidenceHi()        
	{
		return mean() + ((1.96 * stddev()) / Math.sqrt(numExperiments));
	}

	// test client
	public static void main(String[] args)
	{
		if (args.length < 2)
			throw new InvalidParameterException("PercolationStats takes 2 parameters.");
		
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		
		PercolationStats ps = new PercolationStats(N, T);
		
		StdOut.println("mean = \t\t\t\t" + ps.mean());
		StdOut.println("stddev = \t\t\t" + ps.stddev());
		StdOut.println("95% confidence interval = \t" + ps.confidenceLo() + ", " + ps.confidenceHi());
		
	}
}