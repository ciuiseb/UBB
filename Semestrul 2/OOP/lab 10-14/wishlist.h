#pragma once

#include "oferta_class.h"
#include "algorithm"
#include "vector"
using std::vector;
class Wishlist {
    vector<Oferta> elems;
public:
    vector<Oferta>& get_all();
    void add(const Oferta& oferta);
    void clear();
    size_t generate(const vector<Oferta>& db);
};


