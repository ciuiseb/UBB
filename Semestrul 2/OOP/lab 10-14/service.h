#pragma once

#include <utility>

#include "optional"
#include "stdexcept"
#include "algorithm"
#include "fstream"
#include "repo.h"
#include "map"
#include "wishlist.h"
#include "memory"

using std::unique_ptr;
using std::optional;
using std::map;

class ServiceException : std::exception {
    string msg;
public:
    explicit ServiceException(string message) : msg(std::move(message)) {};

    const char *what() const noexcept override {
        return msg.c_str();
    }
};

class ActiuneUndo;

class Service {
    AbstractRepo &repo;
    Wishlist wl;
    map<string, int> types;
    vector<unique_ptr<ActiuneUndo>> ActiuniUndo;
public:
    explicit Service(AbstractRepo &repo) : repo(repo) {};

    Service(const Service &o) = delete;


    ~Service() {}

    void adauga(const Oferta &oferta);

    void sterge(const string &denumire);

    void modifica(const string &denumire, const string &atribut, const string &valoare_noua);

    vector<Oferta> filtreaza(const string &atribut, const string &value);

    void sorteaza(const string &atribut);

    static bool filter_compare(const Oferta &oferta, const std::string &key, const std::string &value);

    static bool sort_compare(const Oferta &oferta1, const Oferta &oferta2, const std::string &key);

    Oferta find(const string &denumire);

    size_t len();

    vector<Oferta> &get_all();

    vector<Oferta> &get_wl();

    void add_to_wl(const Oferta &oferta);

    void clear_wl();

    size_t generate_wl();

    void export_wl(string &filename);

    map<string, int> get_types();

    void undo();
};

class ActiuneUndo {
public:
    ActiuneUndo() = default;

    virtual ~ActiuneUndo() {}

    virtual void doUndo() {}

};

class UndoAdauga : public ActiuneUndo {
    string denumire;
    Service &service;
public:
    UndoAdauga(Service &service, string den) : service(service), denumire(den) {}

    ~UndoAdauga() {
    }

    void doUndo() override {
        service.sterge(denumire);
    }

};

class UndoSterge : public ActiuneUndo {
    Oferta oferta;
    Service &service;
public:
    UndoSterge(Service &service, Oferta oferta) : service(service), oferta(oferta) {}

    ~UndoSterge() {
    }

    void doUndo() override {
        service.adauga(oferta);
    }
};

class UndoModifica : public ActiuneUndo {
    string denumire, atribut, val_veche;
    Service &service;
public:
    UndoModifica(Service &service, string denumire, string atribut, string val) : service(service), denumire(denumire),
                                                                                  atribut(atribut), val_veche(val) {}

    UndoModifica(Service &service, string denumire, string atribut, int val) : service(service), denumire(denumire),
                                                                               atribut(atribut),
                                                                               val_veche(std::to_string(val)) {}

    void doUndo() override {
        service.modifica(denumire, atribut, val_veche);
    }

    ~UndoModifica() {}
};
