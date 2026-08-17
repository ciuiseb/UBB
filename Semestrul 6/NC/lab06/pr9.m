Re = [0.2, 2, 20, 200, 2000, 20000];
cD = [103, 13.9, 2.72, 0.800, 0.401, 0.433];
Re_query = [5, 50, 500, 5000];

X = log10(Re);
Y = log10(cD);
X_query = log10(Re_query);

[Y_query, ~] = spline_cubic(X, Y, X_query);
cD_query = 10.^Y_query;

for i = 1:length(Re_query)
    fprintf('Re = %-4d -> cD = %.4f\n', Re_query(i), cD_query(i));
end

Re_fine = logspace(log10(min(Re)), log10(max(Re)), 500);
X_fine = log10(Re_fine);

[Y_fine, dY_fine] = spline_cubic(X, Y, X_fine);
cD_fine = 10.^Y_fine;

derivata_cD_fine = (cD_fine ./ Re_fine) .* dY_fine;

figure('Position', [150, 150, 800, 500]);
yyaxis left
loglog(Re, cD, 'ko', 'MarkerFaceColor', 'k', 'MarkerSize', 8, 'DisplayName', 'Date masurate');
hold on;
loglog(Re_fine, cD_fine, 'b-', 'LineWidth', 2, 'DisplayName', 'Aproximare Spline Natural');
ylabel('c_D (scara log)');
ax = gca; ax.YColor = 'b'; 

yyaxis right
semilogx(Re_fine, derivata_cD_fine, 'r-', 'LineWidth', 2, 'DisplayName', 'Derivata dc_D/dRe');
ylabel('Derivata dc_D/dRe');
ax.YColor = 'r';

xlabel('Reynolds Re (log scale)');
title('Coeficientul de franare si derivata sa');
grid on;

lines = findobj(gca, 'Type', 'Line');
legend(lines([1, 2, 3]), 'Location', 'best');
hold off;