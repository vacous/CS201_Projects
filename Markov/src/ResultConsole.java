import java.io.File;
import java.util.ArrayList;
import java.util.Random;
// Provide result for analysis 
public class ResultConsole {
	public static void main(String[] args)
	{
		testBrute();
		System.out.println("------------------------------------------");
		testEfficient();
		System.out.println("------------------------------------------");
		testDifferentHash();
		System.out.println("------------------------------------------");
		testClinton();
		System.out.println("------------------------------------------");
		compareTree();
		testEffMarkove();

	}
	
	
	public static void testBrute()
	{
		String[] test_file_array = {"data/alice.txt","data/alice.txt","data/alice.txt",
				"data/alice.txt","data/alice.txt","data/alice.txt",
				"data/alice.txt","data/alice.txt","data/alice.txt",
				"data/hawthorne.txt","data/hawthorne.txt","data/hawthorne.txt"};
		int[] order_array = {1,1,1,5,5,5,10,10,10,1,1,1};
		int[] return_length_array = {200,400,800,200,400,800,200,400,800,200,400,800};
		ArrayList<Double> timer_result = new ArrayList<>();
		ArrayList<Integer> file_length = new ArrayList<>(); 
		ArrayList<Integer> return_text_length = new ArrayList<>();
		for (int idx = 0; idx < test_file_array.length; idx +=1)
		{	
			String current_address = test_file_array[idx];
			int current_order = order_array[idx];
			int current_return = return_length_array[idx];
			// read in file  
			String filename = current_address;
			File f = new File(filename);
			String text = TextSource.textFromFile(f);
			double start_time = System.nanoTime();
			// new Brute for testing 
			BruteMarkov test_brute = new BruteMarkov(current_order);
			test_brute.setTraining(text);
			String generated_text = test_brute.getRandomText(current_return);
			double total_time = System.nanoTime() - start_time;
			timer_result.add(total_time/Math.pow(10, 9));
			file_length.add(text.length());
			return_text_length.add(generated_text.length());
		}
		System.out.println(timer_result);
		System.out.println(file_length);
		System.out.println(return_text_length);
	}
	
	
	public static void testEfficient()
	{
		String file_address = "data/alice.txt";
		String filename = file_address;
		File f = new File(filename);
		String text = TextSource.textFromFile(f);
		EfficientMarkov test_efficient = new EfficientMarkov(5);
		int[] output_size = {200,400,800,1600};
		test_efficient.setTraining(text);
		ArrayList<Double> running_time = new ArrayList<>();
		for (int idx = 0; idx < output_size.length; idx +=1)
		{
			double start = System.nanoTime();
			test_efficient.getRandomText(output_size[idx]);
			double total_time = System.nanoTime() - start;
			running_time.add(total_time/Math.pow(10, 9));
		}
		System.out.println(running_time);
	}

	public static void testDifferentHash()
	{
		String file_address = "data/alice.txt";
		String filename = file_address;
		File f = new File(filename);
		String text = TextSource.textFromFile(f);
		int[] output_length = {200,400,800,1600,3200,6400};
		ArrayList<Double> good_hash_time = new ArrayList<>();
		EfficientWordMarkov test_word_good = new EfficientWordMarkov(3);
		test_word_good.setTraining(text);
		for (int idx = 0; idx < output_length.length; idx +=1)
		{
			double start_good = System.nanoTime();
			test_word_good.getRandomText(output_length[idx]);
			double total_good = System.nanoTime() - start_good;
			good_hash_time.add(total_good/Math.pow(10, 9));			
		}
		System.out.print("Good Hashing: ");
		System.out.println(good_hash_time);
	}

	public static void testClinton()
	{
		String file_address = "data/clinton-convention.txt";
		String filename = file_address;
		File f = new File(filename);
		String text = TextSource.textFromFile(f);
		int output_size = text.length() * 1000;
		System.out.println(text.length());
		ArrayList<Integer> generated_size = new ArrayList<>();
		Random test_random = new Random();
		int total_length = 0;
		for (int idx = 0; idx < 300; idx +=1)
		{	
			int seed_value = test_random.nextInt(Integer.MAX_VALUE);
			EfficientMarkov test_word = new EfficientMarkov(3);
			test_word.setTraining(text);
			test_word.setSeed(seed_value);
			String generated_text = test_word.getRandomText(output_size);
			generated_size.add(generated_text.length());
			total_length += generated_text.length();
		}
		System.out.println(generated_size);
		System.out.println(total_length/300);
	}
	
	public static void compareTree()
	{
		String file_address = "data/alice.txt";
		String filename = file_address;
		File f = new File(filename);
		String text = TextSource.textFromFile(f);
		Random test_random = new Random();
		int output_size = 2000;
		for (int idx = 0; idx < 1; idx +=1)
		{	
			int seed_value = test_random.nextInt(Integer.MAX_VALUE);
			EfficientWordMarkov test_word = new EfficientWordMarkov(1);
			test_word.setSeed(seed_value);
			test_word.setTraining(text);
			output_size += idx*1000;
			test_word.getRandomText(output_size);
			test_word.print_running();
			//
			EfficientWordMarkov2 test_tree = new EfficientWordMarkov2(1);
			test_tree.setSeed(seed_value);
			test_tree.setTraining(text);
			test_tree.getRandomText(output_size);
			test_tree.print_running();
		}
	}
	
	public static void testEffMarkove()
	{
		String file_address = "data/confucius.txt";
		String filename = file_address;
		File f = new File(filename);
		String text = TextSource.textFromFile(f);
//		Random test_random = new Random();
//		String text = "a b b b b c d e";
		EfficientWordMarkov test_mark = new EfficientWordMarkov(3);
		test_mark.setSeed(621);
		test_mark.setTraining(text);
		System.out.println(test_mark.getRandomText(200));
	}
}
