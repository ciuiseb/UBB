//
// Created by ciuis on 5/22/2024.
//

#ifndef SIMULARE_CAZ_H
#define SIMULARE_CAZ_H

#include <utility>

#include "string"

using std::string;

class Caz{
    int id;
    string badV;
    string goodV;
    string type;
    string severity;
public:
    Caz(int id, string badV, string goodV, string type, string severity):
    id(id), badV(std::move(badV)), goodV(std::move(goodV)), type(std::move(type)), severity(std::move(severity)){}
    int getId(){ return id;}
    string getSeverity(){ return severity;}
    string getBadV(){ return badV;}
    string getGoodV(){ return goodV;}
    string getType(){ return type;}
};
#endif //SIMULARE_CAZ_H
