//
// Created by ciuis on 6/22/2024.
//

#ifndef PRACTIC_SERVICE_H
#define PRACTIC_SERVICE_H

#include <utility>

#include "repo.h"
#include "sstream"

using std::stringstream;

class Service {
    Repo &repo;
public:
    /// Construieste un service
    /// \param r Repository
    Service(Repo &r) : repo(r) {}

    /// Returneaza elementele din service
    /// \return vector de utilaje
    vector<Utilaj> &getAll() { return repo.getAll(); }

    /// Returneaza un dictionar cu toate tipurile si frecventa lor
    /// \return un disctionar cu perechi <string, int>
    map<string, int> &getTypes() { return repo.getTypes(); }

    /// Returneaza un vector cu freventa cilindriilor
    /// \return vector cu 4 pozitii, pentru cele 4 tipuri de cilindrii
    vector<int> &getCilindrii() { return repo.getCilindrii(); }

    /// Sterge un element din service, daca nu este ultimul cu nr respectiv de cilindrii
    /// \param row int, e[0, marime service - 1]
    void remove(int row) {
        auto u = getAll()[row];
        if (getCilindrii()[log2(u.getNrCilindrii())] == 1) {
            string er = "Utilajul selectat este ultimul cu numarul sau de cilindrii, si nu poate fi sters";
            throw std::domain_error(er);
        }
        getCilindrii()[log2(u.getNrCilindrii())]--;
        getTypes()[u.getTip()]--;
        getAll().erase(getAll().begin() + row);
        repo.writeToFile();
    }

    /// Modifica atributele unui utilaj, in afara de ID. Acestea sunt sigur valide
    /// \param row int, e[0, marime service - 1]
    /// \param denumire string nevid
    /// \param tip string nevid
    /// \param cilindrii int, e{1,2,4,8}
    void modify(int row, string denumire, string tip, int cilindrii) {
        getTypes()[getAll()[row].getTip()]--;
        getCilindrii()[log2(getAll()[row].getNrCilindrii())]--;

        getAll()[row].setDenumire(std::move(denumire));
        getAll()[row].setTip(std::move(tip));
        getAll()[row].setNrCilindrii(cilindrii);

        getTypes()[getAll()[row].getTip()]++;
        getCilindrii()[log2(getAll()[row].getNrCilindrii())]++;

        repo.writeToFile();
    }

};

#endif //PRACTIC_SERVICE_H
