bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a db 1001_1111b
    b dw 1010_0110_0000_1101b
    c db 0
    ;  1000_0111b = 87h

; our code starts here

;11.bitii 0-3 bitii 2-5 ai lui A, 
;bitii 4-7 bitii 6-9 ai lui B.
segment code use32 class=code
    start:
        mov bx, 0
        mov al, [a]
        
        and al, 0011_1100b  ; am izolat bitii 2-5 a lui A
        
        mov cl, 2
        shr al, cl ; i-am adus pe pozitiile corespunzatoare
        or bl, al  ; i-am pus in rezultat
        
        
        mov ax, [b]
        and ax, 0000_0011_1100_0000b ; am izolat bitii 6-9 a lui B
        
        mov cl, 2
        shr ax, cl ; i-am adus pe pozitiile corespunzatoare 
        
        or bl, al ; i-am pus in rez 
        
        mov [c], bl 
        
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
