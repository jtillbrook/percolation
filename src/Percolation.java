import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class Percolation {
	private int size;
	private int top = 0;	// virtual top node
	private int bottom;		// virtual bottom node
	private WeightedQuickUnionUF wquf;
	private int [][] grid;
	
	// create N-by-N grid, with all sites blocked
	public Percolation(int N)      
	{	
		if (N <= 0)
			throw new IllegalArgumentException("N cannot be less or equal to zero.");
		size = N;
		bottom = size * size + 1;
		grid = new int[size][size];
		wquf = new WeightedQuickUnionUF((size * size) + 2);
	}
	
	// open site (row i, column j) if it is not open already
	public void open(int i, int j)          
	{
		checkBounds(i,j);
		
		grid[i-1][j-1] = 1;								// mark node as open
		
		if (i==1)
			wquf.union(xyTo1D(i,j), top);				// connect to virtual top
		
		if (i == size)
			wquf.union(xyTo1D(i,j), bottom);			// connect to virtual bottom
		
		if ((i>1) && isOpen(i-1, j))					// connect to open (row-1, col)
			wquf.union(xyTo1D(i-1, j), xyTo1D(i,j));
		
		if ((i<size) && isOpen(i+1, j))
			wquf.union(xyTo1D(i+1, j), xyTo1D(i,j));	// connect to open (row+1, col)
		
		if ((j>1) && isOpen(i, j-1))
			wquf.union(xyTo1D(i, j-1), xyTo1D(i,j));	// connect to open (row, col-1)
		
		if ((j<size) && isOpen(i, j+1))
			wquf.union(xyTo1D(i, j+1), xyTo1D(i,j));	// connect to open (row, col+1)		
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j)
	{	
		checkBounds(i,j);		
		return grid[i-1][j-1] == 1;
	}
	
	// is site (row i, column j) full?
	public boolean isFull(int i, int j)     
	{	
		checkBounds(i,j);		
		return wquf.connected(top, xyTo1D(i, j));
	}
	
	// does the system percolate?
	public boolean percolates()             
	{
		return wquf.connected(top, bottom);
	}
	
	private int xyTo1D(int i, int j) {
		return ((i-1) * size + j);
	}
	
	private void checkBounds(int i, int j)
	{
		if ((i < 1 || i > size) || (j < 1 || j > size))
			throw new IndexOutOfBoundsException("Index i/j out of bounds.");
	}

	public static void main(String[] args)  // test client (optional)
	{
		final int num_tests = 1000;
		final int grid_size = 20;
			
		StdOut.println("Begin Generating Random Sites...");
		
		int row, col, count;
		double sum = 0.0;
		
		for (int i=0; i < num_tests; i++)
		{
			count = 0;
			Percolation p = new Percolation(grid_size);
			while (!p.percolates())
			{
				// choose site at random
				row = StdRandom.uniform(1, grid_size+1);
				col = StdRandom.uniform(1, grid_size+1);
				
				if (!p.isOpen(row, col))
				{
					p.open(row, col);
					count++;
				}
			}
			sum += count;			
		}
		
		StdOut.println("Average number of sites opened: " + sum/num_tests);
		
		
	}
}