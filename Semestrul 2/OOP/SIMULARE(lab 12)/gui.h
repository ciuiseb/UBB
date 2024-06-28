//
// Created by ciuis on 5/22/2024.
//

#ifndef SIMULARE_GUI_H
#define SIMULARE_GUI_H

#include "service.h"
#include <QWidget>
#include "QTableWidget"
#include "QHBoxLayout"
#include "QLineEdit"
#include "QFormLayout"
#include "QMessageBox"
#include "QRadioButton"
#include "QVBoxLayout"

class GUI : public QWidget {
    Service &service;
    QTableWidget *tabel;

    QLineEdit *id;
    QLineEdit *badV;
    QLineEdit *goodV;
    QLineEdit *tip;
    QLineEdit *severity;

    QRadioButton *sortByGoodV;
    QRadioButton *sortByTip;
    QRadioButton *sortBySeverity;
public:
    GUI(Service &service, QWidget *parent = nullptr) :
            service(service), QWidget(parent) {
//        resize(500,500);
        auto hLayout = new QHBoxLayout;
        setLayout(hLayout);

        tabel = new QTableWidget;
        hLayout->addWidget(tabel);
        service.sortBySeverity();
        populateTable(service.getAll());

        id = new QLineEdit;
        badV = new QLineEdit;
        goodV = new QLineEdit;
        tip = new QLineEdit;
        severity = new QLineEdit;

        auto fLayour = new QFormLayout;
        hLayout->addLayout(fLayour);
        fLayour->addRow("Id: ", id);
        fLayour->addRow("Forma gresita: ", badV);
        fLayour->addRow("forma corecta: : ", goodV);
        fLayour->addRow("Tip: ", tip);
        fLayour->addRow("Severitate: ", severity);

        auto addButton = new QPushButton("Adauga!");
        fLayour->addWidget(addButton);
        connect(addButton,
                &QPushButton::clicked,
                [&] {
                    if (id->text().isEmpty() || badV->text().isEmpty() || goodV->text().isEmpty() ||
                        tip->text().isEmpty() || severity->text().isEmpty()) {
                        errorMessage("Campurile trebuie sa fie completate!");
                        return;
                    }
                    try {
                        service.add(id->text().toInt(), badV->text().toStdString(), goodV->text().toStdString(),
                                    tip->text().toStdString(), severity->text().toStdString());
                        if (sortByTip->isActiveWindow())
                            service.sortByType();

                        if (sortByGoodV->isActiveWindow())
                            service.sortByGoodV();

                        if (sortBySeverity->isActiveWindow())
                            service.sortBySeverity();
                        populateTable(service.getAll());
                    } catch (std::domain_error &e) {
                        errorMessage(QString::fromStdString(e.what()));
                    }

                });

        auto vLayout = new QVBoxLayout;
        hLayout->addLayout(vLayout);
        sortByGoodV = new QRadioButton("Sortare in functie de versiunea corecta");
        sortByTip = new QRadioButton("Sortare in functie de tip");
        sortBySeverity = new QRadioButton("Sortare in functie de severitate");
        sortBySeverity->click();
        vLayout->addWidget(sortBySeverity);
        vLayout->addWidget(sortByGoodV);
        vLayout->addWidget(sortByTip);
        connect(sortByGoodV,
                &QPushButton::clicked,
                [&] {
                    service.sortByGoodV();
                    populateTable(service.getAll());
                });
        connect(sortBySeverity,
                &QPushButton::clicked,
                [&] {
                    service.sortBySeverity();
                    populateTable(service.getAll());
                });
        connect(sortByTip,
                &QPushButton::clicked,
                [&] {
                    service.sortByType();
                    populateTable(service.getAll());
                });

    }

private:
    void errorMessage(QString m) {
        auto msg = new QMessageBox;
        msg->setText(m);
        msg->addButton(QMessageBox::Ok);
        msg->show();
    }

    void populateTable(vector<Caz> vec) {
        tabel->clear();
        tabel->setRowCount(service.getAll().size());
        tabel->setColumnCount(5);
        for (int i = 0; i < vec.size(); ++i) {
            auto caz = *(vec.begin() + i);
            auto type = new QTableWidgetItem(QString::fromStdString(caz.getType()));
            auto severity = new QTableWidgetItem(QString::fromStdString(caz.getSeverity()));
            tabel->setItem(i, 0, new QTableWidgetItem(QString::number(caz.getId())));
            tabel->setItem(i, 1, new QTableWidgetItem(QString::fromStdString(caz.getBadV())));
            tabel->setItem(i, 2, new QTableWidgetItem(QString::fromStdString(caz.getGoodV())));
            tabel->setItem(i, 3, type);
            tabel->setItem(i, 4, severity);
            if (caz.getType() == "typo") {
                type->setBackground(Qt::black);
            } else if (caz.getType() == "grammar") {
                type->setBackground(Qt::red);
            } else if (caz.getType() == "incorrect tense") {
                type->setBackground(Qt::blue);
            }

            if (caz.getSeverity() == "minor") {
                severity->setBackground(Qt::green);
            } else if (caz.getSeverity() == "medium") {
                severity->setBackground(Qt::yellow);
            } else if (caz.getSeverity() == "major") {
                severity->setBackground(Qt::red);
            }
        }
    }
};

#endif //SIMULARE_GUI_H
