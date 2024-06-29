bits 32 

global start        

extern exit              
import exit msvcrt.dll    

segment data use32 class=data
    a dw 1010_0110_0000_1101b
    b dw 1011_0100_0100_0101b
    c dw 0001_0000_1111_1000b
    d dw 0b
    suma dw 0
    ;   0001_0100_0100_1100
    
;TODO
; 31. Se dau cuvintele A, B si C. Sa se formeze cuvantul D ca suma a numerelor reprezentate de:
; biţii de pe poziţiile 1-5 ai lui A
; biţii de pe poziţiile 6-10 ai lui B
; biţii de pe poziţiile 11-15 ai lui C
segment code use32 class=code
    start:
        mov ax, 0
        
        mov ax, word[a]
        and ax, 0000_0000_0001_1110b
        shr ax, 1
        add [suma], ax
        
        mov bx, word[b]
        and bx, 0000_0111_1100_0000b
        
        add ax, bx 
        
        mov bx, word[c]
        and bx, 1111_1000_0000_0000b
        
        add ax, bx
        
        mov [d], ax
        
    
        ; exit(0)
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
