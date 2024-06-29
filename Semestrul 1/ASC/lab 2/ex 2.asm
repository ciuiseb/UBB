bits 32 

global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a dw 1
    b dw 2
    c dw 3
    d dw 4

;TODO
;(b-c)+(d-a)
segment code use32 class=code
    start:
        mov al, [b]
        sub al, [c]
        sub al, [a]
        
        mov ah, 0
        
        add ax, [d]
        push    dword 0      
        call    [exit]       
