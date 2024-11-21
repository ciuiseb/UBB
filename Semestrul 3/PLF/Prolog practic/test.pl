% dubleaza_pare(L, R)
% L - lista de intregi, i, lista initiala
% R - lista de intregi, o, lista initiala din care s au dublat numerele pare

% Caz de baza - lista goala
dubleaza_pare([], []).

% Caz favorabil, avem numar par pe prima pozitie
dubleaza_pare([H | T], [H, H | Rez]):-
    H mod 2 =:= 0,
    dubleaza_pare(T, Rez).

% Caz nefavorabil, avem numar impar pe prima pozitie
dubleaza_pare([H | T], [H | Rez]):-
    H mod 2 =:= 1,
    dubleaza_pare(T, Rez).

% L - lista de numere, i 
% R - lista de numere, 0, lista initiala fara secvente de numere negative

elimina_negative(L, R):-
    elimina_aux(L, 0, R).

% elimina_aux(L: lista de numere, flag, R: lista de numere)
% i, i, o
% flag - 1 daca ultimul numar a fost negativ, 0 altfel
elimina_aux([], _, []).

elimina_aux([H], _, [H]):-
    H > 0.

elimina_aux([H], 0, [H]):-
    H < 0.

elimina_aux([H], 1, []):-
    H < 0.

elimina_aux([L1, L2 | T], _, [L1 | R]):-
    L1 > 0, 
    elimina_aux([L2 | T], 0, R).

elimina_aux([L1, L2 | T], 0, [L1 | R]):-
    L1 < 0,
    L2 > 0, 
    elimina_aux([L2 | T], 1, R).

elimina_aux([L1, L2 | T], 1, R):-
    L1 < 0,
    elimina_aux([L2 | T], 1, R).

elimina_aux([L1, L2 | T], 0, R):-
    L1 < 0,
    L2 < 0,
    elimina_aux([L2 | T], 1, R).

transforma(L, R):-
    elimina_negative(L, R1),
    dubleaza_pare(R1, R).