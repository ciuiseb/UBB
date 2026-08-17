format long

n = 9;
k_max = floor((n-1)/2); 
k = 0:k_max;
T_5 = sum( ((-1).^k ./ (2*k + 1)) .* (1/5).^(2*k + 1) );
T_239 = sum( ((-1).^k ./ (2*k + 1)) .* (1/239).^(2*k + 1) );

P_9 = 16 * T_5 - 4 * T_239
eroare_relativa_9 = abs(P_9 - pi) / pi

P_eps = 0;
k_iter = 0;
termen = 1; 

while abs(termen) > eps
    termen_5 = 16 * ((-1)^k_iter / (2*k_iter + 1)) * (1/5)^(2*k_iter + 1);
    termen_239 = 4 * ((-1)^k_iter / (2*k_iter + 1)) * (1/239)^(2*k_iter + 1);
    
    termen = termen_5 - termen_239;
    P_eps = P_eps + termen; 
    
    k_iter = k_iter + 1;
end

n_necesar = 2 * (k_iter - 1) + 1
P_eps