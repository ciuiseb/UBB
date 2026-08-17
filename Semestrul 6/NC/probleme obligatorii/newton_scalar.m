function [x, iter, err] = newton_scalar(f, df, x0, tol, max_iter)
    x = x0;
    for iter = 1:max_iter
        derivata = df(x);
        
        if derivata == 0
            error('Derivata nula intalnit');
        end
        
        x_new = x - f(x) / derivata;
        err = abs(x_new - x);
        
        if err < tol
            x = x_new;
            return;
        end
        
        x = x_new;
    end
end