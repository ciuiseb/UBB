bits 32 

global start        

extern exit               
import exit msvcrt.dll    

segment data use32 class=data
    a db 1
    b db 1
    c db 1
    d dw 2

;TODO
;[(d/2)*(c+b)-a*a]/b
segment code use32 class=code
    start:
        mov ax, [d]
        mov bl, 2
        div bl ; ax = d / 2
        
        mov bl, [c]
        add bl, [b]
        mov bh, 0 ; bx = b + c
        
        mul bl ; ax = (d/2)*(c+b)
        
        push ax
        pop cx ; cx = (d/2)*(c+b)
        
        mov al, [a]
        mul byte [a] ; ax = a * a
        
        push ax
        pop bx  ; bx = a * a
        
        push cx
        pop ax  ; ax = (d/2)*(c+b)
        
        sub ax, bx ; ax = (d/2)*(c+b)-a*a
        
        div byte [b]
        
        
        
        
        
        
        ; mov eax, 0
        ; mov ax, [d]
        ; mov dx, 0
        ; mov bx, 2
        ; div bx  ;ax
        ; mov bx, 0
        
        ; mov bl, [c]
        ; mov bh, 0
        ; mov cl, [b]
        ; mov ch, 0
        ; add bx, cx
        ; mov cx, 0
        ; mul bx
        ; mov bx, 0
                ; ; eax <- (d/2)*(c+b) 
        ; push eax
        ; pop ecx
        
        ; mov ax, [a]
        ; mov ah, 0
        ; mul ax
        ; sub ecx, eax
        
      ; ;  push eax
       ; ; pop ebx ; eax -> ebx
        
        ; push ecx
        ; pop dx
        ; pop ax ; ecx -> dx:ax
        
        ; mov bx, [b]
        ; mov bh, 0
        ; div bx
        
        
        
        
        
        push    dword 0      ; push the parameter for exit onto the stack
        call    [exit]       ; call exit to terminate the program
