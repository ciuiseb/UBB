#include "../domain/bills_class.h"
#include "assert.h"
void bill_test(){
    struct Bill*bill = create_bill(101, 10, "gaz");
    assert(strcmp(get_type(bill), "gaz") == 0);
    assert(get_sum(bill) == 10);
    assert(get_ap_number(bill) == 101);

    set_sum(bill, 2);
    assert(get_sum(bill) == 2);

    set_ap_number(bill, 2);
    assert(get_ap_number(bill) == 2);

    set_type(bill, "apa");
    assert(strcmp(get_type(bill), "apa") == 0);

    destroy_bill(bill);
}