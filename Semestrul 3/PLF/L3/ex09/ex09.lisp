(defun subs (e l1 l)
    (cond
        ((null l) nil)
        ((atom l) 
            (if (equal l e) l1 l))
        (t 
            (mapcar #'(lambda (x) (subs e l1 x)) l))
    )
)