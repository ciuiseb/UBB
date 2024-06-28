//
// Created by ciuis on 5/29/2024.
//

#pragma once

#include <QMessageBox>
#include "../service.h"
#include "my_table_model.h"
#include "QWidget"
#include "QHBoxLayout"
#include "user_menu.h"
#include "observer.h"
#include "QListView"

class WishListCRUDWindow : public QWidget, public Observer {
Q_OBJECT

    Service &service;
    QListView *wishList;
    MyTableModel *model;

public:
    explicit WishListCRUDWindow(Service &s, QWidget *parent = nullptr) :
            service(s), QWidget(parent) {
        auto userHLayout = new QHBoxLayout{};
        setLayout(userHLayout);
        auto userVLayout = new QVBoxLayout{};
        userHLayout->addLayout(userVLayout);

        wishList = new QListView;
        model = new MyTableModel(service.get_wl());

        refreshWishList();
        userHLayout->addWidget(wishList);

        auto generateWishList = new QPushButton("Genereaza wishllist");
        userVLayout->addWidget(generateWishList);
        connect(generateWishList,
                &QPushButton::clicked,
                this,
                &WishListCRUDWindow::generateWl);

        auto clearWishList = new QPushButton("Goleste wishllist");
        userVLayout->addWidget(clearWishList);
        connect(clearWishList,
                &QPushButton::clicked,
                this,
                &WishListCRUDWindow::clearWl);
    }

signals:

    void generate();

    void clear();

private slots:

    void generateWl() {
        emit generate();
    }

    void clearWl() {
        emit clear();
    }

    void refreshWishList() {
        model->setElems(service.get_wl());
    }


public:
    void updateObserver() override {
        refreshWishList();
    }
};
