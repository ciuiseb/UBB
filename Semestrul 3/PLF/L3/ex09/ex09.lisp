(defun subs (e new l)
    (cond
        ((null l) nil)
        ((atom l) 
            (if (equal l e) new (list l)))
        (t 
            (list (apply #'append (mapcar #'(lambda (x) (subs e new x)) l))))
    )
)