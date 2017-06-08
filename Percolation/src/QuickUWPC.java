
public class QuickUWPC implements IUnionFind{
	private int[] parent;
	private int[] size;
	private int count;
	
	public QuickUWPC()
	{
		parent = null;
		size = null;
		count = 0;
	}
	
	public QuickUWPC(int n) {
		initialize(n);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(int n) {
		count = n;
		parent = new int[n];
		size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
		// TODO Auto-generated method stub
		
	}

	@Override
	public int components() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public int find(int p) {
		// TODO Auto-generated method stub
		validate(p);
        while (p != parent[p])
        {
        	parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
	}
	
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (n-1));  
        }
    }
	
	@Override
	public boolean connected(int p, int q) {
		// TODO Auto-generated method stub
		return find(p) == find(q);
	}

	@Override
	public void union(int p, int q) {
		// TODO Auto-generated method stub
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        // make smaller root point to larger one
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
        }
        else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
        }
        count--;
	}

}
