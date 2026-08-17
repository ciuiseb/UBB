clc; clear; close all;

a = -pi; 
b = pi;
m = 9;          
n = m + 1;
t = pi/5;       

f = @(x) x .* sin(x.^2);
df = @(x) sin(x.^2) + 2 .* x.^2 .* cos(x.^2);

k = 0:m;
x_nodes = ((b + a) / 2) + ((b - a) / 2) * cos((2*k + 1) * pi / (2 * n));
y_nodes = f(x_nodes);
dy_nodes = df(x_nodes);

xx = linspace(a, b, 1000);

yy_exact = f(xx);

yy_Lagrange = lagrange_interp(x_nodes, y_nodes, xx);
yy_Hermite  = hermite_interp(x_nodes, y_nodes, dy_nodes, xx);

figure('Name', 'Interpolare ', 'NumberTitle', 'off');
plot(xx, yy_exact, 'k-', 'LineWidth', 2); hold on;
plot(xx, yy_Lagrange, 'b--', 'LineWidth', 1.5);
plot(xx, yy_Hermite, 'r-.', 'LineWidth', 1.5);
plot(x_nodes, y_nodes, 'ko', 'MarkerSize', 6, 'MarkerFaceColor', 'g');
legend('f(x) exact', 'Polinom Lagrange', 'Polinom Hermite', 'Noduri Cebisev', 'Location', 'best');
title('Interpolarea functiei f(x) = x sin(x^2) pe [-\pi, \pi]');
xlabel('x'); ylabel('y');
grid on;

% subpunct b
val_exact = f(t);
val_Lagrange = lagrange_interp(x_nodes, y_nodes, t);
val_Hermite  = hermite_interp(x_nodes, y_nodes, dy_nodes, t);

fprintf('(b) Aproximarea in punctul t = pi/5\n');
fprintf('Valoarea exacta f(t)   = %.10f\n', val_exact);
fprintf('Aproximarea Lagrange   = %.10f\n', val_Lagrange);
fprintf('Aproximarea Hermite    = %.10f\n\n', val_Hermite);

% subpunct c
err_practic_L = abs(val_exact - val_Lagrange);
err_practic_H = abs(val_exact - val_Hermite);

max_L = 0;
max_H = 0;
x_search = linspace(a, b, 200);

for i = 1:length(x_search)
    int_L = integral(@(th) f(x_search(i) + exp(1i.*th)) .* exp(-1i*10.*th), 0, 2*pi, 'RelTol', eps, 'AbsTol', eps);
    max_L = max(max_L, abs(int_L / (2*pi)));
    
    int_H = integral(@(th) f(x_search(i) + exp(1i.*th)) .* exp(-1i*20.*th), 0, 2*pi, 'RelTol', eps, 'AbsTol', eps);
    max_H = max(max_H, abs(int_H / (2*pi)));
end

err_teoretic_L = max_L * abs(prod(t - x_nodes));
err_teoretic_H = max_H * prod((t - x_nodes).^2);

fprintf('(c) Analiza erorilor in punctul t = pi/5\n');
fprintf('Lagrange:\n');
fprintf('Eroare practica  = %e\n', err_practic_L);
fprintf('Eroare teoretica = %e\n', err_teoretic_L);
fprintf('Hermite:\n');
fprintf('Eroare practica  = %e\n', err_practic_H);
fprintf('Eroare teoretica = %e\n\n', err_teoretic_H);


function y_eval = lagrange_interp(x_nodes, y_nodes, x_eval)
    N = length(x_nodes);
    y_eval = zeros(size(x_eval));
    for i = 1:length(x_eval)
        S = 0;
        for j = 1:N
            P = 1;
            for k = 1:N
                if k ~= j
                    P = P * (x_eval(i) - x_nodes(k)) / (x_nodes(j) - x_nodes(k));
                end
            end
            S = S + P * y_nodes(j);
        end
        y_eval(i) = S;
    end
end

function y_eval = hermite_interp(x_nodes, y_nodes, dy_nodes, x_eval)
    N = length(x_nodes);
    z = zeros(1, 2*N);
    Q = zeros(2*N, 2*N);
    
    for i = 1:N
        z(2*i-1) = x_nodes(i);
        z(2*i)   = x_nodes(i);
        Q(2*i-1, 1) = y_nodes(i);
        Q(2*i, 1)   = y_nodes(i);
        Q(2*i, 2)   = dy_nodes(i); 
        if i ~= 1
            Q(2*i-1, 2) = (Q(2*i-1, 1) - Q(2*i-2, 1)) / (z(2*i-1) - z(2*i-2));
        end
    end
    for i = 3:2*N
        for j = 3:i
            Q(i, j) = (Q(i, j-1) - Q(i-1, j-1)) / (z(i) - z(i-j+1));
        end
    end
    y_eval = zeros(size(x_eval));
    for k = 1:length(x_eval)
        val = Q(1, 1);
        produs = 1;
        for i = 1:(2*N - 1)
            produs = produs * (x_eval(k) - z(i));
            val = val + Q(i+1, i+1) * produs;
        end
        y_eval(k) = val;
    end
end