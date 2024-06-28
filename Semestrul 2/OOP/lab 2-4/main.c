#include <stdio.h>
#include "repo/repo.h"
#include "ui/ui.h"
#include "tests/tests.h"

int main() {
    test_all();
    struct BillService *service = create_service();
    while (1) {
        menu();
        char option = get_option();
        if (option == '1') {
            char *nr = get_str("Introduceti nr: ");
            if (!validate_nr(nr)) {
                char *sum = get_str("Introduceti suma: ");
                if (!validate_sum(sum)) {
                    char *type = get_str("Introduceti tipul: ");
                    if (!validate_type(type)) {
                        struct Bill *bill = create_bill(atoi(nr), atoi(sum), type);
                        add_to_service(service, bill);
                    } else {
                        errors_msg(validate_type(type));
                    }
                } else {
                    errors_msg(validate_sum(sum));
                }
            } else {
                errors_msg(validate_nr(nr));
            }


        } else if (option == '2') {
            char *nr = get_str("Introduceti nr: ");
            if (!validate_nr(nr)) {
                char *sum = get_str("Introduceti suma: ");
                if (!validate_sum(sum)) {
                    char *type = get_str("Introduceti tipul: ");
                    if (!validate_type(type)) {
                        struct Bill *bill = create_bill(atoi(nr), atoi(sum), type);
                        int index = find_bill(service, bill);

                        if (index != -1) {
                            char *att = get_str("Alegeti o proprietate(nr/suma/tip): ");
                            if (!validate_nr_sum_type(att)) {
                                char *change = get_str("Cu ce?: ");
                                modify_bill(service, index, att, change);
                            } else {
                                errors_msg(validate_nr_sum_type(att));
                            }
                        }else{
                            printf("nu exista elementul");
                        }
                    } else {
                        errors_msg(validate_type(type));
                    }
                } else {
                    errors_msg(validate_sum(sum));
                }
            } else {
                errors_msg(validate_nr(nr));
            }
        } else if (option == '3') {
            char *nr = get_str("Introduceti nr: ");
            if (!validate_nr(nr)) {
                char *sum = get_str("Introduceti suma: ");
                if (!validate_sum(sum)) {
                    char *type = get_str("Introduceti tipul: ");
                    if (!validate_type(type)) {
                        struct Bill *bill = create_bill(atoi(nr), atoi(sum), type);
                        int index = find_bill(service, bill);

                        if (index != -1) {
                            delete_from_service(service, index);
                        } else {
                            printf("Nu exista cheltuiala cautata\n");
                        }
                    } else {
                        errors_msg(validate_type(type));
                    }
                } else {
                    errors_msg(validate_sum(sum));
                }
            } else {
                errors_msg(validate_nr(nr));
            }

        } else if (option == '4') {
            char *att = get_str("Alegeti o proprietate(nr/suma/tip): ");
            if (!validate_nr_sum_type(att)) {
                char *value = get_str("Introduceti filtrul: ");
                Vector *res = filter_by(service, att, value);
                print_vector(res);
            } else {
                errors_msg(validate_nr_sum_type(att));
            }

        } else if (option == '5') {
            char *att = get_str("Sortati dupa(nr/suma): ");
            if (!validate_nr_sum(att)) {
                char *mode = get_str("Crescator(da/nu): ");
                if (!validate_yes_no(mode)) {
                    sort_by(service, att, mode);
                    print_vector(get_vector_s(service));
                } else {
                    errors_msg(validate_yes_no(mode));

                }
            } else {
                errors_msg(validate_nr_sum(att));
            }

        } else if (option == '0') {
            break;

        } else {
            printf("Optiune invalida!\n");
        }
    }

    destroy_service(service);
    return 0;

}