(defun liniar(l) ; l - lista ne/liniara
    (cond 
        ((null l)
             t)
        ((listp (car l)) 
            nil)
        (t 
            (liniar (cdr l)))
    )
)

(defun count_numbers(l) ;l - lista liniara de atomi 
    (cond 
        ((null l)
             0)
        (( numberp (car l)) 
            (+ 1 (count_numbers (cdr l))))
        (t
            (count_numbers (cdr l)))
    )
)

(defun sterge(l) ; lista neliniara de atomi
    (cond 
        ((null l) nil)

        ((and (listp (car l)) (not (liniar (car l))))
            (cons (sterge (car l)) (sterge (cdr l))))

        ((and (listp (car l)) (liniar (car l)) (equal 0 (mod (count_numbers (car l)) 2)))
            (sterge (cdr l)))
            
        (t
            (cons (car l) (sterge (cdr l))))
    )
)

