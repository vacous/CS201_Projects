import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import com.sun.corba.se.spi.orbutil.fsm.Input;

/**
 * Simulate percolation thresholds for a grid-base system using depth-first-search,
 * aka 'flood-fill' techniques for determining if the top of a grid is connected
 * to the bottom of a grid.
 * <P>
 * Modified from the COS 226 Princeton code for use at Duke. The modifications
 * consist of supporting the <code>IPercolate</code> interface, renaming methods
 * and fields to be more consistent with Java/Duke standards and rewriting code
 * to reflect the DFS/flood-fill techniques used in discussion at Duke.
 * <P>
 * @author Kevin Wayne, wayne@cs.princeton.edu
 * @author Owen Astrachan, ola@cs.duke.edu
 * @author Jeff Forbes, forbes@cs.duke.edu
 */


public class PercolationDFS implements IPercolate {
	// possible instance variable for storing grid state
	public int[][] myGrid;
	private int myOpenSites;
	public ArrayList<Point> searchedPoints = new ArrayList<>();

	/**
	 * Initialize a grid so that all cells are blocked.
	 * 
	 * @param n
	 *            is the size of the simulated (square) grid
	 */
	public PercolationDFS(int n) {
		if (n<=0)
			// out of bounds
			throw new IllegalArgumentException("size must be no negative");
		// TODO complete constructor and add necessary instance variables
		myOpenSites = 0;
		myGrid = new int[n][n];
		for (int[] row: myGrid)
			Arrays.fill(row, BLOCKED);
	}

	public void open(int i, int j) {
		// TODO complete open
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i +"," +j+
					" is bad!");
		if (myGrid[i][j] != BLOCKED)
			return;
		myOpenSites++;
		restore();
		myGrid[i][j] = OPEN;
		int row = 0;
		for(int col = 0; col < myGrid.length; col +=1)
		{			
			dfs(row,col);
		}
		
	}

	
	public boolean isOpen(int i, int j) {
		// TODO complete isOpen
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i +"," +j+
					" is bad!");
		return myGrid[i][j] == OPEN;
	}

	public boolean isFull(int i, int j) {
		// TODO complete isFull
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i +"," +j+
					" is bad!");
		return myGrid[i][j] == FULL;
	}

	public int numberOfOpenSites() {
		// TODO return the number of calls to open new sites
		return myOpenSites;
	}

	public boolean percolates() {
		int row = myGrid.length - 1;
		for(int col = 0; col < myGrid.length; col +=1)
		{
			if (isFull(row, col))
			{
				return true;
			}
		}
		return false;
	}
	
	
	public void restore()
	{
		for(Point each_point: searchedPoints)
		{
			myGrid[each_point.x][each_point.y] = OPEN;
		}
		searchedPoints = new ArrayList<>();
	}

	
	/**
	 * Private helper method to mark all cells that are open and reachable from
	 * (row,col).
	 * 
	 * @param row
	 *            is the row coordinate of the cell being checked/marked
	 * @param col
	 *            is the col coordinate of the cell being checked/marked
	 */
	private void dfs(int row, int col) {
		if (row < 0 || row >= myGrid.length || col < 0 ||
				col >= myGrid.length)
			// out of bounds
			return;
		if (isFull(row, col) || !isOpen(row, col))
			return;
		myGrid[row][col] = FULL;
		searchedPoints.add(new Point(row, col));
		dfs(row -1, col); // up
		dfs(row, col - 1); // left
		dfs(row, col + 1); // right
		dfs(row + 1, col); // down	
	}
}
