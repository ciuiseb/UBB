#include <stdio.h>

int main() {
    int n, suma = 0, x;

    scanf("%d", &n);

    while (n > 0) {
        scanf("%d", &x);
        suma += x;
        n -= 1; // decrement
    }

    printf("%d", suma);

    return 0;
}
