% a) insert_at (l, e, pos, n, rez)

insert_at([], _, _, _, []).
insert_at([H | T], E, Pos, N, [H | Rez]) :-
    \+ Pos is N, 
    NextPos is Pos + 1, 
    insert_at(T, E, NextPos, N, Rez).
insert_at(L, E, N, N, [E | L]).

insert(L, E, N, R) :- insert_at(L, E, 1, N, R).