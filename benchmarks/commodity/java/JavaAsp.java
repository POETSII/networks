package com.xrbpowered.compute.asp;

public class JavaAsp {

	public static void test(int nnodes, int nedges, boolean alg1, boolean alg2) {
		Graph g = Graph.create(nnodes, nedges);
		
		long t0 = System.currentTimeMillis();
		double res1 = alg1 ? Alg1.asp(g) : 0;
		double t1 = (System.currentTimeMillis()-t0)/1000.0;
		
		t0 = System.currentTimeMillis();
		double res2 = alg2 ? Alg2.asp(g) : 0;
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
