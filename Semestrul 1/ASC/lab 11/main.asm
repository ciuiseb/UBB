bits 32

global start

extern exit, scanf, printf, convert_s_to_b
import exit msvcrt.dll
import printf msvcrt.dll
import scanf msvcrt.dll


section data use32 class data
    format_input db "%s", 0       
    format_output db "%X ", 0  

    input_buffer db 0,0,0,0
section code use32 class code
;TODO
;11.Se citeste de la tastatura un sir de mai multe numere in baza 2. Sa se afiseze aceste numere in baza 16.

start:
    read_input:
        ;citire
        push dword input_buffer
        push dword format_input
        call [scanf]
        add esp, 4*2
        
        ;verificare oprire
        cmp byte[input_buffer], "$" ; 10 ; conditie oprire???!?
        je final
               
        pusha
        
        push dword input_buffer ; conversie din string in numar
        call convert_s_to_b
        
        ;afisare drept hexa   
        push dword eax 
        push dword format_output
        call [printf]
        add esp, 4*2
        
        popa ; revenire la registrii anteriori
        
        jmp read_input
        
    final:
    push dword 0
    call [exit]
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    