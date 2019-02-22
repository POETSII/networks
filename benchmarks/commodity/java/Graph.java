package com.xrbpowered.compute.asp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	
	public void printDegreeDistr() {
		ArrayList<Integer> list = new ArrayList<>();
		for(int i=0; i<nnodes; i++) {
			int degree = map[i].nedges;
			while(list.size()<degree+1)
				list.add(0);
			list.set(degree, list.get(degree)+1);
		}
		for(int i=0; i<list.size(); i++)
			System.out.printf("%d, %d\n", i, list.get(i));
	}
	
	public static Graph generateRandom(int nnodes, int nedges) {
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
		
		return g;
	}
	
	public static Graph loadGraphML(String path) {
		try {
			InputStream in = new FileInputStream(new File(path));
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			in.close();
			Element root = (Element)doc.getDocumentElement().getElementsByTagName("graph").item(0);
			
			HashMap<String, Integer> nodeMap = new HashMap<>();
			org.w3c.dom.Node n = root.getFirstChild();
			int nnodes = 0;
			while(n!=null) {
				if(n.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE) {
					Element e = (Element) n;
					if(e.getNodeName().equals("node")) {
						nodeMap.put(e.getAttribute("id"), nnodes);
						nnodes++;
					}
				}
				n = n.getNextSibling();
			}
			
			Graph g = new Graph(nnodes);
			
			n = root.getFirstChild();
			while(n!=null) {
				if(n.getNodeType()==org.w3c.dom.Node.ELEMENT_NODE) {
					Element e = (Element) n;
					if(e.getNodeName().equals("edge")) {
						int src = nodeMap.get(e.getAttribute("source"));
						int dst = nodeMap.get(e.getAttribute("target"));
						g.map[src].add(dst);
						g.map[dst].add(src); // bug with directed graphs?
					}
				}
				n = n.getNextSibling();
			}
			
			return g;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		Graph g = loadGraphML("D:/Workspace/work/poets/dists/n4.graphml");
		//g.dump();
		g.printDegreeDistr();
	}
	
}
