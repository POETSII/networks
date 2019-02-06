from statistics import mean
from collections import defaultdict


def calculate_asp(graph):
    """Calculate the all-pair average shortest path of an undirected graph."""

    single_src_asps = [
        _calculate_asp_single_src(graph, src)
        for src in list(graph)
    ]

    return mean(single_src_asps)



def _calculate_asp_single_src(graph, src):
    """Calculate the average shortest path (for a single src node)."""

    current = {src}
    visited = set()

    sum_ = 0  # running sum of weighed distanced
    depth = 1  # current search depth

    while current:

        visited |= current
        destinations = set()

        for src in current:
            destinations |= graph[src]

        destinations -= visited

        # Accumulate weighed distances
        sum_ += len(destinations) * depth
        depth += 1

        current = destinations

    nodes = list(graph)
    npaths = len(visited) - 1
    return sum_ / npaths
