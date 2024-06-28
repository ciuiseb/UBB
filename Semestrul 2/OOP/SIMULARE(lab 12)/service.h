//
// Created by ciuis on 5/22/2024.
//

#ifndef SIMULARE_SERVICE_H
#define SIMULARE_SERVICE_H

#include "repo.h"
#include "validator.h"

class Service {
    Repo repo;
public:
    Service(Repo r) :
            repo(r) {
    }
    // returneaza referinta la elemeele din repo
    vector<Caz> &getAll() {
        return repo.getAll();
    };
    //valideaza id-ul pentru unicitate, tipul si severitatea, apoi adauga in repo
    //altfel arunca eraore din validator
    void add(int id, const string& badV, const string& goodV, const string& type, const string& severity) {
        Validator validator(getAll());
        validator.validate(id, type, severity);
        repo.add(id, badV, goodV, type, severity);
    }
    //sorteaza alfabetic in functie de forma alfabetica corecta
    void sortByGoodV() {
        std::sort(getAll().begin(), getAll().end(),
                  [&](Caz a, Caz b) {
                      return a.getGoodV() > b.getGoodV();
                  });
    }
    //sorteaza alfabetic in functie de tip
    void sortByType() {
        std::sort(getAll().begin(), getAll().end(),
                  [&](Caz a, Caz b) {
                      return a.getType() > b.getType();
                  });
    }
    // sorteaza in functie de gradul de severitate: minor, medium si major
    void sortBySeverity() {
        std::sort(getAll().begin(), getAll().end(),
                  [&](Caz a, Caz b) {
                      if (a.getSeverity() == "minor") return true;
                      if (a.getSeverity() == "medium" && b.getSeverity() == "major") return true;
                      //if(a.getSeverity() == "major")
                      return false;
                  });
    }
};

#endif //SIMULARE_SERVICE_H
