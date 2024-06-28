//
// Created by ciuis on 6/22/2024.
//

#ifndef PRACTIC_REPO_H
#define PRACTIC_REPO_H

#include <utility>

#include "utilaje.h"
#include "vector"
#include "map"
#include "fstream"
#include "cstring"
#include "algorithm"

using std::vector;
using std::map;

class Repo {
    string filePath;
    vector<Utilaj> elems;
    map<string, int> types;
    vector<int> cilindriiT{0, 0, 0, 0};
public:
    /// Construieste un repository bazat pe citire din fisier
    /// \param file string, FILE
    Repo(string file) : filePath(std::move(file)) {
        readFromFile();
    }

    /// Returneaza elementele dintr-un repository
    /// \return vector de tipul Utilaj
    vector<Utilaj> &getAll() { return elems; }

    /// Returneaza un dictionar cu toate tipurile si frecventa lor
    /// \return un disctionar cu perechi <string, int>
    map<string, int> &getTypes() { return types; }

    /// Returneaza un vector cu freventa cilindriilor
    /// \return vector cu 4 pozitii, pentru cele 4 tipuri de cilindrii
    vector<int> &getCilindrii() { return cilindriiT; }

    /// Adauga un nou utilaj in repo, incrementeaza frecventa tipului si a numarului de cilindrii
    /// \param id int unic
    /// \param denumire string nevid
    /// \param tip string nevid
    /// \param cilindrii int, e{1,2,4,8}
    void add(int id, string denumire, string tip, int cilindrii) {
        elems.emplace_back(id, denumire, tip, cilindrii);
        types[tip]++;
        cilindriiT[log2(cilindrii)]++;
    }

    ///Updateaza fisierul cu elementele curente
    void writeToFile() {
        std::ofstream out(filePath);
        for (auto &el: getAll()) {
            out << el.getId() << "," << el.getDenumire() << "," << el.getTip() << "," << el.getNrCilindrii()
                << std::endl;
        }
        out.close();
    }

private:
    ///Citeste din fisier si populeaza repository-ul
    void readFromFile() {
        std::ifstream in(filePath);
        char buffer[256];
        while (in.getline(buffer, sizeof(buffer))) {
            vector<string> parts;
            char *token = strtok(buffer, ",");
            while (token) {
                parts.push_back(token);
                token = strtok(nullptr, ",");
            }
            add(stoi(parts[0]), parts[1], parts[2], stoi(parts[3]));
        }
        in.close();
    }
};

#endif //PRACTIC_REPO_H
