%{
#include <stdio.h>
#include <stdlib.h>

extern int yylex();
extern int yylineno;
extern char* yytext;
extern FILE *yyin;

void yyerror(const char *s);
%}


%union {
    int intval;
    double floatval;
    char* strval;
}

%token IMPORT 2
%token INITUM 3
%token FINIS 4
%token NUMERUS 5
%token RATIO 6
%token STRUCTURA 7
%token ET 8
%token SEMICOLON 9
%token ACCIPE 10
%token LEGE 11
%token SCRIBE 12
%token DUM 13
%token SI 14
%token ALITER 15
%token LPAREN 16
%token RPAREN 17
%token LBRACE 18
%token RBRACE 19
%token PLUS 20
%token MINUS 21
%token MULT 22
%token DIV 23
%token POW 24
%token EQ 25
%token NEQ 26
%token LT 27
%token LTE 28
%token GT 29
%token GTE 30
%token FINEMSI 31


%token <strval> ID 32
%token <intval> CONST_INT 33
%token <floatval> CONST_REAL 34

%left PLUS MINUS
%left MULT DIV
%right POW
%nonassoc EQ NEQ LT LTE GT GTE

%%


program:
      IMPORT INITUM LBRACE stmt_list RBRACE FINIS { printf(">> Program Syntactically Correct!\n"); }
     ;

stmt_list:
      stmt
    | stmt_list stmt
    ;


stmt:
      decl_stmt
    | assign_stmt
    | io_stmt
    | if_stmt
    | while_stmt
    ;

decl_stmt:
      type decl_list SEMICOLON
    ;

decl_list:
      decl_item
    | decl_list ET decl_item
    ;

decl_item:
      ID
    | ID ACCIPE expr
    ;

type:
      NUMERUS
    | RATIO
    | STRUCTURA
    ;

assign_stmt:
      ID ACCIPE expr SEMICOLON
    ;

io_stmt:
      LEGE ID SEMICOLON
    | SCRIBE expr SEMICOLON
    ;

if_stmt:
      SI LPAREN condition RPAREN LBRACE stmt_list RBRACE
    | SI LPAREN condition RPAREN LBRACE stmt_list RBRACE FINEMSI
    | SI LPAREN condition RPAREN LBRACE stmt_list RBRACE ALITER LBRACE stmt_list RBRACE
    ;


while_stmt:
      DUM LPAREN condition RPAREN LBRACE stmt_list RBRACE
    ;

condition:
      expr EQ expr
    | expr NEQ expr
    | expr LT expr
    | expr LTE expr
    | expr GT expr
    | expr GTE expr
    ;

expr:
      ID
    | CONST_INT
    | CONST_REAL
    | expr PLUS expr
    | expr MINUS expr
    | expr MULT expr
    | expr DIV expr
    | expr POW expr
    | LPAREN expr RPAREN
    ;

%%

void yyerror(const char *s) {
    fprintf(stderr, "Syntax Error on line %d: %s at token '%s'\n", yylineno, s, yytext);
}

int main(int argc, char **argv) {
    if (argc > 1) {
        yyin = fopen(argv[1], "r");
        if (!yyin) {
            fprintf(stderr, "Error: Could not open file '%s'\n", argv[1]);
            return 1;
        }
    }

    return yyparse();
}