//
// Created by ciuis on 5/29/2024.
//

#pragma once

#include "vector"

using std::vector;

class Observer {
public:
    virtual void updateObserver() = 0;
    virtual ~Observer(){}
};

class Observable {
    vector<Observer *> observers;
protected:
    virtual void notify() {
        for (auto obs: observers) {
            obs->updateObserver();
        }
    }

public:
    void addObserver(Observer *obs) { observers.push_back(obs); }

    void removeObserver(Observer *obs) {
        observers.erase(std::remove(observers.begin(),observers.end(),obs));
    }
};
