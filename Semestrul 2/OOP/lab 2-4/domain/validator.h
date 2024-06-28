#include "bills_class.h"
#include "stdlib.h"
#include "stdbool.h"
#include "ctype.h"

int validate_nr(char *nr);

/*  Validates if the argument is a positive integer
 *  pre: nr = int
 *  post: 0 - valid
 *        1 - invalid
 */

int validate_sum(char *sum);

/*  Validates if the argument is a positive integer
 *  pre: nr = int
 *  post: 0 - valid
 *        2 - invalid
 */

int validate_type(char *type);

/*  Validates if the argument is a bill from {apa, gaz, curent}
 *  pre: nr = int
 *  post: 0 - valid
 *        3 - invalid
 */

int validate_nr_sum_type(char *option);

/*  Validates if the argument is either a sum or ap_number attribute
 *  pre: nr = int
 *  post: 0 - valid
 *        4 - invalid
 */

int validate_nr_sum(char *option);

/*  Validates if the argument is either a bill attribute
 *  pre: nr = int
 *  post: 0 - valid
 *        4 - invalid
 */

int validate_yes_no(char *option);
/*  Validates if the argument is either "da" or "nu"
 *  pre: nr = int
 *  post: 0 - valid
 *        5 - invalid
 */

bool is_number(char *str);
/*  Verifies if the given string can be converted to a number
 * pre: str = char
 * post: true - it can
 *       false - it can't
 */