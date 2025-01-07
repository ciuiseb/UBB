; a)

(defun suma(l1 l2)
    (mapcar #'+ l1 l2)
)

; b) 
(defun flatten(l)
    (cond
        ((null l) nil)
        ((listp (car l)) (append (flatten (car l)) (flatten (cdr l))))
        (t (cons (car l) (flatten (cdr l))))
    )
)

; c) +-
(defun rs(l buffer)
    (cond 
        ((null l) buffer)
        ((listp (car l))
            (cons ( cons
                buffer
                (rs (car l) nil))
                (rs (cdr l) nil)))
        (t
            (rs (cdr l) (cons (car l) buffer)))
    )
)

;d)
(defun m(l res)
    (cond 
        ((null l) res)
        ((and (atom (car l)) (> (car l) res)) (m (cdr l) (car l)))
        (t (m (cdr l) res))
    )
)
