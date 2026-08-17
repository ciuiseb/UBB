clear; clc; close all;

x = linspace(-1, 1, 100); 
coeffs = [0, 1, -1/2, 1/3, -1/4];

[a22, b22] = aprox_pade(coeffs, 2, 2);
R22 = polyval(flip(a22), x) ./ polyval(flip(b22), x);

[a31, b31] = aprox_pade(coeffs, 3, 1);
R31 = polyval(flip(a31), x) ./ polyval(flip(b31), x);

figure;
plot(x, log(1 + x),  'k', 'LineWidth', 1); hold on;
plot(x, R22, '--r'); 
plot(x, R31, '-.b');
legend('ln(1+x)', 'R_{2,2}', 'R_{3,1}');
title('ln(1+x)');
grid on;