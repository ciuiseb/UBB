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

class ModifyOfferWindow : public QWidget {
Q_OBJECT

    Service &service;
    QLineEdit *denumireField, *newValField;
    QComboBox *atributField;
public:
    explicit ModifyOfferWindow(Service &service, QWidget *parent = nullptr) : QWidget(parent), service(service) {
        auto modifyFLayout = new QFormLayout;
        auto modifyVLayout = new QVBoxLayout;

        setLayout(modifyVLayout);
        modifyVLayout->addLayout(modifyFLayout);

        denumireField = new QLineEdit;
        modifyFLayout->addRow("Denumire:", denumireField);

        atributField = new QComboBox;
        atributField->addItem("denumire");
        atributField->addItem("destinatie");
        atributField->addItem("tip");
        atributField->addItem("pret");

        modifyFLayout->addRow("Atribut", atributField);

        newValField = new QLineEdit;
        modifyFLayout->addRow("Valore noua:", newValField);

        auto modifyButton = new QPushButton("Modifica");
        modifyVLayout->addWidget(modifyButton);
        connect(modifyButton,
                &QPushButton::clicked,
                this,
                &ModifyOfferWindow::modifyOffer
        );
    }

signals:

    void modifiedStatus(const QString &status);

private slots:

    void modifyOffer() {
        auto denumire = denumireField->text();
        auto atribut = atributField->currentText();
        auto newVal = newValField->text();

        try {
            auto search_result = service.find(denumire.toStdString());
            service.modifica(denumire.toStdString(), atribut.toStdString(), newVal.toStdString());
        }
        catch (const ServiceException &e) {
            emit modifiedStatus(e.what());
            return;
        }

        emit modifiedStatus("Oferta a fost modificata!");
    }
};