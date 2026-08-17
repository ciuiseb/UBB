function [a, b] = aprox_pade(c, m, k)
    Cmatrix = zeros(k, k);
    Bmatrix = zeros(k, 1);
    
    for i = 1:k
        for j = 1:k
            index = m + i - j;
            if index >= 0 && index < length(c)
                Cmatrix(i, j) = c(index + 1);
            else
                Cmatrix(i, j) = 0;
            end
        end
        Bmatrix(i) = -c(m + i + 1);
    end
    
    b_coefficients = Cmatrix \ Bmatrix;
    b = [1; b_coefficients];
    
    a = zeros(m + 1, 1);
    for j = 0:m
        sum_val = 0;
        for l = 0:j
            if l <= k
                sum_val = sum_val + c(j - l + 1) * b(l + 1);
            end
        end
        a(j + 1) = sum_val;
    end
end