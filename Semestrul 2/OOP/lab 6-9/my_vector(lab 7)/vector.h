//#pragma once

#include "iostream"
#include "string"
#include "stdexcept"

template<typename ElemType>
class IteratorDLL;

template<typename ElemType>
class Node {
public:
    ElemType data;
    Node<ElemType> *prev;
    Node<ElemType> *next;

    explicit Node(const ElemType &val) : data(val), prev(nullptr), next(nullptr)  {}
};

template<typename ElemType>
class vector {
    friend class IteratorDLL<ElemType>;

    Node<ElemType> *head;
    Node<ElemType> *tail;
    int len{0};
public:
    vector() : head(nullptr), tail(nullptr) {}

    vector(const vector &other);

    vector& operator=(const vector &other);

    ~vector(){clear();}

    void clear();

    void push_back(const ElemType &value);

    void delete_pos(int index);

    ElemType &at(int index) const;

    ElemType &operator[](int index);

    int size();

    IteratorDLL<ElemType> begin();

    IteratorDLL<ElemType> end();

};

template<typename ElemType>
class IteratorDLL {
    const vector<ElemType> v;
    Node<ElemType> *current;
public:
    IteratorDLL(const vector<ElemType>&vec, const std::string& flag);

    ~IteratorDLL() = default;

    IteratorDLL &operator++();

    ElemType &operator*();

    ElemType &element();

    void next();

    bool operator==(IteratorDLL &other);

    bool operator!=(IteratorDLL &other);

};

template<typename ElemType>
vector<ElemType>::vector(const vector &other):head(nullptr), tail(nullptr), len(other.len) {
    Node<ElemType> *temp = other.head;
    while (temp != nullptr) {
        push_back(temp->data);
        temp = temp->next;
    }
}

template<typename ElemType>
vector<ElemType>& vector<ElemType>::operator=(const vector<ElemType> &other) {
    if (this != &other) {
        clear();

        Node<ElemType>* temp = other.head;
        while (temp != nullptr) {
            push_back(temp->data);
            temp = temp->next;
        }
    }
    return *this;
}
template<typename ElemType>
IteratorDLL<ElemType>::IteratorDLL(const vector<ElemType>&vec, const std::string& flag):v{vec} {
    if (flag == "F_HEAD") {
        current = vec.head;
    } else if (flag == "F_TAIL") {
        current = nullptr;
    }
}

template<typename ElemType>
void vector<ElemType>::push_back(const ElemType &value) {
    auto *new_node = new Node<ElemType>(value);
    if (head == nullptr) {
        head = tail = new_node;
    } else {
        tail->next = new_node;
        new_node->prev = tail;
        tail = new_node;
    }
    len++;
}


template<typename ElemType>
void vector<ElemType>::clear() {
    Node<ElemType>* current = head;
    while (current != nullptr) {
        Node<ElemType>* next = current->next;
        delete current;
        current = next;
    }
    head = nullptr;
    tail = nullptr;
    len = 0;
}
template<typename ElemType>
void vector<ElemType>::delete_pos(const int index) {

    Node<ElemType> *current = head;
    for (int i = 0; i < index; ++i) {
        current = current->next;
    }
    if (head == tail) {
        head = tail = nullptr;
    } else if (current == head) {
        head = head->next;
        head->prev = nullptr;
    } else if (current == tail) {
        tail = tail->prev;
        tail->next = nullptr;
    } else {
        current->prev->next = current->next;
        current->next->prev = current->prev;
    }
    delete current;
    len--;
}

template<typename ElemType>
ElemType &vector<ElemType>::at(int index) const {
    Node<ElemType> *current = head;
    for (int i = 0; i < index; ++i) {
        current = current->next;
    }
    return current->data;
}

template<typename ElemType>
ElemType &vector<ElemType>::operator[](int index) {
    return at(index);
}

template<typename ElemType>
int vector<ElemType>::size() {
    return len;
}

template<typename ElemType>
IteratorDLL<ElemType> vector<ElemType>::begin() {
    return IteratorDLL<ElemType>(*this, "F_HEAD");
}

template<typename ElemType>
IteratorDLL<ElemType> vector<ElemType>::end() {
    return IteratorDLL<ElemType>(*this, "F_TAIL");
}

template<typename ElemType>
IteratorDLL<ElemType> &IteratorDLL<ElemType>::operator++() {
    next();
    return *this;
}

template<typename ElemType>
ElemType &IteratorDLL<ElemType>::operator*() {
    return current->data;
}

template<typename ElemType>
void IteratorDLL<ElemType>::next() {
    current = current->next;
}

template<typename ElemType>
ElemType &IteratorDLL<ElemType>::element() {
    return current->data;
}

template<typename ElemType>
bool IteratorDLL<ElemType>::operator==(IteratorDLL<ElemType> &other) {
    return (current == other.current);
}

template<typename ElemType>
bool IteratorDLL<ElemType>::operator!=(IteratorDLL<ElemType> &other) {
    return !(current == other.current);
}
























