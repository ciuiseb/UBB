bits 32
global convert_s_to_b

convert_s_to_b:
    mov esi, [esp + 4]
    mov eax, 0 ; <- rezultat
    mov ebx, 1; <-multiplicator pentru conversie
    mov ecx, 0; in el calculam lungimea, care e si numarul de pasi
    
    .find_length:
        cmp byte[esi + ecx], 0
        je .convert
        
        inc ecx 
        jmp .find_length
    
    .convert:
        movzx edx, byte[esi + ecx - 1] ; urm caracter
        sub edx, '0' ;string -> nr
        
        imul edx, ebx ;inmultim cu multiplicator pentru a pune bytul pe poz corecta
        add eax, edx ; adaugam in rez
        
        add ebx, ebx ;ebx *= 2
        loop .convert
    ret 4
    
    
    
    
    
    