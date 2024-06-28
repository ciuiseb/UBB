//
// Created by ciuis on 6/22/2024.
//

#ifndef PRACTIC_GUI_H
#define PRACTIC_GUI_H

#include "QWidget"
#include "QTableView"
#include "QHBoxLayout"
#include "QVBoxLayout"
#include "QFormLayout"
#include "QPushButton"
#include "QRadioButton"
#include "QLineEdit"
#include "QMessageBox"

#include "service.h"
#include "custom_model_view.h"
#include "roti.h"

class GUI : public QWidget {
    Service &service;


    QTableView *table;
    CustomViewModel *model;

    QLineEdit *denumireLE;
    QLineEdit *tipLE;
    QRadioButton *cilindru1;
    QRadioButton *cilindru2;
    QRadioButton *cilindru4;
    QRadioButton *cilindru8;

    DrawRoti *paint;
public:
    /// Creeaza o fereastra tip GUI, cu elementele luate dintr-un service
    /// \param s service
    GUI(Service &s) : service(s) {
        auto vLayout = new QVBoxLayout;
        setLayout(vLayout);
        auto hLayout = new QHBoxLayout;
        vLayout->addLayout(hLayout);

        //1-2
        model = new CustomViewModel(service.getAll());
        table = new QTableView;
        table->setModel(model);
        table->setSelectionMode(QAbstractItemView::SingleSelection);
        table->setSelectionBehavior(QAbstractItemView::SelectRows);
        hLayout->addWidget(table);

        //3
        auto fLayout = new QFormLayout;
        hLayout->addLayout(fLayout);
        auto removeBtn = new QPushButton("Delete");
        fLayout->addWidget(removeBtn);
        connect(removeBtn,
                &QPushButton::clicked,
                this,
                &GUI::remove);
        //4
        denumireLE = new QLineEdit;
        tipLE = new QLineEdit;
        cilindru1 = new QRadioButton;
        cilindru2 = new QRadioButton;
        cilindru4 = new QRadioButton;
        cilindru8 = new QRadioButton;
        auto modifyBtn = new QPushButton("Update");

        fLayout->addRow("Denumire: ", denumireLE);
        fLayout->addRow("Tip: ", tipLE);
        fLayout->addRow("Cilindru = 1: ", cilindru1);
        fLayout->addRow("Cilindru = 2: ", cilindru2);
        fLayout->addRow("Cilindru = 4: ", cilindru4);
        fLayout->addRow("Cilindru = 8: ", cilindru8);
        fLayout->addWidget(modifyBtn);

        connect(table->selectionModel(),
                &QItemSelectionModel::selectionChanged,
                this,
                &GUI::loadUtilaj);

        connect(modifyBtn,
                &QPushButton::clicked,
                this,
                &GUI::modify);
        //5
        paint = new DrawRoti(service.getCilindrii());
        vLayout->addWidget(paint);
    }

private slots:

    ///Apeleaza functia remove din service. Daca sunt erori, afiseaza un message box
    void remove() {
        try {
            service.remove(table->selectionModel()->currentIndex().row());
            updateTable();
        } catch (std::domain_error &e) {
            errorBox(e.what());
        }
    };

    void modify() {
        ///Verifica integritatea campurilor denumire si tip, dupa care
        /// apeleaza functia modify din service. Daca sunt erori, afiseaza un message box
        if (denumireLE->text().isEmpty()) {
            errorBox("Campul denumireii nu poate sa fie gol");
            return;
        }
        if (tipLE->text().isEmpty()) {
            errorBox("Campul tipului nu poate sa fie gol");
            return;
        }
        auto denumire = denumireLE->text().toStdString();
        auto tip = tipLE->text().toStdString();
        int nrCilindrii;
        if (cilindru1->isChecked()) {
            nrCilindrii = 1;
        } else if (cilindru2->isChecked()) {
            nrCilindrii = 2;
        } else if (cilindru4->isChecked()) {
            nrCilindrii = 4;
        } else if (cilindru8->isChecked()) {
            nrCilindrii = 8;
        }
        service.modify(table->selectionModel()->currentIndex().row(), denumire, tip, nrCilindrii);
        updateTable();
    }

    //Incaraca in line edit-uri atributele denumire si tip, si electeaza radio buttonul corespunzator
    //numarului de cilindrii
    void loadUtilaj() {
        auto u = service.getAll()[table->selectionModel()->currentIndex().row()];
        denumireLE->setText(QString::fromStdString(u.getDenumire()));
        tipLE->setText(QString::fromStdString(u.getTip()));

        switch (u.getNrCilindrii()) {
            case 1:
                cilindru1->toggle();
                break;
            case 2:
                cilindru2->toggle();
                break;
            case 4:
                cilindru4->toggle();
                break;
            case 8:
                cilindru8->toggle();
                break;
            default:
                break;
        }
    }

private:
    // Transmite modelului noul set de date, la fel si painterului.
    // Transmite painterului un semnal pentru a se actualiza
    void updateTable() {
        model->setData(service.getAll());
        paint->updateCilindrii(service.getCilindrii());
        paint->update();
    };

    /// Afiseaza un message box cu un text
    /// \param e Qstring
    void errorBox(const QString &e) {
        auto msg = new QMessageBox;
        msg->setText(e);
        msg->addButton(QMessageBox::Ok);
        msg->show();
    }
};

#endif //PRACTIC_GUI_H
