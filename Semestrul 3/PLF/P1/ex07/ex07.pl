% a)
contains([Target | _], Target).
contains([H | T], Target) :-
    \+ H is Target, 
    contains(T, Target).

% remove(l, target, res)
remove([], _, []).
remove([H | T], Target, [H | Rez]) :-
    \+ H is Target, 
    remove(T, Target, Rez).
remove([Target | T], Target, T).

% reunion (l, k, res)
reunion([], L, L).
reunion(L, [], L).
reunion([H | T], K, [H | Rez]) :-
    \+ contains(K, H), 
    reunion(T, K, Rez).
reunion([H | T], K, [H | Rez]) :-
    contains(K, H),
    remove(K, H, Rest), 
    reunion(T, Rest, Rez).

% b)
pair_with_all(_, [], []).
pair_with_all(E, [H | T], [[E , H] | Rez]):-
    pair_with_all(E, T, Rez).

pair([_], []).
pair([H| T], [P | Rez]) :-
    pair_with_all(H, T, P), 
    pair(T, Rez).
