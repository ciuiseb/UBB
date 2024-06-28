//
// Created by ciuis on 5/9/2024.
//

#pragma once

#include <QPushButton>
#include "QWidget"
#include "QLineEdit"
#include "QLabel"
#include "../service.h"
#include <QFormLayout>
#include "QVBoxLayout"
#include "QString"

class SearchOfferWindow : public QWidget {
Q_OBJECT

    Service &service;
    QLineEdit *denumireField;
public:
    explicit SearchOfferWindow(Service &service, QWidget *parent = nullptr) : QWidget(parent), service(service) {
        auto searchWindowFLayout = new QFormLayout;
        setLayout(searchWindowFLayout);

        denumireField = new QLineEdit{};
        searchWindowFLayout->addRow("Introduceti denumirea:", denumireField);

        auto searchButton = new QPushButton("Cauta");
        searchWindowFLayout->addWidget(searchButton);
        connect(searchButton,
                &QPushButton::clicked,
                this,
                &SearchOfferWindow::searchOffer
        );
    }

signals:

    void searchResult(QString status);

private slots:

    void searchOffer() {
        auto denumire = denumireField->text();
        try {
            auto oferta = service.find(denumire.toStdString());
            emit searchResult(QString::fromStdString(oferta.getDenumire()));
        } catch (ServiceException &e) {
            emit searchResult(e.what());
            return;
        }
    }

};
