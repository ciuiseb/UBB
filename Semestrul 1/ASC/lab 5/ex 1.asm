bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    s db 1, 2, 3, 4, 5
    len_s equ $ - s 
    d1 times len_s db 0 ; sir pt nr pare 
    d2 times len_s db 0 ; sir pt nr impare
; our code starts here

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
