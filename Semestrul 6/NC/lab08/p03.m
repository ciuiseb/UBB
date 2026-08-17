clear; clc; format long;
tol = 1e-10;
n = 1;
err_est = inf;
prev_approx = inf;

f_x = @(x) 0.25 * sin((x + 3) / 2);

while err_est >= tol
    [nodes, weights] = gauss_chebisev_2(n);
    current_approx = sum(weights .* f_x(nodes));
    err_est = abs(current_approx - prev_approx);
    if err_est >= tol
        prev_approx = current_approx;
        n = n + 1;
    end
end

f_initial = @(t) sqrt(3.*t - t.^2 - 2) .* sin(t);
exact_val = integral(f_initial, 1, 2, 'RelTol', 1e-14, 'AbsTol', 1e-14);

abs_err = abs(exact_val - current_approx);


fprintf('Aproximare:          %.15f\n', current_approx);
fprintf('Valoare exact:   %.15f\n', exact_val);
fprintf('Eroare:              %.3e\n', abs_err);