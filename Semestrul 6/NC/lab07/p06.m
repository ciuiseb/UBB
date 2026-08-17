function p06()
    R = 1;
    g = 10;
    p_valori = [0.5, 0.75, 1.0];
    tol = 1e-9; 
    
    fprintf('   p   |    T adaptiv    |    T rOmberg   \n');
    fprintf('-------------------------------------------\n');
    
    for i = 1:length(p_valori)
        p = p_valori(i);
        k2 = p^2 / (6*R^2 - p^2);
        f_integrand = @(t) 1 ./ sqrt(1 - k2 * sin(t).^2);
        prefactor = 8 * R * sqrt(R / (g * (6*R^2 - p^2)));
        
        I_adaptiv = own_adaptiv(f_integrand, 0, 2*pi, tol);
        T_adaptiv = prefactor * I_adaptiv;
        
        I_romberg = own_romberg(f_integrand, 0, 2*pi, tol);
        T_romberg = prefactor * I_romberg;
        
        % Afișăm rezultatele pentru p-ul curent
        fprintf('%.2f  |  %.11f  |  %.11f\n', p, T_adaptiv, T_romberg);
    end
    fprintf('-------------------------------------------\n');
end

function I = own_adaptiv(f, a, b, tol)
    m = (a + b) / 2;
    fa = f(a); fb = f(b); fm = f(m);
    
    S = (b - a) / 6 * (fa + 4*fm + fb);
    I = simpson_recursiv(f, a, b, tol, S, fa, fm, fb);
end

function I = simpson_recursiv(f, a, b, tol, S_vechi, fa, fm, fb)
    m = (a + b) / 2;
    h = (b - a) / 2;
    m1 = a + h/2;
    m2 = m + h/2;
    fm1 = f(m1);
    fm2 = f(m2);
    
    S_stanga  = h / 6 * (fa + 4*fm1 + fm);
    S_dreapta = h / 6 * (fm + 4*fm2 + fb);
    S_nou = S_stanga + S_dreapta;
    
    if abs(S_nou - S_vechi) < tol
        I = S_nou;
    else
        I = simpson_recursiv(f, a, m, tol/2, S_stanga, fa, fm1, fm) + ...
            simpson_recursiv(f, m, b, tol/2, S_dreapta, fm, fm2, fb);
    end
end

function I = own_romberg(f, a, b, tol)
    max_iter = 15; 
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