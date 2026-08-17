function [x, iter, err] = metoda_secantei(f, x0, x1, tol, max_iter)
    for iter = 1:max_iter
        f0 = f(x0);
        f1 = f(x1);
        
        if f1 - f0 == 0
            error('Numitor nul ');
        end
        
        x = x1 - f1 * (x1 - x0) / (f1 - f0);
        err = abs(x - x1);
        
        if err < tol
            return;
        end
        
        x0 = x1;
        x1 = x;
    end
end