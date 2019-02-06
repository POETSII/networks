function plot_points

% points = csvread('output/points-2000-nodes.csv');

points = csvread('output/points-k.csv');

nnodes = points(:, 1);
nedges = points(:, 2);
asp_vals = points(:, 3:end);

qs = quantile(asp_vals, [0.25 0.5 0.75], 2);

plot(nnodes, qs);

xlabel('Nodes');
ylabel('Time (sec)');
title('ASP Performance');

grid on;

legend('0.25 quantile', 'mean', '0.75 quantile');

% xlim([0 5]);

end
