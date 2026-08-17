function p10_rezolvat()
    clc; clear; close all;

    %% Demonstrații Simbolice (Subpunctele a și b)
    fprintf('--- DEMONSTRATII SIMBOLICE ---\n');
    
    % Definim lungimea intervalului H = b - a
    % si valorile functiei in cele 5 noduri echidistante (pt Boole)
    syms H f0 f1 f2 f3 f4 real
    % f0 = f(a)
    % f1 = f(a + H/4)
    % f2 = f(a + H/2) -> nodul din mijloc
    % f3 = f(a + 3H/4)
    % f4 = f(a + H) = f(b)

    % --- Subpunctul (a) ---
    % R_{i,1} : Regula Trapezului pe tot intervalul H
    R_i_1 = (H / 2) * (f0 + f4);
    
    % R_{i+1,1} : Regula Trapezului repetata pe 2 subintervale de lungime H/2
    R_i_plus_1_1 = (H / 4) * (f0 + 2*f2 + f4);
    
    % Extrapolarea Romberg R_{i,2}
    Romberg_R_i_2 = (4 * R_i_plus_1_1 - R_i_1) / 3;
    
    % Regula Simpson clasica cu pasul h = H/2
    % (H/2)/3 * (f0 + 4*f2 + f4)
    Simpson_Teoretic = (H / 6) * (f0 + 4*f2 + f4);
    
    dif_a = simplify(Romberg_R_i_2 - Simpson_Teoretic);
    fprintf('a) Diferenta simbolica Romberg R_{i,2} - Simpson = %s\n', char(dif_a));

    % --- Subpunctul (b) ---
    % Avem nevoie si de R_{i+2,1} : Trapez repetat pe 4 subintervale (lungime H/4)
    R_i_plus_2_1 = (H / 8) * (f0 + 2*f1 + 2*f2 + 2*f3 + f4);
    
    % Urmatoarea extrapolare R_{i+1,2} (baza pe R_{i+2,1} si R_{i+1,1})
    Romberg_R_i_plus_1_2 = (4 * R_i_plus_2_1 - R_i_plus_1_1) / 3;
    
    % Extrapolarea Romberg R_{i,3}
    Romberg_R_i_3 = (16 * Romberg_R_i_plus_1_2 - Romberg_R_i_2) / 15;
    
    % Regula Boole din enunt
    Boole_Teoretic = (H / 90) * (7*f0 + 32*f1 + 12*f2 + 32*f3 + 7*f4);
    
    dif_b = simplify(Romberg_R_i_3 - Boole_Teoretic);
    fprintf('b) Diferenta simbolica Romberg R_{i,3} - Boole-Villarceau = %s\n\n', char(dif_b));


    %% Verificare Practică și Număr Evaluări (Subpunctul c)
    fprintf('--- VERIFICARE PRACTICA ---\n');
    
    a_val = 1;
    b_val = 2;
    exact_val = 2 * log(2) - 1;
    num_iters = 9;
    
    h_vals = zeros(1, num_iters);
    err_trap = zeros(1, num_iters);
    err_simp = zeros(1, num_iters);
    err_bool = zeros(1, num_iters);
    
    fprintf('%-5s | %-20s | %-20s | %-20s\n', 'N', 'Eval. Trapez', 'Eval. Simpson', 'Eval. Boole');
    fprintf(repmat('-', 1, 75)); fprintf('\n');
    
    current_n = 2;
    for i = 1:num_iters
        h = (b_val - a_val) / current_n;
        h_vals(i) = h;
        
        % Obtinem atat integrala cat si numarul de evaluari
        [I_trap, evals_t] = trapez(a_val, b_val, current_n);
        [I_simp, evals_s] = simpson(a_val, b_val, current_n);
        [I_bool, evals_b] = boole_manual(a_val, b_val, current_n);
        
        err_trap(i) = abs(I_trap - exact_val);
        err_simp(i) = abs(I_simp - exact_val);
        err_bool(i) = abs(I_bool - exact_val);
        
        fprintf('%-5d | %-20d | %-20d | %-20d\n', current_n, evals_t, evals_s, evals_b);
        
        current_n = current_n * 2;
    end
    
    % Plotare
    figure('Name', 'Erori Integrare', 'Color', 'w');
    loglog(h_vals, err_trap, 'o-', 'LineWidth', 1.5, 'DisplayName', 'Trapez');
    hold on;
    loglog(h_vals, err_simp, 's-', 'LineWidth', 1.5, 'DisplayName', 'Simpson');
    loglog(h_vals, err_bool, '^-', 'LineWidth', 1.5, 'DisplayName', 'Boole');
    
    % Pante de referinta teoretice: O(h^2), O(h^4), O(h^6)
    ref_trap = h_vals.^2;
    ref_simp = h_vals.^4;
    ref_bool = 0.1 * h_vals.^6;
    
    loglog(h_vals, ref_trap, 'k--', 'HandleVisibility', 'off'); 
    loglog(h_vals, ref_simp, 'k-.', 'HandleVisibility', 'off');
    loglog(h_vals, ref_bool, 'k:', 'HandleVisibility', 'off');
    
    xlabel('Pasul h');
    ylabel('Eroarea Absoluta');
    title('Eroarea in functie de pasul h (grafic log-log)');
    legend('Location', 'northwest');
    grid on;
    set(gca, 'XDir', 'reverse'); % Inversam axa X ca pasul sa descreasca spre dreapta
    hold off;
end

%% Funcții Helper

function y = f(x)
    y = log(x);
end

function [I, eval_count] = trapez(a, b, n)
    h = (b - a) / n;
    I = 0.5 * f(a) + 0.5 * f(b);
    eval_count = 2; % f(a) si f(b)
    for i = 1:(n - 1)
        I = I + f(a + i * h);
        eval_count = eval_count + 1;
    end
    I = I * h;
end

function [I, eval_count] = simpson(a, b, n_macro)
    h = (b - a) / (2 * n_macro);
    I = f(a) + f(b);
    eval_count = 2;
    for i = 1:n_macro
        I = I + 4 * f(a + (2*i - 1) * h);
        eval_count = eval_count + 1;
    end
    for i = 1:(n_macro - 1)
        I = I + 2 * f(a + (2*i) * h);
        eval_count = eval_count + 1;
    end
    I = I * (h / 3);
end

function [I, eval_count] = boole_manual(a, b, n)
    h = (b - a) / (4 * n);
    I = 0;
    eval_count = 0;
    for i = 0:(n-1)
        x0 = a + (4*i) * h;
        x1 = a + (4*i + 1) * h;
        x2 = a + (4*i + 2) * h;
        x3 = a + (4*i + 3) * h;
        x4 = a + (4*i + 4) * h;
        
        I = I + (4 * h / 90) * (7*f(x0) + 32*f(x1) + 12*f(x2) + 32*f(x3) + 7*f(x4));
        % Sunt 5 evaluari pe macro-interval (desi nodurile de granita se repeta,
        % implementarea de fata evalueaza functia din nou)
        eval_count = eval_count + 5; 
    end
end