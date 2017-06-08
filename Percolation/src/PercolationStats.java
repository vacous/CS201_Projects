import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * Compute statistics on Percolation afte performing T independent experiments on an N-by-N grid.
 * Compute 95% confidence interval for the percolation threshold, and  mean and std. deviation
 * Compute and print timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 * @author Josh Hug
 */

public class PercolationStats {
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	private int mySize;
	private int myTrialNum;
	private ArrayList<Double> thresholdRecord;

	// TODO Add methods as described in assignment writeup
	public PercolationStats(int grid_size, int trial_num)
	{	
		thresholdRecord = new ArrayList<>();
		if(grid_size <= 0 || trial_num <= 0)
		{
			throw new IllegalArgumentException("size and the trial num must be positive"); 
		}
		mySize = grid_size;
		myTrialNum = trial_num;
		for(int current_trial_num = 0; current_trial_num < trial_num; current_trial_num +=1)
		{
			List<Point> sites = getShuffledCells();
			IPercolate current_perc = new PercolationUF(grid_size);
			for(Point cell: sites)
			{
				current_perc.open(cell.x, cell.y);
				if(current_perc.percolates())
				{
					break;
				}
			}
			double current_sites = (double)current_perc.numberOfOpenSites();
			thresholdRecord.add(current_sites/(grid_size * grid_size));	
		}
	}
	
	private List<Point> getShuffledCells() {
		ArrayList<Point> list = new ArrayList<Point>();
		for (int i = 0; i < mySize; i++)
			for (int j = 0; j < mySize; j++)
				list.add(new Point(i,j));
		Collections.shuffle(list, ourRandom );
		return list;
	}
	
	public double mean()
	{
		int counter = 0;
		double total = 0;
		for(double each_threshold: thresholdRecord)
		{
			total += each_threshold;
			counter += 1;
		}
		return total/counter;
	}
	
	public double stddev()
	{
		int counter = 0;
		double mean = mean();
		double sq_diff_sum = 0;
		for(double each_threshold: thresholdRecord)
		{
			sq_diff_sum += Math.pow((each_threshold - mean), 2);
			counter += 1;
		}
		return Math.pow(sq_diff_sum/(counter-1),0.5);
	}
	
	
	public double confidenceLow()
	{
		return mean() - 1.96 * stddev()/Math.pow(myTrialNum, 0.5);
	}
	
	public double confidenceHigh()
	{
		return mean() + 1.96 * stddev()/Math.pow(myTrialNum, 0.5);
	}
	
	public static void main(String[] args)
	{
		PercolationStats test_perc = new PercolationStats(20, 100);
		System.out.println("mean: " + test_perc.mean());
		System.out.println("Confidence: " + 
				test_perc.confidenceLow() + " ~ " + test_perc.confidenceHigh());
	}
	
	
}
