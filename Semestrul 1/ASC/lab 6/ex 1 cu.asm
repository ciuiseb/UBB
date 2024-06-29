bits 32

global start
extern exit
import exit msvcrt.dll

segment data use32 class = data
    a dd 11112222h, 33334444h
    len_a equ ($ - a)/4
    b1 times len_a dw 0 ; parte superioara 
    b2 times len_a dw 0 ; parte inferioara
;TODO (folosind operatii pe siruri)
;11.Se da un sir A de dublucuvinte. Construiti doua siruri de octeti  
; - B1: contine ca elemente partea superioara a cuvintelor superioare din A
; - B2: contine ca elemente partea inferioara a cuvintelor inferioare din A
segment code use32 class = code 
    start:
        mov ecx, len_a
        jecxz final
        
        mov esi, a  ;punem in esi sirul sursa
        mov edi, b1 ;punem in edi primul sir destinatie
        cld         ;parcurgem de la stanga la dreapta
        repeta_1:
            push ecx ; salvam ecx 
            
            lodsd   ;eax = [esi + ds]
            shr eax, 16 ;am adus partea superioara peste cea inferioara
            
            stosw ; am pus rezultatul in b1
        loop repeta_1
        
        mov ecx, len_a
        jecxz final
        
        mov esi, a
        mov edi, b2

        repeta_2:
            push ecx ; salvam ecx 
            
            lodsd   ;eax = [esi + ds]
            
            stosw ; am pus wordul inferior in b2
        loop repeta_2
        
        final:
        
        push dword 0
        call [exit]
        
        
        
        
        
        
        
        
        
        
        