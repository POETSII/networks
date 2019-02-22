function plot_points

makePDF = 1;

points_py = csvread('data/python.csv');
points_java_algo1 = csvread('data/java-algo1.csv');
points_java_algo2 = csvread('data/java-algo2.csv');
points_c = csvread('data/c.csv');

clf; hold on;


plot_set(points_c, 'k');
plot_set(points_java_algo1, 'b');
plot_set(points_java_algo2, [0 0.5 0]);
plot_set(points_py, 'r');

xlabel('Nodes');
ylabel('Time (sec)');
title('ASP Performance (Edges = 10 x Nodes)');

grid on;

legs = {'C', 'Java (Algo 1)', 'Java (Algo 2)', 'Python'};


legend(legs, 'Location', 'NorthWest');

box on;

set(gca, 'yscale', 'log');

ylim([1e-2 1e4]);

if makePDF == 1

    makeLines1pt();

	ppdf2('results/plot_points.pdf', [12 8]);

end

end

function plot_set(points, col)

nnodes = points(:, 1);
% nedges = points(:, 2);
time = points(:, 3:end);


plot(nnodes, time, '-o', 'color', col);

end