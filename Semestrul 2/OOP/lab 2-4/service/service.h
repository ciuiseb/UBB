#pragma once

#include "../repo/repo.h"
#include "stdlib.h"
#include <stdbool.h>
#include "../domain/validator.h"

struct BillService;

struct BillService *create_service();

/* Creates a new service with an empty repo;
 * pre: n/a
 * post: n/a
 */
struct Bill **get_all_s(struct BillService *service);
/* returns a list containing all the elements in the service's repo
 * pre: service = BillService*
 * post:n/a
 */

Vector *get_vector_s(struct BillService *service);
/*  Returns the adress of the vector from within repo
 * pre: service = BillService*
 * post:n/a
 */
void add_to_service(struct BillService *service, struct Bill *element);

/* Adds a new element to the service's repo
 * pre: service = BillService*, element = Bill*
 * post: n/a
 */
int find_bill(struct BillService *service, struct Bill *bill);

/* Search if a given bill exists in service, returning the index of it's first apparition
 * pre: service = BillService*, bill = Bill*
 * post: -1 - not found
 *      bill's index - found
 */
void delete_from_service(struct BillService *service, int index);
/*  Deletes a given position from the service's repo, if it exists
 * pre: service = BillService*, bill = Bill**
 * post: n/a
 */

void modify_bill(struct BillService *service, int index, char *attribute, char *modification);
/* Modifies the attribute of a given bill, which is referred to by its index,  to a new one
 * pre: service = BillService*, index = int, attribute = char*(attribute of bill), modification = char*
 * post: bill->attribute = modification
 */
int get_service_len(struct BillService *service);
/* Returns the amount of elements found in service's repo
 * pre: service = BillService*
 * post: n/a
 */
Vector *filter_by(struct BillService *service, char *attribute, char *value);
/*  Filters the current elements by nr_apartament( if it's =), by sum(if it's <) or by type(if it's =), and returns a new list only with those
 * pre: service = BillService*, attribute = char*(attribute of bill), value = char*
 * post: n/a
 */

bool cmp(struct Bill *x, struct Bill *y, char *key, bool reverse);
/*  Checks which one's of x and y key attribute is greater(or lower if reverse = ture)
 * pre: x, y = Bill*, key = sum/ nr_ap, reverse = 1/0
 *  post: n/a
 */

void sort_by(struct BillService *service, char *attribute, char *mode);
/*  Sorts the current service by an attribute, ascending ot descending
 * pre: service = BillService*, attribute = char*(sum/ nr_ap), mode = da/nu
 * post: the service's repo is sorted
 */
void destroy_service(struct BillService *service);
/*  Frees the space occupied by a service and all it's elements
 * pre: service = BillService*
 * post: n/a
 */