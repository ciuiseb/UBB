bits 32 

global start        

extern exit              
import exit msvcrt.dll    

segment data use32 class=data
    a dw 1010_0110_0000_1101b ; = A60D
    b db 1001_1111b
    c dd 1
    ; 1001_1111__1010_0110_0000_1101__1111__1000
    ; 9FA6 0DF8
	
;TODO
; 9. Se de cuvantul A si octetul B. Sa se obtina dublucuvantul C astfel:
; bitii 0-3 ai lui C coincid cu bitii 6-9 ai lui A 
; bitii 4-5 ai lui C au valoarea 1
; bitii 6-7 ai lui C coincid cu bitii 1-2 ai lui B
; bitii 8-23 ai lui C coincid cu bitii lui A
; bitii 24-31 ai lui C coincid cu bitii lui B
segment code use32 class=code
    start:
        mov ebx, 0
        mov ax, word[a]
        and ax, 0000_0011_1100_0000b ;am izolat bitii 6-9 a lui A
        
        mov cl, 6
        shr ax, cl ;am adus bitii pe poz coresp
        
        or bx, ax   ; i-am pus in rez
        
        or bx, 0000_0000_0011_0000b
        
        mov al, byte[b]
        and al, 0000_0110b ; am izolat bitii 1-2 a lui B
        
        mov cl, 5
        shl ax, cl ;am adus bitii pe poz coresp
        
        or bx, ax ; i-am pus in rez
        
        mov eax, 0
        mov ax, [a]
        
        mov cl, 8
        rol eax, cl ; am pus bitii din A pe poz coresp in C
        
        or ebx, eax
        
        mov eax, 0
        mov al, byte[b]
        rol eax, 24 ; am pus bitii din B pe poz coresp in C
        
        or ebx, eax ; ebx = rez
        
        mov [c], ebx ; C = rez
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
