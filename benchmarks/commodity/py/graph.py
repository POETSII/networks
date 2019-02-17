from random import sample
from collections import defaultdict


def create_graph(nnodes, nedges):
    """Create an undirected graph with given node and edge counts.

    A graph is here represented as a map: src node -> {dst node}.
    """

    # Verify that nedges is below maximum
    max_nedges = nnodes * (nnodes-1) / 2
    assert nedges <= max_nedges, "max nedges = %d" % max_nedges

    # Declare node and edge sets
    nodes = range(nnodes)
    edge_set = set()

    # Populate edge set
    while len(edge_set) < nedges:
        edge = tuple(sorted(sample(nodes, 2)))
        edge_set.add(edge)

    # Compute graph from edge set
    graph = defaultdict(set)
    for src, dst in edge_set:
        graph[src].add(dst)
        graph[dst].add(src)

    return graph
