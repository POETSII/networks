#!/usr/bin/env python3

from asp import calculate_asp
from time import time
from graph import create_graph
from files import write_csv


def time_it(func):
    """Create wrapper that returns function execution time."""
    def inner(*args, **kwargs):
        start = time()
        result = func(*args, **kwargs)
        end = time()
        duration = end - start
        return result, duration
    return inner


def main():
    output_file = "../data/python.csv"
    nnodes = 4000
    nrepeats = 1
    k = 2
    nedges = int(nnodes * (nnodes-1) / 2)
    points = []
    calculate_asp_timed = time_it(calculate_asp)
    for nnodes in range(1000, 11000, 1000):
        nedges = nnodes * k
        measurements = []
        for _ in range(nrepeats):
            graph = create_graph(nnodes, nedges)
            asp, duration = calculate_asp_timed(graph)
            measurements.append(duration)
        point = (nnodes, nedges, *measurements)
        # print(point)
        points.append(point)
    write_csv(output_file, points)


if __name__ == '__main__':
    main()
