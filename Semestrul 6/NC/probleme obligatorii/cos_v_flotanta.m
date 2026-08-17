function c = cos_v_flotanta(x)
    x = mod(x, 2*pi);
    
    c = 0;
    n = 1;
    current = 1; 
    
    while abs(current) >= 1e-8 
        c = c + current;
        current = -current * (x^2) / ((2*n - 1) * (2*n));
        
        n = n + 1;
    end
end