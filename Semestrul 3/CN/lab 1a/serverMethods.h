//
// Created by ciuis on 10/16/2024.
//

#ifndef LAB_1A_SERVERMETHODS_H
#define LAB_1A_SERVERMETHODS_H

#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include "unistd.h"

#include "stdint.h"

uint16_t getExerciseServer(uint16_t c);

void solveServer(uint16_t option, uint16_t c);

void solveServer_1(uint16_t c);

void solveServer_2(uint16_t c);

void solveServer_3(uint16_t c);

void solveServer_4(uint16_t c);

void solveServer_5(uint16_t c);

void solveServer_6(uint16_t c);

void solveServer_7(uint16_t c);

void solveServer_8(uint16_t c);

void solveServer_9(uint16_t c);

void solveServer_10(uint16_t c);

#endif //LAB_1A_SERVERMETHODS_H
