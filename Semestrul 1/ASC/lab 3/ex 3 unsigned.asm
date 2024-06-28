bits 32

global start

extern exit
import exit msvcrt.dll

segment data use32 class = data:
    a dd 1
    b db 1
    c db 1
    x dq 1
    
segment code use32 class = code:
    start:
        ;(a+b)/(2-b*b+b/c)-x; a-doubleword; b,c-byte; x-qword
        ;2 / 2 - 1 = 0
        mov eax, 0
        mov ax, [a]
        add al, [b]
        adc ah, 0
        
        push eax ; stiva  = a + b
        
        mov al, [b] ; al = b
        mov ah, 0   ; ax = b
        div byte[c] ; al = b/c      ah = b%c
        
        mov bx, ax  ; bl = b/c      bh = b%c
        mov al, [b]     
        mul byte[b] ; ax = b*b
        
        mov bh, 0   ; bx = b/c
        
        sub bx, ax  ;bx = -b*b+b/c
        add bx, byte 2  ;bx = (2-b*b+b/c)
        
        mov dx, 0
        pop ax  ;   dx:ax = a + b
        
        div bx  ; ax = (a+b)/(2-b*b+b/c)
        
        push ax
        mov eax, 0
        pop ax  ; eax = (a+b)/(2-b*b+b/c)
        
        mov edx, 0 ;edx: eax = (a+b)/(2-b*b+b/c)
        
        mov ebx, [x]
        mov ecx, [x+4]
        
        sub eax, ebx
        sbb edx, ecx
        
        
        
        push dword 0
        call [exit]