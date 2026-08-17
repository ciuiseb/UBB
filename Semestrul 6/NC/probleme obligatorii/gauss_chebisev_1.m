function [x, w] = gauss_chebisev_1(n)
    k = (1:n)'; 
    x = cos((2*k - 1) * pi / (2 * n));
    % nodurile au ponderile egale
    w = (pi / n) * ones(n, 1);
    x = flipud(x); 
end