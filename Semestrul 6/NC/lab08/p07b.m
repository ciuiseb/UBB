clear; clc; format long;

f_c_leg = @(z) cos(((z + 1)/2).^2);
f_s_leg = @(z) sin(((z + 1)/2).^2);
tol = 1e-10;
n = 1; 
err_c = inf; err_s = inf;
prev_Ic = inf; prev_Is = inf;

while (err_c >= tol) || (err_s >= tol)
    [nodes, weights] = golub_welsch(n);
    
    curr_Ic = sum(weights .* f_c_leg(nodes));
    curr_Is = sum(weights .* f_s_leg(nodes));
    
    err_c = abs(curr_Ic - prev_Ic);
    err_s = abs(curr_Is - prev_Is);
    
    if (err_c >= tol) || (err_s >= tol)
        prev_Ic = curr_Ic; prev_Is = curr_Is;
        n = n + 1;
    end
end

fprintf('Ic = %.15f\n', curr_Ic);
fprintf('Is = %.15f\n', curr_Is);
