#include <stdbool.h>
#include "service_tests.h"
#include "assert.h"

void service_test() {
    //test create_service and get_service_len
    struct Bill *bill = create_bill(101, 10, "gaz");
    struct BillService *service = create_service();

    assert(get_service_len(service) == 0);
    //test add_to_service
    add_to_service(service, bill);

    assert(get_service_len(service) == 1);
    //test get_vector_s
    assert(get_sum(get_vector_s(service)->data[0]) == 10);

    //test find_bill

    int index = find_bill(service, bill);
    assert(index == 0);

    struct Bill *other_bill = create_bill(101, 111, "gaz");
    index = find_bill(service, other_bill);
    assert(index == -1);

    //test delete_from_service
    delete_from_service(service, 0);
    assert(get_service_len(service) == 0);

    //test modify_bill
    add_to_service(service, bill);

    modify_bill(service, 0, "nr", "2");
    assert(get_ap_number(get_all_s(service)[0]) == 2);

    modify_bill(service, 0, "suma", "2");
    assert(get_sum(get_all_s(service)[0]) == 2);

    modify_bill(service, 0, "tip", "apa");
    assert(strcmp(get_type(get_all_s(service)[0]), "apa") == 0);

    modify_bill(service, 0, "aaaaa", "2");
    assert(get_sum(get_all_s(service)[0]) == 2 &&
           strcmp(get_type(get_all_s(service)[0]), "apa") == 0 &&
           get_ap_number(get_all_s(service)[0]) == 2);

    while (get_service_len(service)) {
        delete_from_service(service, 0);
    }
    //test filter_by
    add_to_service(service, bill);
    add_to_service(service, other_bill);

    Vector *filtered = filter_by(service, "nr", "2");
    assert(get_sum(filtered->data[0]) == 2);

    pop(filtered, 0); // altfel il distruge pe other_bill
    destroy_vector(filtered);
    filtered = filter_by(service, "suma", "100");

    assert(get_ap_number(filtered->data[0]) == 2);

    pop(filtered, 0); // altfel il distruge pe other_bill
    destroy_vector(filtered);
    filtered = filter_by(service, "tip", "gaz");
    assert(get_ap_number(filtered->data[0]) == 101);

    pop(filtered, 0); // altfel il distruge pe other_bill
    destroy_vector(filtered);

    // test cmp

    assert(cmp(bill, other_bill, "nr", false) == false);
    assert(cmp(bill, other_bill, "suma", false) == false);

    assert(cmp(bill, other_bill, "nr", true) == true);
    assert(cmp(bill, other_bill, "suma", true) == true);

    assert(cmp(bill, other_bill, "aaaaa", true) == 0);

    // test sort_by
    sort_by(service, "nr", "nu");
    assert(get_ap_number(get_all_s(service)[0]) == 2);
    sort_by(service, "suma", "nu");
    assert(get_sum(get_all_s(service)[0]) == 2);

    sort_by(service, "nr", "da");
    assert(get_ap_number(get_all_s(service)[0]) == 101);
    sort_by(service, "suma", "da");
    assert(get_sum(get_all_s(service)[0]) == 111);


    //test desctructor
    destroy_service(service);
}