bits 32 

global start        

extern exit               
import exit msvcrt.dll    
                          
segment data use32 class=data
    a dd 1
    b db 1
    c db 1
    x dq 1

;TODO
;(a+b)/(2-b*b+b/c)-x; a-doubleword; b,c-byte; x-qword
segment code use32 class=code
    start:
        mov cx, [a]     ; cx = a
        mov al, [b]     ; al = b
        cbw             ; ax = b
        
        add cx, ax      ; cx = a + b
        
        imul byte[b]     ; ax = b*b
        
        mov bx, word 2   ; bx = 2
        sub bx, ax      ; bx = 2 - b*b
        
        mov al, [b]     ; al = b
        cbw             ; ax = b
        
        idiv byte[c]     ; al = b / c
        
        cbw             ; ax = b / c 
        
        add bx, ax      ; bx = 2-b*b+b/c
        
        mov ax, cx 
        mov dx, 0       ; dx:ax = a + b
        
        idiv bx          ; ax = (a+b)/(2-b*b+b/c)
        
        cwde            ; eax = (a+b)/(2-b*b+b/c)
        mov edx, 0      ; edx:eax = (a+b)/(2-b*b+b/c)
        
        mov ebx, [x]
        mov ecx, [x + 4] ; ecx:ebx = x 
        
        sub eax, ebx 
        sbb edx, ecx    ; edx:eax = res
        
                    
        push    dword 0   
        call    [exit]       
