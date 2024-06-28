#include "Matrice.h"

#include <exception>

using namespace std;


Matrice::Matrice(int m, int n){
    if(m <= 0 || n <= 0){
        throw std::exception();
    }
    nrL = m, nrC = n;
    capacity = 10, size = 0;
	elems = new Pereche[capacity];
    urm = new int[capacity];
    primLiber = 0;
    for(int i = 0; i < capacity; ++i){
        urm[i] = -1;
        elems[i] = std::make_tuple(-1,-1, -1);
    }

}



int Matrice::nrLinii() const{
	return nrL;
}


int Matrice::nrColoane() const{
	return nrC;
}


TElem Matrice::element(int i, int j){
    if(i < 0 || j < 0 || i >= nrLinii() || j >= nrColoane()){
        throw std::exception();
    }
    for(int index = 0; index < capacity; ++index){
        if(pairRow(index) == i && pairColumn(index) == j){
            return val(index);
        }
    }
	return NULL_TELEMENT;
}



TElem Matrice::modifica(int i, int j, TElem e) {
    if(i < 0 || j < 0 || i >= nrLinii() || j >= nrColoane()){
        throw std::exception();
    }
    if(getKey(e) == -1){
        if(element(i, j) != NULL_TELEMENT) {
            size--;
            return -1;
        }
    }
    if(size >= capacity){
        rehash();
    }
    int key = getKey(e);
    if(val(key) == -1){ // pozitia cheii este libera
        elems[key] = make_tuple(i, j, e);
        size++;
        checkPrimLiber();
        return -1;
    }
    while(urm[key] != -1 &&
        (pairRow(key) != i || pairColumn(key) != j)){ // pozitia e ocupata, deci cautam daca exista
        key = urm[key];
    }
    if(urm[key] == -1 && pairRow(key) != i || pairColumn(key) != j){ // nu exista, deci adaugam la final
        elems[primLiber] = make_tuple(i, j, e);
        size++;
        checkPrimLiber();
        return -1;
    } else{ // exista, deci doar modificam
        TElem oldValue = val(key);
        elems[key] = make_tuple(i, j, e);
        return oldValue;
    }
}

int Matrice::getKey(TElem val) const {
    return val % capacity;
}

void Matrice::rehash() {
    auto oldElems = new Pereche [capacity];
    int* oldUrm = new int[capacity];
    std::copy(elems, elems + capacity, oldElems);
    std::copy(urm, urm + capacity, oldUrm);

    int newCapacity = 2 * capacity;
    delete[] elems;
    delete[] urm;
    elems = new Pereche[newCapacity];
    urm = new int[newCapacity];

    //init urm and elems

    for(int i = 0; i < newCapacity; ++i){
        urm[i] = -1;
        elems[i] = std::make_tuple(-1,-1, -1);
    }
    size = 0;
    primLiber = 0;
    capacity = newCapacity;
    for(int i = 0; i < capacity / 2; ++i){
        if(std::get<0>(oldElems[i]) != -1)
            modifica(std::get<0>(oldElems[i]), std::get<1>(oldElems[i]), std::get<2>(oldElems[i]));
    }
    delete[] oldElems;
    delete[] oldUrm;
}


void Matrice::checkPrimLiber() {
    if(size >= capacity)
        return;
    if(val(primLiber) != -1){
        int newPrimLiber = 0;
        while(val(newPrimLiber) != -1) {
            newPrimLiber++;
        }
        primLiber = newPrimLiber;
    }
}

TElem Matrice::val(int index) {
    return get<2>(elems[index]);
}

int Matrice::pairRow(int index) {
    return get<0>(elems[index]);

}

int Matrice::pairColumn(int index) {
    return get<1>(elems[index]);
}
//teta n amortizat
TElem Matrice::stergeMax() {

    if(size == 0)
        throw std::exception();
    TElem maxim = 0;
    for(int i = 0; i < capacity; ++i){
        if(val(i) != -1) {
            if (val(i) > maxim) {
                maxim = val(i);
            }
        }
    }
    // in index_maxim e pozitia
    
    int curent = getKey(maxim), anterior = -1;
    while(val(curent) != maxim){
        anterior = curent;
        curent = urm[curent];
    }
    if(urm[curent] == -1){
        urm[anterior] = -1;
    }
    if(anterior == -1){
        TElem oldVal = val(curent);
        modifica(pairRow(curent), pairColumn(curent), -1);
        return oldVal;
    } else {
        urm[anterior] = urm[curent];
        TElem oldVal = val(curent);
        modifica(pairRow(curent), pairColumn(curent), -1);
        return oldVal;
    }
}