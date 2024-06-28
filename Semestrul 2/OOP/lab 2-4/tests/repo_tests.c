#include "repo_tests.h"
#include "assert.h"

void repo_test(){
    //test get_repo_len and create_repo
    struct Bill*bill = create_bill(101, 10, "gaz");
    struct BillsRepository* repo = create_repo();

    assert(get_repo_len(repo) == 0);

    //test add_to_repo

    add_to_repo(repo, bill);

    assert(get_repo_len(repo) == 1);
    //test get_all
    assert(get_sum(get_all(repo)[0]) == 10);
    //test get_vector
    assert(get_sum(get_vector(repo)->data[0]) == 10);
    //test delete_from_repo
    delete_from_repo(repo, 0);
    assert(get_repo_len(repo) == 0);

    //test destroy_repo
    destroy_bill(bill);
    destroy_repo(repo);
}