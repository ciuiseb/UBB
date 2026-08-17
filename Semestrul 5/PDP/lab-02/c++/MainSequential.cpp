#include <iostream>
#include <fstream>
#include <chrono>
#include <cstdlib>
#include "cstring"
#include "OutputValidator.cpp"

using namespace std;

int **image;
int **kernel;

int imageRows, imageCols;
int kernelRows, kernelCols;

void convolve() {
    int kernelSize = kernelRows;
    int halfSize = kernelSize / 2;

    int* prevRowCopy = nullptr;

    for (int i = 0; i < imageRows; i++) {

        int* currentRowCopy = (int*)malloc(imageCols * sizeof(int));
        if (currentRowCopy == nullptr) {
            fprintf(stderr, "Error: Failed to allocate memory for currentRowCopy.\n");
            if (prevRowCopy != nullptr) {
                free(prevRowCopy);
            }
            return;
        }
        memcpy(currentRowCopy, image[i], imageCols * sizeof(int));

        for (int j = 0; j < imageCols; j++) {
            int sum = 0;

            // randul de de-a supra - lucram cu copia
            if (prevRowCopy != nullptr) {
                for (int kj = 0; kj < kernelSize; kj++) {
                    int readCol = j + kj - halfSize;
                    if (readCol >= 0 && readCol < imageCols) {
                        sum += prevRowCopy[readCol] * kernel[0][kj];
                    }
                }
            }

            // randul curent - idem
            for (int kj = 0; kj < kernelSize; kj++) {
                int readCol = j + kj - halfSize;
                if (readCol >= 0 && readCol < imageCols) {
                    sum += currentRowCopy[readCol] * kernel[1][kj];
                }
            }

            // ki = 2 - e nemodificat
            if (i + 1 < imageRows) {
                for (int kj = 0; kj < kernelSize; kj++) {
                    int readCol = j + kj - halfSize;
                    if (readCol >= 0 && readCol < imageCols) {
                        sum += image[i + 1][readCol] * kernel[2][kj];
                    }
                }
            }

            image[i][j] = sum;
        }

        if (prevRowCopy != nullptr) {
            free(prevRowCopy);
        }
        prevRowCopy = currentRowCopy;
    }
    if (prevRowCopy != nullptr) {
        free(prevRowCopy);
    }
}

int main() {
    ifstream fin(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL III\SEM I\PPD\lab-02\common\data.txt)");
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


    fin.close();

    auto start = chrono::high_resolution_clock::now();
    convolve();
    auto end = chrono::high_resolution_clock::now();

    cout << "Time: "
         << chrono::duration_cast<chrono::milliseconds>(end - start).count()
         << " ms" << endl;
    
    OutputValidator::validate(image, imageRows, imageCols);

    for (int i = 0; i < imageRows; ++i) free(image[i]);
    for (int i = 0; i < kernelRows; ++i) free(kernel[i]);

    free(image);
    free(kernel);

    return 0;
}
