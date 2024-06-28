#include "validator.h"


int validate_nr(char* nr){
    if (!is_number(nr) || atoi(nr) < 0) {
        return 1;
    }
    return 0;
}

int validate_sum(char* sum){
    if (!is_number(sum) || atoi(sum) < 0) {
        return 2;
    }
    return 0;
}

int validate_type(char*type){
    char accepted_bills[3][20];

    strcpy(accepted_bills[0], "apa");
    strcpy(accepted_bills[1], "gaz");
    strcpy(accepted_bills[2], "curent");
    bool ok = false;
    for (int i = 0; i < 3; ++i) {
        if (strcmp(type, accepted_bills[i]) == 0) {
            ok = true;
        }
    }
    if(!ok){
        return 3;
    }
    return 0;
}

int validate_nr_sum_type(char*option){
    char accepted_inputs[3][20];

    strcpy(accepted_inputs[0], "nr");
    strcpy(accepted_inputs[1], "suma");
    strcpy(accepted_inputs[2], "tip");
    bool ok = false;
    for (int i = 0; i < 3; ++i) {
        if (strcmp(option, accepted_inputs[i]) == 0) {
            ok = true;
        }
    }
    if(!ok){
        return 4;
    }
    return 0;
}

int validate_nr_sum(char*option){
    char accepted_inputs[2][20] ={ "nr", "suma"};

    bool ok = false;
    for (int i = 0; i < 2; ++i) {
        if (strcmp(option, accepted_inputs[i]) == 0) {
            ok = true;
        }
    }
    if(!ok){
        return 4;
    }
    return 0;
}
int validate_yes_no(char*option){
    char accepted_inputs[2][20];

    strcpy(accepted_inputs[0], "da");
    strcpy(accepted_inputs[1], "nu");
    bool ok = false;
    for (int i = 0; i < 2; ++i) {
        if (strcmp(option, accepted_inputs[i]) == 0) {
            ok = true;
        }
    }
    if(!ok){
        return 5;
    }
    return 0;
}
bool is_number(char*str){
    for(int i = 0;str[i];++i)
        if(!isdigit(str[i]) || str[i] == '-')
            return false;
    return true;
}