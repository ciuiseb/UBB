%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "bison.tab.h"

char *strdup(const char *s);
%}

%option yylineno
%option noyywrap

DIGIT       [0-9]
LETTER      [a-zA-Z]
ID          {LETTER}({LETTER}|{DIGIT})*
INTEGER     {DIGIT}+
REAL        {DIGIT}+\.{DIGIT}+

%%

[ \t\n\r]+          { /* Ignore whitespace */ }

"Import"            { printf("Import,2\n"); return IMPORT; }
"Initum"            { printf("Initum,3\n"); return INITUM; }
"Finis"             { printf("Finis,4\n"); return FINIS; }
"Numerus"           { printf("Numerus,5\n"); return NUMERUS; }
"Ratio"             { printf("Ratio,6\n"); return RATIO; }
"Structura"         { printf("Structura,7\n"); return STRUCTURA; }
"et"                { printf("et,8\n"); return ET; }
"accipe"            { printf("accipe,10\n"); return ACCIPE; }
"Lege"              { printf("Lege,11\n"); return LEGE; }
"Scribe"            { printf("Scribe,12\n"); return SCRIBE; }
"Dum"               { printf("Dum,13\n"); return DUM; }
"FinemSi"           { printf("FinemSi,31\n"); return FINEMSI; }
"Si"                { printf("Si,14\n"); return SI; }
"Aliter"            { printf("Aliter,15\n"); return ALITER; }

";"                 { printf(";,9\n"); return SEMICOLON; }
"("                 { printf("(,16\n"); return LPAREN; }
")"                 { printf("),17\n"); return RPAREN; }
"{"                 { printf("{,18\n"); return LBRACE; }
"}"                 { printf("},19\n"); return RBRACE; }
"+"                 { printf("+,20\n"); return PLUS; }
"-"                 { printf("-,21\n"); return MINUS; }
"*"                 { printf("*,22\n"); return MULT; }
"/"                 { printf("/,23\n"); return DIV; }
"**"                { printf("**,24\n"); return POW; }
"=="                { printf("==,25\n"); return EQ; }
"!="                { printf("!=,26\n"); return NEQ; }
"<"                 { printf("<,27\n"); return LT; }
"<="                { printf("<=,28\n"); return LTE; }
">"                 { printf(">,29\n"); return GT; }
">="                { printf(">=,30\n"); return GTE; }

{REAL}              {
                        printf("REAL,%s\n", yytext);
                        yylval.floatval = atof(yytext);
                        return CONST_REAL;
                    }

{INTEGER}           {
                        printf("INTEGER,%s\n", yytext);
                        yylval.intval = atoi(yytext);
                        return CONST_INT;
                    }

{ID}                {
                        printf("ID,%s\n", yytext);
                        yylval.strval = strdup(yytext);
                        return ID;
                    }

.                   { fprintf(stderr, "Lexical Error line %d: %s\n", yylineno, yytext); }

%%