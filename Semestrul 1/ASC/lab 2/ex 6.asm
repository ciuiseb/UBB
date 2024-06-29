bits 32 

global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a dw 10
    b dw 20
    c dw 30 
    d dw 40

;TODO
; (a+c)-(b+b+d)
segment code use32 class=code
    start:
        
        mov eax, 0
        mov ax, [a]
        mov ebx, 0
        mov bx, [c]
        add eax, ebx
        mov ebx, 0
        
        mov bx, [c]
        mov ecx, 0
        mov cx, [d]
        add ecx, ecx
        add ebx, ecx
        
        sub eax, ebx
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
