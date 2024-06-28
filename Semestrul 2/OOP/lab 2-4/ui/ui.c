//#pragma warning(disable : 4996)

#include "ui.h"

#define MAX_LENGTH 100

void menu() {
    printf("1. Adauga cheltuieli pentru apartament\n");
    printf("2. Modificarea unei cheltuieli existente (tipul, suma)\n");
    printf("3. Stergere cheltuiala\n");
    printf("4. Vizualizare lista de cheltuieli filtrat dupa o proprietate (suma, tipul,apartament)\n");
    printf("5. Vizualizare lista de cheltuieli ordonat dupa suma sau tip (crescator/descrescator)\n");
    printf("0. Exit\n");
}

char get_option() {
    char option;
    scanf("%c", &option);
    while (getchar() != '\n');
    return option;
}

char *get_str(const char *prompt) {
    printf("%s\n", prompt);
    char *input = malloc(MAX_LENGTH * sizeof(char));


    if (fgets(input, MAX_LENGTH, stdin) == NULL) {
        fprintf(stderr, "Error reading input\n");
        free(input);
        return NULL;
    }

    size_t length = strlen(input);
    if (input[length - 1] == '\n') {
        input[length - 1] = '\0';
    }

    return input;

}

void print_vector(Vector *v) {
    for (int i = 0; i < v->size; ++i) {
        printf("Nr ap:%d   Suma:%d   Tip:%s\n", get_ap_number(v->data[i]), get_sum(v->data[i]), get_type(v->data[i]));
    }
}

void errors_msg(int error) {
    if (error == 1) {
        printf("Numar de apartament invalid!\n");
    }
    if (error == 2) {
        printf("Suma invalida!\n");
    }

    if (error == 3) {
        printf("Tip invalid!\n");
    }
    if (error == 4) {
        printf("Atribut invalid!\n");
    }
    if (error == 5) {
        printf("Mod invalid!\n");
    }
}