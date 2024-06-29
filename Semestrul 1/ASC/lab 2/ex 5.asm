bits 32 

global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a db 1
    b db 2
    c db 3
    d dw 4

;TODO
; (a+a)-(b+b)-c
segment code use32 class=code
    start:
        mov al, [a]
        mov ah, 0
        add ax, ax
        
        mov bl, [b]
        mov bh, 0
        add bx, bx
        
        mov cx, c
        sub ax, bx
        sub ax, cx
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
