#include "MD.h"
#include "IteratorMD.h"
#include <exception>
#include <iostream>
#include "map"

using namespace std;


MD::MD() {
    head = nullptr;
    tail = nullptr;
}


void MD::adauga(TCheie c, TValoare v) {
    /**
     Complexitate: T(n) ∈ θ(1)
     */
    TElem new_pair;
    new_pair.first = c, new_pair.second = v;
    Node *new_node = new Node(new_pair);

    if (head == nullptr) {
        head = tail = new_node;
    } else {
        tail->next = new_node;
        new_node->prev = tail;
        tail = new_node;
    }
    len++;
}


bool MD::sterge(TCheie c, TValoare v) {
    /**
     Complexitate: T(n) ∈ θ(n)
                   CF: perechea e pe prima pozitie => se parcurge whileul doar o data => T(n) ∈ θ(1)
                   CM:perechea poate fi pe oricare dintre pozitiile 1,2,..,n => T(n) = (1+2+..+n)/n = (n+1)n/2n = (n+1)/2 = > T(n) ∈ θ(n)
                   CD: perechea e pe prima pozitie => se parcurge whileul doar o data => T(n) ∈ θ(n)

     */
    Node* current = head;
    while(current != nullptr){
        if(current->data == TElem(c, v))
        {
            if (head == tail) {
                head = tail = nullptr;
            } else if (current == head) {
                head = head->next;
                head->prev = nullptr;
            } else if (current == tail) {
                tail = tail->prev;
                tail->next = nullptr;
            } else {
                current->prev->next = current->next;
                current->next->prev = current->prev;
            }
            len--;
            return true;
        }
        current = current->next;
    }
    return false;
}


vector<TValoare> MD::cauta(TCheie c) const {
    /**
     Complexitate: T(n) ∈ θ(n)
                   CF: perechea e pe prima pozitie => se parcurge whileul doar o data => T(n) ∈ θ(1)
                   CM:perechea poate fi pe oricare dintre pozitiile 1,2,..,n => T(n) = (1+2+..+n)/n = (n+1)n/2n = (n+1)/2 = > T(n) ∈ θ(n)
                   CD: perechea e pe prima pozitie => se parcurge whileul doar o data => T(n) ∈ θ(n)

     */
    vector<TValoare> res;
    Node* current = head;
    while(current != nullptr){
        if(current->data.first == c){
            res.push_back(current->data.second);
        }
        current = current->next;
    }
    return res;
}


int MD::dim() const {
    /**
     * Complexitate: T(n) ∈ θ(1)
     */
    return len;
}


bool MD::vid() const {
    /**
     * Complexitate: T(n) ∈ θ(1)
     */
    return (len == 0);
}

IteratorMD MD::iterator() const {
    /**
     * Complexitate: T(n) ∈ θ(1)
     */
    return IteratorMD(*this);
}


MD::~MD() {
    /**
     * Complexitate: T(n) ∈ θ(n)
     */
    Node* current = head;
    while (current != nullptr) {
        Node* next = current->next;
        delete current;
        current = next;
    }
    head = nullptr;
    tail = nullptr;
    len = 0;
}

TValoare MD::ceaMaiFreqVal() const{
    /**
     * Complexitate: T(n) = n + nr_val, dar nr_val <= n => max(n, nr_val) = n => T(n) ∈ θ(n)
     */
    if (vid()){
        throw std::exception();
    }
    map<TValoare , int> freqs;
    Node* current = head;
    while(current != nullptr){
        freqs[current->data.second]++;
        current = current->next;
    }
    TElem res;
    for (const auto& pair : freqs) {
        if(pair.second > res.second){
            res = pair;
        }
    }
    return res.first;
}
