% a)

delete([], _, []).
delete([Target | T], Target, Rez) :- delete(T, Target, Rez).
delete([H |T], Target, [H | Rez]) :-
    \+ H is Target, 
    delete(T, Target, Rez).

toset([], []).
toset([H| T], [H| Rez]):-
    delete(T, H, T1), 
    toset(T1, Rez).

% b)

destruct([], [], [], 0, 0).
destruct([H |T], [H | Even], Uneven, Eno, Uno):-    
        H mod 2 =:= 0, 
        destruct(T, Even, Uneven, Eno1, Uno), 
        Eno is Eno1 + 1.
destruct([H |T], Even, [H | Uneven], Eno, Uno):-    
        \+ H mod 2 =:= 0, 
        destruct(T, Even, Uneven, Eno, Uno1), 
        Uno is Uno1 + 1.