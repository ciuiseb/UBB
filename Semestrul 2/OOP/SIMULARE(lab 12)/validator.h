//
// Created by ciuis on 5/22/2024.
//

#ifndef SIMULARE_VALIDATOR_H
#define SIMULARE_VALIDATOR_H

#include "caz.h"
#include "exception"

class Validator{
    vector<Caz>&elems;
public:
    Validator(vector<Caz>&elems):elems(elems){

    }
        void validate(int id, const string& type, const string& severity){
        vector<string> types = {"typo", "grammar", "incorrect tesnse"};
        vector<string> severityes = {"minor", "medium", "major"};

        int type_good = 0;
        for(auto t:types){
            if(t == type){
                type_good = 1;break;
            }
        }
        if(type_good== 0){
            throw std::domain_error("Type must be one of typo, grammar or incorrect tense");
        }

        int seveirty_good = 0;
        for(auto s:severityes){
            if(s == severity){
                    seveirty_good = 1;break;
            }
        }
        if(seveirty_good== 0){
            throw std::domain_error("Severity must be one of minor, medium or major");
        }

        for(auto caz:elems){
            if(caz.getId() == id){
                throw std::domain_error("Id-ul trebuie sa fie unic");
            }
        }
    }
};
#endif //SIMULARE_VALIDATOR_H
