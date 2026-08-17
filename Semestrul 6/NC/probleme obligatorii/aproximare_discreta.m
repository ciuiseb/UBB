function [c, y_eval, eroare] = aproximare_discreta(x, y, n_grad, x_eval)
    x = x(:); y = y(:); 
    
    m = length(x);
    A = zeros(m, n_grad + 1);
    
    for j = 0:n_grad
        A(:, j+1) = x.^j;
    end
    c = A \ y;
    if nargin < 4
        x_eval = x;
    end
    x_eval = x_eval(:);
    
    A_eval = zeros(length(x_eval), n_grad + 1);
    for j = 0:n_grad
        A_eval(:, j+1) = x_eval.^j;
    end
    y_eval = A_eval * c;
    
    if length(x_eval) == length(y)
        eroare = norm(y - y_eval, inf);
    else
        eroare = NaN; 
    end
end