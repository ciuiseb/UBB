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

class AddOfferToWlWindow : public QWidget {
Q_OBJECT

    Service &service;
    QLineEdit *denumireField;
public:
    explicit AddOfferToWlWindow(Service &service, QWidget *parent = nullptr) : QWidget(parent), service(service) {
        auto addWindowFLayout = new QFormLayout;
        setLayout(addWindowFLayout);

        denumireField = new QLineEdit;
        addWindowFLayout->addRow("Introduceti denumirea:", denumireField);

        auto addButton = new QPushButton("Adauga");
        addWindowFLayout->addWidget(addButton);
        connect(addButton,
                &QPushButton::clicked,
                this,
                &AddOfferToWlWindow::addOffer
        );
    }

signals:

    void addedStatus(const QString& status);

private slots:

    void addOffer() {
        auto denumire = denumireField->text();
        try {
            auto search_result = service.find(denumire.toStdString());
            service.add_to_wl(search_result);
        } catch (ServiceException &e) {
            emit addedStatus(e.what());
            return;
        }
        emit addedStatus("Oferta adaugata!");
    }
};
