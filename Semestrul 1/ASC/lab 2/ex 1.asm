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
;(a+c-d) +d - (b+b-c) = a + c - d -b -b + c
segment code use32 class=code
    start:
        
        mov al, [a]
        add al, [c] ;al = a + c
        
        mov ah, 0 ; ax = a + c
        
        sub ax, [d] ; ax = a + c - data
        
        sub ax, [b]
        sub ax, [b]
        
        add ax, [c]
        
        
        push    dword 0     
        call    [exit]       
