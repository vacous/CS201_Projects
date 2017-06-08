import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Simulate a system to see its Percolation Threshold, but use a UnionFind
 * implementation to determine whether simulation occurs. The main idea is that
 * initially all cells of a simulated grid are each part of their own set so
 * that there will be n^2 sets in an nxn simulated grid. Finding an open cell
 * will connect the cell being marked to its neighbors --- this means that the
 * set in which the open cell is 'found' will be unioned with the sets of each
 * neighboring cell. The union/find implementation supports the 'find' and
 * 'union' typical of UF algorithms.
 * <P>
 * 
 * @author Owen Astrachan
 * @author Jeff Forbes
 *
 */

public class PercolationUF implements IPercolate {
	private final int OUT_BOUNDS = -1;
	public int[][] myGrid;
	private IUnionFind myUninos;
	private int openSites;
	private int virtual_top;
	private int virtual_bot;

	/**
	 * Constructs a Percolation object for a nxn grid that that creates
	 * a IUnionFind object to determine whether cells are full
	 */
	public PercolationUF(int n) {
		if (n<=0)
			// out of bounds
			throw new IllegalArgumentException("size must be no negative");
		// TODO complete PercolationUF constructor
		myUninos = new QuickUWPC(n*n+2);
		myGrid = new int[n][n];
		virtual_top= n*n;
		virtual_bot = n*n+1;
		for (int[] row: myGrid)
			Arrays.fill(row, BLOCKED);
	}

	/**
	 * Return an index that uniquely identifies (row,col), typically an index
	 * based on row-major ordering of cells in a two-dimensional grid. However,
	 * if (row,col) is out-of-bounds, return OUT_BOUNDS.
	 */
	public int getIndex(int row, int col) {
		// TODO complete getIndex
		if (row < 0 || row >= myGrid.length || col < 0 || col >= myGrid[0].length)
			// out of bounds
			return OUT_BOUNDS;
		return myUninos.find(row * myGrid.length + col);
		
	}

	public void open(int i, int j) {
		// TODO complete open
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i +"," +j+
					" is bad!");
		if(!isOpen(i, j))
		{
			myGrid[i][j] = OPEN;
			openSites += 1;
		}
		connect(i, j);
	}

	public boolean isOpen(int i, int j) {
		// TODO complete isOpen
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i +"," +j+
					" is bad!");
		return myGrid[i][j] == OPEN  || myGrid[i][j] == FULL;
	}

	public boolean isFull(int i, int j) {
		// TODO complete isFull
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i +"," +j+
					" is bad!");
		int current_idx = i * myGrid.length + j;
		if(myUninos.connected(current_idx, virtual_top))
		{
			myGrid[i][j] = FULL;
		}
		return myGrid[i][j] == FULL;
	}
	
	private boolean checkAvailable(int i, int j)
	{
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			return false;
		return myGrid[i][j] != BLOCKED;
	}
	
	public int numberOfOpenSites() {
		// TODO return the number of calls to open new sites
		return openSites;
	}
	
	private double percTime;
	public boolean percolates() {
		double start = System.nanoTime();
		boolean if_percolate = myUninos.connected(virtual_top, virtual_bot);
		percTime += (System.nanoTime()-start)/Math.pow(10, 9);
		return if_percolate;
	}
	public double getPercTime()
	{
		return percTime;
	}
	/**
	 * Connect new site (row, col) to all adjacent open sites
	 */
	private void connect(int row, int col) {
		// TODO complete connect
		int length = myGrid.length;
		int current_idx = row * length + col;
		if(row == 0)
		{
			myUninos.union(current_idx, virtual_top);
		}
		if(row == myGrid.length-1)
		{
			myUninos.union(current_idx, virtual_bot);
		}
		ArrayList<Point> four_sides = new ArrayList<>();
		four_sides.add(new Point(row-1, col));
		four_sides.add(new Point(row+1, col));
		four_sides.add(new Point(row, col-1));
		four_sides.add(new Point(row, col+1));
		for(Point each_point: four_sides)
		{
			if(checkAvailable(each_point.x, each_point.y)){
				int side_idx = each_point.x * length + each_point.y;
				myUninos.union(current_idx,side_idx);
			}
		}
	}

}
