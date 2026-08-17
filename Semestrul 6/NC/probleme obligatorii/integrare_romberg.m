function I = integrare_romberg(f, a, b, tol)
    max_iter = 20; 
    R = zeros(max_iter, max_iter);
    
    h = b - a;
    R(1,1) = h/2 * (f(a) + f(b));
    
    for i = 2:max_iter
        h = h / 2;
        sum_f = 0;
        for k = 1:2^(i-2)
            sum_f = sum_f + f(a + (2*k - 1)*h);
        end
        R(i,1) = 0.5 * R(i-1,1) + h * sum_f;
        
        for j = 2:i
            R(i,j) = R(i,j-1) + (R(i,j-1) - R(i-1,j-1)) / (4^(j-1) - 1);
        end
        
        if abs(R(i,i) - R(i-1,i-1)) < tol
            I = R(i,i);
            return;
        end
    end
    
    I = R(max_iter, max_iter);
end