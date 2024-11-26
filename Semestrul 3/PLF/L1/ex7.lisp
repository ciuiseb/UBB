;a) Sa se scrie o functie care testeaza daca o lista este liniara. 
(defun is_linear(l)
    (cond
        ((null l)
                 t)
        ((listp (car l))
                 nil)
        (t 
                (is_linear (cdr l)))
    )
)
; b) Definiti o functie care substituie prima aparitie a unui element intr-o 
; lista data.
(defun replace_c (l target value)     
    (cond         
        ((null l)
                 '())         
        ((and (numberp (car l)) (equal (car l) target))
                 (cons value (cdr l)))
        ((and (listp (car l)) (find_c (car l) target))
                 (cons (replace_c (car l) target value) (cdr l)))
        (t
                 (cons (car l) (replace_c (cdr l) target value)))
    )
)

(defun find_c (l target)     
   (cond          
       ((null l)
             nil)         
       ((and (listp (car l)) (find_c (car l) target))
             t)
       ((and (numberp (car l)) (equal (car l) target))
             t)
       (t
             (find_c (cdr l) target))
   )
)
; c) Sa se inlocuiasca fiecare sublista a unei liste cu ultimul ei element. 
; Prin sublista se intelege element de pe primul nivel, care este lista. 
; Exemplu: (a (b c) (d (e (f)))) ==> (a c (e (f))) ==> (a c (f)) ==> (a c f) 
(a (b c) (d ((e) f))) ==> (a c ((e) f)) ==> (a c f)
(defun replace_sublists (l)
    (cond 
        ((null l)
                 '())
        ((and (listp (car l)) (numberp (last_c (car l))))
                 (cons (last_c (car l)) (replace_sublists (cdr l))))
        ((listp (car l))
                 (replace_sublists (cdr l)))
        (t
                 (cons (car l) (replace_sublists (cdr l))))
    ))

(defun last_c (l)
    (cond
        ((atom l)
                 l)                         
        ((null (cdr l))
                 (last_c (car l)))    
        (t
                 (last_c (cdr l)))                 
    ))
; d) Definiti o functie care interclaseaza fara pastrarea dublurilor doua liste 
; liniare sortate.
(defun interclasare(l k)
    (cond 
        ((and (null l) (null k))
                 '())
        ((null l)
                 k)
        ((null k)
                 l)
        ((< (car l) (car k))
                 (cons (car l) (interclasare (cdr l) k)))
        ((> (car l) (car k))
                 (cons (car k) (interclasare l (cdr k))))
        ((equal (car l) (car k))
                 (cons (car l) (interclasare (cdr l) (cdr k))))
    )
)