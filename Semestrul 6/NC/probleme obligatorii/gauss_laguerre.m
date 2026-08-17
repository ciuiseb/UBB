function [x, w] = gauss_laguerre(n)
    k = 1:n;
    alpha = 2 * k - 1;
    beta = 1:(n-1);
    mu_0 = 1;
    [x, w] = golub_welsch(alpha, beta, mu_0);
end