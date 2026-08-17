function [a, b, c, d] = spline_coeficienti(x, y, tip, val_A, val_B)
    n = length(x);
    h = diff(x);
    A = zeros(n, n);
    rhs = zeros(n, 1);
    
    for i = 2:n-1
        A(i, i-1) = h(i-1);
        A(i, i)   = 2 * (h(i-1) + h(i));
        A(i, i+1) = h(i);
        rhs(i)    = 3/h(i) * (y(i+1) - y(i)) - 3/h(i-1) * (y(i) - y(i-1));
    end
    
    switch lower(tip)
        case 'natural'
            A(1, 1) = 1; rhs(1) = 0;
            A(n, n) = 1; rhs(n) = 0;
        case 'complet'
            A(1, 1) = 2*h(1); A(1, 2) = h(1);
            rhs(1) = 3/h(1) * (y(2) - y(1)) - 3*val_A;
            A(n, n-1) = h(n-1); A(n, n) = 2*h(n-1);
            rhs(n) = 3*val_B - 3/h(n-1) * (y(n) - y(n-1));
        case 'derivate_secunde'
            A(1, 1) = 1; rhs(1) = val_A / 2;
            A(n, n) = 1; rhs(n) = val_B / 2;
    end
    
    c_temp = A \ rhs;
    a = y(1:n-1);
    b = zeros(n-1, 1);
    c = c_temp(1:n-1);
    d = zeros(n-1, 1);
    
    for i = 1:n-1
        b(i) = (y(i+1) - y(i))/h(i) - h(i)*(2*c_temp(i) + c_temp(i+1))/3;
        d(i) = (c_temp(i+1) - c_temp(i)) / (3*h(i));
    end
end