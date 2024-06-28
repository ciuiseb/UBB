//
// Created by ciuis on 5/29/2024.
//

#pragma once

#include "QWidget"
#include <vector>
#include "QPainter"
#include "QPaintDevice"
#include "QRandomGenerator"
#include "observer.h"

using std::vector;

class CirclesWindow : public QWidget, public Observer {
Q_OBJECT

    Service &service;
    QVector<QRect> circles;

public:
    explicit CirclesWindow(Service &service, QWidget *parent = nullptr) :
            QWidget(parent), service(service) {
        redrawCircles();
    }

protected:
    void paintEvent(QPaintEvent *event) override {
        QPainter painter(this);
        painter.setRenderHint(QPainter::Antialiasing);

        for (const QRect &circle : circles) {
            painter.drawEllipse(circle);
        }
    }

public slots:
    void redrawCircles() {
        circles.clear();

        for (int i = 0; i < service.get_wl().size(); ++i) {
            int radius = QRandomGenerator::global()->bounded(10, 50);
            int x = QRandomGenerator::global()->bounded(width() - radius * 2);
            int y = QRandomGenerator::global()->bounded(height() - radius * 2);
            circles.append(QRect(x, y, radius * 2, radius * 2));
        }

        update();
    }
public:
    void updateObserver() override{
        redrawCircles();
    }
};
