import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TimePercolate {
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	
	
	public static void main(String[] args)
	{
		ArrayList<Double> DFS_running_time = new ArrayList<>();
		ArrayList<Double> QF_running_time = new ArrayList<>();
		ArrayList<Double> UWPC_running_time = new ArrayList<>();
		ArrayList<Integer> test_size = new ArrayList<>();
		for(int size = 5; size < 500; size +=5)
		{
			test_size.add(size);
		}
		ourRandom.setSeed(RANDOM_SEED);
		for(Integer grid_size: test_size)
		{	
			System.out.println(grid_size);
			PercolationDFS current_dfs = new PercolationDFS(grid_size);
			PercolationUF current_UWPC = new PercolationUF(grid_size);
			PercolationUF02 current_UF = new PercolationUF02(grid_size);
			List<Point> shuffled_points = getShuffledCells(grid_size);
			DFS_running_time.add(timeRunning(shuffled_points, current_dfs));
			double current_QF_time = timeRunning(shuffled_points, current_UF);
			double current_QWPC_time = timeRunning(shuffled_points, current_UWPC);
			QF_running_time.add(current_QF_time);
			UWPC_running_time.add(current_QWPC_time);
		}
		System.out.println("DFS Time: " + DFS_running_time);
		System.out.println("QF Time: " + QF_running_time);
		System.out.println("UWPC Time: " + UWPC_running_time);
		
	}

	private static List<Point> getShuffledCells(int input_size) {
		ArrayList<Point> list = new ArrayList<Point>();
		for (int i = 0; i < input_size; i++)
			for (int j = 0; j < input_size; j++)
				list.add(new Point(i,j));
		Collections.shuffle(list, ourRandom);
		return list;
	}
	
	private static double timeRunning(List<Point> shuffledPoints, IPercolate input_perc)
	{
		double start = System.nanoTime();
		for(Point each_point: shuffledPoints)
		{
			input_perc.open(each_point.x, each_point.y);
			if(input_perc.percolates())
			{
				break;
			}
		}
		return (System.nanoTime() - start)/Math.pow(10, 9);
	}

}
