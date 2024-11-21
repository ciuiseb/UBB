//
// Created by ciuis on 10/16/2024.
//
#include "serverMethods.h"

uint16_t getExerciseServer(uint16_t c) {
    uint16_t option;
    recv(c, &option, sizeof(option), MSG_WAITALL);

    return ntohs(option);
}

void solveServer(uint16_t option, uint16_t client) {
    if (option < 1 || option > 10)
        return;
    printf("Acesta rezolva problema nr %hu\n", option);
    switch (option) {
        case 1:
            solveServer_1(client);
            return;
        case 2:
            solveServer_2(client);
            return;
        case 3:
            solveServer_3(client);
            return;
        case 4:
            solveServer_4(client);
            return;
        case 5:
            solveServer_5(client);
            return;
        case 6:
            solveServer_6(client);
            return;
        case 7:
            solveServer_7(client);
            return;
        case 8:
            solveServer_8(client);
            return;
        case 9:
            solveServer_9(client);
            return;
        case 10:
            solveServer_10(client);
            return;
        default:
            return;
    }
}

void solveServer_1(uint16_t c) {
    uint16_t suma = 0, size;
    recv(c, &size, sizeof(size), MSG_WAITALL);
    size = ntohs(size);

    uint16_t numbers[size];
    for (uint16_t i = 0; i < size; ++i) {
        recv(c, &numbers[i], sizeof(numbers[i]), MSG_WAITALL);
        suma += ntohs(numbers[i]);
    }

    suma = htons(suma);
    send(c, &suma, sizeof(suma), 0);
}

void solveServer_2(uint16_t c) {
    char buffer[256];
    recv(c, &buffer, sizeof(buffer), MSG_WAITALL);

    uint16_t numberOfSpaces = 0;

    for (int i = 0; buffer[i]; ++i)
        numberOfSpaces += (buffer[i] == ' ');
    numberOfSpaces = htons(numberOfSpaces);

    send(c, &numberOfSpaces, sizeof(numberOfSpaces), 0);
}

void solveServer_3(uint16_t c) {
    char buffer[256];
    recv(c, &buffer, sizeof(buffer), MSG_WAITALL);

    for (int i = 0; i < strlen(buffer) / 2; ++i) {
        char aux = buffer[i];
        buffer[i] = buffer[strlen(buffer) - i - 1];
        buffer[strlen(buffer) - i - 1] = aux;
    }

    send(c, &buffer, sizeof(buffer), 0);
}

void solveServer_4(uint16_t c) {
    char buffer_1[256], buffer_2[256];
    uint16_t length_1, length_2;

    recv(c, &length_1, sizeof(length_1), MSG_WAITALL);
//    length_1 = ntohs(length_1);
    recv(c, &buffer_1, length_1, MSG_WAITALL);
    buffer_1[length_1] = 0;

    recv(c, &length_2, sizeof(length_2), MSG_WAITALL);
//    length_2 = ntohs(length_2);
    recv(c, &buffer_2, length_2, MSG_WAITALL);
    buffer_2[length_2] = 0;


    char result[length_1 + length_2 + 1];
    int index_1 = 0, index_2 = 0;
    for (; buffer_1[index_1] && buffer_2[index_2];) {
        if (buffer_1[index_1] < buffer_2[index_2]) {
            result[index_1 + index_2] = buffer_1[index_1];
            ++index_1;
        } else {
            result[index_1 + index_2] = buffer_2[index_2];
            ++index_2;
        }
    }
    for (; buffer_1[index_1]; ++index_1) {
        result[index_1 + index_2] = buffer_1[index_1];
    }
    for (; buffer_2[index_2]; ++index_2) {
        result[index_1 + index_2] = buffer_2[index_2];
    }

    send(c, &result, length_1 + length_2, 0);
}

void solveServer_5(uint16_t c) {
    uint16_t number;

    recv(c, &number, sizeof(number), MSG_WAITALL);
    number = ntohs(number);
    uint16_t divizors[number], numberOfDivizors = 0;
    for (int divizor = 1; divizor < number / 2; ++divizor) {
        if (number % divizor == 0) {
            divizors[numberOfDivizors++] = htons(divizor);
            divizors[numberOfDivizors++] = htons(number / divizor);
        }
    }
    numberOfDivizors = htons(numberOfDivizors);

    send(c, &numberOfDivizors, sizeof(numberOfDivizors), 0);
    send(c, &divizors, ntohs(numberOfDivizors) * sizeof(uint16_t), 0);
}

void solveServer_6(uint16_t c) {
    char buffer[256], target;
    uint16_t length;

    recv(c, &length, sizeof(length), MSG_WAITALL);
    length = ntohs(length);
    recv(c, buffer, length, MSG_WAITALL);

    buffer[length] = '\0';
    recv(c, &target, sizeof(target), MSG_WAITALL);

    uint16_t found[length], nrOfApparitions = 0;

    for (int i = 0; i < length; ++i) {
        if (buffer[i] == target) {
            found[nrOfApparitions++] = htons(i);
        }
    }

    uint16_t network_nrOfApparitions = htons(nrOfApparitions);
    send(c, &network_nrOfApparitions, sizeof(network_nrOfApparitions), 0);

    if (nrOfApparitions > 0) {
        send(c, found, ntohs(network_nrOfApparitions) * sizeof(uint16_t), 0);
    }
}

void solveServer_7(uint16_t c) {
    char buffer[256];
    uint16_t len, startingPoint, substringLength;

    recv(c, &len, sizeof(len), MSG_WAITALL);
    len = ntohs(len);
    recv(c, &buffer, len, MSG_WAITALL);
    buffer[len] = '\0';
    recv(c, &startingPoint, sizeof(startingPoint), MSG_WAITALL);
    startingPoint = ntohs(startingPoint);
    recv(c, &substringLength, sizeof(substringLength), MSG_WAITALL);
    substringLength = ntohs(substringLength);

    char result[substringLength];
    strncpy(result, (buffer + startingPoint), substringLength);
    result[substringLength] = '\0';

    send(c, &result, substringLength, 0);
}

void solveServer_8(uint16_t c) {
    uint16_t len1, len2;
    int arr1[256], arr2[256], common[256];
    int commonCount = 0;

    recv(c, &len1, sizeof(len1), MSG_WAITALL);
    len1 = ntohs(len1);
    recv(c, arr1, len1 * sizeof(int), MSG_WAITALL);

    recv(c, &len2, sizeof(len2), MSG_WAITALL);
    len2 = ntohs(len2);
    recv(c, arr2, len2 * sizeof(int), MSG_WAITALL);

    for (int i = 0; i < len1; ++i) {
        for (int j = 0; j < len2; ++j) {
            if (arr1[i] == arr2[j]) {
                common[commonCount++] = arr1[i];
                break;
            }
        }
    }
    uint16_t commonCountNet = htons(commonCount);
    send(c, &commonCountNet, sizeof(commonCountNet), 0);

    if (commonCount > 0) {
        send(c, common, commonCount * sizeof(int), 0);
    }
}

void solveServer_9(uint16_t c) {
    uint16_t len1, len2;
    int arr1[256], arr2[256], result[256];
    int resultCount = 0;

    recv(c, &len1, sizeof(len1), MSG_WAITALL);
    len1 = ntohs(len1);
    recv(c, arr1, len1 * sizeof(int), MSG_WAITALL);

    recv(c, &len2, sizeof(len2), MSG_WAITALL);
    len2 = ntohs(len2);
    recv(c, arr2, len2 * sizeof(int), MSG_WAITALL);

    for (int i = 0; i < len1; ++i) {
        int found = 0;
        for (int j = 0; j < len2; ++j) {
            if (arr1[i] == arr2[j]) {
                found = 1;
                break;
            }
        }
        if (!found) {
            result[resultCount++] = arr1[i];
        }
    }

    uint16_t resultCountNet = htons(resultCount);
    send(c, &resultCountNet, sizeof(resultCountNet), 0);

    if (resultCount > 0) {
        send(c, result, resultCount * sizeof(int), 0);
    }
}

void solveServer_10(uint16_t c) {
    char str1[256], str2[256];
    uint16_t len1, len2, maxCount = 0;
    char maxChar = '\0';

    recv(c, &len1, sizeof(len1), MSG_WAITALL);
    len1 = ntohs(len1);
    recv(c, str1, len1, MSG_WAITALL);
    str1[len1] = '\0';

    recv(c, &len2, sizeof(len2), MSG_WAITALL);
    len2 = ntohs(len2);
    recv(c, str2, len2, MSG_WAITALL);
    str2[len2] = '\0';

    for (int i = 0; i < len1 && i < len2; i++) {
        if (str1[i] == str2[i]) {
            int count = 1;
            for (int j = i + 1; j < len1 && j < len2; j++) {
                if (str1[j] == str2[j] && str1[j] == str1[i]) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                maxChar = str1[i];
            }
        }
    }

    send(c, &maxChar, sizeof(maxChar), 0);
    uint16_t maxCountNet = htons(maxCount);
    send(c, &maxCountNet, sizeof(maxCountNet), 0);
}