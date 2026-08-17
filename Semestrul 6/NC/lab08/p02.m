clear; clc; format long;
tol = 1e-10;

n = 1;
err_est = inf;
prev_approx = inf;

while err_est >= tol
    [nodes, weights] = gauss_chebisev_1(n);
    f_x = cos(2 * nodes);
    current_approx = sum(weights .* f_x);
    err_est = abs(current_approx - prev_approx);
    if err_est >= tol
        prev_approx = current_approx;
        n = n + 1;
    end
end

exact_val = pi * besselj(0, 2);
abs_err = abs(exact_val - current_approx);

fprintf('Aproximare:          %.15f\n', current_approx);
fprintf('Valoare exacta):   %.15f\n', exact_val);
fprintf('Eroare:              %.3e\n', abs_err);