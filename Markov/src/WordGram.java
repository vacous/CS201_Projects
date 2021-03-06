import java.util.ArrayList;
import java.util.Arrays;


public class WordGram implements Comparable<WordGram>{
	private String[] myWords;
	private int myHash;
	
	public WordGram(String[] source, int start, int size)
	{
		myWords = new String[size];
		System.arraycopy(source, start, myWords, 0, size);
//		// given method
//		int hashcode_value = 0;
//		for (String each: myWords)
//		{
////			System.out.println(each.hashCode());
//			hashcode_value += each.hashCode() * each.hashCode();
////			System.out.println("--------------------");
//		}
//		myHash = hashcode_value;
		// improved hashing 01
//		myHash = myWords.hashCode();
		// improved hashing 02 (best)
		StringBuilder for_hash = new StringBuilder();
		for (String each: myWords)
		{
			for_hash.append(each);
		}
		String concat_all = for_hash.toString();
		myHash = concat_all.hashCode();
		// final method 
//		myHash = this.toString().hashCode();
//		// old final method
//		ArrayList<String> all_words = new ArrayList<>(Arrays.asList(myWords));
//		myHash = all_words.toString().hashCode();

	}
		
	public boolean equals(Object other_obj)
	{
		if ( !(other_obj instanceof WordGram))
		{
			return false;
		}
		else 
		{
			WordGram input_word_gram = (WordGram)other_obj;
			if (this.myHash != input_word_gram.myHash)
			{
				return false;
			}
		}
		return true;
	}
	
	public int compareTo(WordGram input_word_gram)
	{	
		int compare_result = 0;
		for (int idx = 0; idx < Math.min(this.myWords.length, input_word_gram.myWords.length); idx += 1)
		{
			String word_1 = this.myWords[idx];
			String word_2 = input_word_gram.myWords[idx];
			compare_result = word_1.compareTo(word_2);
			if (compare_result != 0)
			{
				return compare_result;
			}
		}
		// if all words before are the same, check the length
		if (this.myWords.length > input_word_gram.myWords.length)
		{
			compare_result = 1;;
		}
		else if (this.myWords.length < input_word_gram.myWords.length)
		{
			compare_result = -1;
		}
		return compare_result;	
	}
	
	public WordGram shiftAdd(String new_word)
	{	
		int word_num = this.length();
		String[] input_source = new String[word_num+1];
		System.arraycopy(this.myWords, 0, input_source, 0, word_num);
		input_source[word_num] = new_word;
		WordGram shift_word_gram = new WordGram(input_source, 1, word_num);
		return shift_word_gram;
	}
	

	public String toString()
	{
		StringBuilder output_string = new StringBuilder();
		output_string.append("{");
		for (String each_word: myWords)
		{
//			output_string.append("\"");
			output_string.append(each_word);
//			output_string.append("\"");
			output_string.append(", ");
		}
		String final_output = output_string.toString().trim();
		final_output = final_output.substring(0, final_output.length()-1);
		final_output += "}";
		return final_output;
	}
	
	public int hashCode()
	{
		return this.myHash;
	}
	
	public int length()
	{
		return this.myWords.length;
	}
	
	public String[] get_word()
	{
		return this.myWords;
	}
}
