clear; clc; format long;

A = 0; 
B = -0.5;
g_c_jac = @(u) (sqrt(2)/2) * cos((u + 1)/2);
g_s_jac = @(u) (sqrt(2)/2) * sin((u + 1)/2);

tol = 1e-10;
n = 1; 
err_c = inf; err_s = inf;
prev_Ic = inf; prev_Is = inf;

while (err_c >= tol) || (err_s >= tol)
    [nodes, weights] = gauss_jacobi(n, A, B);
    
    curr_Ic = sum(weights .* g_c_jac(nodes));
    curr_Is = sum(weights .* g_s_jac(nodes));
    
    err_c = abs(curr_Ic - prev_Ic);
    err_s = abs(curr_Is - prev_Is);
    
    if (err_c >= tol) || (err_s >= tol)
        prev_Ic = curr_Ic; prev_Is = curr_Is;
        n = n + 1;
    end
end
fprintf('Ic = %.15f\n', curr_Ic);
fprintf('Is = %.15f\n', curr_Is);