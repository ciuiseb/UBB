; a)

(defun prod(i j)
    (cond
        ((null i) 0)
        (t
            (+ ( * (car i) (car j)) (prod (cdr i) (cdr j))))
    )
)

; b)
(defun depth(l)
    (cond
        ((null l) 1)
        ((listp (car l))
                    (+ (depth (car l)) (depth (cdr l))))
        (t
            (depth (cdr l)))
    )
)

            