nr_aparitii(_, [], 0).
nr_aparitii(N, [N | T], R) :-
    nr_aparitii(N, T, R1),
    R is R1 + 1.
nr_aparitii(N, [_ | T], R) :-
    nr_aparitii(N, T, R).

elimina([], []).

elimina([L1 | T], [L1 | TR]) :- 
    nr_aparitii(L1, [L1 | T], 1),  
    elimina(T, TR).

elimina([L1 | T], TR) :- 
    nr_aparitii(L1, [L1 | T], N), 
    N > 1,
    elimina(T, TR).