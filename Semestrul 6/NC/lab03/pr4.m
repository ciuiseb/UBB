clc; clear;

n = 500; 
A = rand(n, n);
x_exact = ones(n, 1);

b = A * x_exact; 

[L, U, P] = lu_function(A);
y = L \ (P * b);
x_calculat = U \ y;

eroare = norm(x_calculat - x_exact, inf);

fprintf('eroare: %e\n', eroare);


function [L, U, P] = lu_function(A)
    n = size(A, 1);
    L = eye(n);
    P = eye(n);
    U = A;
    
    for k = 1:n-1
        [~, max_idx] = max(abs(U(k:n, k)));
        pivot_row = max_idx + k - 1;
        if pivot_row ~= k
            U([k, pivot_row], :) = U([pivot_row, k], :);
            P([k, pivot_row], :) = P([pivot_row, k], :);
            if k > 1
                L([k, pivot_row], 1:k-1) = L([pivot_row, k], 1:k-1);
            end
        end
        for i = k+1:n
            L(i, k) = U(i, k) / U(k, k);
            U(i, k:n) = U(i, k:n) - L(i, k) * U(k, k:n);
        end
    end
end