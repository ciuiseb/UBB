//
// Created by ciuis on 6/22/2024.
//

#ifndef PRACTIC_CUSTOM_MODEL_VIEW_H
#define PRACTIC_CUSTOM_MODEL_VIEW_H

#include "QAbstractTableModel"
#include "utilaje.h"
#include "vector"

using std::vector;

class CustomViewModel : public QAbstractTableModel {
Q_OBJECT

    vector<Utilaj> &elems;
public:
    /// Construieste un widget bazat pe un vector de utilaje
    /// \param e vector utilaje
    CustomViewModel(vector<Utilaj> &e) : elems(e) {}

    /// Returneaza numarul de randuri
    /// \param parent
    /// \return int
    int rowCount(const QModelIndex &parent = QModelIndex()) const override {
        return elems.size();
    }

    /// Returneaza numarul de coloane
    /// \param parent
    /// \return int
    int columnCount(const QModelIndex &parent = QModelIndex()) const override {
        return 5;
    }

    /// Actualizeaza casutele din tabel
    QVariant data(const QModelIndex &index, int role = Qt::DisplayRole) const override {
        std::sort(elems.begin(),
                  elems.end(),
                  [](Utilaj &a, Utilaj &b) {
                            if (a.getTip() != b.getTip())
                                 return a.getTip() < b.getTip();
                            return a.getDenumire() < b.getDenumire();
                  });
        if (role == Qt::DisplayRole) {
            Utilaj u = elems[index.row()];
            int sameType = 0;
            for (auto &el: elems) {
                if (el.getTip() == u.getTip())
                    sameType++;
            }
            vector<QString> values = {QString::number(u.getId()),
                                      QString::fromStdString(u.getDenumire()),
                                      QString::fromStdString(u.getTip()),
                                      QString::number(u.getNrCilindrii()),
                                      QString::number(sameType)};
            return values[index.column()];
        }
        return {};
    }

    /// Seteaza headerul tabelului. In cazul meu seteaza headeuril orizontal
    QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override {
        vector<QString> headers = {"ID", "Denumire", "Tip", "Nr Cilindrii", "Utilaje cu acelasi tip"};
        if (role == Qt::DisplayRole && orientation == Qt::Horizontal) {
            return headers[section];
        }
        return {};
    }

    /// Actualieaza vectorul elemente
    /// \param newElems vector de Utilaje
    void setData(vector<Utilaj> &newElems) {
        elems = newElems;
        emit layoutChanged();
    }
};

#endif //PRACTIC_CUSTOM_MODEL_VIEW_H
