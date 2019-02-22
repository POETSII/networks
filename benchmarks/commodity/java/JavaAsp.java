package com.xrbpowered.compute.asp;

public class JavaAsp {

	public static boolean verbose = true;
	
	public static void test(int nnodes, int nedges, boolean alg1, boolean alg2) {
		Graph g = Graph.generateRandom(nnodes, nedges);
		
		long t0 = System.currentTimeMillis();
		Double res1 = alg1 ? Alg1.asp(g, verbose) : Double.valueOf(0);
		if(res1==null) {
			// graph is not valid, try another
			System.out.println("Discard");
			test(nnodes, nedges, alg1, alg2);
			return;
		}
		double t1 = (System.currentTimeMillis()-t0)/1000.0;
		
		t0 = System.currentTimeMillis();
		Double res2 = alg2 ? Alg2.asp(g, verbose) : Double.valueOf(0);
		if(res2==null) {
			// graph is not valid, try another
			System.out.println("Discard");
			test(nnodes, nedges, alg1, alg2);
			return;
		}
		double t2 = (System.currentTimeMillis()-t0)/1000.0;
		System.out.printf("%d, %d, %.2f, %.2f, %.1f, %.1f\n", nnodes, nedges, t1, t2, res1, res2);
	}
	
	public static void main(String[] args) {
		int k = 10;
		System.out.printf("nodes, edges, alg1time, alg2time, alg1res, alg2res\n");
		for(int nnodes = 1000; nnodes<=20000; nnodes+=1000) {
			int nedges = nnodes * k;
			test(nnodes, nedges, true, true);
		}
		// test(20000, 500000, true, false);
	}

}
