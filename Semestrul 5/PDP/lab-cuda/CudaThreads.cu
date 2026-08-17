#include <iostream>
#include <fstream>
#include <chrono>
#include <vector>
#include <cuda_runtime.h>

using namespace std;

__global__ void convolve_kernel(const int *d_image, const int *d_kernel, int *d_result,
                                int rows, int cols, int kRows, int kCols) {

    int c = blockIdx.x * blockDim.x + threadIdx.x;
    int r = blockIdx.y * blockDim.y + threadIdx.y;

    int kCenterX = kCols / 2;
    int kCenterY = kRows / 2;

    if (r < rows && c < cols) {
        int sum = 0;

        for (int ki = 0; ki < kRows; ++ki) {
            for (int kj = 0; kj < kCols; ++kj) {

                int imgRow = r + (ki - kCenterY);
                int imgCol = c + (kj - kCenterX);

                if (imgRow >= 0 && imgRow < rows && imgCol >= 0 && imgCol < cols) {

                    int imgVal = d_image[imgRow * cols + imgCol];
                    int kernVal = d_kernel[ki * kCols + kj];

                    sum += imgVal * kernVal;
                }
            }
        }
        d_result[r * cols + c] = sum;
    }
}

int main() {
    ifstream fin(R"(..\common\data.txt)");
    if (!fin) {
        cerr << "Error: Could not open input file." << endl;
        return 1;
    }

    int imageRows, imageCols;
    int kernelRows, kernelCols;

    fin >> imageRows >> imageCols;

    size_t imageSize = imageRows * imageCols * sizeof(int);
    int *h_image = (int *) malloc(imageSize);
    int *h_result = (int *) malloc(imageSize);

    for (int i = 0; i < imageRows; ++i) {
        for (int j = 0; j < imageCols; ++j) {
            fin >> h_image[i * imageCols + j];
        }
    }

    fin >> kernelRows >> kernelCols;
    size_t kernelSize = kernelRows * kernelCols * sizeof(int);
    int *h_kernel = (int *) malloc(kernelSize);

    for (int i = 0; i < kernelRows; ++i) {
        for (int j = 0; j < kernelCols; ++j) {
            fin >> h_kernel[i * kernelCols + j];
        }
    }
    fin.close();

    int *d_image, *d_kernel, *d_result;

    cudaMalloc(&d_image, imageSize);
    cudaMalloc(&d_kernel, kernelSize);
    cudaMalloc(&d_result, imageSize);

    cudaMemcpy(d_image, h_image, imageSize, cudaMemcpyHostToDevice);
    cudaMemcpy(d_kernel, h_kernel, kernelSize, cudaMemcpyHostToDevice);

    dim3 threadsPerBlock(16, 16);
    dim3 blocksPerGrid(
            (imageCols + threadsPerBlock.x - 1) / threadsPerBlock.x,
            (imageRows + threadsPerBlock.y - 1) / threadsPerBlock.y
    );

    auto start = chrono::high_resolution_clock::now();

    convolve_kernel<<<blocksPerGrid, threadsPerBlock>>>(d_image, d_kernel, d_result,
                                                        imageRows, imageCols, kernelRows, kernelCols);
    cudaDeviceSynchronize();

    auto end = chrono::high_resolution_clock::now();
    cout << "Time: " << chrono::duration_cast<chrono::milliseconds>(end - start).count() << " ms" << endl;

    cudaMemcpy(h_result, d_result, imageSize, cudaMemcpyDeviceToHost);

    int printRows = (imageRows <= 20) ? imageRows : 10;
    int printCols = (imageCols <= 20) ? imageCols : 10;
    cout << "\n--- Result Matrix (" << ((imageRows <= 20) ? "Full" : "Preview") << ") ---" << endl;
    for (int i = 0; i < printRows; ++i) {
        for (int j = 0; j < printCols; ++j) {
            cout << h_result[i * imageCols + j] << " ";
        }
        cout << endl;
    }

    cudaFree(d_image);
    cudaFree(d_kernel);
    cudaFree(d_result);
    free(h_image);
    free(h_kernel);
    free(h_result);

    return 0;
}