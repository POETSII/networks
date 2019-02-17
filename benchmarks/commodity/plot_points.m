function plot_points

points_py = csvread('output/points-k.csv');
points_java_algo1 = csvread('data/java-algo1.csv');
points_java_algo2 = csvread('data/java-algo2.csv');

clf; hold on;

plot_set(points_py, 'r');
plot_set(points_java_algo1, 'b');
plot_set(points_java_algo2, [0 0.5 0]);

xlabel('Nodes');
ylabel('Time (sec)');
title('ASP Performance (Edges = 2 x Nodes)');

grid on;

legend('Python', 'Java (Algo 1)', 'Java (Algo 2)');

box on;

set(gca, 'yscale', 'log');

end

function plot_set(points, col)

nnodes = points(:, 1);
% nedges = points(:, 2);
time = points(:, 3:end);


plot(nnodes, time, '-o', 'color', col);

end