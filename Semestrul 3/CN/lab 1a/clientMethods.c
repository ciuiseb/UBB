//
// Created by ciuis on 10/16/2024.
//
#include "clientMethods.h"

uint16_t chooseExerciseClient(uint16_t c) {
    uint16_t option;
    printf("What exercise do you want to solve? ");
    scanf("%hu", &option);
    getchar();

    option = htons(option);
    send(c, &option, sizeof(option), 0);

    return ntohs(option);
}

void solveClient(uint16_t option, uint16_t c) {
    switch (option) {
        case 1:
            solveClient_1(c);
            break;
        case 2:
            solveClient_2(c);
            break;
        case 3:
            solveClient_3(c);
            break;
        case 4:
            solveClient_4(c);
            break;
        case 5:
            solveClient_5(c);
            break;
        case 6:
            solveClient_6(c);
            break;
        case 7:
            solveClient_7(c);
            break;
        case 8:
            solveClient_8(c);
            break;
        case 9:
            solveClient_9(c);
            break;
        case 10:
            solveClient_10(c);
            break;
        default:
            break;
    }
}

void solveClient_1(uint16_t c) {
    uint16_t size, suma;
    printf("size = ");
    scanf("%hu", &size);
    getchar();

    if (size == 0 || size > 256) {
        printf("Lungime invalida. Trebuie sa fie intre 1 si 256.\n");
        return;
    }

    uint16_t numbers[size];
    for (uint16_t i = 0; i < size; ++i) {
        printf("numbers[%hu] = ", i);
        scanf("%hu", &numbers[i]);
        getchar();
        numbers[i] = htons(numbers[i]);
    }

    size = htons(size);
    send(c, &size, sizeof(size), 0);
    send(c, &numbers, sizeof(uint16_t) * ntohs(size), 0);

    recv(c, &suma, sizeof(suma), 0);
    suma = ntohs(suma);
    printf("Suma este %hu\n", suma);
}

void solveClient_2(uint16_t c) {
    char buffer[256];
    printf("Sir de caractere: ");
    fgets(buffer, sizeof(buffer), stdin);

    send(c, &buffer, sizeof(buffer), 0);

    uint16_t numberOfSpaces;
    recv(c, &numberOfSpaces, sizeof(numberOfSpaces), 0);
    numberOfSpaces = ntohs(numberOfSpaces);
    printf("Sirul avea %hu spatii\n", numberOfSpaces);
}

void solveClient_3(uint16_t c) {
    char buffer[256];
    printf("Sir de caractere: ");
    fgets(buffer, sizeof(buffer), stdin);

    send(c, &buffer, sizeof(buffer), 0);

    recv(c, &buffer, sizeof(buffer), MSG_WAITALL);
    printf("Sirul onglidit este %s\n", buffer);
}

void solveClient_4(uint16_t c) {
    char buffer_1[256], buffer_2[256];
    printf("Primul sir: ");
    fgets(buffer_1, sizeof(buffer_1), stdin);

    printf("Al doilea sir: ");
    fgets(buffer_2, sizeof(buffer_2), stdin);

    uint16_t length_1 = strlen(buffer_1), length_2 = strlen(buffer_2);

    send(c, &length_1, sizeof(length_1), 0);
    send(c, &buffer_1, length_1, 0);

    send(c, &length_2, sizeof(length_2), 0);
    send(c, &buffer_2, length_2, 0);

    char result[length_1 + length_2 + 1];
    recv(c, &result, length_1 + length_2, MSG_WAITALL);
    result[length_1 + length_2] = '\0';
    printf("Sirurile interclasate: %s", result);
}

void solveClient_5(uint16_t c) {
    uint16_t number;
    printf("Numarul este: ");
    scanf("%hu", &number);
    getchar();
    number = htons(number);

    send(c, &number, sizeof(number), 0);

    uint16_t numberOfDivizors;
    recv(c, &numberOfDivizors, sizeof(numberOfDivizors), MSG_WAITALL);
    numberOfDivizors = ntohs(numberOfDivizors);

    uint16_t divizors[numberOfDivizors];
    recv(c, &divizors, numberOfDivizors * sizeof(uint16_t), MSG_WAITALL);

    printf("Divizorii sunt: ");
    for (int i = 0; i < numberOfDivizors; ++i) {
        printf("%hu, ", ntohs(divizors[i]));
    }
    printf("\n");
}

void solveClient_6(uint16_t c) {
    char buffer[256], target;
    printf("Sirul: ");
    fgets(buffer, sizeof(buffer), stdin);

    buffer[strcspn(buffer, "\n")] = 0;

    printf("Caracterul cautat: ");
    scanf(" %c", &target);

    uint16_t length = strlen(buffer);
    uint16_t network_length = htons(length);

    if (send(c, &network_length, sizeof(network_length), 0) < 0) {
        perror("send length error");
        return;
    }

    if (send(c, buffer, length, 0) < 0) {
        perror("send buffer error");
        return;
    }

    if (send(c, &target, sizeof(target), 0) < 0) {
        perror("send target error");
        return;
    }

    uint16_t nrOfApparitions;
    recv(c, &nrOfApparitions, sizeof(nrOfApparitions), MSG_WAITALL);
    nrOfApparitions = ntohs(nrOfApparitions);

    uint16_t found[nrOfApparitions];

    if (recv(c, found, nrOfApparitions * sizeof(uint16_t), MSG_WAITALL) < 0) {
        perror("recv found error");
        return;
    }

    printf("Pozitiile pe care apare caracterul cautat in sir sunt: ");
    for (int i = 0; i < nrOfApparitions; ++i) {
        printf("%hu, ", ntohs(found[i]));
    }
    printf("\n");
}

void solveClient_7(uint16_t c) {
    char buffer[256], target;
    printf("Sirul: ");
    fgets(buffer, sizeof(buffer), stdin);

    uint16_t startingPosition, substringLength;

    printf("Pozitia initiala: ");
    scanf("%hu", &startingPosition);
    startingPosition = htons(startingPosition);

    printf("Lungimea dorita: ");
    scanf("%hu", &substringLength);
    substringLength = htons(substringLength);

    uint16_t length = strlen(buffer);
    length = htons(length);

    send(c, &length, sizeof(length), 0);
    send(c, buffer, ntohs(length), 0);
    send(c, &startingPosition, sizeof(startingPosition), 0);
    send(c, &substringLength, sizeof(substringLength), 0);

    substringLength = ntohs(substringLength);
    char result[substringLength];
    recv(c, &result, substringLength + 1, MSG_WAITALL);
    printf("Sirul cerut este: %s \n", result);
}

void solveClient_8(uint16_t c) {
    int arr1[256], arr2[256], common[256];
    uint16_t len1, len2;

    printf("Introduceti lungimea primului sir: ");
    scanf("%hu", &len1);
    printf("Introduceti elementele primului sir: ");
    for (int i = 0; i < len1; i++) {
        scanf("%d", &arr1[i]);
    }

    printf("Introduceti lungimea celui de-al doilea sir: ");
    scanf("%hu", &len2);
    printf("Introduceti elementele celui de-al doilea sir: ");
    for (int i = 0; i < len2; i++) {
        scanf("%d", &arr2[i]);
    }

    uint16_t len1Net = htons(len1);
    send(c, &len1Net, sizeof(len1Net), 0);
    send(c, arr1, len1 * sizeof(int), 0);

    uint16_t len2Net = htons(len2);
    send(c, &len2Net, sizeof(len2Net), 0);
    send(c, arr2, len2 * sizeof(int), 0);

    uint16_t commonCountNet;
    recv(c, &commonCountNet, sizeof(commonCountNet), MSG_WAITALL);
    int commonCount = ntohs(commonCountNet);

    if (commonCount > 0) {
        recv(c, common, commonCount * sizeof(int), MSG_WAITALL);
        printf("Numerele comune sunt: ");
        for (int i = 0; i < commonCount; i++) {
            printf("%d ", common[i]);
        }
        printf("\n");
    } else {
        printf("Nu exista numere comune.\n");
    }
}

void solveClient_9(uint16_t c) {
    int arr1[256], arr2[256], result[256];
    uint16_t len1, len2;

    printf("Introduceti lungimea primului sir: ");
    scanf("%hu", &len1);
    printf("Introduceti elementele primului sir: ");
    for (int i = 0; i < len1; i++) {
        scanf("%d", &arr1[i]);
    }

    printf("Introduceti lungimea celui de-al doilea sir: ");
    scanf("%hu", &len2);
    printf("Introduceti elementele celui de-al doilea sir: ");
    for (int i = 0; i < len2; i++) {
        scanf("%d", &arr2[i]);
    }

    uint16_t len1Net = htons(len1);
    send(c, &len1Net, sizeof(len1Net), 0);
    send(c, arr1, len1 * sizeof(int), 0);

    uint16_t len2Net = htons(len2);
    send(c, &len2Net, sizeof(len2Net), 0);
    send(c, arr2, len2 * sizeof(int), 0);

    uint16_t resultCountNet;
    recv(c, &resultCountNet, sizeof(resultCountNet), MSG_WAITALL);
    int resultCount = ntohs(resultCountNet);

    if (resultCount > 0) {
        recv(c, result, resultCount * sizeof(int), MSG_WAITALL);
        printf("Numerele care sunt in primul sir dar nu in al doilea sunt: ");
        for (int i = 0; i < resultCount; i++) {
            printf("%d ", result[i]);
        }
        printf("\n");
    } else {
        printf("Nu exista numere unice in primul sir.\n");
    }

}

void solveClient_10(uint16_t c) {
    char str1[256], str2[256], maxChar;
    uint16_t len1, len2, maxCount;

    printf("Introduceti primul sir de caractere: ");
    fgets(str1, sizeof(str1), stdin);
    str1[strcspn(str1, "\n")] = 0; // Remove newline character
    len1 = strlen(str1);

    printf("Introduceti al doilea sir de caractere: ");
    fgets(str2, sizeof(str2), stdin);
    str2[strcspn(str2, "\n")] = 0;
    len2 = strlen(str2);

    uint16_t len1Net = htons(len1);
    send(c, &len1Net, sizeof(len1Net), 0);
    send(c, str1, len1, 0);

    uint16_t len2Net = htons(len2);
    send(c, &len2Net, sizeof(len2Net), 0);
    send(c, str2, len2, 0);

    recv(c, &maxChar, sizeof(maxChar), MSG_WAITALL);
    recv(c, &maxCount, sizeof(maxCount), MSG_WAITALL);
    maxCount = ntohs(maxCount);

    printf("Caracterul cel mai comun pe pozitii identice este '%c' cu %hu aparitii.\n", maxChar, maxCount);
}