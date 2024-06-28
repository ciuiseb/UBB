#include "IteratorMDO.h"
#include "MDO.h"
#include <vector>
#include "algorithm"

#include <exception>

using namespace std;

MDO::MDO(Relatie r) :
        rel(r), len(0), prim(-1), primLiber(0), cp(2) {
    /**
     Complexitate: T(n) ∈ θ(1)
     */

    elems = new TElem[cp];
    urm = new int[cp];
    pre = new int[cp];

    for (int i = 0; i < cp - 1; i++)
    {
        urm[i] = i + 1;
        pre[i] = -1;
    }
    urm[cp - 1] = -1;
    pre[cp - 1] = -1;
    prim = -1;
    primLiber = 0;
}


void MDO::adauga(TCheie c, TValoare v) {
    /**
     Complexitate: T(n) ∈ θ(n)
                   CF: perechea se potriveste pe prima pozitie => se parcurge whileul doar o data => T(n) ∈ θ(1)
                   CM:perechea se poate potrivi pe oricare dintre pozitiile 1,2,..,n => T(n) = (1+2+..+n)/n = (n+1)n/2n = (n+1)/2 = > T(n) ∈ θ(n)
                   CD: perechea se potriveste pe ultima pozitie => se parcurge whileul de n ori => T(n) ∈ θ(n)

     */
    TElem newElem;
    newElem.first = c, newElem.second = v;

    if(prim == -1){
        int i = creeazaNod(newElem);
        if (prim != -1)
            pre[prim] = i;
        urm[i] = -1;
        pre[i] = -1;
        prim = i;
        len = 1;
        return;
    }

    int anterior = -1, curent = prim;
    while(curent != -1 && rel(elems[curent].first, c)){
        anterior = curent;
        curent = urm[curent];
    }
    if (curent == -1) {
        int i = creeazaNod(newElem);
        urm[anterior] = i;
        pre[i] = anterior;
        urm[i] = -1;
    } else {
        if (anterior == -1) {
            int i = creeazaNod(newElem);
            urm[i] = prim;
            pre[prim] = i;
            prim = i;
            pre[i] = -1;
        } else {
            int i = creeazaNod(newElem);
            urm[anterior] = i;
            pre[i] = anterior;
            urm[i] = curent;
            pre[curent] = i;
        }
    }
    len++;
}


vector<TValoare> MDO::cauta(TCheie c) const {
    /**
     Complexitate: T(n) ∈ θ(n)
                   CF: perechea e pe prima pozitie => se parcurge whileul doar o data => T(n) ∈ θ(1)
                   CM:perechea poate fi pe oricare dintre pozitiile 1,2,..,n => T(n) = (1+2+..+n)/n = (n+1)n/2n = (n+1)/2 = > T(n) ∈ θ(n)
                   CD: perechea e pe ultima pozitie => se parcurge whileul de n ori => T(n) ∈ θ(n)

     */
    vector<TValoare> res;
    int curent = prim;
    while(curent != -1 && rel(elems[curent].first, c)){
        if(elems[curent].first == c){
            res.push_back(elems[curent].second);
        }
        curent = urm[curent];
    }
    return res;
}

bool MDO::sterge(TCheie c, TValoare v) {
    /**
     Complexitate: T(n) ∈ θ(n)
                   CF: perechea e pe prima pozitie => se parcurge whileul doar o data => T(n) ∈ θ(1)
                   CM:perechea poate fi pe oricare dintre pozitiile 1,2,..,n => T(n) = (1+2+..+n)/n = (n+1)n/2n = (n+1)/2 = > T(n) ∈ θ(n)
                   CD: perechea e pe ultima pozitie => se parcurge whileul de n ori => T(n) ∈ θ(n)

     */
    if(vid())
        return false;
    int curent = prim;
    if(elems[curent] == TElem(c,v)){
        prim = urm[curent];
        pre[prim] = -1;
        dealoca(curent);
        return true;
    }

    while(curent!= -1 && elems[curent] != TElem(c,v)){
        curent = urm[curent];
    }
    if(curent == -1)
        return false;
    urm[pre[curent]] = urm[curent];
    pre[urm[curent]] = pre[curent];

    dealoca(curent);
    return true;
}

int MDO::dim() const {
    /**
     Complexitate: T(n) ∈ θ(1)
     */
    return len;
}

bool MDO::vid() const {
    /**
     Complexitate: T(n) ∈ θ(1)
     */
    return dim() == 0;
}

IteratorMDO MDO::iterator() const {
    /**
     Complexitate: T(n) ∈ θ(1)
     */
    return IteratorMDO(*this);
}

MDO::~MDO() {
    /**
     Complexitate: T(n) ∈ θ(1)
     */
    delete[] urm;

}

int MDO::aloca() {
    int i = primLiber;
    primLiber = urm[primLiber];
    return i;
}

void MDO::dealoca(int i) {
    urm[i] = primLiber;
    pre[primLiber] = i;
    primLiber = i;
    len--;
}


void MDO::redim() {
    int cp_nou = cp * 2;
    TElem* elem_nou = new TElem[cp_nou];
    int* urm_nou = new int[cp_nou];
    int* pre_nou = new int[cp_nou];

    for (int i = 0; i < cp; ++i)
    {
        elem_nou[i] = elems[i];
        urm_nou[i] = urm[i];
        pre_nou[i] = pre[i];
    }

    for (int i = cp; i < cp_nou - 1; ++i)
    {
        urm_nou[i] = i + 1;
        pre_nou[i] = -1;
    }
    urm_nou[cp_nou - 1] = -1;
    pre_nou[cp_nou - 1] = -1;

    primLiber = cp;

    delete[] urm;
    delete[] pre;
    delete[] elems;

    elems = elem_nou;
    urm = urm_nou;
    pre = pre_nou;
    cp = cp_nou;
}

int MDO::creeazaNod(TElem el) {
    if(primLiber == -1)
        redim();
    int i = aloca();

    elems[i] = el;

    return i;
}
//CF = CD = CM = Teta(n);
vector<TElem> MDO::multimeValorilor() {
    if(vid())
        throw std::exception();
    vector<TElem>res;
    auto it = iterator();
    while (it.valid()){
        auto elem = it.element();

        bool exista = false;
        for(auto val : res){
            if(val.second == elem.second)
                exista = true;
        }
        if(!exista)
            res.push_back(elem);
        it.urmator();
    }
    return res;
}
