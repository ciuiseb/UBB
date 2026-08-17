function x = descompunere_lup(A, b)n = size(A, 1);
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
    
    Pb = P * b;
    y = zeros(n, 1);
    y(1) = Pb(1); 
    for i = 2:n
        y(i) = Pb(i) - L(i, 1:i-1) * y(1:i-1);
    end

    x = zeros(n, 1);
    x(n) = y(n) / U(n, n);
    for i = n-1:-1:1
        x(i) = (y(i) - U(i, i+1:n) * x(i+1:n)) / U(i, i);
    end
end