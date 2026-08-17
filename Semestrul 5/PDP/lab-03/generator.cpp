#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <random>
#include <chrono>

void generate_and_write(const std::string& filename, int digits) {
    if (digits <= 0) return;

    std::ofstream outfile(filename);
    if (!outfile.is_open()) {
        std::cerr << "Error opening " << filename << std::endl;
        return;
    }

    outfile << digits << "\n";

    unsigned seed = std::chrono::high_resolution_clock::now().time_since_epoch().count();
    std::mt19937 generator(seed);
    std::uniform_int_distribution<int> dist(0, 9);
    std::uniform_int_distribution<int> dist_nonzero(1, 9);

    for (int i = 0; i < digits; ++i) {
        int d;
        if (i == 0) {
            d = dist_nonzero(generator);
        } else {
            d = dist(generator);
        }

        outfile << d;
        if (i < digits - 1) {
            outfile << " ";
        }
    }

    outfile.close();
}

void clear_file(const std::string& filename) {
    std::ofstream outfile(filename, std::ios::trunc);
    if (outfile.is_open()) {
        outfile.close();
    }
}

int main() {
    int digits1 = 100;
    int digits2 = 100000;

    generate_and_write("common/numar1.txt", digits1);
    generate_and_write("common/numar2.txt", digits2);

    clear_file("common/result.txt");
    return 0;
}