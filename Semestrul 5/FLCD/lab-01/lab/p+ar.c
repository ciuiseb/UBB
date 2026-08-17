#include <stdio.h>

int main() {
    double r, perimetru, arie;

    scanf("%lf", &r);

    perimetru = 2 * r * 3.14;
    arie = 3.14 * r * r;

    printf("%.2lf %.2lf\n", perimetru, arie);

    return 0;
}
