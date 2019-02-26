package com.xrbpowered.compute.asp;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class GraphTool {

	public static boolean verbose = false;
	public static boolean timing = false;
	
	public enum InCommand {
		graphml, random, reconstruct
	}
	
	public enum OutCommand {
		asp, asp2, distr, ddiff, save, dump
	}
	
	public static void help() {
		System.out.println("Usage:");
		System.out.println("java -jar gtool.jar <in> [<cmd>] [-vt]");
		System.out.println("\n<in> is one of:");
		System.out.println("-in <graphml>\n\tload graph from GraphML file");
		System.out.println("-rand <num_nodes> <num_edges>\n\tgenerate random graph");
		System.out.println("-recon <csv>\n\tre-synthesise graph to match degree distribution from CSV file");
		System.out.println("\n<cmd> is one of:");
		System.out.println("-dump\n\tprint entire graph");
		System.out.println("-out <graphml>\n\tsave graph as GraphML file");
		System.out.println("-dd\n\tprint degree distribution");
		System.out.println("-ddiff <csv>\n\tprint degree distribution difference against CSV file");
		System.out.println("-asp\n-asp1\n-asp2\n\tcalculate average shortest path");
		System.out.println("\nOther options:");
		System.out.println("-v\n-verbose\n\tdetailed information on progress");
		System.out.println("-t\n-timing\n\ttime each function");
	}
	
	public static void main(String[] args) {
		InCommand inCmd = null;
		String inPath = null;
		int nnodes = 0;
		int nedges = 0;
		HashMap<OutCommand, String> outCmd = new HashMap<>();
		
		for(int i=0; i<args.length; i++) {
			switch(args[i]) {
				case "-help":
					help();
					return;
				case "-in":
					inCmd = InCommand.graphml;
					inPath = args[++i];
					break;
				case "-rand":
					inCmd = InCommand.random;
					try {
						nnodes = Integer.parseInt(args[++i]);
						nedges = Integer.parseInt(args[++i]);
					}
					catch (NumberFormatException e) {
						System.err.println("Bad number");
						System.exit(1);
						return;
					}
					break;
				case "-recon":
					inCmd = InCommand.reconstruct;
					inPath = args[++i];
					break;
					
				case "-dump":
					outCmd.put(OutCommand.dump, null);
					break;
				case "-out":
					outCmd.put(OutCommand.save, args[++i]);
					break;
				case "-ddiff":
					outCmd.put(OutCommand.ddiff, args[++i]);
					break;
				case "-dd":
					outCmd.put(OutCommand.distr, null);
					break;
				case "-asp":
				case "-asp1":
					outCmd.put(OutCommand.asp, null);
					break;
				case "-asp2":
					outCmd.put(OutCommand.asp2, null);
					break;

				case "-v":
				case "-verbose":
					verbose = true;
					break;
				case "-t":
				case "-timing":
					timing = true;
					break;
					
				default:
					System.err.println("Unknown program argument");
					help();
					System.exit(1);
					return;
			}
		}
		if(inCmd==null) {
			System.err.println("No input");
			help();
			System.exit(1);
			return;
		}
		
		Graph g = null;
		switch(inCmd) {
			case graphml:
				g = Graph.loadGraphML(inPath);
				break;
			case reconstruct:
				DegreeDistr dd = DegreeDistr.loadCsv(inPath);
				g = dd.reconstruct(10);
				break;
			case random:
				g = Graph.generateRandom(nnodes, nedges);
				break;
		}
		System.out.printf("Nodes: %d\nEdges: %d\n", g.nnodes, g.nedges());
		
		for(Entry<OutCommand, String> e : outCmd.entrySet()) {
			long t0 = System.currentTimeMillis();
			switch(e.getKey()) {
				case dump:
					g.dump();
					break;
				case save:
					Graph.saveGraphML(e.getValue(), g);
					break;
				case distr:
					g.getDegreeDistr().print();
					break;
				case ddiff:
					DegreeDistr dd = DegreeDistr.loadCsv(e.getValue());
					g.getDegreeDistr().printDiff(dd);
					break;
				case asp: {
					Double res = Alg1.asp(g, verbose);
					if(res==null)
						System.out.println("Disconnected graph");
					else
						System.out.printf("ASP: %.4f\n", res);
					break;
				}
				case asp2: {
					Double res = Alg2.asp(g, verbose);
					if(res==null)
						System.out.println("Disconnected graph");
					else
						System.out.printf("ASP (Alg. 2): %.4f\n", res);
					break;
				}
			}
			if(timing) {
				System.out.printf("Elapsed: %d\n", System.currentTimeMillis()-t0);
			}
		}
	}

}
