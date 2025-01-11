% a) remove(l, target, result)
remove([], _, []).
remove([H | T], Target, [H | Rez]):-
    \+ H is Target, 
    remove(T, Target, Rez).
remove([Target | T], Target, Rez) :-
    remove(T, Target, Rez).

% b)
count([], _, 0).
count([H | T], Target, Rez):-
    \+ H is Target, 
    count(T, Target, Rez).
count([Target | T], Target, Rez):-
    count(T, Target, R1),
    Rez is R1 + 1.

frecv([], []).
frecv([H | T], [[H, Fr] | Rez]) :-
    count([H | T], H, Fr), 
    remove(T, H, R1), 
    frecv(R1, Rez).