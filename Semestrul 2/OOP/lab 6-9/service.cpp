#include "service.h"

optional<int> validator_oferta(const Oferta &oferta) {
    if (oferta.getDenumire().empty()) {
        return {1};
    }
    if (oferta.getDestinatie().empty()) {
        return {2};
    }
    if (oferta.getPret() < 0) {
        return {4};
    }
    return {};
}

size_t Service::len() {
    return repo.len();
}


vector<Oferta> &Service::get_all() {
    return repo.getAll();
}

void Service::adauga(const Oferta &oferta) {
    string errors[4] = {"Denumire invalida!", "Destionatie invalida!", "Tip invalid!", "Pret invalid!"};
    auto validationResult = validator_oferta(oferta);

    if (validationResult.has_value())
        throw ServiceException(string("Eroare: ") + errors[validationResult.value() - 1]);
    try{
        find(oferta.getDenumire());
        throw std::domain_error("Exista deja o oferta cu acest nume!");
    }catch (const ServiceException &){
        repo.add(oferta);
        types[oferta.getTip()]++;
        ActiuniUndo.push_back(std::make_unique<UndoAdauga>(*this, oferta.getDenumire()));
    }
}

Oferta Service::find(const string &denumire) {
    auto offers = get_all();
    auto iterator = std::find_if(offers.begin(),
                                 offers.end(),
                                 [denumire](const Oferta& oferta) {
                                        return (oferta.getDenumire() == denumire);
    });
    if (iterator == offers.end()) {
        throw ServiceException("Nu exista oferta!");
    }
    return *iterator;
}

void Service::sterge(const string &denumire) {
    find(denumire);

    auto remove = std::remove_if(get_all().begin(), get_all().end(), [&](const Oferta& oferta) {
        return (oferta.getDenumire() == denumire);
    });
    types[remove->getTip()]--;
    ActiuniUndo.push_back(std::make_unique<UndoSterge>(*this, *remove));
    get_all().erase(remove);
}

void Service::modifica(const string &denumire, const string &atribut, const string &valoare_noua) {
    for (auto &oferta: get_all()) {

        if (oferta.getDenumire() == denumire) {
            if (atribut == "destinatie") {
                ActiuniUndo.push_back(std::make_unique<UndoModifica>(*this, denumire, atribut, oferta.getDestinatie()));
                oferta.setDestinatie(valoare_noua);
            } else if (atribut == "denumire") {
                ActiuniUndo.push_back(std::make_unique<UndoModifica>(*this, valoare_noua, atribut, oferta.getDenumire()));
                oferta.setDenumire(valoare_noua);
            } else if (atribut == "pret") {
                ActiuniUndo.push_back(std::make_unique<UndoModifica>(*this, denumire, atribut, oferta.getPret()));
                oferta.setPret(stoi(valoare_noua));
            } else if (atribut == "tip") {
                types[oferta.getTip()]--;
                types[valoare_noua]++;
                ActiuniUndo.push_back(std::make_unique<UndoModifica>(*this, denumire, atribut, oferta.getTip()));
                oferta.setTip(valoare_noua);
            }
            break;
        }
    }
}

vector<Oferta> Service::filtreaza(const string &atribut, const string &value) {
    vector<Oferta> res;
    std::copy_if(get_all().begin(),
                 get_all().end(),
                 std::back_inserter(res),
                 [&](const Oferta &oferta) {
                    return filter_compare(oferta, atribut, value);
                 });

    return res;
}

void Service::sorteaza(const string &atribut) {
    std::sort(get_all().begin(),
              get_all().end(),
              [atribut](const Oferta &oferta1, const Oferta &oferta2) {
                  return sort_compare(oferta1, oferta2, atribut);
              });
}

bool Service::filter_compare(const Oferta &oferta, const std::string &key, const std::string &value) {
    if (key == "destinatie") {
        return oferta.getDestinatie() == value;
    } else if (key == "pret") {
        return oferta.getPret() <= stoi(value);
    }
    return false;
}

bool Service::sort_compare(const Oferta &oferta1, const Oferta &oferta2, const std::string &key) {
    if (key == "denumire") {
        return oferta1.getDenumire() < oferta2.getDenumire();
    } else if (key == "destinatie") {
        return oferta1.getDestinatie() < oferta2.getDestinatie();
    } else if (key == "tip") {
        return oferta1.getTip() < oferta2.getTip();
    } else if (key == "pret") {
        return oferta1.getPret() < oferta2.getPret();
    }
    return false;
}

void Service::add_to_wl(const Oferta &oferta) {
    if(get_all().empty())
        throw ServiceException("Nu se pot adauga oferte in wl(nu exista oferte)!");
    wl.add(oferta);
}

void Service::clear_wl() {
    wl.clear();
}

size_t Service::generate_wl() {
    if(get_all().empty())
        throw ServiceException("Nu se pot adauga oferte in wl(nu exista oferte)!");
    return wl.generate(get_all());
}

vector<Oferta> &Service::get_wl() {
    return wl.get_all();
}

void Service::export_wl(string &filename) {
    std::ofstream out(filename);
    out << "<!DOCTYPE html>\n"
           "<html>\n"
           "    <head>\n"
           "        <title>Wishlist</title>\n"
           "    </head>\n"
           "<body>\n"
           "    <h1>Wishlist</h1>\n"
           "    <ul>\n";

    for (const auto &oferta: get_wl()) {
        out << "        <li>" << "Denumire: " <<oferta.getDenumire() << " " << "Destinatie: " << oferta.getDestinatie() << " "
            << "Tip: " << oferta.getTip() << " " << "Pret: " << oferta.getPret() << "</li>";
    }

    out << "    </ul>\n"
           "    </body>\n"
           "</html>\n";
    out.close();
}

map<string, int> Service::get_types(){
    return types;
}

void Service::undo(){
    if(ActiuniUndo.empty())
        throw ServiceException("Nu se poate efecuta undo!");
    ActiuniUndo.back()->doUndo();
    ActiuniUndo.pop_back();
}
