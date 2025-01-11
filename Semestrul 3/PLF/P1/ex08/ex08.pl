% a)
contains([Target | _], Target).
contains([H | T], Target) :-
    \+ H is Target, 
    contains(T, Target).

isset([]).
isset([H | T]):-
    \+ contains(T, H), 
    isset(T).

% b)
remove_3(L, E, Rez):- remove_aux(L, E, 0, Rez).

remove_aux([], _, _, []).
remove_aux([H |T], E, Count, [H | Rez]) :-
    \+ H is E, 
    remove_aux(T, E, Count, Rez).
remove_aux([E |T], E, Count, Rez):-
    Count < 2, 
    NewCount is Count + 1, 
    remove_aux(T, E, NewCount, Rez).
remove_aux([E | T], E, 2, T).