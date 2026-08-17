#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <mpi.h>
#include <algorithm>
#include "validator.h"

int main(int argc, char** argv) {
    MPI_Init(&argc, &argv);

    int procceses_count;
    MPI_Comm_size(MPI_COMM_WORLD, &procceses_count);
    int current_id;
    MPI_Comm_rank(MPI_COMM_WORLD, &current_id);

    double start_time, end_time;

    if (current_id == 0) {
        start_time = MPI_Wtime();

        std::ifstream file1("common/numar1.txt");
        std::ifstream file2("common/numar2.txt");

        int length_one, length_two;
        file1 >> length_one;
        file2 >> length_two;

        int max_len = std::max(length_one, length_two);
        int num_workers = procceses_count - 1;

        int each = max_len / num_workers;
        int start = 0;

        std::vector<int> chunk_sizes(num_workers + 1);

        for (int worker_id = 1; worker_id <= num_workers; ++worker_id) {
            int chunk_size = each;
            chunk_sizes[worker_id] = chunk_size;

            std::vector<int> chunk_a(chunk_size);
            std::vector<int> chunk_b(chunk_size);

            int padding_one = max_len - length_one;
            int padding_two = max_len - length_two;

            for (int i = 0; i < chunk_size; ++i) {
                int global_idx = start + i;

                if (global_idx < padding_one) chunk_a[i] = 0;
                else file1 >> chunk_a[i];

                if (global_idx < padding_two) chunk_b[i] = 0;
                else file2 >> chunk_b[i];
            }

            MPI_Send(&chunk_size, 1, MPI_INT, worker_id, 0, MPI_COMM_WORLD);
            MPI_Send(chunk_a.data(), chunk_size, MPI_INT, worker_id, 1, MPI_COMM_WORLD);
            MPI_Send(chunk_b.data(), chunk_size, MPI_INT, worker_id, 2, MPI_COMM_WORLD);

            start += chunk_size;
        }

        file1.close();
        file2.close();

        std::vector<int> global_result;
        global_result.reserve(max_len + 1);

        int final_carry = 0;
        MPI_Recv(&final_carry, 1, MPI_INT, 1, 99, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        if (final_carry > 0) {
            global_result.push_back(final_carry);
        }

        for (int worker_id = 1; worker_id <= num_workers; ++worker_id) {
            int s = chunk_sizes[worker_id];
            std::vector<int> result_chunk(s);

            MPI_Recv(result_chunk.data(), s, MPI_INT, worker_id, 100, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            global_result.insert(global_result.end(), result_chunk.begin(), result_chunk.end());
        }

        end_time = MPI_Wtime();
        std::reverse(global_result.begin(), global_result.end());
        Validator::validate(global_result);

        std::cout << "Time: " << (end_time - start_time) * 1000 << " ms" << std::endl;

    } else {
        int my_size;
        MPI_Recv(&my_size, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        std::vector<int> a(my_size);
        std::vector<int> b(my_size);
        std::vector<int> sum(my_size);

        MPI_Recv(a.data(), my_size, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        MPI_Recv(b.data(), my_size, MPI_INT, 0, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        int carry_in = 0;
        if (current_id < procceses_count - 1) {
            MPI_Recv(&carry_in, 1, MPI_INT, current_id + 1, 10, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        }

        int current_carry = carry_in;
        for (int i = my_size - 1; i >= 0; --i) {
            int val = a[i] + b[i] + current_carry;
            sum[i] = val % 10;
            current_carry = val / 10;
        }
        if (current_id > 1) {
            MPI_Send(&current_carry, 1, MPI_INT, current_id - 1, 10, MPI_COMM_WORLD);
        } else {
            MPI_Send(&current_carry, 1, MPI_INT, 0, 99, MPI_COMM_WORLD);
        }
        MPI_Send(sum.data(), my_size, MPI_INT, 0, 100, MPI_COMM_WORLD);
    }

    MPI_Finalize();
    return 0;
}