% a)

contains([Target | _], Target):- !.
contains([_ | T], Target):- contains(T, Target).

diff([], _, []).
diff([H| T], K, [H | Rez]) :-
    \+ contains(K, H), 
    diff(T, K, Rez).

diff( [H | T], K, Rez):-
    contains(K, H), 
    diff(T, K, Rez).

diffmultimi(L, K, [R1| R2]):-
    diff(L, K, R1), 
    diff(K, L, [R2]).

% b)
addone([], []).
addone([H | T], [H, 1 | Rez]):-
    H mod 2 =:= 0, 
    addone(T, Rez).
addone([H | T], [H | Rez]):-
    \+ H mod 2 =:= 0, 
    addone(T, Rez).