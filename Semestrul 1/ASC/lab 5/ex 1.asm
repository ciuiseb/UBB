bits 32

global start
extern exit
import exit msvcrt.dll

segment data use32 class=data
    s db 1, 2, 3, 4, 5
    len_s equ $ - s 
    d1 times len_s db 0 ; sir pt nr pare 
    d2 times len_s db 0 ; sir pt nr impare

;TODO
;11. Se da un sir de octeti S. Sa se obtina sirul 
;D1 ce contine toate numerele pare din S si 
;sirul D2 ce contine toate numerele impare din S.
segment code use32 class=code
    start:
        mov ecx, len_s
        mov esi, 0 ; indice in s 
        
        mov edi, 0 ; indice in d1
        mov ebx, 0 ; indice in d2
        
        jecxz Final
         
        Repeta:
            ;citire
            mov al, [s + esi]
            test al, 1 ; verificam paritatea nr
            jz Par
            ; Impar: 
            mov [d2 + ebx], al ;e impar, deci il punem in d2
            inc ebx
            jmp Continue
        Par:
            mov [d1 + edi], al ; e par deci il punem in d1 
            inc edi
        Continue:
            inc esi
            loop Repeta
        Final:
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
