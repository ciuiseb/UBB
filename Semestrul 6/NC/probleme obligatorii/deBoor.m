function pt = deBoor(grad, noduri, pct_control, t)
    k = find(noduri <= t, 1, 'last');
    
    if isempty(k) || k == length(noduri)
        k = length(noduri) - 1;
    end
    
    d = pct_control(k-grad : k, :);
    
    for r = 1:grad
        for j = grad:-1:r
            alpha = (t - noduri(k - grad + j)) / (noduri(k + j - r + 1) - noduri(k - grad + j));
            d(j+1, :) = (1 - alpha) * d(j, :) + alpha * d(j+1, :);
        end
    end
    
    pt = d(grad + 1, :);
end