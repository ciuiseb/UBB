function [x, w] = gauss_jacobi(n, A, B)
    if n == 1
        x = (B - A) / (A + B + 2);
        w = (2^(A + B + 1) * gamma(A + 1) * gamma(B + 1)) / gamma(A + B + 2);
        return;
    end
    m = 0:(n-1);
    alpha = (B^2 - A^2) ./ ((2*m + A + B) .* (2*m + A + B + 2));
    if A + B == 0
        alpha(1) = (B - A) / 2; 
    end
    
    m_sub = 1:(n-1);
    sub_num = 4 .* m_sub .* (m_sub + A) .* (m_sub + B) .* (m_sub + A + B);
    sub_den = (2.*m_sub + A + B - 1) .* (2.*m_sub + A + B).^2 .* (2.*m_sub + A + B + 1);
    beta = sqrt(sub_num ./ sub_den);
    
    mu_0 = (2^(A + B + 1) * gamma(A + 1) * gamma(B + 1)) / gamma(A + B + 2);
    [x, w] = golub_welsch(alpha, beta, mu_0);
end