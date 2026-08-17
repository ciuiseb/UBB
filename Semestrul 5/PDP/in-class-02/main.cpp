#include <iostream>
#include <random>
#include <thread>
#include <vector>
#include <chrono>

using namespace std;

#define SIZE 100000000

vector<int> a(SIZE);
vector<int> b(SIZE);
vector<int> c(SIZE);
void genVect(vector<int> arr) {
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dist(1, 100);

    for (int i = 0; i < SIZE; ++i) {
        arr[i] = dist(gen);
    }
}

int op(int x, int y)
{
    return (int) sqrt(x*x*x*x*x + y*y*y*y*y);
}

void solve(int ss, int ee) {
    for (int i = ss; i < ee; ++i) {
        c[i] = op(a[i], b[i]);
    }
}


void run(int noOfThreads = 1) {
    vector<thread> threads;

    int chunk = SIZE / noOfThreads;
    int remaining = SIZE % noOfThreads;
    int start = 0;

    auto startTime = chrono::high_resolution_clock::now();

    for (int i = 0; i < noOfThreads; ++i) {
        int endThread = start + chunk;
        if (i < remaining) {
            endThread++;
        }

        threads.emplace_back(solve, start, endThread);
        start = endThread;
    }

    for (auto &t : threads) {
        t.join();
    }

    auto endTime = chrono::high_resolution_clock::now();
    auto resultTime = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "Time: " << resultTime.count() << " ms" << endl;
}

int main() {
    genVect(a);
    genVect(b);

    run(8);

    return 0;
}