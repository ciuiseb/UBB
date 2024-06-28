bits 32 

global start        

extern exit, scanf, fprintf, fopen, fclose               
import exit msvcrt.dll
import scanf msvcrt.dll
import fprintf msvcrt.dll
import fopen msvcrt.dll
import fclose msvcrt.dll

segment data use32 class=data
    mod_acces db "w", 0
    nume_fisier  db "cuvinte2.txt", 0
    
    descriptor dd 0
    
    l db 0
    format_input_int db "%d", 0
    
    format_input_string db "%s", 0
    cuvant db 0
    
    sir_caractere db "!@#%-+*&)(", 0 ; 10
    
segment code use32 class=code
    start:
        push dword l
        push dword format_input_int
        call [scanf]
        add esp, 4*2
        
        push dword mod_acces
        push dword nume_fisier
        call [fopen]
        
        mov [descriptor], eax 
        
        cmp eax, 0
        je final

        citeste:
        pushad 
            push dword cuvant
            push dword format_input_string
            call [scanf]
            add esp, 4*2
        popad
            cmp byte[cuvant], "$"
            je final
            
            jmp find_length
            length_found:
            
            cmp eax, [l]
            je check
            
            check_done:
            jmp citeste

        
        final:
        push dword [descriptor]
        call [fclose]
        add esp, 4
        push    dword 0      
        call    [exit]       

        
        
find_length:
mov esi, cuvant
mov eax, 0
len:
    cmp byte[esi], 0
    je length_found
    
    inc eax
    inc esi 
    jmp len       
        
        
check:
    mov esi, cuvant
    ck:
        cmp byte[esi], 0
        je check_done
        
        mov ecx, 10
        mov edi, sir_caractere
        repeta:
            mov dl, byte[edi]
            cmp byte[esi], dl 
            je afisare
            inc edi 
            loop repeta
            
            inc esi
        jmp ck
        jmp check

afisare:
mov byte[cuvant + eax], " "
mov byte[cuvant + eax + 1], 0
pushad 
push dword cuvant
push dword [descriptor]
call [fprintf]
add esp, 4*2
popad

jmp citeste        
        
        
;un! me-e d@i sapt# opt ban@ane a* &mar ba+ (*) $
        
        
        
        
        
        
        
        
        
  