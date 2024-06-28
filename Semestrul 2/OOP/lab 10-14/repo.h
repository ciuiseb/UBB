#pragma once

#include <utility>

#include "oferta_class.h"
#include "fstream"

class AbstractRepo {
public:
    virtual vector<Oferta> &getAll() = 0;

    virtual void add(const Oferta &oferta) = 0;

    virtual size_t len() = 0;

    virtual ~AbstractRepo() = default;
};

class OffersRepo : public AbstractRepo {
    vector<Oferta> elems;
public:
    vector<Oferta> &getAll() override;

    void add(const Oferta &oferta) override;

    size_t len() override;
};

class OffersFileRepo : public AbstractRepo {
    vector<Oferta> elems;
    string filePath;
public:
    explicit OffersFileRepo(string file) :
            filePath(std::move(file)) {
        readFromFile();
    }

    vector<Oferta> &getAll() override;

    void add(const Oferta &oferta) override;

    size_t len() override;

    void readFromFile();
};