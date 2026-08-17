clc; clear;

n2 = 2;

%  Cramer 
% un determinant 2x2 necesita 2 inmultiri + 1 scadere = 3 flops
flops_det2 = 3; 
% avem 3 determinanti si 2 impartiri
flops_cramer2 = 3 * flops_det2 + n2;
fprintf('Regula lui Cramer:    %d FLOPs\n', flops_cramer2);

% eliminarea Gaussiana
% Forward 
% - 1 divizare pt pivot, aplicare pe randul 2 (1 inmultire, 1 scadere pt A; idem pt b) => 5
flops_gauss2_forward = 5;
% Back 
% - n^2 operatii pentru un sistem nxn la back substitution => 4
flops_gauss2_back = n2^2;
flops_gauss2_total = flops_gauss2_forward + flops_gauss2_back;
fprintf('Eliminarea Gaussiana: %d FLOPs\n\n', flops_gauss2_total);

n3 = 3;

% Cramer (n=3)
% dezvoltare Laplac pt 3x3: 3 * det(2x2) + 3 inmultiri exterioare + 2 adunari => 14 flops
flops_det3 = 3 * flops_det2 + 3 + 2;
% 4 determinanti + 3 impartiri
flops_cramer3 = 4 * flops_det3 + n3;
fprintf('Regula lui Cramer:    %d FLOPs\n', flops_cramer3);

% 2. Eliminarea Gaussiană (n=3)
% Forward = sum(k=1 to n-1) din (n-k)*(2n - 2k + 3)
% k=1 : 2 multiplicatori -> (3-1)*(2*3 - 2*1 + 3) = 14 flops
% k=2: 1 multiplicator -> (3-2)*(2*3 - 2*2 + 3) = 5 flops
flops_gauss3_forward = 14 + 5;
% Back Substitution => n^2 = 9
flops_gauss3_back = n3^2;
flops_gauss3_total = flops_gauss3_forward + flops_gauss3_back;
fprintf('Eliminarea Gaussiana: %d FLOPs\n\n', flops_gauss3_total);


fprintf('Analiza\n');
fprintf('Pentru n=2: Gauss (%d) este putin mai eficient decat Cramer (%d).\n', flops_gauss2_total, flops_cramer2);
fprintf('Pentru n=3: Gauss (%d) este mult mai eficient decat Cramer (%d).\n', flops_gauss3_total, flops_cramer3);