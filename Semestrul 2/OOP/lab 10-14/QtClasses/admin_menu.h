//
// Created by ciuis on 5/9/2024.
//

#pragma once

#include <QPushButton>
#include "QWidget"
#include "../service.h"
#include "QHBoxLayout"
#include "QVBoxLayout"
#include "QListWidget"
#include "admin_add_offer.h"
#include "admin_delete_offer.h"
#include "admin_modify_offer.h"
#include "admin_search_offer.h"
#include "admin_sort_offers.h"
#include "admin_filter_offers.h"
#include "my_table_model.h"
#include "QTableView"
#include "QDebug"

class AdminWindow : public QWidget {
Q_OBJECT

    Service&service;
    QTableView*offersList;
    QHBoxLayout*typesLayout;
    vector<QPushButton*>typesBtns;
    MyTableModel*model;

public:
    explicit AdminWindow(Service &service, QWidget *parent = nullptr) : QWidget(parent), service(service) {
        resize(550, 400);
        auto adminHLayout = new QHBoxLayout{};
        setLayout(adminHLayout);
        auto adminVLayout = new QVBoxLayout{};
        adminHLayout->addLayout(adminVLayout);


        offersList = new QTableView;
        model = new MyTableModel(service.get_all());
        offersList->setModel(model);
        adminHLayout->addWidget(offersList);

        auto addOffer = new QPushButton("Adauga oferta");
        adminVLayout->addWidget(addOffer);
        connect(
                addOffer,
                &QPushButton::clicked,
                this,
                &AdminWindow::addOffer
        );

        auto deleteOffer = new QPushButton("Sterge oferta");
        adminVLayout->addWidget(deleteOffer);
        connect(
                deleteOffer,
                &QPushButton::clicked,
                this,
                &AdminWindow::deleteOffer
        );

        auto modifyOffer = new QPushButton("Modifica oferta");
        adminVLayout->addWidget(modifyOffer);
        connect(modifyOffer,
                &QPushButton::clicked,
                this,
                &AdminWindow::modifyOffer
        );

        auto searchOffer = new QPushButton("Cauta oferta");
        adminVLayout->addWidget(searchOffer);
        connect(searchOffer,
                &QPushButton::clicked,
                this,
                &AdminWindow::searchOffer
        );

        auto filterOffers = new QPushButton("Filtreaza ofertele");
        adminVLayout->addWidget(filterOffers);
        connect(filterOffers,
                &QPushButton::clicked,
                this,
                &AdminWindow::filterOffers);

        auto sortOffers = new QPushButton("Sorteaza ofertele");
        adminVLayout->addWidget(sortOffers);
        connect(sortOffers,
                &QPushButton::clicked,
                this,
                &AdminWindow::sortOffers
        );
        auto undoButton = new QPushButton("Undo");
        adminVLayout->addWidget(undoButton);
        connect(
                undoButton,
                &QPushButton::clicked,
                this,
                &AdminWindow::undo
        );
        auto backFromAdmin = new QPushButton("Back");
        adminVLayout->addWidget(backFromAdmin);
        connect(
                backFromAdmin,
                &QPushButton::clicked,
                this,
                &AdminWindow::showMainMenu
        );
        typesLayout = new QHBoxLayout;
        adminVLayout->addLayout(typesLayout);
    }

signals:

    void backToMainMenu();

private slots:

    void addOffer();

    void deleteOffer();

    void modifyOffer();

    void searchOffer();

    void filterOffers();

    void sortOffers();

    void refreshOfferList();

    void undo();

    void showMainMenu();

    void refreshTypes();

    void createTypeBtn(const std::pair<const string, int>&);

    void msgBox(const QString& text);
};