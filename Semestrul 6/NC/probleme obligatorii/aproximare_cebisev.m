function [c, P_eval, eroare] = aproximare_cebisev(f, n_grad, x_eval)
    c = zeros(n_grad + 1, 1);
    
    integrand0 = @(x) f(x) .* 1 ./ sqrt(1 - x.^2);
    c(1) = (1/pi) * integral(integrand0, -1, 1);
    
    for k = 1:n_grad
        integrand_k = @(x) f(x) .* cos(k * acos(x)) ./ sqrt(1 - x.^2);
        c(k+1) = (2/pi) * integral(integrand_k, -1, 1);
    end
    if nargin < 3
        P_eval = [];
        eroare = [];
        return;
    end
    
    P_eval = c(1) * ones(size(x_eval));
    for k = 1:n_grad
        P_eval = P_eval + c(k+1) * cos(k * acos(x_eval));
    end
    
    f_eval = f(x_eval);
    eroare = abs(f_eval - P_eval);
end