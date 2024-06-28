#include <QApplication>

#include "teste.h"
#include "gui.h"

int main(int argc, char *argv[]) {
    test();
    QApplication a(argc, argv);
    Repo r(R"(utilaje.txt)");cd ..
    Service s(r);
    GUI main(s);
    main.resize(1200, 500);
    main.show();
    return QApplication::exec();
}
