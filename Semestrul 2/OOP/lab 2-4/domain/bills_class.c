#include "bills_class.h"

struct Bill {
    int ap_number;
    int sum;
    char *type;
};

struct Bill *create_bill(int ap_number, int sum, const char *type) {
    struct Bill *new_bill = (struct Bill *) malloc(sizeof(struct Bill));


    new_bill->type = (char *) malloc(1 + strlen(type));

    strcpy(new_bill->type, type);

    new_bill->ap_number = ap_number;
    new_bill->sum = sum;


    return new_bill;
}

void set_ap_number(struct Bill *bill, int number) {
    bill->ap_number = number;
}

int get_ap_number(struct Bill *bill) {
    return bill->ap_number;
}

void set_sum(struct Bill *bill, int new_sum) {
    bill->sum = new_sum;
}

int get_sum(struct Bill *bill) {
    return bill->sum;
}

void set_type(struct Bill *bill, char *new_type) {


    char *temp = malloc(strlen(new_type) + 1);


    strcpy(temp, new_type);

    if (bill->type != NULL) {
        free(bill->type);
    }
    bill->type = temp;
}

char *get_type(struct Bill *bill) {
    return (bill->type);
}

void destroy_bill(struct Bill *bill) {
    if (bill != NULL) {
        free(bill->type);
        free(bill);
    }
}
