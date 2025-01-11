% a)
contains([Target | _], Target).
contains([H | T], Target) :-
    \+ H is Target, 
    contains(T, Target).

intersection(_, [], []).
intersection([], _, []).
intersection([H | T], K, [H | Rez]) :-
    contains(K, H), 
    intersection(T, K, Rez).
intersection([H | T], K, Rez) :-
    \+ contains(K, H), 
    intersection(T, K, Rez).

% b)

build(To, To, [To]).
build(From, To, [From | Rez]):-
    From =< To,
    Next is From + 1, 
    build(Next, To, Rez).