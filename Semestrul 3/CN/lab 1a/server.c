#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <stdint.h>
#include <unistd.h>

#include "serverMethods.h"
int main() {
    int s;
    struct sockaddr_in server, client;
    int c, l;

    s = socket(AF_INET, SOCK_STREAM, 0);
    if (s < 0) {
        printf("Eroare la crearea socketului server\n");
        return 1;
    }

    memset(&server, 0, sizeof(server));
    server.sin_port = htons(1234);
    server.sin_family = AF_INET;
    server.sin_addr.s_addr = INADDR_ANY;

    if (bind(s, (struct sockaddr *) &server, sizeof(server)) < 0) {
        printf("Eroare la bind\n");
        return 1;
    }

    listen(s, 5);
    printf("Server is listening on port 1234...\n");

    l = sizeof(client);
    memset(&client, 0, sizeof(client));

    while (1) {
        c = accept(s, (struct sockaddr *) &client, (socklen_t * ) & l);
        if (c < 0) {
            printf("Eroare la acceptarea conexiunii\n");
            continue;
        }
        printf("S-a conectat un client.\n");

        solveServer(
                getExerciseServer(c),
                c
        );
        close(c);
    }

    close(s);
    return 0;
}

