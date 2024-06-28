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
#include "QComboBox"
#include "QListWidget"


class FilterOffersWindow : public QWidget {
Q_OBJECT

    Service &service;
    QComboBox *atributField;
    QLineEdit *valueField;
    QWidget*restultWindow;

public:
    void close_result(){
        restultWindow->close();
    }
public:
    explicit FilterOffersWindow(Service &service, QWidget *parent = nullptr) : QWidget(parent), service(service) {
        auto filterFLayout = new QFormLayout;
        auto filterVLayout = new QVBoxLayout;

        setLayout(filterVLayout);
        filterVLayout->addLayout(filterFLayout);

        atributField = new QComboBox;
        atributField->addItem("destinatie");
        atributField->addItem("pret");

        filterFLayout->addRow("Atribut", atributField);

        valueField = new QLineEdit;
        filterFLayout->addRow("Valoare:", valueField);

        auto filterButton = new QPushButton("Filtreaza");
        filterVLayout->addWidget(filterButton);
        connect(filterButton,
                &QPushButton::clicked,
                this,
                &FilterOffersWindow::filterOffer
        );
    }

signals:

    void filterStatus(const QString &status);

private slots:

    void filterOffer() {
        auto atribut = atributField->currentText();
        auto valueString = valueField->text();
        if (atribut == "pret") {
            bool converted;
            valueString.toInt(&converted);
            if (!converted) {
                emit filterStatus("Pretul trebuie sa fie un numar!");
                return;
            }
        }

        auto offers = service.filtreaza(atribut.toStdString(), valueString.toStdString());

        restultWindow = new QWidget;
        auto layout = new QVBoxLayout;
        restultWindow->setLayout(layout);
        auto offersList = new QListWidget;
        layout->addWidget(offersList);
        for (const auto &offer: offers) {
//            offersList->addItem(offer.toQString());
        }
        auto okButton = new QPushButton("OK");
        layout->addWidget(okButton);
        connect(okButton,
                &QPushButton::clicked,
                this,
                [=, this]{emit filterStatus("");});
        this->close();
        restultWindow->show();
    }
};
