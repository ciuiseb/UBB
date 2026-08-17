function [x, w] = gauss_chebisev_2(n)
    k = (1:n)'; 
    theta = k * pi / (n + 1);
    x = cos(theta);
    w = (pi / (n + 1)) * sin(theta).^2;
    x = flipud(x); 
    w = flipud(w);
end