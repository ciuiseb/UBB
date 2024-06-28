#include "tests.h"

void all_tests() {
    test_oferta();
    test_offers_repo();
    test_service();
    test_wishlist();
    test_undo();
}

void test_oferta() {
    Oferta oferta("Denumire", "Destinatie", "Tip", 100);
    ///Getters and setters
    assert(oferta.getDenumire() == "Denumire");
    assert(oferta.getDestinatie() == "Destinatie");
    assert(oferta.getTip() == "Tip");
    assert(oferta.getPret() == 100);

    oferta.setDenumire("New Denumire");
    assert(oferta.getDenumire() == "New Denumire");

    oferta.setDestinatie("New Destinatie");
    assert(oferta.getDestinatie() == "New Destinatie");

    oferta.setTip("New Tip");
    assert(oferta.getTip() == "New Tip");

    oferta.setPret(100);
    assert(oferta.getPret() == 100);
}

void test_offers_repo() {
    Oferta oferta("Denumire", "Destinatie", "Tip", 100);
    AbstractRepo*repo = new OffersRepo();
    ///Add to repo
    repo->add(oferta);

    assert(repo->len() == 1);
    ///Get all
    assert(repo->getAll()[0].getPret() == 100);

    delete repo;
}

void test_service() {
    AbstractRepo*repo = new OffersRepo();
    Service service(*repo);

    Oferta oferta("Denumire", "Destinatie", "Tip", 100);
    ///test validare input
    oferta.setDenumire("");
    try {
        service.adauga(oferta);
        assert(false);
    } catch (const ServiceException &e) {
        assert(true);
    }
    oferta.setDenumire("Denumire");
    oferta.setDestinatie("");
    try {
        service.adauga(oferta);
        assert(false);
    } catch (const ServiceException &e) {
        assert(true);
    }
    oferta.setDestinatie("Destinatie");
    oferta.setPret(-100);
    try {
        service.adauga(oferta);
        assert(false);
    } catch (const ServiceException &e) {
        assert(true);
    }
    ///test adaugare
    oferta.setPret(100);
    service.adauga(oferta);
    assert(service.len() == 1);
    ///test validare exisistenta
    try {
        service.adauga(oferta);
        assert(false);
    } catch (const std::domain_error &e) {
        assert(true);
    }
    ///test find
    auto o = service.find("Denumire");

    try {
        o = service.find("Denumiraaae");
        assert(false);
    } catch (const ServiceException &e) {
        assert(true);
    }

    try {
        service.find("aaa");
        assert(false);
    } catch (const ServiceException &e) {
        assert(true);
    }

    ///test sterge

    service.sterge("Denumire");
    assert(service.len() == 0);

    ///test modifica
    service.adauga(oferta);

    service.modifica(oferta.getDenumire(), "destinatie", "Destinatie noua");
    assert(service.get_all()[0].getDestinatie() == "Destinatie noua");

    service.modifica(oferta.getDenumire(), "tip", "Tip nou");
    assert(service.get_all()[0].getTip() == "Tip nou");

    service.modifica(oferta.getDenumire(), "pret", "991");
    assert(service.get_all()[0].getPret() == 991);

    service.modifica(oferta.getDenumire(), "denumire", "Denumire noua");
    assert(service.get_all()[0].getDenumire() == "Denumire noua");

    ///test filtreaza

    assert(service.filtreaza("destinatie", "Destinatie noua").size() == 1);

    assert(service.filtreaza("pret", "100").size() == 0);

    assert(service.filtreaza("pret", "1000").size() == 1);

    service.adauga(oferta);
    ///test sorteaza
    service.sorteaza("pret");
    assert(service.get_all()[0].getDenumire() == "Denumire");

    service.sorteaza("denumire");
    assert(service.get_all()[0].getDenumire() == "Denumire");

    service.sorteaza("destinatie");
    assert(service.get_all()[0].getDenumire() == "Denumire");

    service.sorteaza("tip");
    assert(service.get_all()[0].getDenumire() == "Denumire");

    assert(service.filter_compare(service.get_all()[0], "aaaaaa", "100") == false);

    assert(service.sort_compare(service.get_all()[0], service.get_all()[0], "aaaaaa") == false);

    ///test wishlist methods
    service.add_to_wl(oferta);
    assert(service.get_wl().size() == 1);

    service.clear_wl();
    assert(service.get_wl().empty());

    service.generate_wl();
    assert(!service.get_wl().empty());
    delete repo;
}

void test_wishlist() {
    Wishlist wl;
    Oferta oferta("Denumire", "Destinatie", "Tip", 100);

    wl.add(oferta);
    assert(wl.get_all().size() == 1);
    assert(wl.get_all()[0].getPret() == 100);

    wl.clear();
    assert(wl.get_all().empty());

    vector <Oferta> db;
    db.push_back(oferta);
    db.push_back(oferta);
    db.push_back(oferta);

    wl.generate(db);

    assert(!wl.get_all().empty());
}

void test_undo(){
    AbstractRepo*repo = new OffersRepo();
    Service service(*repo);

    Oferta oferta("Denumire", "Destinatie", "Tip", 100);

    ///undo adauga
    service.adauga(oferta);
    assert(service.len() == 1);
    service.undo();
    assert(service.len() == 0);

    ///undo sterge
    service.adauga(oferta);
    service.sterge("Denumire");
    assert(service.len() == 0);
    service.undo();
    assert(service.len() == 1);

    ///undo modifica
    service.modifica(oferta.getDenumire(), "destinatie", "Destinatie noua");
    assert(service.get_all()[0].getDestinatie() == "Destinatie noua");
    service.undo();
    assert(service.get_all()[0].getDestinatie() == "Destinatie");

    service.modifica(oferta.getDenumire(), "tip", "Tip nou");
    assert(service.get_all()[0].getTip() == "Tip nou");
    service.undo();
    assert(service.get_all()[0].getTip() == "Tip");

    service.modifica(oferta.getDenumire(), "pret", "991");
    assert(service.get_all()[0].getPret() == 991);
    service.undo();
    assert(service.get_all()[0].getPret() == 100);

    service.modifica(oferta.getDenumire(), "denumire", "Denumire noua");
    assert(service.get_all()[0].getDenumire() == "Denumire noua");
    service.undo();
    assert(service.get_all()[0].getDenumire() == "Denumire");

    delete repo;
}
