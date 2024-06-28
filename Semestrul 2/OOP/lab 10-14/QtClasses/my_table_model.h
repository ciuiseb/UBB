//
// Created by ciuis on 5/30/2024.
//
#pragma once

#include "QWidget"
#include "QAbstractTableModel"
#include "../service.h"

class MyTableModel : public QAbstractTableModel {
Q_OBJECT

    vector<Oferta> &elems;
public:
    MyTableModel(vector<Oferta> &e)
            : elems(e) {}

    [[nodiscard]] int rowCount(const QModelIndex &parent = QModelIndex()) const override {
        return elems.size();
    }

    [[nodiscard]] int columnCount(const QModelIndex &parent = QModelIndex()) const override {
        return 4;
    }

    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override {
        if (role == Qt::DisplayRole) {
            Oferta o = elems[index.row()];
            vector<QString> ats = {QString::fromStdString(o.getDenumire()),
                                   QString::fromStdString(o.getDestinatie()),
                                   QString::fromStdString(o.getTip()),
                                   QString::number(o.getPret())};

            return ats[index.column()];
        }
        return {};
    }

public:
    void setElems(vector<Oferta>&e) {
        elems = e;
        emit layoutChanged();
    };

};


