import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.PriorityQueue;


/**
 * General trie/priority queue algorithm for implementing Autocompletor
 * 
 * @author Austin Lu
 * @author Jeff Forbes
 */
public class TrieAutocomplete implements Autocompletor {

	/**
	 * Root of entire trie
	 */
	protected Node myRoot;

	/**
	 * Constructor method for TrieAutocomplete. Should initialize the trie
	 * rooted at myRoot, as well as add all nodes necessary to represent the
	 * words in terms.
	 * 
	 * @param terms
	 *            - The words we will autocomplete from
	 * @param weights
	 *            - Their weights, such that terms[i] has weight weights[i].
	 * @throws NullPointerException
	 *             if either argument is null
	 * @throws IllegalArgumentException
	 *             if terms and weights are different weight
	 */
	public TrieAutocomplete(String[] terms, double[] weights) {
		if (terms == null || weights == null)
			throw new NullPointerException("One or more arguments null");
		// Represent the root as a dummy/placeholder node
		myRoot = new Node('-', null, 0);

		for (int i = 0; i < terms.length; i++) {
			add(terms[i], weights[i]);
		}
	}

	/**
	 * Add the word with given weight to the trie. If word already exists in the
	 * trie, no new nodes should be created, but the weight of word should be
	 * updated.
	 * 
	 * In adding a word, this method should do the following: Create any
	 * necessary intermediate nodes if they do not exist. Update the
	 * subtreeMaxWeight of all nodes in the path from root to the node
	 * representing word. Set the value of myWord, myWeight, isWord, and
	 * mySubtreeMaxWeight of the node corresponding to the added word to the
	 * correct values
	 * 
	 * @throws a
	 *             NullPointerException if word is null
	 * @throws an
	 *             IllegalArgumentException if weight is negative.
	 */
	public void add(String word, double weight) {
		// TODO: Implement add
		if(word == null) throw new NullPointerException("Word is null!");
		if(weight < 0) throw new IllegalArgumentException("Weight negative!");
		Node iter_node = myRoot;
		iter_node.mySubtreeMaxWeight = Math.max(iter_node.mySubtreeMaxWeight, weight);
		for(int idx = 0; idx < word.length(); idx +=1)
		{	
			char current_char = word.charAt(idx);
			if(!iter_node.children.containsKey(current_char))
			{	
				Node add_node = new Node(current_char, iter_node, weight);
				iter_node.children.put(current_char, add_node);
				iter_node = add_node;
			}
			else
			{
				iter_node = iter_node.getChild(current_char);
				double max_weight = Math.max(iter_node.mySubtreeMaxWeight, weight);
				iter_node.mySubtreeMaxWeight = max_weight;
			}			
			if(idx == word.length()-1)
			{
				iter_node.setWeight(weight);
				iter_node.setWord(word);
				iter_node.isWord = true;
			}
		}
	}

	/**
	 * Required by the Autocompletor interface. Returns an array containing the
	 * k words in the trie with the largest weight which match the given prefix,
	 * in descending weight order. If less than k words exist matching the given
	 * prefix (including if no words exist), then the array instead contains all
	 * those words. e.g. If terms is {air:3, bat:2, bell:4, boy:1}, then
	 * topKMatches("b", 2) should return {"bell", "bat"}, but topKMatches("a",
	 * 2) should return {"air"}
	 * 
	 * @param prefix
	 *            - A prefix which all returned words must start with
	 * @param k
	 *            - The (maximum) number of words to be returned
	 * @return An Iterable of the k words with the largest weights among all
	 *         words starting with prefix, in descending weight order. If less
	 *         than k such words exist, return all those words. If no such words
	 *         exist, return an empty Iterable
	 * @throws a
	 *             NullPointerException if prefix is null
	 */
	public Iterable<String> topMatches(String prefix, int k) {
		// TODO: Implement topKMatches
		ArrayList<String> out = new ArrayList<>();
		Node iter_node = myRoot;
		for(int idx = 0; idx < prefix.length(); idx +=1)
		{
			char current_char = prefix.charAt(idx);
			if(iter_node.children.containsKey(current_char))
				iter_node = iter_node.children.get(current_char);
			else 
				return out;
		}
		// after the prefix do poping
		PriorityQueue<Node> iter_queue = new PriorityQueue<>(new Node.ReverseSubtreeMaxWeightComparator());
		PriorityQueue<Node> out_queue = new PriorityQueue<>((n1,n2) -> (int)Math.signum(n1.myWeight - n2.myWeight)); 
		if(iter_node.isWord) out_queue.add(iter_node);
		for(char each: iter_node.children.keySet())	iter_queue.add(iter_node.children.get(each));
		while(iter_queue.size() != 0 
				&& out_queue.size() < k
				|| ifFirstSmall(out_queue.peek(), iter_queue.peek()))
		{
			Node head_node = iter_queue.poll();
			if(head_node.isWord) out_queue.add(head_node);
			for(char each: head_node.children.keySet()) iter_queue.add(head_node.children.get(each));
		}
		ArrayList<Node> out_list = new ArrayList<>(out_queue);
		Collections.sort(out_list, (n1,n2) -> (int)Math.signum(n2.myWeight - n1.myWeight));
		if(out_list.size() > k)
			for(int idx = 0; idx < k; idx +=1)
				out.add(out_list.get(idx).myWord);
		else
			for(Node each: out_list)
				out.add(each.myWord);
		return out;
	}
		
	
	private boolean ifFirstSmall(Node n1, Node n2)
	{	
		if(n1 == null || n2 == null) return false;
		else return n1.myWeight <= n2.mySubtreeMaxWeight;
	}
	
	/**
	 * Given a prefix, returns the largest-weight word in the trie starting with
	 * that prefix.
	 * 
	 * @param prefix
	 *            - the prefix the returned word should start with
	 * @return The word from with the largest weight starting with prefix, or an
	 *         empty string if none exists
	 * @throws a
	 *             NullPointerException if the prefix is null
	 */
	public String topMatch(String prefix) {
		// TODO: Implement topMatch
		if(prefix == null) throw new NullPointerException("No prefix");
		Node iter_node = myRoot;
		for(int idx = 0; idx < prefix.length(); idx +=1)
		{
			char current_char = prefix.charAt(idx);
			if(iter_node.children.containsKey(current_char))
				iter_node = iter_node.children.get(current_char);
			else 
				return "";
		}
		while(iter_node.mySubtreeMaxWeight != iter_node.myWeight)
		{
			iter_node = findMaxNode(iter_node);
			if(iter_node == null) return "";
		}
		return iter_node.myWord;
	}
	
	private Node findMaxNode(Node start_node)
	{	
		double max_weight = start_node.mySubtreeMaxWeight;
		Map<Character, Node> in_map = start_node.children;
		for(char each: in_map.keySet())
		{
			Node each_node = in_map.get(each);
			if(each_node.mySubtreeMaxWeight == max_weight) return each_node;	
		}
		return null;
	}
	
	
	/**
	 * Return the weight of a given term. If term is not in the dictionary,
	 * return 0.0
	 */
	public double weightOf(String term) {
		// TODO complete weightOf
		Node iter_node = myRoot;
		for(int idx = 0; idx < term.length(); idx +=1)
		{
			iter_node = iter_node.children.get(term.charAt(idx));
			if(iter_node == null) return 0.0;
		}
		return iter_node.myWeight;
	}

	/**
	 * Optional: Returns the highest weighted matches within k edit distance of
	 * the word. If the word is in the dictionary, then return an empty list.
	 * 
	 * @param word
	 *            The word to spell-check
	 * @param dist
	 *            Maximum edit distance to search
	 * @param k
	 *            Number of results to return
	 * @return Iterable in descending weight order of the matches
	 */
	public Iterable<String> spellCheck(String word, int dist, int k) {
		return null;
	}
		
}
