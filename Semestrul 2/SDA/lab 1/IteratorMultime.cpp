#include "IteratorMultime.h"

IteratorMultime::IteratorMultime(const Multime &m) : multime(m) {
    /*
     * pre:
     * post: se pune indexul pe prima pozitie
     * Complexitate: θ(1)
     */
    this->index = 0;
}


void IteratorMultime::prim() {
    /*
     * pre:
     * post: indexul curent se face 0
     * Complexitate: θ(1)
     */
    this->index = 0;
}


void IteratorMultime::urmator() {
    /*
     * pre:
     * post: Daca pozitia curenta e valida, trece la urmatoarea
     * Complexitate: θ(1 + θ(valid_urm)) = θ(1)
     */
    if(this->valid_urmator())
    {
        this->index++;
    }
    else
        throw std::out_of_range("Pozitie invalida!");
}

void IteratorMultime::anterior() {
    /*
     * pre:
     * post: Daca pozitia curenta e valida, trece la cea precedenta
     * Complexitate: θ(1 + θ(valid_ant)) = θ(1)
     */
    if(this->valid_anterior())
    {
        this->index--;
    }
    else
        throw std::out_of_range("Pozitie invalida!");
}
TElem IteratorMultime::element() const {
    /*
     * pre:
     * post: Daca pozitia curenta e valida, returneaza elementul din multime
     * Complexitate: θ(1)
     */
    if(this->valid_urmator() && this->valid_anterior())
    {
        return this->multime.v[this->index];
    }
    else
        throw std::out_of_range("Pozitie invalida!");
}

bool IteratorMultime::valid_urmator() const {
    /*pre:
     * post: True daca pointerul inca e pe pozitie din sir
     *      false altfel
     *Complexitate: θ(1)
     */
    return (this->multime.nr_elems > this->index);
}

bool IteratorMultime::valid_anterior() const {
/*pre:
 * post: True daca pointerul inca e pe pozitie din sir
 *      false altfel
 *Complexitate: θ(1)
 */
return (this->index >= 0);
}
