package com.xrbpowered.compute.asp;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DegreeDistr {

	private ArrayList<Integer> list = new ArrayList<>();
	
	public void print() {
		for(int i=0; i<list.size(); i++)
			System.out.printf("%d, %d\n", i, list.get(i));
	}
	
	public void printDiff(DegreeDistr dd) {
		for(int i=0; i<list.size(); i++) {
			if(!list.get(i).equals(dd.list.get(i)))
				System.out.printf("%d: %d != %d\n", i, list.get(i), dd.list.get(i));
		}
	}
	
	private void pad(int degree) {
		while(list.size()<degree+1)
			list.add(0);
	}
	
	public void addTo(int degree) {
		pad(degree);
		list.set(degree, list.get(degree)+1);
	}

	public void set(int degree, int count) {
		pad(degree);
		list.set(degree, count);
	}

	private static class StubNode {
		public int degree;
		public int remDegree;
		public int nodeIndex;
		public boolean inAcc = false;
		
		public StubNode(int index, int degree) {
			this.nodeIndex = index;
			this.degree = degree;
			this.remDegree = degree;
		}
		
		public boolean updateRemDegree(Graph g) {
			remDegree = degree-g.map[nodeIndex].nedges;
			return remDegree<=0;
		}
	}
	
	private StubNode degreeWeightedRandom(List<StubNode> pool, Random random) {
		int max = 0;
		for(StubNode s : pool)
			max += s.remDegree;
		int x = random.nextInt(max);
		for(StubNode s : pool) {
			if(x < s.remDegree)
				return s;
			x -= s.remDegree;
		}
		return null;
	}
	
	public Graph reconstruct(int maxRetries) {
		LinkedList<StubNode> pool = new LinkedList<>();
		for(int degree=1; degree<list.size(); degree++) {
			int count = list.get(degree);
			for(int i=0; i<count; i++) {
				pool.add(new StubNode(pool.size(), degree));
			}
		}
		Graph g = new Graph(pool.size());

		Random random = new Random();

		LinkedList<StubNode> acc = new LinkedList<>();
		StubNode start = degreeWeightedRandom(pool, random);
		acc.add(start);
		start.inAcc = true;
		
		int retries = 0;
		while(!pool.isEmpty() && !acc.isEmpty() && retries<=maxRetries*pool.size()) {
			retries++;
			StubNode src = degreeWeightedRandom(acc, random);
			StubNode dst = pool.get(random.nextInt(pool.size()));
			if(src==dst)
				continue;
			if(src.remDegree<2 && dst.remDegree<2 && acc.size()<2)
				continue;
			
			g.map[src.nodeIndex].add(dst.nodeIndex);
			g.map[dst.nodeIndex].add(src.nodeIndex);
			
			if(src.updateRemDegree(g)) {
				pool.remove(src);
				acc.remove(src);
			}
			if(dst.updateRemDegree(g)) {
				pool.remove(dst);
				acc.remove(dst);
			}
			else if(!dst.inAcc) {
				acc.add(dst);
				dst.inAcc = true;
			}
		}
		
		if(!pool.isEmpty() || !acc.isEmpty())
			System.out.printf("Imperfect reconstruction! (acc: %d, pool: %+d)\n", acc.size(), pool.size()-acc.size());
		
		return g;
	}
	
	public static DegreeDistr loadCsv(String path) {
		try {
			DegreeDistr dd = new DegreeDistr();
			Scanner in = new Scanner(new File(path));
			while(in.hasNextLine()) {
				String[] s = in.nextLine().trim().split("\\s*,\\s*", 2);
				int degree = Integer.parseInt(s[0]);
				int count = Integer.parseInt(s[1]);
				dd.set(degree, count);
			}
			in.close();
			return dd;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
