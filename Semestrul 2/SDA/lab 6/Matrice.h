#pragma once

#include "tuple"
#include "vector"
typedef int TElem;

typedef std::tuple<int , int , TElem> Pereche;

using std::get;
using std::vector;

#define NULL_TELEMENT 0

class Matrice {

private:
	/* aici e reprezentarea */
    int nrL, nrC;
    int capacity, size;
    int primLiber;
    Pereche *elems;
    int* urm;

    void checkPrimLiber();
    void rehash();
    int getKey(TElem val) const;
    TElem val(int elemsIndex);
    int pairRow(int elemsIndex);
    int pairColumn(int elemsIndex);
public:
	//constructor
	//se arunca exceptie daca nrLinii<=0 sau nrColoane<=0
	Matrice(int nrLinii, int nrColoane);
    int dim() { return size;}

	//destructor
	~Matrice(){};

	//returnare element de pe o linie si o coloana
	//se arunca exceptie daca (i,j) nu e pozitie valida in Matrice
	//indicii se considera incepand de la 0
	TElem element(int i, int j);


	// returnare numar linii
	int nrLinii() const;

	// returnare numar coloane
	int nrColoane() const;


	// modificare element de pe o linie si o coloana si returnarea vechii valori
	// se arunca exceptie daca (i,j) nu e o pozitie valida in Matrice
	TElem modifica(int i, int j, TElem);
    TElem stergeMax();
};







