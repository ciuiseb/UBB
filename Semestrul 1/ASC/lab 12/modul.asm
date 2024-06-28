bits 32
global _convert_s_to_b

segment code public code use32
_convert_s_to_b:
    push ebp
    mov ebp, esp 
    
    mov esi, [esp + 8] ; subsirul curent
    mov ecx, [esp + 12]; lungimea
    mov eax, 0 ; <- rezultat
    mov ebx, 1; <-multiplicator pentru conversie
    
    .convert:
        movzx edx, byte[esi + ecx - 1] ; urm caracter
        sub edx, '0' ;string -> nr
        
        imul edx, ebx ;inmultim cu multiplicator pentru a pune bytul pe poz corecta
        add eax, edx ; adaugam in rez
        
        add ebx, ebx ;ebx *= 2
        loop .convert
        
    mov esp, ebp 
    pop ebp 
    ret 4
  