//
// Created by ciuis on 5/9/2024.
//
#include "user_menu.h"

void UserWindow::addOffer() {
    auto window = new AddOfferToWlWindow(service);
    window->show();
    this->hide();
    connect(
            window,
            &AddOfferToWlWindow::addedStatus,
            [window, this](const QString &status) {
                window->close();
                notify();

                msgBox(status);
                this->show();
                notify();
            }
    );
}


void UserWindow::exportWl() {
    auto window = new ExportWishListWindow(service);
    window->show();
    this->hide();
    connect(
            window,
            &ExportWishListWindow::exportStatus,
            [window, this](const QString &status) {
                window->close();

                msgBox(status);
                this->show();
            }
    );
}

void UserWindow::generateOffer() {
    try {
        service.generate_wl();
        notify();
        msgBox("au fost adaugate oferte!");
        this->show();
    } catch (const ServiceException &e) {
        msgBox(e.what());
        this->show();
    }

}

void UserWindow::clear() {
    service.clear_wl();
    notify();
    msgBox("Wishlistul a fost golit");
}
void UserWindow::CRUD() {
    auto window = new WishListCRUDWindow(service);
    addObserver(window);
    window->show();
    connect(window,
            &WishListCRUDWindow::generate,
            this,
            &UserWindow::generateOffer);
    connect(window,
            &WishListCRUDWindow::clear,
            this,
            &UserWindow::clear);
}

void UserWindow::draw() {
    auto window = new CirclesWindow(service);
    addObserver(window);
    window->show();
}


