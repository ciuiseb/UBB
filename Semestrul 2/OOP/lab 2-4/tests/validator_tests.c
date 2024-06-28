#include "validator_tests.h"

void validator_tests(){
    //test is_number
    assert(is_number("123") == true);
    assert(is_number("a") == false);

    //test validate_nr
    assert(!validate_nr("123"));
    assert(validate_nr("aaa"));
    assert(validate_nr("-1"));

    //test validate_sum
    assert(!validate_sum("123"));
    assert(validate_sum("aaa"));
    assert(validate_sum("-1"));

    //test validate_type
    assert(!validate_type("apa"));
    assert(validate_type("aa"));

    //test validate_nr_suma_tip
    assert(!validate_nr_sum_type("nr"));
    assert(validate_nr_sum_type("aaa"));

    //test validate_nr_suma_tip
    assert(!validate_nr_sum("nr"));
    assert(validate_nr_sum("aaa"));

    //test validate_yes_no
    assert(!validate_yes_no("da"));
    assert(validate_yes_no("poate"));


}