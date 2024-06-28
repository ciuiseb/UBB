//
// Created by ciuis on 6/22/2024.
//

#ifndef PRACTIC_UTILAJE_H
#define PRACTIC_UTILAJE_H

#include <utility>

#include "string"

using std::string;

class Utilaj {
    int id;
    string denumire;
    string tip;
    int nrCilindrii;
public:
    /// Consturieste un utilaj
    /// \param id int, unic
    /// \param denumire string nevid
    /// \param tip string nevid
    /// \param cilindrii int, e{1,2,4,8}
    Utilaj(int id, string denumire, string tip, int cilindrii) :
            id(id), denumire(std::move(denumire)), tip(std::move(tip)), nrCilindrii(cilindrii) {}

    /// Returneaza id-ul unui utilaj
    /// \return int reprezentadn id-ul
    int getId() { return id; }

    /// Returneaza numarul de cilindrii unui utilaj
    /// \return int reprezentadn numarul de cilindrii
    int getNrCilindrii() { return nrCilindrii; }

    /// Returneaza denumirea unui utilaj
    /// \return string reprezentand denumirea
    string getDenumire() { return denumire; }

    /// Returneaza tipul unui utilaj
    /// \return string reprezentand tipul
    string getTip() { return tip; }

    /// Atribuite un nou numar de cilintrii unui utilaj
    /// \param n int, n e {1,2,4,8
    void setNrCilindrii(int n) { nrCilindrii = n; };

    /// Atribuie o noua denumire unui utilaj
    /// \param n string nevind
    void setDenumire(string n) { denumire = std::move(n); };

    /// Atribuie un nou tip unui utilaj
    /// \param n string nevind
    void setTip(string n) { tip = std::move(n); };
};

#endif //PRACTIC_UTILAJE_H
