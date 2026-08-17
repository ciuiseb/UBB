%{
#include <stdio.h>
#include <string.h>
#include "limbaj.tab.h"
%}

%option noyywrap

%%

"Initum"    { return INITUM; }
"Finis"     { return FINIS; }
"Numerus"   { return NUMERUS; }
"Lege"      { return LEGE; }
"Scribe"    { return SCRIBE; }
"accipe"    { return ACCIPE; }

"+"         { return PLUS; }
"-"         { return MINUS; }
"*"         { return MUL; }
"/"         { return DIV; }

"=="        { return EQ; }
"!="        { return NEQ; }
"<="        { return LE; }
">="        { return GE; }
"<"         { return LT; }
">"         { return GT; }

"("         { return LPAREN; }
")"         { return RPAREN; }
"{"         { return LBRACE; }
"}"         { return RBRACE; }
";"         { return SEMICOLON; }

[0-9]+      {
              yylval.num = atoi(yytext);
              return CONST_INT;
            }

[a-zA-Z][a-zA-Z0-9]* {
              yylval.str = strdup(yytext);
              return ID;
            }

[ \t\n\r]+    { /*  */ }

.           { printf("Eroare lexicala: %s\n", yytext); }

%%