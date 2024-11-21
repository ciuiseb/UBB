//
// Created by ciuis on 10/16/2024.
//

#ifndef LAB_1A_CLIENTMETHODS_H
#define LAB_1A_CLIENTMETHODS_H

#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <string.h>
#include <arpa/inet.h>
#include "unistd.h"

#include "stdint.h"

uint16_t chooseExerciseClient(uint16_t c);

void solveClient(uint16_t option, uint16_t c);

void solveClient_1(uint16_t c);

void solveClient_2(uint16_t c);

void solveClient_3(uint16_t c);

void solveClient_4(uint16_t c);

void solveClient_5(uint16_t c);

void solveClient_6(uint16_t c);

void solveClient_7(uint16_t c);

void solveClient_8(uint16_t c);

void solveClient_9(uint16_t c);

void solveClient_10(uint16_t c);

#endif //LAB_1A_CLIENTMETHODS_H
