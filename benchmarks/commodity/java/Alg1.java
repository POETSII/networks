package com.xrbpowered.compute.asp;

public class Alg1 {

	public static double asp(Graph g) {
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

}
