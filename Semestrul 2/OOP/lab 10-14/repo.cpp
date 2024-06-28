#include "repo.h"
#include "fstream"
vector<Oferta> &OffersRepo::getAll() {
    return elems;
}

void OffersRepo::add(const Oferta &oferta) {
    elems.push_back(oferta);
}

size_t OffersRepo::len() {
    return elems.size();
}

vector<Oferta> &OffersFileRepo::getAll() {
    return elems;
}

void OffersFileRepo::add(const Oferta &oferta) {
    elems.push_back(oferta);
}

size_t OffersFileRepo::len() {
    return elems.size();
}

void OffersFileRepo::readFromFile() {

    std::ifstream f(filePath);
    char line[256];
    while (f.getline(line, sizeof(line))) {
        vector<string> parts;
        char *token = strtok(line, ",");
        while (token) {
            parts.push_back(token);
            token = strtok(nullptr, ",");
        }
        elems.emplace_back(parts[0], parts[1], parts[2], stoi(parts[3]));
    }
    f.close();
}
