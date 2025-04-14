/* lisp.y - Analizor sintactic pentru microinterpretorul Lisp */
%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct list {
    int value;
    struct list *next;
} list;

list *cons(int val, list *lst) {
    list *node = (list *)malloc(sizeof(list));
    node->value = val;
    node->next = lst;
    return node;
}

int car(list *lst) { return lst ? lst->value : 0; }
list *cdr(list *lst) { return lst ? lst->next : NULL; }
list *append(list *l1, list *l2) {
    if (!l1) return l2;
    list *head = l1;
    while (l1->next) l1 = l1->next;
    l1->next = l2;
    return head;
}

void print_list(list *lst) {
    printf("(");
    while (lst) {
        printf("%d ", lst->value);
        lst = lst->next;
    }
    printf(")\n");
}
%}

%union {
    int ival;
    list *lst;
}

%token <ival> NUMBER
%token CONS CAR CDR APPEND
%type <lst> form i_form l_form enum

%%
form: i_form  { $$ = $1; print_list($$); }
    | l_form  { $$ = $1; print_list($$); }
    ;

i_form: '(' i_command ')' { $$ = $2; }
      | NUMBER { $$ = cons($1, NULL); }
      ;

l_form: '(' l_command ')' { $$ = $2; }
      | enum ')' { $$ = $1; }
      ;

i_command: CAR l_form { $$ = cons(car($2), NULL); }
         | '+' form i_form { $$ = cons(car($2) + car($3), NULL); }
         ;

l_command: CDR l_form { $$ = cdr($2); }
         | CONS i_form l_form { $$ = cons(car($2), $3); }
         | APPEND l_form l_form { $$ = append($2, $3); }
         ;

enum: NUMBER enum { $$ = cons($1, $2); }
     | NUMBER { $$ = cons($1, NULL); }
     ;

file: file form '\n' | file '\n' | /* empty */ ;
%%

int main() {
    yyparse();
    return 0;
}

void yyerror(const char *msg) {
    fprintf(stderr, "Error: %s\n", msg);
}
