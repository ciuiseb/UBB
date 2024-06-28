#pragma once

#include "bills_class.h"
#include "stdlib.h"
#include "stdio.h"

#define INITIAL_CAPACITY 10

typedef struct {
    int size;
    int capacity;
    struct Bill **data;
} Vector;

Vector*create_vector();
/* Creates and returns a new empty vector of capacity 10
 * pre: n/a
 * post: n/a
 */

void destroy_vector(Vector*v);
/* Frees the space occupied by a given vector and all it's elements
 * pre: v = Vector*
 * post: n/a
 */

void resize_vector(Vector**v);
/* Doubles the capacity of a given vector
 * pre: v = pointer to a Vector*
 * post: n/a
 */

void push_back(Vector**v, struct Bill*element);
/*  Adds 'element' at the end of 'v'
 *  pre: v = pointer to a Vector*
 *  post: n/a
 */

void pop(Vector*v, int index);
/*  Removes 'index'-th element from 'v'
 *  pre: v = pointer to a Vector*, index = int
 *  post: n/a
 */








