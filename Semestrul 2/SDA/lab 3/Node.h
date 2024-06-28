#pragma once

#include <utility>

#include "iostream"

typedef int TCheie;
typedef int TValoare;
typedef std::pair<TCheie, TValoare> TElem;

class Node {
public:
    TElem data;
    Node *prev;
    Node *next;

    explicit Node(TElem val) : data(std::move(val)), prev(nullptr), next(nullptr)  {}
};