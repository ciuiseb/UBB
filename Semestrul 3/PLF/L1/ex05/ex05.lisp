; a)

(defun ic(l1 l2)
    (cond 
        ((null l1) l2)
        ((null l2) l1)
        (( < (car l1) (car l2))
            (cons (car l1) ( ic (cdr l1) l2 )))
        (( > (car l1) (car l2))
            (cons (car l2) ( ic l1 (cdr l2) )))
        (t 
            (cons (car l1) (cons (car l1) (ic (cdr l1) (cdr l2)))))
    )
)

; b)

(defun r(l target new)
    (cond 
        ((null l) nil)
        ((listp (car l))
            (cons (r (car l) target new) (r (cdr l) target new)))
        ((equal (car l) target) 
            (cons new (r (cdr l) target new)))
        (t
            (cons (car l) (r (cdr l) target new)))

    )
)

; c)
