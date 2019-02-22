package com.xrbpowered.compute.asp;

import java.util.Arrays;

public class Alg1 {

	public static Double asp(Graph g, boolean verbose) {
		long len = 0;
		int nodes = 0;
		
		int[] buf = new int[g.nnodes];
		boolean[] vis = new boolean[g.nnodes];
		
		for(int n=0; n<g.nnodes; n++) {
			if(verbose && n%1000==0)
				System.out.println(n);
			Arrays.fill(vis, false);
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
			if(n==0 && nodes!=g.nnodes-1)
				return null; // not fully connected
		}
		return len/(double)nodes;
	}

}
