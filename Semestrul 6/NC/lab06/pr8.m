clc; clear; close all;

k = 1:11;
t = ((k - 1) / 10)';
y = erf(t);

t_fine = linspace(0, 1, 1000)';
y_fine_exact = erf(t_fine);

% a)
n_max = 5;
errors_max = zeros(n_max, 1);

figure('Name', 'Problema 8: Studiul Erorilor', 'Position', [100, 100, 1000, 400]);

subplot(1, 2, 1);
hold on;
for n = 1:n_max
    A = zeros(length(t), n);
    for j = 1:n
        A(:, j) = t.^(2*j - 1);
    end
    c = A \ y;
    
    A_fine = zeros(length(t_fine), n);
    for j = 1:n
        A_fine(:, j) = t_fine.^(2*j - 1);
    end
    y_approx = A_fine * c;
    
    eroare_curba = abs(y_fine_exact - y_approx);
    errors_max(n) = max(eroare_curba);
    
    plot(t_fine, eroare_curba, 'DisplayName', sprintf('Polinomial n = %d', n), 'LineWidth', 1.2);
end

% b) 
z = 1 ./ (1 + t);
E = exp(-t.^2);
A_b = [ones(length(t), 1), E, z.*E, z.^2.*E, z.^3.*E];
c_b = A_b \ y;

z_fine = 1 ./ (1 + t_fine);
E_fine = exp(-t_fine.^2);
A_b_fine = [ones(length(t_fine), 1), E_fine, z_fine.*E_fine, z_fine.^2.*E_fine, z_fine.^3.*E_fine];
y_approx_b = A_b_fine * c_b;
eroare_curba_b = abs(y_fine_exact - y_approx_b);

plot(t_fine, eroare_curba_b, 'k--', 'DisplayName', 'Model (b) z, E', 'LineWidth', 1.5);

set(gca, 'YScale', 'log');
title('Eroarea intre noduri |erf(t) - Aproximare|');
xlabel('t'); ylabel('Eroare (scara log)');
legend('Location', 'best');
grid on;

subplot(1, 2, 2);
plot(1:n_max, errors_max, '-ro', 'LineWidth', 1.5, 'MarkerFaceColor', 'r');
set(gca, 'YScale', 'log');
title('Dependenta erorii maxime de n');
xlabel('n'); ylabel('Eroarea maxima (scara log)');
xticks(1:n_max);
grid on;




