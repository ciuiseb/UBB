bits 32
global start
extern exit
import exit msvcrt.dll
extern printf
import printf msvcrt.dll
extern scanf
import scanf msvcrt.dll

segment data use32 class=data
    format_in db "%d", 0
    format_out db "%d", 10, 0
    _a dd 0
    _b dd 0
    _rezultat dd 0

segment code use32 class=code
    start:
    push _a
    push format_in
    call [scanf]
    add esp, 8
    push _b
    push format_in
    call [scanf]
    add esp, 8
    mov eax, [_a]
    push eax
    mov eax, [_b]
    push eax
    pop ebx
    pop eax
    imul eax, ebx
    push eax
    mov eax, 1
    push eax
    pop ebx
    pop eax
    imul eax, ebx
    push eax
    mov eax, 9
    push eax
    pop ebx
    pop eax
    add eax, ebx
    push eax
    mov eax, 1
    push eax
    pop ebx
    pop eax
    sub eax, ebx
    push eax
    pop eax
    mov [_rezultat], eax
    push dword [_rezultat]
    push format_out
    call [printf]
    add esp, 8
    push dword 0
    call [exit]
