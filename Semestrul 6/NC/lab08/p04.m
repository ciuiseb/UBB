clear; clc; format long;
tol = 1e-7;

n = 1;
err_est = inf;
prev_approx = inf;

f_x = @(x) exp(0.25) .* (x - 0.5).^2 .* cos(x - 0.5);

while err_est >= tol
    [nodes, weights] = gauss_hermite(n);
    current_approx = sum(weights .* f_x(nodes));
    err_est = abs(current_approx - prev_approx);
    
    if err_est >= tol
        prev_approx = current_approx;
        n = n + 1;
    end
end

f_initial = @(t) exp(-t.^2 - t) .* t.^2 .* cos(t);
exact_val = integral(f_initial, -Inf, Inf, 'RelTol', 1e-12, 'AbsTol', 1e-12);

abs_err = abs(exact_val - current_approx);

fprintf('Aproximare:          %.15f\n', current_approx);
fprintf('Valoare exacta:   %.15f\n', exact_val);
fprintf('Eroare:              %.3e\n', abs_err);