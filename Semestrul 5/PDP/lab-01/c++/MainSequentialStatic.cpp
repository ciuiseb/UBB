#include <iostream>
#include <fstream>
#include <chrono>
#include "OutputValidator.cpp"

using namespace std;

const int MAX_IMAGE_ROWS = 10000;
const int MAX_IMAGE_COLS = 10000;
const int MAX_KERNEL_SIZE = 5;

int image[MAX_IMAGE_ROWS][MAX_IMAGE_COLS];
int kernel[MAX_KERNEL_SIZE][MAX_KERNEL_SIZE];
int result[MAX_IMAGE_ROWS][MAX_IMAGE_COLS];

int imageRows, imageCols;
int kernelRows, kernelCols;

void convolve() {
    int resultRows = imageRows;
    int resultCols = imageCols;

    for (int i = 0; i < resultRows; ++i) {
        for (int j = 0; j < resultCols; ++j) {
            int sum = 0;
            for (int ki = 0; ki < kernelRows; ++ki) {
                for (int kj = 0; kj < kernelCols; ++kj) {
                    if ((i + ki) < imageRows && (j + kj) < imageCols)
                        sum += image[i + ki][j + kj] * kernel[ki][kj];
                }
            }
            result[i][j] = sum;
        }
    }
}

int main() {
    ifstream fin(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL III\SEM I\PPD\lab-01\common\data.txt)");

    fin >> imageRows >> imageCols;

    for (int i = 0; i < imageRows; ++i)
        for (int j = 0; j < imageCols; ++j)
            fin >> image[i][j];

    fin >> kernelRows >> kernelCols;

    for (int i = 0; i < kernelRows; ++i)
        for (int j = 0; j < kernelCols; ++j)
            fin >> kernel[i][j];

    fin.close();

    auto start = chrono::high_resolution_clock::now();
    convolve();
    auto end = chrono::high_resolution_clock::now();

    cout << "Time: "
         << chrono::duration_cast<chrono::milliseconds>(end - start).count()
         << " ms" << endl;

    OutputValidator::validate(result, imageRows, imageCols);

    return 0;
}
