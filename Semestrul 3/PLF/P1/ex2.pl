% Problema 2 P1
% a)

% CMMDC al doua numere
% cmmdc( A, B, C)
% A - int, i, primul numar
% B - int, i, al doilea numar
% C - int, o, rezultatu;
% caz de baza
cmmdc(A, A, A).

% Algoritmul lui Euclid
cmmdc(A, B, R) :-
    A > B,
    A1 is A - B,
    cmmdc(A1, B, R).

cmmdc(A, B, R) :-
    A < B,
    B1 is B - A,
    cmmdc(A, B1, R).

% CMMMC al doua numere
% cmmmc( A, B, C)
% A - int, i primul numar 
% B - int, i al doilea numar 
% C - int, o rezultatul
cmmmc(A, A, A).

cmmmc(A, B, R) :-
    cmmdc(A, B, DIV), 
    R is A * B // DIV.

% CMMMC al unei liste de numere 
% cmmmc_lista( [L1 | LN], R)
% [L1 | LN] - lista, i 
% R - int, o rezultatul
cmmmc_lista([L], R):-
    R is L.
cmmmc_lista([L1 | LN], R):-
    cmmmc_lista(LN, R2),
    cmmmc(L1, R2, R).

% b)
% Adaugarea unei valori la fiecare 2^k, k =1..
% Daca lista este goala, nu adaugam nimic

add( [], _, _, _, []).
% Daca suntem pe pozitia cautata, inseram dupa primul element
add([L1 | LN], VALUE, CURENT, TARGET, [L1, VALUE | RESULT]) :-
    CURENT = TARGET, 
    NEWCURENT is CURENT + 1, 
    NEWTARGET is TARGET * 2, 
    NEWVALUE is VALUE - 1,
    add(LN, NEWVALUE, NEWCURENT, NEWTARGET, RESULT).

% Daca nu suntem pe pozitia cautata
add([L1 | LN], VALUE, CURENT, TARGET, [L1 | RESULT]) :-
    CURENT \= TARGET, 
    NEWCURENT is CURENT + 1,
    add(LN, VALUE, NEWCURENT, TARGET, RESULT).
% [L1 | LN] lista de int, i 
% VALUE -int, i
% CURENT -int, i, se updateaza automat
% TARGET -int, i, se updateaza automat
% RESULT - lista de int, o

insert(L, VALUE, RESULT) :-
    add(L, VALUE, 1, 1, RESULT).
% L - lista de int, i 
% VALUE - int, i 
% RESULT - lista de int, o