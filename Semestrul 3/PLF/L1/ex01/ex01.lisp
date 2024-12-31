; a)
(defun insert_aux(l index r)
    (cond
        ((null l)
            nil)
        (( = (mod index 2) 0 )
            (cons (car l) (cons r (insert_aux (cdr l) (+ index 1) r))))
        (t
            (cons (car l) (insert_aux (cdr l) (+ index 1) r)))
    )
)

(defun insert(l r)
    (insert_aux l 1 r)
)

; b) 
(defun flatten(l)
    (cond
        ((null l )
             nil)
        ((atom (car l) )
            (cons (car l) (flatten (cdr l))))
        (t 
            (append (flatten (car l)) (flatten(cdr l))))
    )
)

(defun reverse_aux(l)
    (cond
        ((null l) nil)
        (t
            (append (reverse_aux (cdr l)) (list (car l))))
    )
)

(defun rev(l)
    (reverse_aux (flatten l))
)

; c)
(defun cmmdc(a b)
    (cond 
    ((= b 0)
        a)
    (t 
        (cmmdc b (mod a b)))
    )
)

(defun cmmdc_list_aux (l result)
    (cond 
        ((null l)
            result)
        (t
            (cmmdc_list_aux (cdr l) (cmmdc result (car l))))
    )
)

(defun cmmdc_list(l)
    (cmmdc_list_aux (cdr (flatten l)) (car (flatten l)))
)

;d)
(defun apparitions-aux(l target result)
    (cond
        ((null l) result)
        ((= (car l) target) 
            (apparitions-aux (cdr l) target (+ 1 result)))
        (t (apparitions-aux (cdr l) target result))
    )
)

(defun apparitions(l target)
    (apparitions-aux (flatten l) target 0)
)