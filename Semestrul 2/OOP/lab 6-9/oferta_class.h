#pragma once

#include <utility>

#include "string"
#include "vector"
#include "cstring"
#include "iostream"

using std::vector;
using std::string;

class Oferta {
    string denumire;
    string destinatie;
    string tip;
    int pret;

public:
    Oferta(string denumire, string destinatie, string tip, int pret)
            : denumire(std::move(denumire)), destinatie(std::move(destinatie)), tip(std::move(tip)), pret(pret) {};

    Oferta(const Oferta &o) {
        pret = o.pret;
        tip = o.tip;
        destinatie = o.destinatie;
        denumire = o.denumire;
    }

    ~Oferta() = default;

    [[nodiscard]] const string &getDenumire() const;

    void setDenumire(const string &denumire);

    [[nodiscard]] string getDestinatie() const;

    void setDestinatie(const string &destinatie);

    [[nodiscard]] string getTip() const;

    void setTip(const string &tip);

    [[nodiscard]] int getPret() const;

    void setPret(int pret);

    bool operator==(const Oferta &other);
};