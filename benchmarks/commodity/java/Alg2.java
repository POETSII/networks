package com.xrbpowered.compute.asp;

import java.util.LinkedList;

public class Alg2 {

	private static class Token {
		public final int node;
		public final int len;
		public Token(int node, int len) {
			this.node = node;
			this.len = len;
		}
	}
	
	public static Double asp(Graph g, boolean verbose) {
		long len = 0;
		int nodes = 0;
		
		for(int n=0; n<g.nnodes; n++) {
			if(verbose && n%1000==0)
				System.out.println(n);
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
			if(n==0 && nodes!=g.nnodes-1)
				return null; // not fully connected
		}
		
		return len/(double)nodes;
	}

}
