bits 32 

global start        

extern exit              
import exit msvcrt.dll    

segment data use32 class=data
    a db 1001_1111b
    b dw 1010_0110_0000_1101b
    c db 0
    ;  1000_0111b = 87h


;TODO
;11.Se dau un octet A si un cuvant B. Sa se obtina un octet C care are
;bitii 0-3 bitii 2-5 ai lui A, 
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
