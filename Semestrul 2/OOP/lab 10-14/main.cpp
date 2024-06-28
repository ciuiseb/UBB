#include <QApplication>
#include "repo.h"
#include "QtClasses/main_menu.h"
#include "QtClasses/user_menu.h"
#include "QtClasses/admin_menu.h"

using std::unique_ptr;
using std::make_unique;

void switchMenus(QWidget &menuToHide, QWidget &menuToShow) {
    menuToHide.hide();
    menuToShow.show();
}

int main(int argc, char **argv) {
    QApplication a(argc, argv);

    OffersFileRepo repo(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL I\SEM II\laburi\OOP\lab 10_11\oferte.txt)");
    Service service(repo);

    MainWindow mainMenu;
    UserWindow userMenu(service);
    AdminWindow adminMenu(service);

    QWidget::connect(
            &mainMenu,
            &MainWindow::showUser,
            [&] { switchMenus(mainMenu, userMenu); }
    );
    QWidget::connect(
            &mainMenu,
            &MainWindow::showAdmin,
            [&] { switchMenus(mainMenu, adminMenu); }
    );
    QWidget::connect(
            &adminMenu,
            &AdminWindow::backToMainMenu,
            [&] { switchMenus(adminMenu, mainMenu); }
    );
    QWidget::connect(
            &userMenu,
            &UserWindow::backToMainMenu,
            [&] { switchMenus(userMenu, mainMenu); }
    );


    mainMenu.show();
    return QApplication::exec();
}