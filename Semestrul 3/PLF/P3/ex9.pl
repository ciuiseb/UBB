% a) Sterge toate secventele de nr consecutive dintr-o lista
% sterge(L, R)
% L - lista, i, lista initiala
% R - lista, o, lista rezultata

% caz de baza
sterg([], []).

% caz pentru 2 elemente
sterg([L1, L2], [L1, L2]) :-
    L1 =\= L2 - 1.

sterg([L1, L2], []) :-
    L1 is L2 - 1.
% Recursiv, >=3 elemente
sterg([L1, L2, L3 | T], R) :-
    sterg([L2, L3 | T], R),
    L1 is L2 - 1,
    L2 is L3 - 1.
sterg([L1, L2, L3 | T], R) :-
    L1 is L2 - 1,
    L2 =\= L3 - 1,
    sterg([L3 | T], R).

sterg([L1, L2, L3 | T], [L1 | R]) :-
    L1 =\= L2 - 1,
    sterg([L2, L3 | T], R).
% b) Sterge toate secventele de elemente consecutive din sublistele listei
% sterge_sublista(L, R)
% L - lista, i, lista initiala 
% R - lista, o, lista rezultat 

% caz de baza 
sterge_sublista([], []).
% daca primul element e lista 
sterge_sublista([[L1 | T1] | T], [R1 | R]):-   
    sterg([L1 | T1], R1), 
    R1 \= [],
    sterge_sublista(T, R).

sterge_sublista([[L1 | T1] | T], R):-   
    sterg([L1 | T1], R1), 
    R1 = [],
    sterge_sublista(T, R).

% daca primul element nu e lista 
sterge_sublista([L1 | T], [L1 | R]):-   
    L1 \= [_ | _],
    sterge_sublista(T, R).