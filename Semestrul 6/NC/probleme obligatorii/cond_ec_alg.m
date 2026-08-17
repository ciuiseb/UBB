function [radacini, conditionari] = cond_ec_alg(c)
    radacini = roots(c);
    n = length(c) - 1;
    c_derivat = polyder(c); 
    puteri = n:-1:0;        
    
    conditionari = zeros(size(radacini));
    
    for i = 1:length(radacini)
        r = radacini(i);
        der_val = polyval(c_derivat, r);
        
        if abs(der_val) < 1e-10
            conditionari(i) = inf; 
        else
            suma_perturbari = sum(abs(c) .* (abs(r) .^ puteri));
            conditionari(i) = suma_perturbari / abs(der_val);
        end
    end
end