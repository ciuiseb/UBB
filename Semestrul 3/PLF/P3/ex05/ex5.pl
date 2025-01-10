%R2az de baza
submultimi_de_k(_, 0, []).

% l1 U submultimi(l2...ln, k-1)
% L - lista, i 
% k - int, i, numarul de elemente ale listelor
% Rez - listele rezultat, o
submultimi_de_k(_, 0, []).

submultimi_de_k([H|T], K, [H|Rest]) :-
    K > 0,
    K1 is K - 1,
    submultimi_de_k(T, K1, Rest).

% submultimi(l2...ln, k), dacă k > 0
% ambele lucreaza pentru a genera toate combin
% Acelasi model de flux

submultimi_de_k([_|T], K, Result) :-
    K > 0,
    submultimi_de_k(T, K, Result).

solve(L, K, Rezultat) :-
    findall(Submultime, submultimi(L, K, Submultime), Rezultat).



exista([Target | _], Target).
exista([H | T], Target) :-
    H \= Target, 
    exista(T, Target).

% Functia principala de apelat
% L - lista de intregi, i 
% k - intreg, i, numarul de elemente ale sublistelor 
% R - lista de liste, o, rezultat
submultimi(L, K, R):-
	submultime(L, K, [], R).

% Functie auxiliara care construieste rezultatul
% submultime(L, k, R_aux, R)
% L - lista de intregi, i 
% k - intreg, i, numarul de elemente ale sublistelor 
% R_aux - lista de liste, i(initial vida), rezultat intermediar
% R - lista de liste, o, rezultat
submultime([], _, R, R).

submultime([L|T], K, R1, R):-
    submultimi_de_k([L|T], K, R2),
    \+ exista(R1, R2), !,
    submultime([L|T], K, [R2|R1], R).

submultime([_|T], K, R1, R):-
    submultime(T, K, R1, R).
