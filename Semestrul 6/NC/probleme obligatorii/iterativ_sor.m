function [x, iteratii, eroare] = iterativ_sor(A, b, omega, tol, max_iter)    
    n = length(b);
    x = zeros(n, 1);
    
    L = tril(A, -1);          
    U = triu(A, 1);           
    D_vec = spdiags(A, 0);    
    D = spdiags(D_vec, 0, n, n); 
    
    M_sor = D + omega * L;
    N_sor = (1 - omega) * D - omega * U;
    wb = omega * b;
    
    for iteratii = 1:max_iter
        x_new = M_sor \ (N_sor * x + wb);
        
        eroare = norm(x_new - x, inf);
        if eroare < tol
            x = x_new;
            return; 
        end
        x = x_new;
    end
end