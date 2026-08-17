function [x, w] = golub_welsch(n)
    k = 1:(n-1);
    beta = k ./ sqrt(4*k.^2 - 1);

    J = diag(beta, 1) + diag(beta, -1);
    [V, D] = eig(J);
    [x, sort_idx] = sort(diag(D));
    V_sorted = V(:, sort_idx);
    mu_0 = 2;
    w = mu_0 * (V_sorted(1, :).^2)';
end