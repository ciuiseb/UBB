#include "oferta_class.h"

void Oferta::setDenumire(const string &denumire_noua) {
    Oferta::denumire = denumire_noua;
}

string Oferta::getDestinatie() const {
    return destinatie;
}

void Oferta::setDestinatie(const string &destinatie_noua) {
    Oferta::destinatie = destinatie_noua;
}

string Oferta::getTip() const {
    return tip;
}

void Oferta::setTip(const string &tip_nou) {
    Oferta::tip = tip_nou;
}

int Oferta::getPret() const {
    return pret;
}

void Oferta::setPret(int pret_nou) {
    Oferta::pret = pret_nou;
}

const string &Oferta::getDenumire() const {
    return denumire;
}

bool Oferta::operator==(const Oferta &other) {
    return (this->tip == other.tip && this->destinatie == other.destinatie && this->denumire == other.denumire &&
            this->pret == other.pret);
}

