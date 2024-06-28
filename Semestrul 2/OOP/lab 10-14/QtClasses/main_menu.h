//
// Created by ciuis on 5/9/2024.
//

#pragma once

#include <QPushButton>
#include "QWidget"
#include "QHBoxLayout"
#include "memory"
using std::unique_ptr;
using std::make_unique;


class MainWindow : public QWidget {
Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = nullptr) : QWidget(parent) {
        auto mainVLayout = new QVBoxLayout();
        auto mainHLayout = new QHBoxLayout();
        this->setLayout(mainVLayout);
        mainVLayout->addLayout(mainHLayout);

        auto adminButton = new QPushButton("Admin menu");
        mainHLayout->addWidget(adminButton);
        connect(
                adminButton,
                &QPushButton::clicked,
                this,
                &MainWindow::showAdminMenu
        );

        auto userButton = new QPushButton("User menu");
        mainHLayout->addWidget(userButton);
        connect(
                userButton,
                &QPushButton::clicked,
                this,
                &MainWindow::showUserMenu
        );

        auto exitButton = new QPushButton("Exit");
        mainVLayout->addWidget(exitButton);
        connect(exitButton,
                &QPushButton::clicked,
                [this] { this->close(); }
        );
    }

signals:

    void showAdmin();

    void showUser();

private slots:

    void showUserMenu() {
        emit showUser();
    }

    void showAdminMenu() {
        emit showAdmin();
    }
};

