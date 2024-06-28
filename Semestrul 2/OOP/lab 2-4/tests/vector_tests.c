#include "vector_tests.h"
#include "assert.h"

void vector_test() {
    // test create_vector
    Vector *test = create_vector();

    assert(test->size == 0);
    assert(test->capacity == 10);

    //test push_back
    struct Bill*bill = create_bill(10, 10, "apa");
    push_back(&test, bill);

    assert(test->size == 1);
    assert(get_sum(test->data[0]) == 10);

    //test resize_vector
    test->capacity = 1;
    struct Bill*other_bill = create_bill(10, 10, "apa");

    push_back(&test, other_bill);


    assert(test->capacity == 2);

    //test pop
    pop(test, -1);
    assert(test->size == 2);

    pop(test, 0);
    assert(test->size == 1);


//    //test destroy_vector
    destroy_vector(test);
}