function [x, w] = gauss_laguerre(n)
    k = 1:n;
    alpha = 2 * k - 1;
   
    k_sub = 1:(n-1);
    beta = k_sub;
    
    J = diag(alpha) + diag(beta, 1) + diag(beta, -1);
    [V, D] = eig(J);
    
    [x, sort_idx] = sort(diag(D));
    V_sorted = V(:, sort_idx);
    mu_0 = 1;
    w = mu_0 * (V_sorted(1, :)'.^2);
end