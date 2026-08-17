function [y_eval, dy_eval] = spline_cubic(x, y, x_eval)
    n = length(x);
    h = diff(x);
    
    A = zeros(n, n);
    rhs = zeros(n, 1);
    A(1, 1) = 1;
    A(n, n) = 1;
    
    for i = 2:n-1
        A(i, i-1) = h(i-1);
        A(i, i)   = 2 * (h(i-1) + h(i));
        A(i, i+1) = h(i);
        rhs(i)    = 3/h(i) * (y(i+1) - y(i)) - 3/h(i-1) * (y(i) - y(i-1));
    end
    
    c = A \ rhs;
    a = y;
    b = zeros(n-1, 1);
    d = zeros(n-1, 1);
    
    for i = 1:n-1
        b(i) = (y(i+1) - y(i))/h(i) - h(i)*(2*c(i) + c(i+1))/3;
        d(i) = (c(i+1) - c(i)) / (3*h(i));
    end
    
    y_eval = zeros(size(x_eval));
    dy_eval = zeros(size(x_eval));
    
    for k = 1:length(x_eval)
        xi = x_eval(k);
        idx = find(x <= xi, 1, 'last');
        
        if isempty(idx)
            idx = 1;
        end
        if idx == n || xi == x(end)
            idx = n - 1;
        end
        
        dx = xi - x(idx);
        y_eval(k)  = a(idx) + b(idx)*dx + c(idx)*dx^2 + d(idx)*dx^3;
        % Derivata exacta de gradul 2 extrasa direct din coeficientii Spline-ului Cubic
        dy_eval(k) = b(idx) + 2*c(idx)*dx + 3*d(idx)*dx^2;
    end
end