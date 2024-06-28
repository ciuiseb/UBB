bits 32 ; assembling for the 32 bits architecture

; declare the EntryPoint (a label defining the very first instruction of the program)
global start        

; declare external functions needed by our program
extern exit               ; tell nasm that exit exists even if we won't be defining it
import exit msvcrt.dll    ; exit is a function that ends the calling process. It is defined in msvcrt.dll
                          ; msvcrt.dll contains exit, printf and all the other important C-runtime specific functions

; our data is declared here (the variables needed by our program)
segment data use32 class=data
    a dd 1
    b db 1
    c db 1
    x dq 1

; our code starts here
segment code use32 class=code
    start:
        ;(a+b)/(2-b*b+b/c)-x; a-doubleword; b,c-byte; x-qword
    
        mov cx, [a]     ; cx = a
        mov al, [b]     ; al = b
        cbw             ; ax = b
        
        add cx, ax      ; cx = a + b
        
        imul byte[b]     ; ax = b*b
        
        mov bx, word 2   ; bx = 2
        sub bx, ax      ; bx = 2 - b*b
        
        mov al, [b]     ; al = b
        cbw             ; ax = b
        
        idiv byte[c]     ; al = b / c
        
        cbw             ; ax = b / c 
        
        add bx, ax      ; bx = 2-b*b+b/c
        
        mov ax, cx 
        mov dx, 0       ; dx:ax = a + b
        
        idiv bx          ; ax = (a+b)/(2-b*b+b/c)
        
        cwde            ; eax = (a+b)/(2-b*b+b/c)
        mov edx, 0      ; edx:eax = (a+b)/(2-b*b+b/c)
        
        mov ebx, [x]
        mov ecx, [x + 4] ; ecx:ebx = x 
        
        sub eax, ebx 
        sbb edx, ecx    ; edx:eax = res
        
                    
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
