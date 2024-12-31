; a)
(defun get_n(l n current)
    (cond
        ((= current n)
             (car l))
        ((> current n)
            nil)
        (t
            (get_n (cdr l) n (+ current 1)))
    )
)

(defun getn(l n)
    (get_n l n 1)
)

; b)

(defun flatten(l)
    (cond
        ((null l) nil)
        ((atom (car l)) 
            (cons (car l) (flatten (cdr l))))
        (t
            (append (flatten (car l)) (flatten (cdr l))))
    )
)

(defun contains_aux(l target)
    (cond
        ((null l) nil)
        ((= (car l) target) t)
        (t (contains_aux (cdr l) target))
    )
)

(defun contains(l target)
    (contains_aux (flatten l) target)
)

;c)

(defun sublists(l)
    (cond
        ((null l) nil)
        ((listp (car l))
            (cons (car l) (sublists (cdr l))))
        (t (sublists (cdr l)))
    )
    )

(defun sl(l)
    (append l (sublists l)))

;d)

(defun to_set(l)
    (cond
        ((null l) nil)
        ((not (contains (cdr l) (car l)))
            (cons (car l) (to_set (cdr l)))
        )
        (t
            (to_set (cdr l)))
    )
)