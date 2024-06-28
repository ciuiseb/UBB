bits 32

global start
extern exit
import exit msvcrt.dll

segment data use32 class=data:
    a dd 11223344h, 55667788h
    len equ ($-a) / 4
    b1 times len dw 0
    b2 times len dw 0
    ;Se da un sir A de dublucuvinte. Construiti doua siruri de octeti  
    ;- B1: contine ca elemente partea superioara a cuvintelor superioare din A
    ;- B2: contine ca elemente partea inferioara a cuvintelor inferioare din A
segment code use32 class=code:
    start:
        mov ecx, len
        jecxz final
        
        mov esi, 0 ; iterator pt A
        mov edi, 0 ; iterator comun pentru B1, B2
    
        repeta:
            ; citire
            mov ax, [a + esi]
            mov dx, [a + esi + 2] 
            ; conform little endian in ax se afla partea inf iar in dx sup
            mov [b1 + edi], dx 
            mov [b2 + edi], ax
        
        add edi, 2
        add esi, 4
        loop repeta
    
    final:
    push dword 0
    call [exit]
    