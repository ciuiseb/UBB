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

class DeleteOfferWindow : public QWidget {
Q_OBJECT

    Service &service;
    QLineEdit *denumireField;
public:
    DeleteOfferWindow(Service &service, QWidget *parent = nullptr) : QWidget(parent), service(service) {
        auto deleteWindowFLayout = new QFormLayout;
        setLayout(deleteWindowFLayout);

        denumireField = new QLineEdit{};
        deleteWindowFLayout->addRow("Introduceti denumirea:", denumireField);

        auto deleteButton = new QPushButton("Sterge");
        deleteWindowFLayout->addWidget(deleteButton);
        connect(deleteButton,
                &QPushButton::clicked,
                this,
                &DeleteOfferWindow::deleteOffer
        );
    }

signals:

    void deletedStatus(QString status);

private slots:

    void deleteOffer() {
        auto denumire = denumireField->text();
        try {
            service.sterge(denumire.toStdString());
        } catch (ServiceException &e) {
            emit deletedStatus(e.what());
            return;
        }
        emit deletedStatus("Oferta a fost stearsa!");
    }
};