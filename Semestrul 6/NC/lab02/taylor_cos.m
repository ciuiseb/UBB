function c = taylor_cos(x)
    x = mod(x, 2*pi);
    c = 0;
    n = 0;
    current = 1;
    
    while abs(current) >= 1e-8
        c = c + current;
        n = n + 1;
        
        current = (-1)^n * (x^(2*n))/factorial(2*n);
    end
end