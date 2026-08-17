clear; clc; format long;

x_test = 2;
y_test = 5;

[aprox, exact, eroare] = rutina_gauss_laguerre(x_test, y_test);

function [approx_val, exact_val, abs_err] = rutina_gauss_laguerre(x, y)
    n = 8;
    [nodes, weights] = gauss_laguerre(n);
   
    % u = x*t  =>  f(u) = 1 / (x*y + u)
    f_u = @(u) 1 ./ (x .* y + u);
    approx_val = sum(weights .* f_u(nodes));
    f_initial = @(t) exp(-x .* t) ./ (y + t);
    exact_val = integral(f_initial, 0, Inf, 'RelTol', 1e-13, 'AbsTol', 1e-13);
   
    abs_err = abs(exact_val - approx_val);
   
    fprintf('Aproximare: %.15f\n', approx_val);
    fprintf('Valoare exacta:   %.15f\n', exact_val);
    fprintf('Eroare:     %.3e\n\n', abs_err);
end