clear; clc; close all;

x = linspace(-1, 1, 100); 
coeffs = [1, 1, 1/2, 1/6, 1/24]; 

[a11, b11] = aprox_pade(coeffs, 1, 1);
R11 = polyval(flip(a11), x) ./ polyval(flip(b11), x);

[a22, b22] = aprox_pade(coeffs, 2, 2);
R22 = polyval(flip(a22), x) ./ polyval(flip(b22), x);

figure;
plot(x, exp(x), 'k', 'LineWidth', 1); hold on;
plot(x, R11, '--r');
plot(x, R22, '-.b');
legend('exp(x)', 'R_{1,1}', 'R_{2,2}');
title('e^x');
grid on;

% rezultatele sunt bune, mai ales pentru R22 care este aproape identica cu
% functia in sine

% functiile Maclaurin nu sunt la fel de bune, deoarece isi pierd precizia
% mult mai repede