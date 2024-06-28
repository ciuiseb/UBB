#include "wishlist.h"
#include "random"
vector<Oferta>& Wishlist::get_all()
{
    return elems;
}
void Wishlist::add(const Oferta& oferta){
    Oferta oferta_noua = oferta;
    elems.push_back(oferta_noua);
}
void Wishlist::clear(){
    elems.clear();
}
size_t Wishlist::generate(const vector<Oferta>& db){
    clear();

    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<size_t>dist(1, db.size());
    size_t wl_size = dist(gen);

    vector<Oferta>shuffeled(db.begin(), db.end());
    std::shuffle(shuffeled.begin(), shuffeled.end(), gen);
    for (int index = 0; index < wl_size; ++index) {
        add(shuffeled.at(index));
    }
    return wl_size;
}