% matericea urmeaza regula: diagonala principala 5, pe dp +-1 si +-3 -1,
%iar in rest 0 (in afara de colturile cu 1); prin urmare voi folosii o matrice rara

clc; clear;
n = 500000;

d0 =  5 * ones(n, 1); 
d1 = -1 * ones(n, 1); 
d3 = -1 * ones(n, 1); 

A = spdiags([d3, d1, d0, d1, d3], [-3, -1, 0, 1, 3], n, n);

A(1, n) = 1;
A(n, 1) = 1;

b = ones(n, 1);
b(1) = 4; b(n)   = 4;
b(2) = 2; b(n-1) = 2;
b(3) = 2; b(n-2) = 2;

tol = 1e-6;         
max_iter = 500;     

L = tril(A, -1);          
U = triu(A, 1);           
D_vec = spdiags(A, 0);    
D = spdiags(D_vec, 0, n, n); 

% Jacobbei
x_jac = zeros(n, 1); 

for k = 1:max_iter
    x_new = (b - L * x_jac - U * x_jac) ./ D_vec;
    
    eroare = norm(x_new - x_jac, inf);
    if eroare < tol
        fprintf('Jacobi eroare %e)\n\n', eroare);
        x_jac = x_new;
        break;
    end
    x_jac = x_new;
end


% Rulam Metoda Gauss-Seidel
x_gs = zeros(n, 1); 
DL = tril(A); % D + L

for k = 1:max_iter
    x_new = DL \ (b - U * x_gs); 
    
    eroare = norm(x_new - x_gs, inf);
    if eroare < tol
        fprintf('Gauss-Seidel eroare: %e)\n\n', eroare);
        x_gs = x_new;
        break;
    end
    x_gs = x_new;
end

% sor
x_sor = zeros(n, 1);
omega = 1.25; 

M_sor = D + omega * L;
N_sor = (1 - omega) * D - omega * U;
wb = omega * b;

for k = 1:max_iter
    x_new = M_sor \ (N_sor * x_sor + wb);
    
    eroare = norm(x_new - x_sor, inf);
    if eroare < tol
        fprintf('SOR (cu omega=%.2f) eroare: %e)\n\n', omega, eroare);
        x_sor = x_new;
        break;
    end
    x_sor = x_new;
end