clear; clc; format long;

f_c = @(x) cos(x) ./ sqrt(x);
f_s = @(x) sin(x) ./ sqrt(x);
tol = 1e-10;

Ic_adapt = integral(f_c, 0, 1, 'RelTol', tol, 'AbsTol', tol);
Is_adapt = integral(f_s, 0, 1, 'RelTol', tol, 'AbsTol', tol);

fprintf('Ic = %.15f\n', Ic_adapt);
fprintf('Is = %.15f\n', Is_adapt);