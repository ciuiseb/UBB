function y_eval = evaluare_spline(x, a, b, c, d, x_eval)
    n_intervale = length(a);
    y_eval = zeros(size(x_eval));
    
    for k = 1:length(x_eval)
        xi = x_eval(k);
        idx = find(x <= xi, 1, 'last');
        
        if isempty(idx)
            idx = 1; 
        end
        if idx > n_intervale
            idx = n_intervale; 
        end
        
        dx = xi - x(idx);
        y_eval(k) = a(idx) + b(idx)*dx + c(idx)*dx^2 + d(idx)*dx^3;
    end
end