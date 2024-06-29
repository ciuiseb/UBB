bits 32

global start
extern exit

import exit msvcrt.dll

segment data use32 class = data:
    a db 1
    b dw 1
    c dd 1
    d dq 1
;TODO
;d-(a+b+c)-(a+a)
;Expected result
; 1 - 3 - 2 = -4
segment code use32 class = code:
    start:
        mov ebx, [d]
        mov ecx, [d+4] ; ecx:ebx = d
        
        mov al, [a] ; al = a
        cbw         ; ax = a    
            
        add ax, word[b] ; ax = a + b
        
        cwde         ; eax = a + b
        
        add eax, dword[c]   ; eax = a+b+c
        
        cdq         ; edx:eax = a+b+c
        
        sub ebx, eax    
        sbb ecx, edx    ; ecx:ebx = d-(a+b+c)
        
        mov al, byte 2
        mul byte[a]     ; ax = 2*a
        
        cwde     ; eax = 2a
        cdq     ; edx:eax = 2a
        
        sub ebx, eax    
        sbb ecx, edx    ; ecx:ebx = d-(a+b+c) - (a+a)
        
        push    dword 0      
        call    [exit]   