function [x, iter, err] = newton_sistem(F, J, x0, tol, max_iter)
    x = x0(:);
    
    for iter = 1:max_iter
        Jx = J(x);
        Fx = F(x);
        
        delta_x = Jx \ Fx;
        x_new = x - delta_x;
        
        err = norm(delta_x, inf);
        if err < tol
            x = x_new;
            return;
        end
        
        x = x_new;
    end
end