function [x, w] = gauss_hermite(n)
    if n == 1
        x = 0;
        w = sqrt(pi);
        return;
    end
    alpha = zeros(1, n);
    k = 1:(n-1);
    beta = sqrt(k / 2);
    mu_0 = sqrt(pi);
    [x, w] = golub_welsch(alpha, beta, mu_0);
end