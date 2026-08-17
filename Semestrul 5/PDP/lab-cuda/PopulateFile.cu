#include <iostream>
#include <fstream>
#include <vector>
#include <filesystem>
#include <ctime>
#include <cuda_runtime.h>
#include <curand_kernel.h>

namespace fs = std::filesystem;

__global__ void setup_kernel(curandState *state, unsigned long seed) {
    int id = threadIdx.x + blockIdx.x * blockDim.x;
    curand_init(seed, id, 0, &state[id]);
}

__global__ void generate_kernel(curandState *state, int* result, int size, int limit) {
    int id = threadIdx.x + blockIdx.x * blockDim.x;
    if (id < size) {
        unsigned int x = curand(&state[id]);
        result[id] = (x % limit) + 1;
    }
}

void populate_and_write(std::ofstream& outFile, int rows, int cols, int limit) {
    int size = rows * cols;
    int *d_data;
    curandState *d_state;

    cudaMalloc(&d_data, size * sizeof(int));
    cudaMalloc(&d_state, size * sizeof(curandState));

    int threads = 256;
    int blocks = (size + threads - 1) / threads;

    setup_kernel<<<blocks, threads>>>(d_state, time(NULL));
    cudaDeviceSynchronize();

    generate_kernel<<<blocks, threads>>>(d_state, d_data, size, limit);
    cudaDeviceSynchronize();

    std::vector<int> h_data(size);
    cudaMemcpy(h_data.data(), d_data, size * sizeof(int), cudaMemcpyDeviceToHost);

    outFile << rows << " " << cols << "\n";
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            outFile << h_data[i * cols + j] << " ";
        }
        outFile << "\n";
    }

    cudaFree(d_data);
    cudaFree(d_state);
}

int main() {
    cudaSetDevice(0);

    int n = 5;
    int m = 5;
    int k = 3;

    fs::path projectRoot = fs::current_path();
    for(int i=0; i<6; i++) {
        if (fs::exists(projectRoot / "CMakeLists.txt")) break;
        if(projectRoot.has_parent_path()) projectRoot = projectRoot.parent_path();
    }

    fs::path commonDir = projectRoot / "common";
    if (!fs::exists(commonDir)) fs::create_directories(commonDir);

    std::ofstream dataFile(commonDir / "data.txt");
    if (dataFile.is_open()) {
        populate_and_write(dataFile, n, m, 100);
        populate_and_write(dataFile, k, k, 10);
        dataFile.close();
    }

    std::ofstream outFile(commonDir / "output.txt", std::ios::trunc);
    if(outFile.is_open()) outFile.close();

    cudaDeviceReset();
    return 0;
}