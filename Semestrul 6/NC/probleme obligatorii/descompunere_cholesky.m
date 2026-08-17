function x = descompunere_cholesky(A, b)
    n = size(A, 1);
    L = zeros(n, n);
    
    for j = 1:n
        for i = j:n
            if i == j
                sum_k = sum(L(j, 1:j-1).^2);
                L(j, j) = sqrt(A(j, j) - sum_k);
            else
                sum_k = sum(L(i, 1:j-1) .* L(j, 1:j-1));
                L(i, j) = (A(i, j) - sum_k) / L(j, j);
            end
        end
    end
    
    y = zeros(n, 1);
    y(1) = b(1) / L(1, 1);
    for i = 2:n
        y(i) = (b(i) - L(i, 1:i-1) * y(1:i-1)) / L(i, i);
    end
    
    U = L'; 
    
    x = zeros(n, 1);
    x(n) = y(n) / U(n, n);
    for i = n-1:-1:1
        x(i) = (y(i) - U(i, i+1:n) * x(i+1:n)) / U(i, i);
    end
end