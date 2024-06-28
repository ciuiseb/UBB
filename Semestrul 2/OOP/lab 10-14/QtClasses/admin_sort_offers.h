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
#include "QComboBox"

class SortOffersWindow : public QWidget {
Q_OBJECT

    Service &service;
    QComboBox *atributField;
public:
    explicit SortOffersWindow(Service &service, QWidget *parent = nullptr) : QWidget(parent), service(service) {
        auto sortFLayout = new QFormLayout;
        auto sortVLayout = new QVBoxLayout;

        setLayout(sortVLayout);
        sortVLayout->addLayout(sortFLayout);

        atributField = new QComboBox;
        atributField->addItem("denumire");
        atributField->addItem("destinatie");
        atributField->addItem("tip");
        atributField->addItem("pret");

        sortFLayout->addRow("Atribut", atributField);


        auto sortButton = new QPushButton("Sorteaza");
        sortVLayout->addWidget(sortButton);
        connect(sortButton,
                &QPushButton::clicked,
                this,
                &SortOffersWindow::sortOffer
        );
    }

signals:

    void sortStatus(const QString &status);

private slots:

    void sortOffer() {
        auto atribut = atributField->currentText();

        service.sorteaza(atribut.toStdString());
        emit sortStatus("Ofertele au fost sortate!");
    }
};