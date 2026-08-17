#include <iostream>
#include <fstream>
#include <string>
#include <cstdlib>   
#include "validator.h"

int* readBigNumFromFile(const std::string& filename, int* outSize) {
    std::ifstream inFile(filename);
    int length;
    inFile >> length;
    *outSize = length;

    int* vec = (int*)malloc(length * sizeof(int));
    for (int i = 0; i < length; ++i) {
        int digit;
        inFile >> digit;
        vec[(length - 1) - i] = digit;
    }

    inFile.close();
    return vec;
}

void printBigNum(const std::string& title, const int* vec, int size) {
    std::cout << title;

    if (size == 0 || vec == nullptr) {
        std::cout << "0" << std::endl;
        return;
    }

    
    for (int i = size - 1; i >= 0; --i) {
        std::cout << vec[i];
    }
    std::cout << std::endl;
}

int main() {
    int size1 = 0;
    int size2 = 0;
    
    int* vec1 = readBigNumFromFile("common/numar1.txt", &size1);
    int* vec2 = readBigNumFromFile("common/numar2.txt", &size2);
    
    int maxLen = std::max(size1, size2);
    int maxSumSize = maxLen + 1;
    int* sum = (int*)malloc(maxSumSize * sizeof(int));

    int carry = 0;
    int currentSumSize = 0; 

    for (int i = 0; i < maxLen; ++i) {
        int d1 = (i < size1) ? vec1[i] : 0;
        int d2 = (i < size2) ? vec2[i] : 0;

        int currentSum = d1 + d2 + carry;
        sum[i] = currentSum % 10;
        carry = currentSum / 10;

        currentSumSize++;
    }
    
    if (carry > 0) {
        sum[currentSumSize] = carry;
        currentSumSize++;
    }


    std::vector<int> sum_vector(sum, sum + currentSumSize);
    Validator::validate(sum_vector);

    free(vec1);
    free(vec2);
    free(sum);
    return 0;
}