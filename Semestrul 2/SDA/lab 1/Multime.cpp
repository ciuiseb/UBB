#include "Multime.h"


Multime::Multime() {
    /*pre: n/a
     * post: vector = gol
     */
    this->v = new TElem[5];
    this->cap = 5;
    this->nr_elems = 0;
}


bool Multime::adauga(TElem elem) {
    /*pre:elem -> TElem, elem nu este in this->v
     *post:False -> elementul se afla deja in multime
     *     True -> elementul a fost adaugat cu succes
     * Complexitate: θ(cauta() + 1) = θ(cauta())
     */
    if (!this->cauta(elem)) {
        if (this->cap == this->dim()) {
            this->resize();
        }
        this->v[this->dim()] = elem;
        this->nr_elems++;
        return true;
    }
    return false;
}

void Multime::resize() {
    /*
     * pre:
     * post: Dubleaza capacitatea vectorului dinamic
     * Complexitate: θ(n)
     */
    TElem *new_vector = new TElem[(this->cap) * 2];

    for (int i = 0; i < this->nr_elems; ++i)
        new_vector[i] = this->v[i];

    delete[] this->v;
    this->v = new_vector;
    this->cap = this->cap * 2;
}

bool Multime::sterge(TElem elem) {
    /*
     * pre: Daca elem exista este in multime, il sterge
     * post: Multime = Multime /{elem}
     * Complexitate: T(n) = 2*T(cauta) => θ(cauta)
     */
    if (this->cauta(elem)) {
        int index = -1;
        for (int i = 0; i < nr_elems; ++i) {
            if (v[i] == elem) {
                index = i;
                break;
            }
        }
        for (int i = index; i < nr_elems - 1; ++i) {
            v[i] = v[i + 1];
        }

        this->nr_elems--;
        return true;
    }

    return false;
}


bool Multime::cauta(TElem elem) const {
    /*
     * pre: elem -> TElem
     * post: True daca eleme este in multime
     *      False altfel
     *  Complexitate: CF: elem e pe prima pozitie => T(1)
     *                CD: elem e pe ultima pozitie => T(n)
     *                CM: elem poate fi pe oricare dintre pozitiile 1,2,.., n => T(n(n+1)/2) = T(n^2)
     *
     */
    for (int i = 0; i < this->dim(); ++i)
        if (this->v[i] == elem) {
            return true;
        }
    return false;
}


int Multime::dim() const {
    /*
     * pre: n/a
     * post: nr elems
     * Compleixtate: θ(1)
     */
    return this->nr_elems;
}

bool Multime::vida() const {
    /*
     * pre: n/a
     * post:True daca multimea e vida
     *      False altfel
     * Complexitate: θ(1)
     */
    return (this->nr_elems == 0);
}


Multime::~Multime() {
    /*
     * pre: n/a
     * post:distruge this->v
     * Complexitate: θ(1)
     */
    delete[] this->v;
}


IteratorMultime Multime::iterator() const {
    /*
     * pre: n/a
     * post: iteratorul multimii
     * Complexitate: θ(1)
     */
    return IteratorMultime(*this);
}

