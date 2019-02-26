package com.xrbpowered.compute.asp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.InvalidParameterException;
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
		
		public boolean isConnectedTo(int dst) {
			for(int i=0; i<nedges; i++) {
				if(edges[i]==dst)
					return true;
			}
			return false;
		}
		
		public boolean add(int dst) {
			for(int i=0; i<nedges; i++) {
				if(edges[i]==dst)
					return false;
			}
			edges[nedges++] = dst;
			return true;
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

	public int nedges() {
		int sum = 0;
		for(int i=0; i<nnodes; i++)
			sum += map[i].nedges;
		return sum;
	}
	
	public void dump() {
		for(int i=0; i<nnodes; i++)
			for(int j=0; j<map[i].nedges; j++)
				System.out.printf("%d -> %d\n", i, map[i].edges[j]);
	}
	
	public DegreeDistr getDegreeDistr() {
		DegreeDistr dd = new DegreeDistr();
		for(int i=0; i<nnodes; i++)
			dd.addTo(map[i].nedges);
		return dd;
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

	public static void saveGraphML(String path, Graph g) {
		try {
			PrintStream out = new PrintStream(new File(path));
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" "
					+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
			out.println("<graph id=\"G\" edgedefault=\"undirected\">");
			
			for(int i=0; i<g.nnodes; i++)
				out.printf("\t<node id=\"%06d\"/>\n", i);
			for(int i=0; i<g.nnodes; i++) {
				Node n = g.map[i];
				for(int j=0; j<n.nedges; j++)
					out.printf("\t<edge source=\"%06d\" target=\"%06d\"/>\n", i, n.edges[j]);
			}
			
			out.println("</graph>\n</graphml>");
			
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		DegreeDistr dd = DegreeDistr.loadCsv("dists/n4.csv");
		long t0 = System.currentTimeMillis();
		Graph g = dd.reconstruct(10);
		System.out.printf("Elapsed: %d\n", System.currentTimeMillis()-t0);
		g.getDegreeDistr().printDiff(dd);
		System.out.println("ASP = "+Alg1.asp(g, false));
		//saveGraphML("dists/n4_recon.graphml", g);

		System.out.println("\n\nOriginal:");
		Graph go = loadGraphML("dists/n4.graphml");
		System.out.println("ASP = "+Alg1.asp(go, false));
	}
	
}
