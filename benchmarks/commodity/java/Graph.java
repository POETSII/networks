import java.security.InvalidParameterException;
import java.util.Random;

public class Graph {

	public static class Node {
		public int nedges = 0;
		public final int[] edges;
		
		public Node(int nmax) {
			this.edges = new int[nmax];
		}
		
		public void add(int dst) {
			for(int i=0; i<nedges; i++) {
				if(edges[i]==dst)
					return;
			}
			edges[nedges++] = dst;
		}
	}
	
	public final int nnodes;
	public final Node[] map;
	
	public Graph(int nnodes) {
		this.nnodes = nnodes;
		this.map = new Node[nnodes];
		for(int i=0; i<nnodes; i++)
			this.map[i] = new Node(nnodes);
	}

	public void dump() {
		for(int i=0; i<nnodes; i++)
			for(int j=0; j<map[i].nedges; j++)
				System.out.printf("%d -> %d\n", i, map[i].edges[j]);
	}
	
	public static Graph create(int nnodes, int nedges) {
		if(nedges > nnodes * (nnodes-1) / 2)
			throw new InvalidParameterException();
		
		Random random = new Random();
		Graph g = new Graph(nnodes);
		for(int i=0; i<nedges; i++) {
			int src, dst;
			do {
				src = random.nextInt(nnodes);
				dst = random.nextInt(nnodes);
			} while(src==dst);
			g.map[src].add(dst);
			g.map[dst].add(src);
		}
		
		// g.dump();
		
		return g;
	}
	
}
