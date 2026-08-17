
function s = taylor_sin(x)
    x = mod(x, 2*pi); % reudcere
    
    s = 0;
    n = 0;
    current = x;
    
    while abs(current) >= 1e-8 %o toleranta poate rezonabila
        s = s + current;
        n = n + 1;
        
        current = (-1)^n * (x^(2*n + 1)) / factorial(2*n + 1);
    end
end