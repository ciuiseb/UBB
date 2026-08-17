#include <iostream>
#include <fstream>
#include <vector>
#include "mpi.h"

int sumaCifre(int numar) {
    if (numar < 10) return numar;
    return numar % 10 + sumaCifre(numar / 10);
}

int f(int numar, int X) {
    if (sumaCifre(numar) < X) return numar * 2;
    return numar / 2;
}

int main(int argc, char **argv) {
    MPI_Init(&argc, &argv);

    int procceses_count;
    MPI_Comm_size(MPI_COMM_WORLD, &procceses_count);
    int current_id;
    MPI_Comm_rank(MPI_COMM_WORLD, &current_id);

    if (current_id == 0) {
        std::ifstream in("files/input.txt");
        int length;
        in >> length;

        int a[length];
        int totalPare = 0, totalImpare = 0;
        for (int i = 0; i < length; ++i) {
            in >> a[i];
            if (a[i] % 2 == 0) {
                ++totalPare;
            } else {
                ++totalImpare;
            }
        }

        in.close();

        int x = 13;

        // pentru procese pare
        int indexCurentPar = 0;
        int eachPare = totalPare / ((procceses_count - 1) / 2);
        int restPare = totalPare % ((procceses_count - 1) / 2);

        for (int workerId = 2; workerId < procceses_count; workerId += 2) {
            int size = eachPare;
            if (restPare) { size++, restPare--; }

            MPI_Send(&size, 1, MPI_INT, workerId, 0, MPI_COMM_WORLD);

            std::vector<int> toSend;

            for (int i = indexCurentPar; i < length && toSend.size() < size; ++i, indexCurentPar++) {
                if (a[i] % 2 == 0) {
                    toSend.push_back(a[i]);
                }
            }

            MPI_Send(toSend.data(), size, MPI_INT, workerId, 0, MPI_COMM_WORLD);
            printf("%zu,", toSend.size());
        }

        //pentru procese impare
        int indexCurentImpar = 0;
        int eachImpare = totalImpare / ((procceses_count - 1) / 2);
        int restImpare = totalImpare % ((procceses_count - 1) / 2);

        for (int workerId = 1; workerId < procceses_count; workerId += 2) {
            int size = eachImpare;
            if (restImpare) { size++, restImpare--; }

            MPI_Send(&size, 1, MPI_INT, workerId, 1, MPI_COMM_WORLD);

            std::vector<int> toSend;

            for (int i = indexCurentImpar; i < length && toSend.size() < size; ++i, indexCurentImpar++) {
                if (a[i] % 2 == 0) {
                    toSend.push_back(a[i]);
                }
            }

            MPI_Send(toSend.data(), size, MPI_INT, workerId, 2, MPI_COMM_WORLD);
            printf("%zu,", toSend.size());
        }
        std::vector<int> restult;
        int cazA = 0, cazB = 0;
        for (int i = 0; i < procceses_count; ++i) {
            int cazAprimit, cazBprimit, size;
            std::vector<int> numbers;
            MPI_Recv(&cazAprimit, 1, MPI_INT, i, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(&cazBprimit, 1, MPI_INT, i, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(&size, 1, MPI_INT, i, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            MPI_Recv(numbers.data(), size, MPI_INT, i, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

            cazA += cazAprimit;
            cazB += cazBprimit;
            for (int j: numbers) restult.push_back(j);
        }
        printf("Caz A: %d\n Caz B: %d\n", cazA, cazB);
        std::ofstream out("files/result.txt");
        for (int i: restult) {
            out << i << " ";
        }

    } else {
        int xAux;
        MPI_Recv(&xAux, 1, MPI_INT,
                 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        int size;
        MPI_Recv(&size, 1, MPI_INT,
                 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        std::vector<int> numbers(size);
        MPI_Recv(numbers.data(), size, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        printf("Process %d recived:", current_id);
        for (int i: numbers) {
            printf(" %d ", i);
        }

        int cazAloc = 0, cazBloc = 0;
        for (int i = 0; i < size; ++i) {
            int copie = numbers[i];
            numbers[i] = f(numbers[i], xAux);
            if (copie > numbers[i]) {
                cazAloc++;
            } else {
                cazBloc++;
            }
        }
        MPI_Send(&cazAloc, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(&cazBloc, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(&size, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
        MPI_Send(numbers.data(), size, MPI_INT, 0, 0, MPI_COMM_WORLD);
    }
    MPI_Finalize();
    return 0;
}
