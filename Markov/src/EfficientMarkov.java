
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class EfficientMarkov implements MarkovInterface<String> {
	private String myText;
	private Random myRandom;
	private int myOrder;
	private HashMap<String, ArrayList<String>> word_map = new HashMap<>();


	private static String PSEUDO_EOS = "";
	private static long RANDOM_SEED = 0;

	public EfficientMarkov(int order) {
		myRandom = new Random(RANDOM_SEED);
		myOrder = order;
	}

	public EfficientMarkov() {
		this(3);
	}

	public void setTraining(String text) {
		myText = text;
	}
	public int size() {
		return myText.length();
	}

	public String getRandomText(int length) {
		StringBuilder sb = new StringBuilder();
		if ((myText.length() - myOrder) <= 0)
		{
			return myText;
		}

		int index = myRandom.nextInt(myText.length() - myOrder);
		String current = myText.substring(index, index + myOrder);
		//System.out.printf("first random %d for '%s'\n",index,current);
		sb.append(current);
		for(int k=0; k < length-myOrder; k++)
		{	
			ArrayList<String> follows = new ArrayList<>();;
			if (word_map.containsKey(current))
			{
				follows = word_map.get(current);
			}
			else
			{
				follows = getFollows(current);
				word_map.put(current, follows);
			}
			if (follows.size() == 0){
				break;
			}
			index = myRandom.nextInt(follows.size());

			String nextItem = follows.get(index);
			if (nextItem.equals(PSEUDO_EOS)) {
				break;
			}
			sb.append(nextItem);
			current = current.substring(1)+ nextItem;

		}
		return sb.toString();

	}

	public ArrayList<String> getFollows(String key){
		ArrayList<String> follows = new ArrayList<String>();

		int pos = 0;  // location where search for key in text starts

		while (pos < myText.length()){
			int start = myText.indexOf(key,pos);
			if (start == -1){
				break;
			}
			if (start + key.length() >= myText.length()){
				follows.add(PSEUDO_EOS);
				break;
			}
			// next line is string equivalent of myText.charAt(start+key.length())
			String next = myText.substring(start+key.length(), start+key.length()+1);
			follows.add(next);
			pos = start+1;  // search continues after this occurrence
		}
		return follows;
	}

	public int getOrder() {

		return myOrder;
	}

	public int getSize()
	{
		return myText.length();
	}

	public void setSeed(long seed) {
		RANDOM_SEED = seed;
		myRandom = new Random(RANDOM_SEED);	
	}

}
