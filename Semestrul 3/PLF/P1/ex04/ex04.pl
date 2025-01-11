% a)

% replace(list, target, new, result)
replace([], _, _, []).
replace([Target | T], Target, New, [New | Rez]) :-
    replace(T, Target, New, Rez).
replace([H | T], Target, New, [H |Rez]):-
    \+ H is Target, 
    replace(T, Target, New, Rez).

% b) remove_nth(list, pos, n, result)
remove_nth([], _, []).
remove_nth([H |T], N, [H |Rez]) :-
    \+ N is 0, 
    N1 is N - 1,
    remove_nth(T, N1, Rez).
remove_nth([_ | T], 1, T).



