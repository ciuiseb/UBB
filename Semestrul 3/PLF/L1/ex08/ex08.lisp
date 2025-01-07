; a)

(defun rn(l current n)
    (cond
        ((null l) nil)
        ((equal current n) (cdr l))
        (t (cons (car l) (rn (cdr l) (+ 1 current) n)))
    )
)

;b
(defun add(l carry)
    (cond 
        ((null l) nil)
        (t 
            (cons (mod(+ (car l) carry) 10) (add (cdr l) (floor (+ (car l) carry) 10)))
        )
        
    )
)

(defun addone(l)
    (reverse (add (reverse l) 1))
)

; c)

(defun e(l target)
    (cond
        ((null l) nil)
        ((equal (car l) target) t)
        (t (e (cdr l) target))
    )
)

(defun flatten(l)
    (cond
        ((null l) nil)
        ((listp (car l))
            (append (flatten (car l)) (flatten (cdr l))))
        (t 
            (cons (car l) (flatten (cdr l))))
    )
)

(defun mkset(l)
    (cond 
        ((null l) nil)
        ((e (cdr l) (car l)) (mkset (cdr l)))
        (t 
            (cons (car l) (mkset (cdr l))))
    )
)

(defun flattenandmakeset(l)
    (mkset (flatten l))
)

; d)
(defun isset(l)
    (cond
        ((null l) t)
        ((e (cdr l) (car l)) nil)
        (t (isset (cdr l)))
    )
)