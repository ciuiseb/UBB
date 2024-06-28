bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit, printf, scanf              ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import printf msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
import scanf msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    n dd 10
    msj_1 db "Nr citit este %u unsigned ", 10, 0
    msj_2 db "Nr citit este %X in hexadecimal", 0
    format db "%d", 0
; our code starts here
segment code use32 class=code
    start:
        push dword n 
        push dword format
        call [scanf]
        add esp, 4*2
        
        mov eax, [n]
        mov ebx, eax 
        
        push eax 
        push dword msj_1
        call [printf]
        add esp, 4 * 2
        
        push ebx  
        push dword msj_2
        call [printf]
        add esp, 4 * 2
        
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
