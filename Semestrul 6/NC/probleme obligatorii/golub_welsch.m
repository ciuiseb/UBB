function [x, w] = golub_welsch(alpha, beta, mu_0)
    J = diag(alpha) + diag(beta, 1) + diag(beta, -1);
    [V, D] = eig(J);
    [x, sort_idx] = sort(diag(D));
    V_sorted = V(:, sort_idx);
    w = mu_0 * (V_sorted(1, :)'.^2);
end