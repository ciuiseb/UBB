(defun srch(l target lvl)
    (cond
        ((null l) -1)
        ((equal (car l) target) lvl)
        ((atom l) (if (equal l target) lvl -1))
        (t 
            (max (srch(cadr l) target (+ 1 lvl)) (srch (caddr l) target (+ 1 lvl)) ))
    )
)