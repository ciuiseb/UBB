//
// Created by ciuis on 6/22/2024.
//

#ifndef PRACTIC_TESTE_H
#define PRACTIC_TESTE_H

#include "service.h"
#include "assert.h"

void testDomain(){
    Utilaj u(1, "u1", "tip1", 2);
    assert(u.getId() == 1);
    assert(u.getNrCilindrii() == 2);
    assert(u.getDenumire() == "u1");
    assert(u.getTip() == "tip1");

    u.setNrCilindrii(4);
    assert(u.getNrCilindrii() == 4);

    u.setDenumire("u2");
    assert(u.getDenumire() == "u2");

    u.setTip("tip2");
    assert(u.getTip() == "tip2");
}
void testRepo(){
    Repo r(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL I\SEM II\.Laburi\OOP\PRACTIC\test.txt)");
    assert(r.getAll().size() == 10);
    assert(r.getTypes().size() == 4);

    assert(r.getCilindrii()[0] == 1);
    assert(r.getCilindrii()[1] == 8);
    assert(r.getCilindrii()[2] == 1);
    assert(r.getCilindrii()[3] == 0);
}
void testService(){
    Repo r(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL I\SEM II\.Laburi\OOP\PRACTIC\test.txt)");
    Service s(r);
    assert(s.getAll().size() == 10);
    assert(s.getTypes().size() == 4);

    try{
        s.remove(8);
        assert(false);
    }catch(std::domain_error&){
        assert(true);
    }
    s.remove(9);
    assert(s.getAll().size() == 9);
    r.add(10,"denumire9","tip1",2);
    r.writeToFile();

    s.modify(1, "denumire veche", "tip vechi", 4);
    assert(s.getAll()[1].getDenumire() == "denumire veche");
    assert(s.getAll()[1].getTip() == "tip vechi");
    assert(s.getAll()[1].getNrCilindrii() == 4);

    s.modify(1, "denumire noua", "tip nou", 8);
    assert(s.getAll()[1].getDenumire() == "denumire noua");
    assert(s.getAll()[1].getTip() == "tip nou");
    assert(s.getAll()[1].getNrCilindrii() == 8);

    s.modify(1, "denumire veche", "tip vechi", 4);
}
void test(){
    testDomain();
    testRepo();
    testService();
}
#endif //PRACTIC_TESTE_H
