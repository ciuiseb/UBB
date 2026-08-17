c = [1, -2, 4/3, -8/27];

% Rădăcina exactă (triplă) pentru referință
radacina_exacta = 2/3;

% Parametrii simulării
n = 1000; % numărul de repetări
dispersie = 1e-5;
sigma = sqrt(dispersie); % deviația standard

figure;
hold on; 
grid on;
xlabel('Partea Reala');
ylabel('Partea Imaginara');

for i = 1:n
    perturbari = sigma * randn(1, length(c));
    c_perturbat = c + perturbari;
    radacini = roots(c_perturbat);
    plot(real(radacini), imag(radacini), 'b.', 'MarkerSize', 5);
end

plot(real(radacina_exacta), imag(radacina_exacta), 'r*', 'MarkerSize', 10, 'LineWidth', 2);
legend('Radacini perturbate', 'Radacina tripla 2/3)');

hold off;