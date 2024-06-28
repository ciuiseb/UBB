//
// Created by ciuis on 5/22/2024.
//

#ifndef SIMULARE_REPO_H
#define SIMULARE_REPO_H

#include "caz.h"
#include "vector"
#include "fstream"
#include "cstring"
using std::vector;

class Repo {
    vector<Caz> elements;
    string filePath;
public:
    Repo(string file) :
            filePath(std::move(file)) {
        populateRepo();
    }
    // returneaza referinta la vectorul de elemente
    vector<Caz>&getAll(){return elements;}
    //adauga un element nonu in repo
    void add(int id, const string& badV, const string& goodV, const string& type, const string& severity){
        elements.emplace_back(id, badV, goodV, type, severity);
    }

private:
    //citeste datele din fisier, le imparte in functie de , si adauga in fisier
    void populateRepo() {
        std::ifstream f(filePath);
        char line[256];
        while(f.getline(line, sizeof(line))){
            vector<string> parts;
            char*token = strtok(line, ",");
            while(token){
                parts.push_back(token);
                token = strtok(nullptr, ",");
            }
            elements.emplace_back(stoi(parts[0]), parts[1], parts[2], parts[3], parts[4]);
        }
        f.close();
    }
};

#endif //SIMULARE_REPO_H
