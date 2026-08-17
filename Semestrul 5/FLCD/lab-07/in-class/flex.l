%{
    #include "bison.tab.h"
%}

%%

[0-9]+ { return NUMAR; }
"+" { return PLUS; }
"(" { return PARANTEZAST; }
")" { return PARANTEZADR; }
[ \t\r]       { }
\n {return 0;}
. { return EROARE; }

%%

int yywrap() {
    return 1;
}