function y_eval = lagrange_clasic(x_nodes, y_nodes, x_eval)
    N = length(x_nodes);
    y_eval = zeros(size(x_eval));
    for i = 1:length(x_eval)
        S = 0;
        for j = 1:N
            P = 1;
            for k = 1:N
                if k ~= j
                    P = P * (x_eval(i) - x_nodes(k)) / (x_nodes(j) - x_nodes(k));
                end
            end
            S = S + P * y_nodes(j);
        end
        y_eval(i) = S;
    end
end