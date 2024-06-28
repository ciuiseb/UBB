#include <QApplication>
#include <QPushButton>

//XCaz(id -unic, wringV, correctCase, type, severity)
//XTeste
//XRepo - citire fisier, get all
//Validator
// Service get all
// teste
//QTableWidget
// Initial sortat dupa severitate
// Qlineedituri pentru id, type, wrong corretn, erori- qmessage box
// sortare cu RadioButtons dupa versiunea corecta, tip, severitate
// celula in care apare tipul se coloreaza
// celula in care apare severitate se coloreaza
#include "gui.h"
#include "tests.h"
int main(int argc, char *argv[]) {
    testAll();
    QApplication a(argc, argv);
    Repo r(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL I\SEM II\laburi\OOP\SIMULARE\cazuri.txt)");
    Service s(r);
    GUI main(s);
    main.show();

    return QApplication::exec();
}