/*
11.Se citeste de la tastatura un sir de mai multe numere in baza 2. Sa se afiseze aceste numere in baza 16.
*/

#include <stdio.h>
#include <string.h>

unsigned int convert_s_to_b(char *sir,int length);

int main()  
{
    char input[50];
    fgets(input, sizeof(input), stdin);

    char *token = strtok(input, " \r\n");

    while (token) {
        int res = convert_s_to_b(token, strlen(token));
        printf("%X ", res);
        token = strtok(NULL, " \r\n");
    }

    return (0);
}