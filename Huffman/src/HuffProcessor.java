import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
/**
 *	Interface that all compression suites must implement. That is they must be
 *	able to compress a file and also reverse/decompress that process.
 * 
 *	@author Brian Lavallee
 *	@since 5 November 2015
 *  @author Owen Atrachan
 *  @since December 1, 2016
 */
public class HuffProcessor {

	public static final int BITS_PER_WORD = 8;
	public static final int BITS_PER_INT = 32;
	public static final int ALPH_SIZE = (1 << BITS_PER_WORD); // or 256
	public static final int PSEUDO_EOF = ALPH_SIZE;
	public static final int HUFF_NUMBER = 0xface8200;
	public static final int HUFF_TREE  = HUFF_NUMBER | 1;
	public static final int HUFF_COUNTS = HUFF_NUMBER | 2;

	public enum Header{TREE_HEADER, COUNT_HEADER};
	public Header myHeader = Header.TREE_HEADER;
	
	/**
	 * Compresses a file. Process must be reversible and loss-less.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be compressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void compress(BitInputStream in, BitOutputStream out){

	    int[] counts = readForCounts(in);
	    HuffNode root = makeTreeFromCounts(counts);
	    //printTree(root, 0);
	    String[] codings = makeCodingsFromTree(root);
	    writeHeader(root, out);	 
	    in.reset();
	    writeCompressedBits(in, codings, out);
	}
	
	// #test method for Compress
	/**
	 * print tree
	 */
	private void printTree(HuffNode root, int level) {
		if(level == 0) {
			System.out.println("level " + level + ":");
		}
		System.out.print(root.value());
		if (root.left() != null && root.right() != null) {
			level++;
			System.out.println("");
			System.out.println("level " + level + ":");
			System.out.println("");
			printTree(root.left(), level);
			System.out.println("");
			printTree(root.right(), level);
		}
		return;
		
	}
	
	
	// #1 Helper Methods for Compress
	/**
	 * count the frequency of each char
	 */
	private int[] readForCounts(BitInputStream in)
	{
		int[] out = new int[256];
		//int count = 0;
		while(true)
		{
			int bits = in.readBits(BITS_PER_WORD);
			if(bits == -1) break;
			//if(out[bits] == 0) count++;
			out[bits]++;
		}
		//System.out.println("size = " + count);
//		int c=0;
//		for (int i =0; i < 256; i++) {
//			if (out[i]!=0) {
//				System.out.println(i + " " + out[i]);
//				c++;
//			}
//		}
//		System.out.println("Nonzero numbers = " + c);
		return out;
	}
	
	// #2 Helper Methods for Compress
	/**
	 * create Huff tree for compress
	 * @param in_counter
	 *           an int[256] that contains the counts for every char
	 */
	private HuffNode makeTreeFromCounts(int[] in_counter)
	{
		//min PQ by node weight
		PriorityQueue<HuffNode> pq = new PriorityQueue<HuffNode>((node1, node2) -> node1.weight() - node2.weight());
		for(int cur_value = 0; cur_value < in_counter.length; cur_value += 1) 
		{
			int cur_weight = in_counter[cur_value];
			if(cur_weight > 0) pq.add(new HuffNode(cur_value, cur_weight));
		}
		pq.add(new HuffNode(PSEUDO_EOF, 1));
		while(pq.size() > 1)
		{
			HuffNode left = pq.poll();
			HuffNode right = pq.poll();
			HuffNode sup_node = new HuffNode(-1, left.weight() + right.weight(), left, right);
			System.out.println(sup_node.weight());
			pq.add(sup_node);
		}
		return pq.poll();
	}
	
	// #3 Helper Methods for Compress
		/**
		 * create coding dictionary from Huff tree
		 * @param root
		 *           root of HuffTree
		 */
	private String[] makeCodingsFromTree(HuffNode root) {
		String[] out = new String[257];
		inspectTree("", root, out);
		return out;
	}
	
	// #3.1 Helper Methods for Compress
	// Helper Methods for makeCodingsFromTree
		/**
		 * help create coding dictionary from Huff tree
		 * @param prrfix record the path
		 * @param root root of HuffTree
		 * @param ans String[257]
		 * 
		 */
	private void inspectTree(String prefix, HuffNode root, String[] ans) {
		if (root.value() != -1) {
			ans[root.value()] = prefix;
			return;
		}
		inspectTree(prefix + "0", root.left(), ans);
		inspectTree(prefix + "1", root.right(), ans);
		return;
	}
	
	// #4 Helper Methods for Compress
		/**
		 * help create coding dictionary from Huff tree
		 * @param root root of HuffTree
		 * @param out for writing
		 */
	private void writeHeader(HuffNode root, BitOutputStream out) {
		out.writeBits(BITS_PER_INT, HUFF_TREE);
		writeTree(root, out);
		return;
	}
	
	private void writeTree(HuffNode root, BitOutputStream out) {
		// isWord
		if (root.value() != -1) {
			out.writeBits(1, 1);
			out.writeBits(9, root.value());
			return;
		}
		out.writeBits(1, 0);
		writeTree(root.left(), out);
		writeTree(root.right(), out);
		return;
	}
		
	
	// #5 Helper Methods for Compress
		/**
		 * help create coding dictionary from Huff tree
		 * @param in input file
		 * @param map dictionary
		 * @param out output compressed file 
		 */
	private void writeCompressedBits(BitInputStream in, String[] map, BitOutputStream out) {
		int bits = 1;
		while(bits >= 0) { //while in is not the end
			bits = in.readBits(8);
			if (bits == -1) {
				writeStringToBits(map, out, 256);
				return;
			}
			writeStringToBits(map, out, bits);
			
		}
		
	}
	
	// #5.1 Helper Methods for Compress
	// Helper Methods for writeCompressedBits
	private void writeStringToBits(String[] map, BitOutputStream out, int bits) {
		String code = map[bits];
		for (int i = 0; i < code.length(); i++) {
			String t = code.substring(i, i+1);
			int temp = Integer.parseInt(t);
			out.writeBits(1, temp);
		}
		return;
	}
		
		
	/**
	 * Decompresses a file. Output file must be identical bit-by-bit to the
	 * original.
	 *
	 * @param in
	 *            Buffered bit stream of the file to be decompressed.
	 * @param out
	 *            Buffered bit stream writing to the output file.
	 */
	public void decompress(BitInputStream in, BitOutputStream out){
		int bits = in.readBits(BITS_PER_INT);
		if(bits != HUFF_TREE && bits != HUFF_NUMBER) throw new HuffException("Bad");
		HuffNode root = build(in);
		HuffNode iter_node = root;
		while(iter_node.value() != PSEUDO_EOF)
		{
			iter_node = read(in.readBits(1), iter_node);
			if(iter_node.left() == null && iter_node.right() == null
					&& iter_node.value() != PSEUDO_EOF)
			{
				out.writeBits(BITS_PER_WORD, iter_node.value());
				iter_node = root;
			}
		}
	}
	
	private HuffNode build(BitInputStream bis)
	{
	      int bits = bis.readBits(1);
	      if (bits == 0) {
	          HuffNode root = new HuffNode(0,0,null,null);
	          root.setLeft(build(bis));
	          root.setRight(build(bis));
	          return root;
	      }
	      else {
	          bits = bis.readBits(9);
	          HuffNode root = new HuffNode(bits,0,null,null);
	          return root;
	      }
	}
	
	private HuffNode read(int nextbit, HuffNode trieTree)
	{
		if(nextbit != 0 && nextbit != 1) 
			throw new IllegalArgumentException();
		if(nextbit == 1) return trieTree.right();
		else return trieTree.left();
	}
	
	public void setHeader(Header header) {
        myHeader = header;
        //System.out.println("header set to "+myHeader);
    }
}