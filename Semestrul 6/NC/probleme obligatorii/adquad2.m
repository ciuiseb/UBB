function I = adquad2(f, a, b, tol)
    m = (a + b) / 2;
    fa = f(a); 
    fb = f(b); 
    fm = f(m);
    
    S = (b - a) / 6 * (fa + 4*fm + fb);
    I = pas_adaptiv(f, a, b, tol, S, fa, fm, fb);
end

function I = pas_adaptiv(f, a, b, tol, S_vechi, fa, fm, fb)
    m = (a + b) / 2;
    h = (b - a) / 2;
    m1 = a + h/2;
    m2 = m + h/2;
    fm1 = f(m1);
    fm2 = f(m2);
    
    S_stanga  = h / 6 * (fa + 4*fm1 + fm);
    S_dreapta = h / 6 * (fm + 4*fm2 + fb);
    S_nou = S_stanga + S_dreapta;
    
    if abs(S_nou - S_vechi) < 15 * tol
        I = S_nou + (S_nou - S_vechi) / 15;
    else
        I = pas_adaptiv(f, a, m, tol/2, S_stanga, fa, fm1, fm) + ...
            pas_adaptiv(f, m, b, tol/2, S_dreapta, fm, fm2, fb);
    end
end