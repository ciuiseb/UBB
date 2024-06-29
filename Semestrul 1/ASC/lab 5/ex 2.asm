bits 32

global start
extern exit
import exit msvcrt.dll

segment data use32 class=data
    s1 db 'a', 'b', 'c', 'd', 'e', 'f'
    len_s1 equ $-s1 
    s2 db '1', '2', '3', '4', '5', '6'
    len_s2 equ $ - s2
    len equ $-s1
    d times len db 1
;TODO
;12. Se dau doua siruri de caractere S1 si S2. 
; Sa se construiasca sirul D prin concatenarea elementelor de pe pozitiile pare din S2 
; cu elementele de pe pozitiile impare din S1.

segment code use32 class=code
    start:
        

        mov ecx, (len_s2/2) ; prima data adaugam elemntele din s2, din 2 in 2
        jecxz Final
        
        mov esi, 1; incepem cu a doua pozitie (indicii sirului pornind de la 1, nu 0)
        mov edi, 0; indicele lui d
        Repeta_1:
            ; citim nr
            mov al, [s2 + esi]
            ; il adaugam in sirul final
            mov [d + edi], al
            
            add esi, 2 ; mergem din 2 in 2
            inc edi 
        loop Repeta_1
        
        ; in cazul in care sunt un nr impar de elemente, nu sunt suficiente len_s1/2 iterari, mai trebuie +1
        mov ecx, len_s1
        and ecx, 1
        add ecx, len_s1/2
        
        jecxz Final
        
        mov esi, 0
        Repeta_2:
            ;citire
            mov al, [s1 + esi]
            ;adaugare in sirul destinatie
            mov [d + edi], al
            
            add esi, 2 ; mergem din 2 in 2
            inc edi
        loop Repeta_2
        
        Final:
        mov al, [d]
        mov al, [d+1]
        mov al, [d+ 4]
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        