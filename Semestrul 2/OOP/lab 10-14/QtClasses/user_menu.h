//
// Created by ciuis on 5/9/2024.
//

#pragma once

#include <QPushButton>
#include "QWidget"
#include "QVBoxLayout"
#include "QListWidget"
#include "../service.h"
#include "user_add_offer.h"
#include "user_export_offers.h"
#include "user_crud_window.h"
#include "user_read_only_window.h"
#include <QLabel>
#include <QMessageBox>
#include "observer.h"

class UserWindow : public QWidget, public Observable {
Q_OBJECT

    Service &service;
public:
    explicit UserWindow(Service &service, QWidget *parent = 0) : QWidget(parent), service(service) {
        resize(250, 250);
        auto userHLayout = new QHBoxLayout{};
        setLayout(userHLayout);
        auto userVLayout = new QVBoxLayout{};
        userHLayout->addLayout(userVLayout);

        auto addOfferToWishlist = new QPushButton("Adauga oferta in wishllist");
        userVLayout->addWidget(addOfferToWishlist);
        connect(addOfferToWishlist,
                &QPushButton::clicked,
                this,
                &UserWindow::addOffer);

        auto generateWishList = new QPushButton("Genereaza wishllist");
        userVLayout->addWidget(generateWishList);
        connect(generateWishList,
                &QPushButton::clicked,
                this,
                &UserWindow::generateOffer);
        auto clearWishList = new QPushButton("Golesete wishllist");
        userVLayout->addWidget(clearWishList);
        connect(clearWishList,
                &QPushButton::clicked,
                this,
                &UserWindow::clear);
        auto exportWishList = new QPushButton("Exporta wishllist");
        userVLayout->addWidget(exportWishList);
        connect(exportWishList,
                &QPushButton::clicked,
                this,
                &UserWindow::exportWl);
        auto wishListCRUDBtn = new QPushButton("WishListCRUDGUI");
        userVLayout->addWidget(wishListCRUDBtn);
        connect(wishListCRUDBtn,
                &QPushButton::clicked,
                this,
                &UserWindow::CRUD);
        auto drawingBtn = new QPushButton("WishListAsCircles");
        userVLayout->addWidget(drawingBtn);
        connect(drawingBtn,
                &QPushButton::clicked,
                this,
                &UserWindow::draw);
        auto backFromUser = new QPushButton("Back");
        userVLayout->addWidget(backFromUser);
        connect(
                backFromUser,
                &QPushButton::clicked,
                this,
                &UserWindow::showMainMenu
        );
    }

signals:

    void backToMainMenu();

private slots:

    void addOffer();

    void exportWl();

    void generateOffer();

    void clear();

//    void refreshWishList();

    void CRUD();

    void draw();

    void showMainMenu() {
        emit backToMainMenu();
    }

    void msgBox(const QString &text) {
        auto msg = new QMessageBox;
        msg->setText(text);
        msg->addButton(QMessageBox::Ok);
        msg->show();
    }
};







