function [x, w] = gauss_legendre(n)
    alpha = zeros(1, n);
    k = 1:(n-1);
    beta = k ./ sqrt(4*k.^2 - 1);
    mu_0 = 2;
    [x, w] = golub_welsch(alpha, beta, mu_0);
end