//
// Created by ciuis on 5/22/2024.
//

#ifndef SIMULARE_TESTS_H
#define SIMULARE_TESTS_H

#include "service.h"
#include "cassert"
#include "QHBoxLayout"
void testCaz(){
    Caz c(1, "e oo masa", "e o masa", "typo", "minor");
    assert(c.getId() == 1);
    assert(c.getBadV() == "e oo masa");
    assert(c.getGoodV() == "e o masa");
    assert(c.getType() == "typo");
    assert(c.getSeverity() == "minor");
}
void testValidator(){
    Repo r(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL I\SEM II\laburi\OOP\SIMULARE\cazuri.txt)");
    Validator v(r.getAll());
    try{
        v.validate(1,"typo", "minor");
        assert(false);
    }catch(std::domain_error&){
        assert(true);
    }
    try{
        v.validate(11,"ty", "minor");
        assert(false);
    }catch(std::domain_error&){
        assert(true);
    }
    try{
        v.validate(11,"typo", "aaa");
        assert(false);
    }catch(std::domain_error&){
        assert(true);
    }
}
void testRepo(){
    Repo r(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL I\SEM II\laburi\OOP\SIMULARE\cazuri.txt)");
    assert(r.getAll().size() == 10);
    r.add(1, "e oo masa", "e o masa", "typo", "minor");
    assert(r.getAll().size() == 11);

}
void testService(){
    Repo r(R"(C:\Users\ciuis\Desktop\FACULTATE\ANUL I\SEM II\laburi\OOP\SIMULARE\cazuri.txt)");
    Service s(r);
    assert(s.getAll().size() == 10);

    s.sortByGoodV();
    assert(s.getAll()[0].getId() == 8);
    s.sortBySeverity();
    assert(s.getAll()[0].getId() == 1);
    s.sortByType();
    assert(s.getAll()[0].getId() == 1);

    s.add(11, "e oo masa", "e o masa", "typo", "minor");
}
void testAll(){
    testCaz();
    testValidator();
    testRepo();
    testService();
}
#endif //SIMULARE_TESTS_H
