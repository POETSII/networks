import java.util.LinkedList;

public class JavaAsp {

	public static double asp1(Graph g) {
		long len = 0;
		int nodes = 0;
		
		int[] buf = new int[g.nnodes];
		
		for(int n=0; n<g.nnodes; n++) {
			//if(n%1000==0)
			//	System.out.println(n);
			boolean[] vis = new boolean[g.nnodes];
			vis[n] = true;
			buf[0] = n;
			int cur = 0;
			int l = 0;
			int lend = 0;
			int end = 0;
			while(cur<=end) {
				if(l>0) {
					len += l;
					nodes++;
				}
				int nodeIndex = buf[cur];
				Graph.Node node = g.map[nodeIndex];
				for(int e=0; e<node.nedges; e++) {
					int dst = node.edges[e];
					if(!vis[dst]) {
						end++;
						buf[end] = dst;
						vis[dst] = true;
					}
				}
				cur++;
				if(cur>lend) {
					l++;
					lend = end;
				}
			}
		}
		
		return len/(double)nodes;
	}
	
	private static class Token {
		public final int node;
		public final int len;
		public Token(int node, int len) {
			this.node = node;
			this.len = len;
		}
	}
	
	public static double asp2(Graph g) {
		long len = 0;
		int nodes = 0;
		
		for(int n=0; n<g.nnodes; n++) {
			LinkedList<Token> tokens = new LinkedList<>();
			boolean[] vis = new boolean[g.nnodes];
			tokens.add(new Token(n, 0));
			vis[n] = true;
			while(!tokens.isEmpty()) {
				Token t = tokens.removeFirst();
				if(t.len>0) {
					len += t.len;
					nodes++;
				}
				Graph.Node node = g.map[t.node];
				for(int e=0; e<node.nedges; e++) {
					int dst = node.edges[e];
					if(!vis[dst]) {
						tokens.add(new Token(dst, t.len+1));
						vis[dst] = true;
					}
				}
			}
		}
		
		return len/(double)nodes;
	}
	
	public static void test(int nnodes, int nedges, boolean alg1, boolean alg2) {
		Graph g = Graph.create(nnodes, nedges);
		
		long t0 = System.currentTimeMillis();
		double res1 = alg1 ? asp1(g) : 0;
		double t1 = (System.currentTimeMillis()-t0)/1000.0;
		
		t0 = System.currentTimeMillis();
		double res2 = alg2 ? asp2(g) : 0;
		double t2 = (System.currentTimeMillis()-t0)/1000.0;
		System.out.printf("%d, %d, %.2f, %.2f, %.1f, %.1f\n", nnodes, nedges, t1, t2, res1, res2);
	}
	
	public static void main(String[] args) {
		int k = 2;
		System.out.printf("nodes, edges, alg1time, alg2time, alg1res, alg2res\n");
		for(int nnodes = 2000; nnodes<=20000; nnodes+=1000) {
			int nedges = nnodes * k;
			test(nnodes, nedges, true, true);
		}
		// test(20000, 500000, true, false);
	}

}
