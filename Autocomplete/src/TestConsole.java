import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
public class TestConsole {
	public static void main(String[] args)
	{
//		Term test_1 = new Term("beeswax", 1);
//		Term test_2 = new Term("beekeeper", 1);
//		ArrayList<Term> test = new ArrayList<>();
//		test.add(test_1);
//		test.add(test_2);
//		Collections.sort(test);
//		System.out.println(test);
		String[] test_terms = {"","auto","an","act","apple","ape","cd"};
		double[] test_weight = {11,3,3,2,7,9,1};
		TrieAutocomplete test_trie = new TrieAutocomplete(test_terms, test_weight);
//		System.out.println(test_trie.topMatch("c"));
		System.out.println(test_trie.topMatch(""));
//		BruteAutocomplete test_brute = new BruteAutocomplete(test_terms, test_weight);
//		System.out.println(test_brute.topMatches("",10));
	}

}
