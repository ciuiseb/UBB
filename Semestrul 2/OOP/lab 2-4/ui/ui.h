#pragma once

#include "../service/service.h"
#include <stdio.h>
#include "stdio.h"

void menu();
/*
 * Prints the application's menu
 */
char get_option();
/*
 * Gets a char;
 */

char* get_str(const char* prompt);
/*
 * Prints a message to the user and returns the string given by them
 */

void print_vector(Vector *v);
/*
 * Prints all the elements in a vector
 */

void errors_msg(int error);
/*
 * Prints an error message based of the error's code
 */

