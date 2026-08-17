function y_eval = lagrange_baricentrica(x_nodes, y_nodes, x_eval)
    N = length(x_nodes);
    w = ones(1, N);
    
    for j = 1:N
        for k = 1:N
            if k ~= j
                w(j) = w(j) / (x_nodes(j) - x_nodes(k));
            end
        end
    end

    y_eval = zeros(size(x_eval));
    
    for i = 1:length(x_eval)
        idx = find(x_nodes == x_eval(i), 1);
        if ~isempty(idx)
            y_eval(i) = y_nodes(idx);
        else
            num = 0;
            den = 0;
            for j = 1:N
                term = w(j) / (x_eval(i) - x_nodes(j));
                num = num + term * y_nodes(j);
                den = den + term;
            end
            y_eval(i) = num / den;
        end
    end
end