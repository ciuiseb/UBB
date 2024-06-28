//
// Created by ciuis on 6/5/2024.
//
#pragma once
typedef int TElem;

class Node{
public:
    TElem val;
    Node* left;
    Node* right;
    explicit Node(TElem val)
        :val(val), left(nullptr), right(nullptr){}
    Node(TElem val, Node*l, Node*r)
            :val(val), left(l), right(r){}
};
