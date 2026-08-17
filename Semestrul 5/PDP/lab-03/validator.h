#ifndef VALIDATOR_H
#define VALIDATOR_H

#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <sstream>
#include <algorithm>

class Validator {
private:
    static std::string resultToString(const std::vector<int> &vec) {
        std::stringstream ss;
        if (vec.empty()) {
            return "0";
        }
        int i = vec.size() - 1;
        while (i > 0 && vec[i] == 0) {
            i--;
        }
        for (; i >= 0; --i) {
            ss << vec[i];
        }
        std::string res = ss.str();
        return res.empty() ? "0" : res;
    }

public:
    Validator() = default;

    static void validate(const std::vector<int> &result) {
        std::string filename = "common/result.txt";
        std::string calculatedStr = resultToString(result);

        std::ifstream inFile(filename);

        if (inFile.peek() == std::ifstream::traits_type::eof()) {
            inFile.close();
            std::ofstream outFile(filename);
            outFile << calculatedStr;

            return;
        }

        std::stringstream buffer;
        buffer << inFile.rdbuf();
        std::string fileContent = buffer.str();
        inFile.close();
        fileContent.erase(std::remove_if(fileContent.begin(), fileContent.end(), ::isspace), fileContent.end());

        if (calculatedStr != fileContent) {
            std::cout << "[Validator] FAILED!" << std::endl;
        }
    }
};

#endif