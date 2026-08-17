function [x, iteratii, eroare] = iterativ_jacobi(A, b, tol, max_iter)
    
    n = length(b);
    x = zeros(n, 1); 
    
    L = tril(A, -1);          
    U = triu(A, 1);           
    D_vec = spdiags(A, 0);    
    
    for iteratii = 1:max_iter
        x_new = (b - L * x - U * x) ./ D_vec;
        
        eroare = norm(x_new - x, inf);
        if eroare < tol
            x = x_new;
            return; 
        end
        x = x_new;
    end
end