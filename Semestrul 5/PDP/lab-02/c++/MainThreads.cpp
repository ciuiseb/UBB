#include <iostream>
#include <fstream>
#include <chrono>
#include <cstdlib>
#include <cstring>
#include <pthread.h>
#include "OutputValidator.cpp"

using namespace std;

int **image;
int **kernel;
int imageRows, imageCols;
int kernelRows, kernelCols;

pthread_barrier_t barrier;

struct MyThreadConstructorData {
    int start;
    int end;
    int** &image;
};

void *convolve(void *arg) {
    auto *data = (MyThreadConstructorData *) arg;
    int start = data->start;
    int end = data->end;
    int** image = data->image;

    int kernelSize = kernelCols;
    int halfSize = kernelSize / 2;

    int *prevRow = nullptr;
    if (start > 0) {
        prevRow = (int *) malloc(imageCols * sizeof(int));
        memcpy(prevRow, image[start - 1], imageCols * sizeof(int));
    }
    int *lastRow = nullptr;
    if (end < imageRows) {
        lastRow = (int *) malloc(imageCols * sizeof(int));
        memcpy(lastRow, image[end], imageCols * sizeof(int));
    }

    pthread_barrier_wait(&barrier);

    for (int i = start; i < end; i++) {
        int *currentRowOriginal = (int *) malloc(imageCols * sizeof(int));
        memcpy(currentRowOriginal, image[i], imageCols * sizeof(int));

        int *nextRowOriginal = (i + 1 == end) ? lastRow : image[i + 1];

        for (int j = 0; j < imageCols; j++) {
            int sum = 0;

            if (prevRow != nullptr) {
                for (int kj = 0; kj < kernelSize; kj++) {
                    int readCol = j + kj - halfSize;
                    if (readCol >= 0 && readCol < imageCols) {
                        sum += prevRow[readCol] * kernel[0][kj];
                    }
                }
            }

            for (int kj = 0; kj < kernelSize; kj++) {
                int readCol = j + kj - halfSize;
                if (readCol >= 0 && readCol < imageCols) {
                    sum += currentRowOriginal[readCol] * kernel[1][kj];
                }
            }

            if (nextRowOriginal != nullptr) {
                for (int kj = 0; kj < kernelSize; kj++) {
                    int readCol = j + kj - halfSize;
                    if (readCol >= 0 && readCol < imageCols) {
                        sum += nextRowOriginal[readCol] * kernel[2][kj];
                    }
                }
            }
            image[i][j] = sum;
        }

        if (i > start) {
            free(prevRow);
        }
        prevRow = currentRowOriginal;
    }

    if (start < end)
        free(prevRow);
    if (data->image != nullptr)
        free(data->image);
    free(data);
    return nullptr;
}


int main(int argc, char *argv[]) {
    int p = atoi(argv[1]);

    ifstream fin("../common/data.txt");
    if (!fin) {
        cerr << "Error: Could not open input file ../common/data.txt" << endl;
        return 1;
    }

    fin >> imageRows >> imageCols;

    image = (int **) malloc(imageRows * sizeof(int *));
    for (int i = 0; i < imageRows; ++i) {
        image[i] = (int *) malloc(imageCols * sizeof(int));
    }

    for (int i = 0; i < imageRows; ++i)
        for (int j = 0; j < imageCols; ++j)
            fin >> image[i][j];

    fin >> kernelRows >> kernelCols;

    kernel = (int **) malloc(kernelRows * sizeof(int *));
    for (int i = 0; i < kernelRows; ++i) {
        kernel[i] = (int *) malloc(kernelCols * sizeof(int));
    }

    for (int i = 0; i < kernelRows; ++i)
        for (int j = 0; j < kernelCols; ++j)
            fin >> kernel[i][j];

    fin.close();

    auto *threads = (pthread_t *) malloc(p * sizeof(pthread_t));

    pthread_barrier_init(&barrier, nullptr, p);

    auto start_time = chrono::high_resolution_clock::now();

    int rowsPerThread = imageRows / p;
    int currentRow = 0;

    for (int t = 0; t < p; t++) {
        int start = currentRow;
        int end = (t == p - 1) ? imageRows : start + rowsPerThread;
        currentRow = end;

        auto *data = (MyThreadConstructorData *) malloc(sizeof(MyThreadConstructorData));
        data->start = start;
        data->end = end;
        data->image = image;

        pthread_create(&threads[t], nullptr, convolve, (void *) &image);
    }

    for (int t = 0; t < p; t++) {
        pthread_join(threads[t], nullptr);
    }

    auto end_time = chrono::high_resolution_clock::now();

    pthread_barrier_destroy(&barrier);
    free(threads);

    cout << "Time: "
         << chrono::duration_cast<chrono::milliseconds>(end_time - start_time).count()
         << " ms" << endl;

    OutputValidator::validate(image, imageRows, imageCols);

    for (int i = 0; i < imageRows; ++i) free(image[i]);
    for (int i = 0; i < kernelRows; ++i) free(kernel[i]);
    for (int i = 0; i < kernelRows; ++i) free(kernel[i]);
    free(image);
    free(kernel);
}