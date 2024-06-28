bits 32 
global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a db 1
    b dw 1
    c dd 1
    d dq 10

; our code starts here
;(d-c)+(b-a)-(b+b+b)
; 10-1 + 1-1 - 3 = 6
segment code use32 class=code
    start:
        mov ebx, [d]
        mov ecx, [d+4] ; ecx:ebx = d
        
        sub ebx, dword[c]
        sbb ecx, 0  ; ecx:ebx = d - c 
        
        mov eax, 0
        mov ax, [b]
        sub al, [a]
        sbb ah, 0   ; eax = b - a 
        
        add ebx, eax
        adc ecx, 0 ; ebx:ecx = (d-c) + (b - a)
        
        mov ax, 3
        mul word [b] ; dx:ax = 3*b
        
        push dx
        push ax
        pop eax ; eax = 3*b
        
        sub ebx, eax
        sbb ecx, 0 ; ecx:ebx = res
        
        
        
        push    dword 0      
        call    [exit]       
