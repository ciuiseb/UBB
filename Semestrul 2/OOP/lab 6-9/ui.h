#pragma once

#include <utility>

#include "service.h"
#include "iostream"
#include "cstdio"
#include <cstdio>
#include "fstream"

using std::cin;
using std::cout;

class UI {
    Service& service;
public:
    explicit UI(Service& service) : service(service) {};

    void main_menu();

    static void display_admin_menu();

    void admin_menu();

    static void display_user_menu();

    void user_menu();

    void adauga_oferta();

    void sterge_oferta();

    void modifica_oferta();

    static void print_all(const vector<Oferta>& vec);

    void cauta_oferta();

    void filtreaza_oferte();

    void sorteaza_oferte();

    void afiseaza_tipurile();

    void adauga_oferta_wl();

    void goleste_wl();

    void genereaza_wl();

    void afiseaza_wl();

    void export_wl();

    void undo();

};