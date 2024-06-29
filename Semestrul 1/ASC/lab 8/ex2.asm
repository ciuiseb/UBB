bits 32

global start

extern exit, scanf, printf, fopen, fprintf, fclose

import exit msvcrt.dll  
import scanf msvcrt.dll  
import printf msvcrt.dll  
import fopen msvcrt.dll  
import fprintf msvcrt.dll
import fclose msvcrt.dll


segment data use32 class = data 
    nume_fisier db "file.txt", 0
    tip_acces db "w", 0
    descriptor dd -1, 0
    
    nr dw 0,0
    format db "%c"
;TODO
;11.Se da un nume de fisier (definit in segmentul de date). 
;Sa se creeze un fisier cu numele dat, 
;apoi sa se citeasca de la tastatura cuvinte si sa se scrie in fisier cuvintele citite pana cand se citeste de la tastatura caracterul '$'.

segment code use32 class = code 
    start:
        push dword tip_acces
        push dword nume_fisier
        call [fopen] ; eax <- descriptor
        add esp, 4 * 2
        
        mov [descriptor], eax
        
        cmp eax, 0
        je final
        push dword nr

        mov ecx, 0 ; ciclu infinit pt ca ne oprim la $
        mov ebx, 0
        
        repeta:
            ;citim numarul
            push dword nr
            push dword format
            call [scanf]
            add esp, 4*2
            
            ;punem nr in registru
            mov bx, [nr]
            ; daca e $ ne oprim
            cmp bx, '$'
            jz continua
            ; altfel il punem in fisier 
            push dword nr
            push dword [descriptor]
            call [fprintf]
            add esp, 4*2
            
        loop repeta
            
        continua:
        push dword [descriptor]
        call [fclose]
        add esp, 4
        
        final:
        push dword 0
        call [exit]
