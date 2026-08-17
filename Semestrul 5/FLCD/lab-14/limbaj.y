%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

extern int yylex();
extern int yyparse();
extern FILE* yyin;

void yyerror(const char *s);

char data_buffer[4096] = "";

void buffer_var(char* name) {
    char temp[100];
    sprintf(temp, "    _%s dd 0\n", name);
    strcat(data_buffer, temp);
}
%}

%union {
    int num;
    char* str;
}

%token <num> CONST_INT
%token <str> ID
%token INITUM FINIS NUMERUS LEGE SCRIBE ACCIPE
%token PLUS MINUS MUL DIV
%token EQ NEQ LT LE GT GE
%token LPAREN RPAREN LBRACE RBRACE SEMICOLON

%start program

%%

program : INITUM LBRACE declaratii
        {
            printf("bits 32\n");
            printf("global start\n");

            printf("extern exit\n");
            printf("import exit msvcrt.dll\n");

            printf("extern printf\n");
            printf("import printf msvcrt.dll\n");

            printf("extern scanf\n");
            printf("import scanf msvcrt.dll\n");

            printf("\nsegment data use32 class=data\n");
            printf("    format_in db \"%%d\", 0\n");
            printf("    format_out db \"%%d\", 10, 0\n");
            printf("%s", data_buffer);

            printf("\nsegment code use32 class=code\n");
            printf("    start:\n");
        }
        lista_instr RBRACE FINIS
        {
            printf("    push dword 0\n");
            printf("    call [exit]\n");
        }
        ;

declaratii :
           | declaratii declaratie
           ;

declaratie : NUMERUS ID SEMICOLON
           {
               buffer_var($2);
           }
           ;

lista_instr :
            | lista_instr instr
            ;

instr : atribuire
      | citire
      | afisare
      ;

atribuire : ID ACCIPE expresie SEMICOLON
          {
              printf("    pop eax\n");
              printf("    mov [_%s], eax\n", $1);
          }
          ;

citire : LEGE ID SEMICOLON
       {
           printf("    push _%s\n", $2);
           printf("    push format_in\n");
           printf("    call [scanf]\n");
           printf("    add esp, 8\n");
       }
       ;

afisare : SCRIBE ID SEMICOLON
        {
            printf("    push dword [_%s]\n", $2);
            printf("    push format_out\n");
            printf("    call [printf]\n");
            printf("    add esp, 8\n");
        }
        | SCRIBE CONST_INT SEMICOLON
        {
            printf("    push dword %d\n", $2);
            printf("    push format_out\n");
            printf("    call [printf]\n");
            printf("    add esp, 8\n");
        }
        ;

expresie : termen
         | expresie PLUS termen
         {
             printf("    pop ebx\n");
             printf("    pop eax\n");
             printf("    add eax, ebx\n");
             printf("    push eax\n");
         }
         | expresie MINUS termen
         {
             printf("    pop ebx\n");
             printf("    pop eax\n");
             printf("    sub eax, ebx\n");
             printf("    push eax\n");
         }
         ;

termen : factor
       | termen MUL factor
       {
           printf("    pop ebx\n");
           printf("    pop eax\n");
           printf("    imul eax, ebx\n");
           printf("    push eax\n");
       }
       | termen DIV factor
       {
           printf("    pop ebx\n");
           printf("    pop eax\n");
           printf("    cdq\n");
           printf("    idiv ebx\n");
           printf("    push eax\n");
       }
       ;

factor : ID
       {
           printf("    mov eax, [_%s]\n", $1);
           printf("    push eax\n");
       }
       | CONST_INT
       {
           printf("    mov eax, %d\n", $1);
           printf("    push eax\n");
       }
       | LPAREN expresie RPAREN
       ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Eroare: %s\n", s);
}

int main(int argc, char** argv) {
    if (argc > 1) {
        yyin = fopen(argv[1], "r");
        if (!yyin) return 1;
    }
    yyparse();
    return 0;
}