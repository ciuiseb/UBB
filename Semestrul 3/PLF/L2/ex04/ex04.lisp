(defun trn(l)
    (cond 
        ((null l) nil)
        ((atom (car l))
            (cons (car l)
                (cons (length (cdr l))
                    (append
                        (trn (cadr l))
                        (trn (caddr l))
                    )
                )
            )
        )
        (t (trn (car l)))
    )
)