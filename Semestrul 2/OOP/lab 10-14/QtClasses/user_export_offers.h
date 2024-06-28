//
// Created by ciuis on 5/9/2024.
//

#pragma once

#include <QPushButton>
#include "QWidget"
#include "QVBoxLayout"
#include "QFormLayout"
#include "QListWidget"
#include "../service.h"
#include <QLabel>

class ExportWishListWindow : public QWidget {
Q_OBJECT

    Service &service;
    QLineEdit *fileNameField;
public:
    explicit ExportWishListWindow(Service &service, QWidget *parent = nullptr) :
            QWidget(parent), service(service) {
        auto exportVLayout = new QVBoxLayout;
        auto exportFLayout = new QFormLayout;
        setLayout(exportVLayout);
        exportVLayout->addLayout(exportFLayout);
        fileNameField = new QLineEdit;
        exportFLayout->addRow("Nume fisier:", fileNameField);
        auto exportButton = new QPushButton("Exporta");
        exportVLayout->addWidget(exportButton);
        connect(exportButton,
                &QPushButton::clicked,
                this,
                &ExportWishListWindow::exportWl);
    }

signals:

    void exportStatus(const QString &status);

private slots:

    void exportWl() {
        auto fileName = fileNameField->text().toStdString();
        fileName += ".txt";
        service.export_wl(fileName);
        emit exportStatus("Wishlistul a fost exportat!");
    }
};
