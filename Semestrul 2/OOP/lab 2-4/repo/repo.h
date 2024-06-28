#pragma once

#include "../domain/bills_class.h"
#include "../domain/my_vector.h"
#include "stdlib.h"

struct BillsRepository;

struct BillsRepository *create_repo();

/* creates a new empty repo with elements of type Bill. Initial capacity 10.
 * pre: n/a
 * post: n/a
 */

struct Bill **get_all(struct BillsRepository *repo);

/* returns a list containing all the elements in the repo
 * pre: repo = BillRepository*
 * post:
 */

Vector * get_vector(struct BillsRepository*repo);
void add_to_repo(struct BillsRepository *repo, struct Bill *element);

/* Adds an element at the back of a given repo
 * pre: repo = BillRepository*, element = Bill*
 * post: n/a
 */
void delete_from_repo(struct BillsRepository *repo, int index);

/*  Deletes a given position from the repo, if it exists
 * pre: repo = BillRepository*, element = Bill*
 * post: a
 */
int get_repo_len(struct BillsRepository *repo);

/* Returns the amount of elements found in repo
 * pre: BillRepository*
 * post: n/a
 */
void destroy_repo(struct BillsRepository *repo);
/* Frees the space occupied by a repo
 * pre: BillRepository*
 * post: n/a
 */