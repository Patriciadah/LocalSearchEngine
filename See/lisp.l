/* lisp.l - Analizor lexical pentru microinterpretorul Lisp */
%{
#include "y.tab.h"
%}

%%
\s+                 ; /* Ignoră spațiile albe */
\(                  return '(';
\)                  return ')';
CONS                return CONS;
CAR                 return CAR;
CDR                 return CDR;
APPEND              return APPEND;
[0-9]+              { yylval.ival = atoi(yytext); return NUMBER; }
\'\([0-9 ]+\)      { yylval.sval = strdup(yytext); return LIST; }
.                   ;
%%

int yywrap() { return 1; }

