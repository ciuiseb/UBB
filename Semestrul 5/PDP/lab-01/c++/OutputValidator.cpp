//
// Created by ciuis on 10/19/2025.
//

#include <iostream>
#include <fstream>
using namespace std;

class OutputValidator {
private:
    template<typename T>
    static void writeResult(T result, int rows, int cols) {
        ofstream pw(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL III\SEM I\PPD\lab-01\common\output.txt)");
        if (!pw.is_open()) {
            cerr << "FileNotFoundException" << endl;
            return;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                pw << result[i][j] << " ";
            }
            pw << endl;
        }
        pw.close();
    }

    template<typename T>
    static void validateImpl(T result, int rows, int cols) {
        ifstream sc(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL III\SEM I\PPD\lab-01\common\output.txt)");

        if (!sc.is_open()) {
            cerr << "FileNotFoundException" << endl;
            return;
        }

        sc.seekg(0, ios::end);
        if (sc.tellg() == 0) {
            sc.close();
            writeResult(result, rows, cols);
            return;
        }
        sc.seekg(0, ios::beg);

        bool identical = true;
        for (int i = 0; i < rows && identical; i++) {
            for (int j = 0; j < cols; j++) {
                int oldVal;
                if (!(sc >> oldVal)) {
                    identical = false;
                    break;
                }
                if (oldVal != result[i][j]) {
                    identical = false;
                    break;
                }
            }
        }
        sc.close();

        if (!identical) {
            cout << "Warning: result differs from previous run!" << endl;
        }
    }

public:
    OutputValidator() = default;

    template<typename T>
    static void validate(T result, int rows, int cols) {
        validateImpl(result, rows, cols);
    }
};
