%{
    #include <stdio.h>
    #include <stdlib.h>

    extern int yylex();
    void yyerror(const char *s);
%}

%token NUMAR PLUS PARANTEZAST PARANTEZADR EROARE

%%

start:
    expr {
        printf("se închid corect\n");
        return 0;
    }
    ;

expr:
    | expr element;

element:
      NUMAR
    | PLUS
    | PARANTEZAST expr PARANTEZADR
    ;

%%

void yyerror(const char *s) {
    printf("NU se închid corect\n");
    exit(0);
}

int main() {
    yyparse();
    return 0;
}