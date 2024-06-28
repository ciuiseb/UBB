#pragma once
#include <stdlib.h>
#include "string.h"

struct Bill;
/*
 * Attributes: ap_number(int), sum(int), type(char*)
 */
struct Bill* create_bill(int ap_number, int sum, const char *type);
/*  Creates and returns a new bill from the given attributes
 *  pre: ap_number = int, sum = int, type = char*
 *  post: n/a
 */
void set_ap_number(struct Bill *bill, int new_number);

/*  sets a new ap number to a given bill
 *  pre: bill = Struct Bill*, new_number = int
 *  post: n/a
 */

int get_ap_number(struct Bill *bill);
/*  Gets the ap number of a given bill
 * pre: bill = Struct Bill*
 * post: n/a
 */
void destroy_bill(struct Bill *bill);

/*  Frees the space occupied by a given bill
 *  pre: bill = struct Bill*
 *  post: n/a
 */

void set_sum(struct Bill *bill, int new_sum);
/*  Sets a new sum to a given bill
 * pre: bill = Struct Bill*, new_sum = int
 * post: n/a
 */
int get_sum(struct Bill *bill);
/*  Gets the sum of a given bill
 * pre: bill = Struct Bill*
 * post: n/a
 */

void set_type(struct Bill *bill, char *new_type);
/*  Sets a new type of a given bill
 * pre: bill = Struct Bill*, new_type = char*
 * post: n/a
 */
char *get_type(struct Bill *bill);
/*  Gets the type of a given bill
 * pre: bill = Struct Bill*
 * post: n/a
 */
