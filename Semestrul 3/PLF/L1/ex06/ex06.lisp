; a)
(defun doublen(l current n)
    (cond 
        ((null l) nil)
        ((equal current n) (cons (car l) l))
        (t
            (cons (car l) (doublen (cdr l) (+ 1 current) n)))
    )
)

; b)
(defun as(l1 l2)
    (cond 
        ((or (null l1) (null l2)) nil)
        (t
            (cons
                 (cons 
                    (car l1) 
                    (car l2))
                 (as (cdr l1) (cdr l2))
            )
        )
    )
)

; c)
(defun slc(l)
    (cond 
        ((null l) 1)
        ((listp (car l))
            (+ (slc (car l)) (slc (cdr l))))
        (t
            (slc (cdr l)))
    )
)

; d)
(defun asl(l)
    (cond 
        ((null l) 0)
        ((atom (car l)) (+ 1 (asl (cdr l))))
        (t (asl (cdr l)))
    )   
)