#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <mpi.h>
#include <algorithm>
#include "validator.h"

void printBigNum(const std::string &title, const std::vector<int> &vec) {
    std::cout << title;
    if (vec.empty()) {
        std::cout << "0" << std::endl;
        return;
    }
    bool printed = false;
    for (auto it = vec.rbegin(); it != vec.rend(); ++it) {
        if (*it != 0) printed = true;
        if (printed) std::cout << *it;
    }
    if (!printed) std::cout << "0";
    std::cout << std::endl;
}

int main(int argc, char **argv) {
    MPI_Init(&argc, &argv);

    int procceses_count;
    MPI_Comm_size(MPI_COMM_WORLD, &procceses_count);
    int current_id;
    MPI_Comm_rank(MPI_COMM_WORLD, &current_id);

    double start_time, end_time;

    std::vector<int> a;
    std::vector<int> b;
    std::vector<int> sum;
    int max_len = 0;
    int chunck_size = 0;

    if (current_id == 0) {
        std::ifstream file1("common/numar1.txt");
        int length_one;
        file1 >> length_one;

        std::ifstream file2("common/numar2.txt");
        int length_two;
        file2 >> length_two;

        max_len = std::max(length_one, length_two);
        chunck_size = max_len / procceses_count;

        a.resize(max_len, 0);
        b.resize(max_len, 0);
        sum.resize(max_len);

        int padding_one = max_len - length_one;
        for (int i = 0; i < length_one; ++i) {
            file1 >> a[i + padding_one];
        }

        int padding_two = max_len - length_two;
        for (int i = 0; i < length_two; ++i) {
            file2 >> b[i + padding_two];
        }

        file1.close();
        file2.close();
        std::reverse(a.begin(), a.end());
        std::reverse(b.begin(), b.end());

        start_time = MPI_Wtime();
    }

    MPI_Bcast(&chunck_size, 1, MPI_INT, 0, MPI_COMM_WORLD);

    std::vector<int> a_aux(chunck_size);
    std::vector<int> b_aux(chunck_size);
    std::vector<int> sum_aux(chunck_size);

    MPI_Scatter(current_id == 0 ? a.data() : nullptr, chunck_size, MPI_INT,
                a_aux.data(), chunck_size, MPI_INT, 0, MPI_COMM_WORLD);

    MPI_Scatter(current_id == 0 ? b.data() : nullptr, chunck_size, MPI_INT,
                b_aux.data(), chunck_size, MPI_INT, 0, MPI_COMM_WORLD);

    int carry = 0;
    if (current_id > 0) {
        MPI_Recv(&carry, 1, MPI_INT, current_id - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
    }

    for (int i = 0; i < chunck_size; ++i) {
        int s = a_aux[i] + b_aux[i] + carry;
        sum_aux[i] = s % 10;
        carry = s / 10;
    }

    if (current_id < procceses_count - 1) {
        MPI_Send(&carry, 1, MPI_INT, current_id + 1, 0, MPI_COMM_WORLD);
    }
    MPI_Gather(sum_aux.data(), chunck_size, MPI_INT,
               current_id == 0 ? sum.data() : nullptr, chunck_size, MPI_INT, 0, MPI_COMM_WORLD);

    if (current_id == procceses_count - 1) {
        MPI_Send(&carry, 1, MPI_INT, 0, 99, MPI_COMM_WORLD);
    }

    if (current_id == 0) {
        int final_carry = 0;
        MPI_Recv(&final_carry, 1, MPI_INT, procceses_count - 1, 99, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        end_time = MPI_Wtime();

        if (final_carry > 0) {
            sum.push_back(final_carry);
        }
        Validator::validate(sum);
        std::cout << "Time: " << (end_time - start_time) * 1000 << " ms" << std::endl;
    }

    MPI_Finalize();
    return 0;
}