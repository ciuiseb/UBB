#pragma once
#include<vector>
#include<utility>
#include "Node.h"
using namespace std;

//typedef int TCheie;
//typedef int TValoare;

//typedef std::pair<TCheie, TValoare> TElem;

class IteratorMD;

class MD
{
	friend class IteratorMD;

private:
	/* aici e reprezentarea */

    Node* head;
    Node* tail;
    int len{0};
public:
	// constructorul implicit al MultiDictionarului
	MD();

	// adauga o pereche (cheie, valoare) in MD
	void adauga(TCheie c, TValoare v);

	//cauta o cheie si returneaza vectorul de valori asociate
	vector<TValoare> cauta(TCheie c) const;

	//sterge o cheie si o valoare 
	//returneaza adevarat daca s-a gasit cheia si valoarea de sters
	bool sterge(TCheie c, TValoare v);

	//returneaza numarul de perechi (cheie, valoare) din MD 
	int dim() const;

	//verifica daca MultiDictionarul e vid 
	bool vid() const;

	// se returneaza iterator pe MD
	IteratorMD iterator() const;
    // returneaza cea mai frecventa valoare
    TValoare ceaMaiFreqVal() const;

    // destructorul MultiDictionarului
	~MD();
};