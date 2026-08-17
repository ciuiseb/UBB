function [x, w] = gauss_hermite(n)
    if n == 1
        x = 0;
        w = sqrt(pi);
        return;
    end
        k = 1:(n-1);
    beta = sqrt(k / 2);
    J = diag(beta, 1) + diag(beta, -1);
    [V, D] = eig(J);
    [x, sort_idx] = sort(diag(D));
    V_sorted = V(:, sort_idx);
    mu_0 = sqrt(pi);
    w = mu_0 * (V_sorted(1, :)'.^2);
end