function s = sin_v_flotanta(x)
    x = mod(x, 2*pi); % reducere
    
    s = 0;
    n = 1;
    current = x; 
    
    while abs(current) >= 1e-8 
        s = s + current;
        current = -current * (x^2) / ((2*n) * (2*n + 1));
        
        n = n + 1;
    end
end