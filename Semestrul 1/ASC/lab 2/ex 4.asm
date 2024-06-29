bits 32 

global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a db 1
    b db 1
    e dw 1
    f dw 1

;TODO
;(e+f)*(2*a+3*b)
segment code use32 class=code
    start:
        mov ax, [e]
        add ax, [f]
        
        mov bl, [a]
        add bl, [a]
        add bl, [b]
        add bl, [b]
        add bl, [b]
        
        mov bh, 0
        mul bx
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
