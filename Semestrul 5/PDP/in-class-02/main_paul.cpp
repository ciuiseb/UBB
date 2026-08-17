#include <iostream>
#include <thread>
#include <mutex>
#include <vector>
#include <chrono>
#include <random>

#define SIZE 100000000

using namespace std;

vector<int> a, b, c;

void genVect(vector<int>& copy) {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> dist(1, 100);

    for (int i = 0; i < SIZE; ++i)
        copy.emplace_back(dist(gen));
}

void printVector(vector<int>& copy) {
    for (int i = 0; i < SIZE; ++i)
        cout << copy[i] << " ";
    cout << endl;
}

int oper(int x, int y) {
    // return x + y;
    return (int) sqrt(x*x*x*x*x + y*y*y*y*y);
}

void solve (int ss, int ee) {
    for (int i = ss; i < ee; ++i) {
        c[i] = oper(a[i], b[i]);
    }
}

void run(int numberOfThreads = 1) {
    vector<thread> threads;

    int start = 0;

    int chunk = SIZE / numberOfThreads;
    int remaining = SIZE % numberOfThreads;

    auto startTime = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < numberOfThreads; ++i) {
        int endThread = start + chunk;
        if (i < remaining) {
            endThread++;
        }

        threads.emplace_back(solve, start, endThread);

        start = endThread;
    }

    for (auto& t : threads) {
        t.join();
    }

    if (SIZE < 10) {
        printVector(a);
        printVector(b);
        printVector(c);
    }

    auto endTime = std::chrono::high_resolution_clock::now();
    auto durata = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "Timpul de executie: " << durata.count() << " ms\n";
}

int main() {
    genVect(a);
    genVect(b);
    genVect(c);

    run(5);
    return 0;
}

