//
// Created by ciuis on 6/22/2024.
//

#ifndef PRACTIC_ROTI_H
#define PRACTIC_ROTI_H

#include "QWidget"
#include "QPainter"

class DrawRoti : public QWidget {
Q_OBJECT

    vector<int> nrCilintrii;
public:
    /// Construieste un widget pe baza unui vector de frecventa a numarului de roi
    /// \param v vector tip int, cu 4 elemente
    DrawRoti(vector<int> v) : nrCilintrii(v) { setMinimumHeight(200); }

    /// (re)Deseneaza elemetenele grafice
    void paintEvent(QPaintEvent *event) override {
        QPainter painter(this);

        for (int i = 0; i < 4; ++i) {
            painter.setBrush(Qt::blue);
            int h = 10 * nrCilintrii[i];
            if (i) {
                painter.drawEllipse(i * 10 * nrCilintrii[i - 1] + 30, QWidget::height() - h, h, h);
            } else {
                painter.drawEllipse(10, QWidget::height() - h, h, h);
            }
        }
    }

    /// Actualieaza vectorul de frcventa
    /// \param newElems vector de int, cu 4 elemente
    void updateCilindrii(vector<int> newnr) {
        nrCilintrii = newnr;
    }
};

#endif //PRACTIC_ROTI_H
