#include "ui.h"

void UI::main_menu() {
    int running = 1;
    while (running) {
        cout << "1. Admin menu\n";
        cout << "2. User menu\n";
        cout << "0. Exit\n";

        char option = 0;
        cin >> option;
        getchar();
        switch (option) {
            case '1':
                admin_menu();
                break;
            case '2':
                user_menu();
                break;
            case '0':
                cout << "Out\n";
                running = 0;
                break;
            default:
                cout << "Optiune invalida!";
        }
    }
}

void UI::admin_menu() {
    int running = 1;
    while (running) {
        display_admin_menu();
        char option = 0;
        cin >> option;
        getchar();
        switch (option) {
            case '1':
                adauga_oferta();
                break;
            case '2':
                sterge_oferta();
                break;
            case '3':
                modifica_oferta();
                break;
            case '4':
                print_all(service.get_all());
                break;
            case '5':
                cauta_oferta();
                break;
            case '6':
                filtreaza_oferte();
                break;
            case '7':
                sorteaza_oferte();
                break;
            case '8':
                afiseaza_tipurile();
                break;
            case '9':
                undo();
                break;
            case '0':
                cout << "Out\n";
                running = 0;
                break;
            default:
                cout << "Optiune invalida!";
        }
    }

}

void UI::user_menu() {
    int running = 1;
    while (running) {
        display_user_menu();
        char option = 0;
        cin >> option;
        getchar();
        switch (option) {
            case '1':
                adauga_oferta_wl();
                break;
            case '2':
                genereaza_wl();
                break;
            case '3':
                goleste_wl();
                break;
            case '4':
                export_wl();
                break;
            case '0':
                cout << "Out\n";
                running = 0;
                break;
            default:
                cout << "Optiune invalida!";
        }
    }
}

void UI::print_all(const vector<Oferta> &vec) {
    for (const auto &oferta: vec) {
        printf("Denumire: %s  Destinatie: %s  Tip: %s Pret: %d\n", oferta.getDenumire().c_str(),
               oferta.getDestinatie().c_str(), oferta.getTip().c_str(),
               oferta.getPret());
    }
}

void UI::display_admin_menu() {
    cout << "1. Adauga oferta\n";
    cout << "2. Sterge oferta\n";
    cout << "3. Modifica oferta\n";
    cout << "4. Afiseaza toate ofertele\n";
    cout << "5. Cauta o oferta\n";
    cout << "6. Filtrare oferte\n";
    cout << "7. Sortare oferte\n";
    cout << "8. Afiseaza tipurile\n";
    cout << "9. Undo\n";
    cout << "0. Back\n";
}

void UI::display_user_menu() {
    cout << "1. Adauga oferta in wishllist\n";
    cout << "2. Genereaza wishllist\n";
    cout << "3. Goleste wishllist\n";
    cout << "4. Exporta wishllist\n";
    cout << "0. Back\n";
}

void UI::adauga_oferta() {
    string denumire, destinatie, tip, pret_string;
    cout << "Denumire: ";
    cin >> denumire;

    cout << "Desinatie: ";
    cin >> destinatie;

    cout << "Tip: ";
    cin >> tip;

    cout << "Pret: ";
    cin >> pret_string;

    try {
        int pret = std::stoi(pret_string);

        Oferta o(denumire, destinatie, tip, pret);
        service.adauga(o);

        cout << "Oferta adaugata!\n";
    } catch (const std::domain_error &e) {
        cout << e.what() << "\n";
    } catch (const std::invalid_argument &) {
        cout << "Pretul trebuie sa fie un numar!\n";
    }
}

void UI::sterge_oferta() {
    string denumire;
    cout << "Denumire: ";
    cin >> denumire;
    try {
        service.sterge(denumire);
        cout << "Oferta a fost stearsa!\n";
    } catch (const ServiceException &e) {
        cout << e.what() << "\n";
    }

}

void UI::modifica_oferta() {
    string denumire, atribut, new_val;

    cout << "Ce oferta?: ";
    cin >> denumire;
    try {
        auto search_result = service.find(denumire);

        cout << "Ce atribut?: ";
        cin >> atribut;
        if (atribut != "destinatie" and atribut != "pret" and atribut != "tip" and atribut != "denumire") {
            cout << "Atribut invalid!";
            return;
        }

        cout << "Cu ce?: ";
        cin >> new_val;

        service.modifica(denumire, atribut, new_val);
    }
    catch (const ServiceException &e) {
        cout << e.what() << "\n";
    }
}

void UI::cauta_oferta() {
    string denumire;
    cin >> denumire;
    try {
        auto search_result = service.find(denumire);
        cout << "Oferta a fost gasita!\n";
        Oferta oferta = search_result;
        printf("Denumire: %s  Destinatie: %s  Tip: %s Pret: %d\n", oferta.getDenumire().c_str(),
               oferta.getDenumire().c_str(),
               oferta.getTip().c_str(), oferta.getPret());
    } catch (const ServiceException &e) {
        cout << e.what() << "\n";
    }
}

void UI::filtreaza_oferte() {
    string atribut, value;

    cout << "In functie de ce?(destinatie/pret): ";
    cin >> atribut;
    if (atribut != "destinatie" and atribut != "pret") {
        cout << "Atribut invalid!";
        return;
    }

    if (atribut == "destinatie")
        cout << "Unde?: ", cin >> value;

    if (atribut == "pret") {
        cout << "Cat?: ", cin >> value;
        try {
            stoi(value);
        } catch (const std::invalid_argument &) {
            cout << "Pretul trebuie sa fie un numar!";
        }
    }
    vector<Oferta> res = service.filtreaza(atribut, value);

    print_all(res);
}

void UI::sorteaza_oferte() {
    string atribut;

    cout << "In functie de ce?: ";
    cin >> atribut;

    if (atribut != "destinatie" and atribut != "pret" and atribut != "tip" and atribut != "denumire") {
        cout << "Atribut invalid!";
        return;
    }

    service.sorteaza(atribut);
    print_all(service.get_all());
}

void UI::adauga_oferta_wl() {
    string denumire;
    cout << "Introduceti numele ofertei: ";
    cin >> denumire;
    try {
        auto search_result = service.find(denumire);
        service.add_to_wl(search_result);
        cout << "Oferta adaugata!\n";
        printf("Sunt %zu oferte in cos!\n", service.get_wl().size());
    } catch (const ServiceException &e) {
        cout << e.what() << "\n";
    }

}

void UI::goleste_wl() {
    service.clear_wl();
    cout << "Wishlistul a fost sters!\n";
    printf("Sunt %zu oferte in cos!\n", service.get_wl().size());
}

void UI::genereaza_wl() {
    try {
        size_t nr_of_vals = service.generate_wl();
        printf("Au fost adaugate %zu oferte\n", nr_of_vals);
        printf("Sunt %zu oferte in cos!\n", service.get_wl().size());
    }
    catch (const ServiceException &e) {
        cout << e.what();
    }
}

void UI::afiseaza_wl() {
    if (service.get_wl().empty()) {
        cout << "Nu exista oferte in wishlist!\n";
        return;
    }
    print_all(service.get_wl());
}

void UI::export_wl() {
    if (service.get_wl().empty()) {
        cout << "Nu exista oferte in wishlist!\n";
        return;
    }
    string filename;

    cout << "Introduceti numele fisierului: ";
    cin >> filename;
    filename += ".html";

    service.export_wl(filename);
    printf("Au fost exportate %zu oferte!\n", service.get_wl().size());
}

void UI::afiseaza_tipurile() {
    for (const auto &type: service.get_types()) {
        printf("Tip: %s     Aparitii:%d\n", type.first.c_str(), type.second);
    }
}

void UI::undo() {
    try {
        service.undo();
    } catch (const ServiceException &e) {
        cout << e.what();
    }
}
