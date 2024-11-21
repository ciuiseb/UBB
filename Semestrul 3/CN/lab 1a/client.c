#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include "unistd.h"

#include "clientMethods.h"

int main() {
    int c;
    struct sockaddr_in server;

    c = socket(AF_INET, SOCK_STREAM, 0);
    if (c < 0) {
        printf("Eroare la crearea socketului client\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = inet_addr("127.0.0.1");

    if (connect(c, (struct sockaddr *) &server, sizeof(server)) < 0) {
        printf("Eroare la conectarea la server\n");
        return 1;
    }

    solveClient(
            chooseExerciseClient(c),
            c
    );

    close(c);
}
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
