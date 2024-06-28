#include <iostream>

#include "TestScurt.h"
#include "TestExtins.h"

int main(){
    testAll();
    std::cout<<"Finished short tests!"<<std::endl;
    testAllExtins();
    std::cout<<"Finished Tests!"<<std::endl;
}
