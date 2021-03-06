import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class EfficientWordMarkov2 implements MarkovInterface<WordGram> {
	private String[] myText;
	private Random myRandom;
	private int myOrder;
	private HashMap<String, ArrayList<Integer>> indexMap = new HashMap<>();
	private ArrayList<Double> extraction_time = new ArrayList<>(); 
	private ArrayList<Integer> map_size = new ArrayList<>();
	
	TreeMap<WordGram, ArrayList<String>> wordMap = new TreeMap<>();
	
	private static String PSEUDO_EOS = "";
	private static long RANDOM_SEED = 1234;
	
	public EfficientWordMarkov2(int order) {
		myRandom = new Random(RANDOM_SEED);
		myOrder = order;
	}

	public void setTraining(String text) {
		myText = text.split("\\s+");
		buildIndexmap(myText);
	}

	public String getRandomText(int length) {
		StringBuilder output_string = new StringBuilder();
		if ((myText.length - myOrder) <= 0)
		{
			for (String each:myText)
			{
				output_string.append(each);
				output_string.append(" ");
			}
			return output_string.toString().trim();
		}
		int index = myRandom.nextInt(myText.length - myOrder);
		WordGram current = new WordGram(myText, index, myOrder);
		appeand_word_gram(output_string, current);
//		output_string.append(current.toString());
//		output_string.append(" ");
		for (int idx = 0; idx < length-myOrder; idx += 1)
		{
			ArrayList<String> current_follows = getFollows(current);
			if (current_follows.size() == 0)
			{
				break;
			}
			index = myRandom.nextInt(current_follows.size());
			String next_word = current_follows.get(index);
			if (next_word.equals(PSEUDO_EOS))
			{
				break;
			}
			output_string.append(next_word);
			output_string.append(" ");
			current = current.shiftAdd(next_word);
		}
//		System.out.println(output_string.toString().trim());
		return output_string.toString().trim();
	}

	public ArrayList<String> getFollows(WordGram key) {
		if (!wordMap.containsKey(key))
		{	
			String head_word = key.get_word()[0];
			int key_len = key.length();
			ArrayList<String> to_add = new ArrayList<>();
			ArrayList<Integer> all_index = indexMap.get(head_word);
			for (int each_index:all_index)
			{
				if (each_index+key_len >= myText.length)
				{
//					to_add.add(PSEUDO_EOS);
					break;
				}
				WordGram possible_wg = new WordGram(myText, each_index, key_len);
				if (possible_wg.equals(key))
				{
					to_add.add(myText[each_index+key_len]);
				}
			}
			wordMap.put(key, to_add);
			return to_add;
		}
		else
		{	
			double start = System.nanoTime();
			ArrayList<String> output_array = wordMap.get(key);
			double total = System.nanoTime() - start;
			extraction_time.add(total);
			map_size.add(wordMap.size());
			return output_array;
		}
	}
	
	private void buildIndexmap(String[] input_text)
	{
		for (int idx = 0; idx < input_text.length; idx +=1)
		{
			String each = input_text[idx];
			if(!indexMap.containsKey(each))
			{
				ArrayList<Integer> to_add = new ArrayList<>();
				to_add.add(idx);
				indexMap.put(each, to_add);
			}
			else
			{
				ArrayList<Integer> to_replace = indexMap.get(each);
				to_replace.add(idx);
				indexMap.put(each, to_replace);
			}
		}
	}
	
	private void appeand_word_gram(StringBuilder input_string_builder, WordGram input_word_gram)
	{
		String[] all_word = input_word_gram.get_word();
		for (String each_word: all_word)
		{
			input_string_builder.append(each_word);
			input_string_builder.append(" ");
		}
	}
	
	public int getSize()
	{
		return myText.length;
	}
	
	public int getOrder() {
		// TODO Auto-generated method stub
		return myOrder;
	}

	public void setSeed(long seed) {
		// TODO Auto-generated method stub
		RANDOM_SEED = seed;
		myRandom = new Random(RANDOM_SEED);	
	}
	
	public int mapSize()
	{
		return wordMap.size();
	}
	
	public void print_running()
	{
		System.out.println(map_size);
		System.out.println(extraction_time);
	}
	
}
