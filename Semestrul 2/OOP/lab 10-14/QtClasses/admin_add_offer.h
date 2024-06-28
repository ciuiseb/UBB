//
// Created by ciuis on 5/9/2024.
//

#pragma once

#include <QPushButton>
#include "QWidget"
#include "QLineEdit"
#include "../service.h"
#include <QFormLayout>
#include "QVBoxLayout"



class AddOfferWindow : public QWidget {
Q_OBJECT

    Service &service;
    QLineEdit *denumireField;
    QLineEdit *destinatieField;
    QLineEdit *tipField;
    QLineEdit *pretField;

public:
    explicit AddOfferWindow(Service &service, QWidget *parent = nullptr) : QWidget(parent), service(service) {
        auto addWindowFLayout = new QFormLayout{};
        auto addWindowVLayout = new QVBoxLayout{};

        setLayout(addWindowVLayout);
        addWindowVLayout->addLayout(addWindowFLayout);

        denumireField = new QLineEdit;
        addWindowFLayout->addRow("Denumire:", denumireField);

        destinatieField = new QLineEdit;
        addWindowFLayout->addRow("Destinatie:", destinatieField);

        tipField = new QLineEdit;
        addWindowFLayout->addRow("Tip:", tipField);

        pretField = new QLineEdit;
        addWindowFLayout->addRow("Pret:", pretField);

        auto addButton = new QPushButton{"Adauga"};
        addWindowVLayout->addWidget(addButton);
        connect(
                addButton,
                &QPushButton::clicked,
                this,
                &AddOfferWindow::addOffer
        );
    }

signals:

    void addedStatus(const QString& status);


private slots:

    void addOffer() {
        auto denumire = denumireField->text();
        auto destinatie = destinatieField->text();
        auto tip = tipField->text();
        auto pretString = pretField->text();

        bool converted;
        auto pret = pretString.toInt(&converted);
        if (!converted) {
            emit addedStatus("Pretul trebuie sa fie un numar!");
            return;
        }
        try{
            Oferta o(denumire.toStdString(), destinatie.toStdString(), tip.toStdString(), pret);
            service.adauga(o);
        }catch(ServiceException&e){
            emit addedStatus(e.what());
            return;
        }
        emit addedStatus("Oferta adaugata!");
    }
};