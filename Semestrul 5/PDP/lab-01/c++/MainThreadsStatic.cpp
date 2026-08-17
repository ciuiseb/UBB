//
// Created by ciuis on 10/19/2025.
//
#include <iostream>
#include <fstream>
#include <chrono>
#include <thread>
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

void convolvePart(int startRow, int endRow) {
    for (int i = startRow; i < endRow; ++i) {
        for (int j = 0; j < imageCols; ++j) {
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

int main(int argc, char* argv[]) {
    ifstream fin(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL III\SEM I\PPD\lab-01\common\data.txt)");
    if (!fin) {
        cerr << "Error: Could not open input file." << endl;
        return 1;
    }

    fin >> imageRows >> imageCols;
    for (int i = 0; i < imageRows; ++i)
        for (int j = 0; j < imageCols; ++j)
            fin >> image[i][j];

    fin >> kernelRows >> kernelCols;
    for (int i = 0; i < kernelRows; ++i)
        for (int j = 0; j < kernelCols; ++j)
            fin >> kernel[i][j];

    fin.close();

    int p = std::stoi(argv[1]);
    thread threads[17];

    int rowsPerThread = imageRows / p;

    auto start = chrono::high_resolution_clock::now();

    for (int t = 0; t < p; ++t) {
        int startRow = t * rowsPerThread;
        int endRow = (t == p - 1) ? imageRows : startRow + rowsPerThread;
        threads[t] = thread(convolvePart, startRow, endRow);
    }

    for (int t = 0; t < p; ++t)
        threads[t].join();

    auto end = chrono::high_resolution_clock::now();

    cout << "Time: "
         << chrono::duration_cast<chrono::milliseconds>(end - start).count()
         << " ms" << endl;

    OutputValidator::validate(result, imageRows, imageCols);

    return 0;
}
