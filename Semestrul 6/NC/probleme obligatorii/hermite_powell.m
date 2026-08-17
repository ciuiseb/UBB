function y_eval = hermite_powell(x, y, dy, x_eval)
    n = length(x);
    N = 2 * n;
    
    z = zeros(N, 1);
    Q = zeros(N, N);
    
    for i = 1:n
        z(2*i-1) = x(i);
        z(2*i)   = x(i);
        Q(2*i-1, 1) = y(i);
        Q(2*i, 1)   = y(i);
        Q(2*i, 2)   = dy(i);
        
        if i > 1
            Q(2*i-1, 2) = (Q(2*i-1, 1) - Q(2*i-2, 1)) / (z(2*i-1) - z(2*i-2));
        end
    end
    
    for j = 3:N
        for i = j:N
            Q(i, j) = (Q(i, j-1) - Q(i-1, j-1)) / (z(i) - z(i-j+1));
        end
    end
    
    y_eval = zeros(size(x_eval));
    for k = 1:length(x_eval)
        val = Q(1, 1);
        produs = 1;
        for i = 1:(N - 1)
            produs = produs * (x_eval(k) - z(i));
            val = val + Q(i+1, i+1) * produs;
        end
        y_eval(k) = val;
    end
end