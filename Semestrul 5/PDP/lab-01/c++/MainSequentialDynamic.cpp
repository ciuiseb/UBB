//
// Created by ciuis on 10/19/2025.
//
#include <iostream>
#include <fstream>
#include <chrono>
#include <cstdlib>
#include "OutputValidator.cpp"

using namespace std;

int **image;
int **kernel;
int **result;

int imageRows, imageCols;
int kernelRows, kernelCols;

void convolve() {
    for (int i = 0; i < imageRows; ++i) {
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

int main() {
    ifstream fin(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL III\SEM I\PPD\lab-01\common\data.txt)");
    if (!fin) {
        cerr << "Error: Could not open input file." << endl;
        return 1;
    }

    fin >> imageRows >> imageCols;

    image = (int **)malloc(imageRows * sizeof(int *));
    for (int i = 0; i < imageRows; ++i)
        image[i] = (int *)malloc(imageCols * sizeof(int));

    for (int i = 0; i < imageRows; ++i)
        for (int j = 0; j < imageCols; ++j)
            fin >> image[i][j];

    fin >> kernelRows >> kernelCols;

    kernel = (int **)malloc(kernelRows * sizeof(int *));
    for (int i = 0; i < kernelRows; ++i)
        kernel[i] = (int *)malloc(kernelCols * sizeof(int));

    for (int i = 0; i < kernelRows; ++i)
        for (int j = 0; j < kernelCols; ++j)
            fin >> kernel[i][j];

    result = (int **)malloc(imageRows * sizeof(int *));
    for (int i = 0; i < imageRows; ++i)
        result[i] = (int *)malloc(imageCols * sizeof(int));

    fin.close();

    auto start = chrono::high_resolution_clock::now();
    convolve();
    auto end = chrono::high_resolution_clock::now();

    cout << "Time: "
         << chrono::duration_cast<chrono::milliseconds>(end - start).count()
         << " ms" << endl;

    OutputValidator::validate(result, imageRows, imageCols);

    for (int i = 0; i < imageRows; ++i) free(image[i]);
    for (int i = 0; i < kernelRows; ++i) free(kernel[i]);
    for (int i = 0; i < imageRows; ++i) free(result[i]);

    free(image);
    free(kernel);
    free(result);

    return 0;
}
