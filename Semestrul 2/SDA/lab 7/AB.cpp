#include "AB.h"
#include "IteratorAB.h"
#include <exception>
#include <string>


AB::AB() {
    //CF = CM = CD = Θ(1)
    root = nullptr;
}

Node *AB::copy(Node *n) const {
    //CF = Θ(1), CM = CD = Θ(n)
    if (n == nullptr)
        return nullptr;
    return new Node(n->val, copy(n->left), copy(n->right));
}

AB::AB(const AB &ab) {
    //CF = Θ(1), CM = CD = Θ(n)
    root = copy(ab.root);
}

AB::AB(TElem e) {
    //CF = CM = CD = Θ(1)
    root = new Node(e);
}

AB::AB(const AB &st, TElem e, const AB &dr) {
    //CF = Θ(1), CM = CD = Θ(n)
    root = new Node(e, copy(st.root), copy(dr.root));
}


void AB::adaugaSubSt(const AB &st) {
    //CF = Θ(1), CM = CD = Θ(n)
    if (vid())
        throw std::exception();
    destroy(root->left);
    root->left = copy(st.root);
}

void AB::adaugaSubDr(const AB &dr) {
    //CF = Θ(1), CM = CD = Θ(n)
    if (vid())
        throw std::exception();
    destroy(root->right);
    root->right = copy(dr.root);
}

TElem AB::element() const {
    //CF = Θ(1), CM = CD = Θ(1)
    if (vid())
        throw std::exception();
    return root->val;
}

AB AB::stang() const {
    //CF = Θ(1), CM = CD = Θ(n)
    if (root == nullptr)
        throw std::exception();
    AB rez;
    rez.root = copy(root->left);
    return rez;
}

AB AB::drept() const {
    //CF = Θ(1), CM = CD = Θ(n)
    if (root == nullptr)
        throw std::exception();
    AB rez;
    rez.root = copy(root->right);
    return rez;
}

AB::~AB() {
    //CF = CM = CD = Θ(n)
    destroy(root);
}

bool AB::vid() const {
    //CF = CM = CD = Θ(1)
    return root == nullptr;
}


IteratorAB *AB::iterator(string s) const {
    //CF = CM = CD = Θ(1)
    if (s == "preordine")
        return new IteratorPreordine(*this);
    if (s == "inordine")
        return new IteratorInordine(*this);
    if (s == "postordine")
        return new IteratorPostordine(*this);
    if (s == "latime")
        return new IteratorLatime(*this);
    return nullptr;
}

void AB::destroy(Node *n) {
    //CF = CM = CD = Θ(n)
    if (n) {
        destroy(n->left);
        destroy(n->right);
        delete n;
    }
}


