clc; clear;

n = 10000;
diag_princ = 4 * ones(n, 1); 
diag_sec = -1 * ones(n-1, 1);
x_exact = ones(n, 1);

rhs = zeros(n, 1);
rhs(1) = diag_princ(1)*x_exact(1) + diag_sec(1)*x_exact(2);
for i = 2:n-1
    rhs(i) = diag_sec(i-1)*x_exact(i-1) + diag_princ(i)*x_exact(i) + diag_sec(i)*x_exact(i+1);
end
rhs(n) = diag_sec(n-1)*x_exact(n-1) + diag_princ(n)*x_exact(n);

x_calculat = cholesky_func(diag_princ, diag_sec, rhs);


eroare = norm(x_calculat - x_exact, inf);
fprintf('Eroare %e\n', eroare);


function x = cholesky_func(a, b, d)
    
    n = length(a);
    
    l = zeros(n, 1);     
    m = zeros(n-1, 1);   
    
    l(1) = sqrt(a(1));
    for k = 1:n-1
        m(k) = b(k) / l(k);
        l(k+1) = sqrt(a(k+1) - m(k)^2);
    end
    
    y = zeros(n, 1);
    y(1) = d(1) / l(1);
    for k = 2:n
        y(k) = (d(k) - m(k-1) * y(k-1)) / l(k);
    end
        x = zeros(n, 1);
    x(n) = y(n) / l(n);
    for k = n-1:-1:1
        x(k) = (y(k) - m(k) * x(k+1)) / l(k);
    end
end