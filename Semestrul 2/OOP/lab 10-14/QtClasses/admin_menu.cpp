#include "admin_menu.h"
#include "QMessageBox"
void AdminWindow::addOffer() {
    //TODO fix
    auto window = new AddOfferWindow(service);
    window->show();
    this->hide();
    connect(
            window,
            &AddOfferWindow::addedStatus,
            [window, this](const QString &status) {

                refreshOfferList();
                refreshTypes();
                this->show();
                msgBox(status);
                window->deleteLater();
            }
    );
}

void AdminWindow::refreshOfferList() {
    model->setElems(service.get_all());
}

void AdminWindow::deleteOffer() {
    auto window = new DeleteOfferWindow(service);
    window->show();
    this->hide();
    connect(
            window,
            &DeleteOfferWindow::deletedStatus,
            [window, this](const QString &status) {
                window->close();
                refreshOfferList();refreshTypes();

                msgBox(status);
                this->show();
            }
    );
}

void AdminWindow::modifyOffer() {
    auto window = new ModifyOfferWindow(service);
    window->show();
    connect(
            window,
            &ModifyOfferWindow::modifiedStatus,
            [window, this](const QString &status) {
                window->close();
                refreshOfferList();refreshTypes();

                msgBox(status);
            }
    );
}

void AdminWindow::searchOffer() {
    auto window = new SearchOfferWindow(service);
    window->show();
    this->hide();
    connect(
            window,
            &SearchOfferWindow::searchResult,
            [window, this](const QString &status) {
                window->close();

                refreshOfferList();refreshTypes();

                msgBox(status);
                this->show();
            }
    );
}


void AdminWindow::sortOffers() {
    auto window = new SortOffersWindow(service);
    window->show();
    this->hide();
    connect(
            window,
            &SortOffersWindow::sortStatus,
            [window, this](const QString &status) {
                window->close();

                refreshOfferList();refreshTypes();

                msgBox(status);
                this->show();
            }
    );
}

void AdminWindow::undo() {
    try {
        service.undo();
        refreshOfferList();refreshTypes();
    } catch (const ServiceException &e) {
        msgBox("Nu se poate face undo!");
    }
}

void AdminWindow::showMainMenu() {
    emit backToMainMenu();
}

void AdminWindow::filterOffers() {
    auto window = new FilterOffersWindow(service);
    window->show();
    this->hide();
    connect(window,
            &FilterOffersWindow::filterStatus,
            [window, this]{
                window->close_result();

                this->show();
            });
}

void AdminWindow::refreshTypes() {
    for(auto button: typesBtns){
        delete button;
    }
    typesBtns.clear();
    for(const auto& type : service.get_types()){
        createTypeBtn(type);
    }
}

void AdminWindow::createTypeBtn(const std::pair<const string, int>& type) {
    auto typeName = QString::fromStdString(type.first);
    auto typeCount = QString::number(type.second);
    auto res = "Sunt " + typeCount + " oferte cu acest tip!";
    auto button = new QPushButton(typeName);
    typesBtns.push_back(button);
    typesLayout->addWidget(button);
    connect(button,
            &QPushButton::clicked,
            [=, this]{
                this->hide();
                auto okWindow = new QWidget;
                auto ly = new QVBoxLayout{};
                okWindow->setLayout(ly);
                ly->addWidget(new QLabel(res));

                auto btn = new QPushButton("OK");
                ly->addWidget(btn);
                okWindow->show();
                QWidget::connect(btn,
                                 &QPushButton::clicked,
                                 [&, okWindow] {
                                     okWindow->close();
                                     this->show();

                                 });
            }
    );
}


void AdminWindow::msgBox(const QString& text) {
    auto msg = new QMessageBox;
    msg->setText(text);
    msg->addButton(QMessageBox::Ok);
    msg->show();
}


