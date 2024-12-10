(defun inorder(tree)
    (cond
        ((null tree)
             nil)
        (t 
           (append (inorder (cadr tree))
                    (list (car tree))
                    (inorder (caddr tree))
           )
        )
    )
)