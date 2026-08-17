clear; clc; format long;
n = 10;

[nodes, weights] = golub_welsch(n);

% transformam intervalul [0,pi] in [-1, 1], pentru a fi simetric
f_sin = @(z) (pi/2) * sin((pi/2 * (z + 1)).^2);
f_cos = @(z) (pi/2) * cos((pi/2 * (z + 1)).^2);

approx_sin = sum(weights .* f_sin(nodes));
approx_cos = sum(weights .* f_cos(nodes));

%desi nu este chiar exact, fiind aplicatia piratata nu pot folosi pachetul simbolic,
%cum este recomandat in indicatii
exact_sin = integral(@(t) sin(t.^2), 0, pi, 'RelTol', 1e-14, 'AbsTol', 1e-14);
exact_cos = integral(@(t) cos(t.^2), 0, pi, 'RelTol', 1e-14, 'AbsTol', 1e-14);


fprintf('sin(t^2)\n');
fprintf('Aproximare:  %.15f\n', approx_sin);
fprintf('Valoare exacta:    %.15f\n', exact_sin);
fprintf('Eroare: %.3e\n\n', abs(exact_sin - approx_sin));

fprintf('cos(t^2)\n');
fprintf('Aproximare:  %.15f\n', approx_cos);
fprintf('Valoare exacta:    %.15f\n', exact_cos);
fprintf('Eroare: %.3e\n', abs(exact_cos - approx_cos));
