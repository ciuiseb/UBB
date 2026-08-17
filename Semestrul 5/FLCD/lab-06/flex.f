%{
#include <stdio.h>
%}

DIGIT          [0-9]
HEX_DIGIT      [0-9a-fA-F]
BIN_DIGIT      [01]
OCT_DIGIT      [0-7]

HEX_LITERAL    0[xX]{HEX_DIGIT}+
BIN_LITERAL    0[bB]{BIN_DIGIT}+
OCT_LITERAL    0{OCT_DIGIT}+
DEC_LITERAL    [1-9]{DIGIT}*
ZERO           0

INTEGER        -?({HEX_LITERAL}|{BIN_LITERAL}|{OCT_LITERAL}|{DEC_LITERAL}|{ZERO})

SIGN           [+-]?
DEC_FLOAT_BASE ({DIGIT}+\.{DIGIT}*|{DIGIT}*\.{DIGIT}+)
EXPONENT       [eE][+-]?{DIGIT}+
HEX_FLOAT_BASE 0[xX]({HEX_DIGIT}+\.{HEX_DIGIT}*|{HEX_DIGIT}*\.{HEX_DIGIT}+)
HEX_EXPONENT   [pP][+-]?{DIGIT}+

REAL_NUMBER    {SIGN}({DEC_FLOAT_BASE}{EXPONENT}?|{HEX_FLOAT_BASE}{HEX_EXPONENT}?)

ID             [a-zA-Z][a-zA-Z0-9]*

VALID_CHAR_ESCAPE    \\[ntr\\']
VALID_CHAR_CONTENT   ([^\\'\n]|{VALID_CHAR_ESCAPE})
CHAR_LITERAL         '{VALID_CHAR_CONTENT}'
CHAR_ERR_EMPTY       \'\'
CHAR_ERR_UNTERM      '{VALID_CHAR_CONTENT}*\n
CHAR_ERR_TOO_LONG    '{VALID_CHAR_CONTENT}{VALID_CHAR_CONTENT}+'
CHAR_ERR_BAD_ESCAPE  '(\\[^ntr\\'\n])'
%%

[ \t\n\r]+            { }
{CHAR_ERR_EMPTY}      { printf("Error: Empty character literal '%s'\n", yytext); }
{CHAR_ERR_UNTERM}     { printf("Error: Unterminated character literal\n"); }
{CHAR_ERR_BAD_ESCAPE} { printf("Error: Invalid escape sequence in char literal '%s'\n", yytext); }
{CHAR_ERR_TOO_LONG}   { printf("Error: Character literal is too long '%s'\n", yytext); }
{CHAR_LITERAL}        { printf("CHARACTER,%s\n", yytext); }

"Import"            { printf("KEYWORD,%s\n", yytext); }
"Initum"            { printf("KEYWORD,%s\n", yytext); }
"Finis"             { printf("KEYWORD,%s\n", yytext); }
"Numerus"           { printf("KEYWORD,%s\n", yytext); }
"Ratio"             { printf("KEYWORD,%s\n", yytext); }
"Structura"         { printf("KEYWORD,%s\n", yytext); }
"et"                { printf("KEYWORD,%s\n", yytext); }
"accipe"            { printf("KEYWORD,%s\n", yytext); }
"Lege"              { printf("KEYWORD,%s\n", yytext); }
"Scribe"            { printf("KEYWORD,%s\n", yytext); }
"Dum"               { printf("KEYWORD,%s\n", yytext); }
"FinemSi"           { printf("KEYWORD,%s\n", yytext); }
"Si"                { printf("KEYWORD,%s\n", yytext); }
"Aliter"            { printf("KEYWORD,%s\n", yytext); }

{REAL_NUMBER}       { printf("REAL,%s\n", yytext); }
{INTEGER}           { printf("INTEGER,%s\n", yytext); }
{ID}                { printf("ID,%s\n", yytext); }

";"                 { printf("PUNCTUATION,%s\n", yytext); }
"("                 { printf("PUNCTUATION,%s\n", yytext); }
")"                 { printf("PUNCTUATION,%s\n", yytext); }
"{"                 { printf("PUNCTUATION,%s\n", yytext); }
"}"                 { printf("PUNCTUATION,%s\n", yytext); }

"+"                 { printf("OPERATOR,%s\n", yytext); }
"-"                 { printf("OPERATOR,%s\n", yytext); }
"*"                 { printf("OPERATOR,%s\n", yytext); }
"/"                 { printf("OPERATOR,%s\n", yytext); }
"**"                { printf("OPERATOR,%s\n", yytext); }
"=="                { printf("OPERATOR,%s\n", yytext); }
"!="                { printf("OPERATOR,%s\n", yytext); }
"<"                 { printf("OPERATOR,%s\n", yytext); }
"<="                { printf("OPERATOR,%s\n", yytext); }
">"                 { printf("OPERATOR,%s\n", yytext); }
">="                { printf("OPERATOR,%s\n", yytext); }

.                   { printf("Error: Unknown character '%s'\n", yytext); }

%%

extern FILE* yyin;

int main(int argc, char **argv) {
    if (argc > 1) {
        yyin = fopen(argv[1], "r");
        if (!yyin) {
            fprintf(stderr, "Error: Could not open file '%s'\n", argv[1]);
            return 1;
        }
    } else {
        fprintf(stderr, "Use as: ./scanner <filename>\n");
        return 1;
    }

    yylex();
    fclose(yyin);
    return 0;
}

int yywrap() {
    return 1;
}