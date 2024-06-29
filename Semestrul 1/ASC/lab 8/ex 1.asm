bits 32

global start
extern exit, scanf, printf
import exit msvcrt.dll
import scanf msvcrt.dll
import printf msvcrt.dll

segment data use32 class = data 
    n dd 0
    format db "%X"
    msj db "Valorea lui n in baza 10 este %u, interpretat fara semn, si %d cu semn"
segment code use32 class = code 
;TODO
;11.Sa se citeasca de la tastatura un numar in baza 16 si sa se afiseze valoarea acelui numar in baza 10.
;Exemplu: Se citeste: 1D; se afiseaza: 29
    start:
    push dword n
    push dword format 
    call [scanf]
    add esp, 4 * 2
    
    mov eax, [n]
    
    mov ebx, eax 
    push eax 
    push ebx 
    push dword msj 
    call [printf]
    add esp, 4 * 2
    
    push dword 0
    call [exit]
    
    
    
    
    
    
    
    
    
    
    
    
    