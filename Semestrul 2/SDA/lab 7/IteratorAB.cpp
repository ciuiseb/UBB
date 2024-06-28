#include "AB.h"
#include "IteratorAB.h"

IteratorInordine::IteratorInordine(const AB &_ab) : ab(_ab) {
    //CF = CM = CD = Θ(prim()) = Θ(h)
    prim();
}

void IteratorInordine::prim() {
    //CF = CM = CD = Θ(h) - h e inaltimea
    while (!s.empty()) s.pop();

    current = ab.root;

    while (current != nullptr) {
        s.push(current);
        current = current->left;
    }

    if (!s.empty())
        current = s.top();
}

bool IteratorInordine::valid() {
    //CF = CM = CD = Θ(1)
    return current != nullptr;
}

TElem IteratorInordine::element() {
    //CF = CM = CD = Θ(1)
    return current->val;
}

void IteratorInordine::urmator() {
    //CF = CM = CD = Θ(h)
    if (not valid())
        throw std::exception();
    auto top = s.top();
    s.pop();

    if(top->right != nullptr){
        top = top->right;
        while(top != nullptr){
            s.push(top);
            top = top->left;
        }
    }

    if (!s.empty()) {
        current = s.top();
    } else {
        current = nullptr;
    }
}

IteratorPreordine::IteratorPreordine(const AB &_ab) : ab(_ab) {
    //CF = CM = CD = Θ(1)
    current = ab.root;
}

void IteratorPreordine::prim() {
    //CF = Θ(1), CM = Θ(n), CD = Θ(n)
    while (!s.empty()) s.pop();

    s.push(ab.root);
    urmator();
}


bool IteratorPreordine::valid() {
    //CF = CM = CD = Θ(1)
    return current != nullptr;
}

TElem IteratorPreordine::element() {
    //CF = CM = CD = Θ(1)
    return current->val;
}

void IteratorPreordine::urmator() {
    //CF = CM = CD = Θ(1)
    if (not valid())
        throw std::exception();
    auto top = s.top();
    s.pop();
    if (top->right != nullptr)
        s.push(top->right);
    if (top->left != nullptr)
        s.push(top->left);

    if (s.empty()) {
        current = nullptr;
    } else {
        current = top;
        removed.push(current);
    }
}
void IteratorPreordine::revinoKPasi(int k) {
    if(removed.size() < k)
        throw std::exception();
    for(int i = 0; i < k; i++, removed.pop());

    current = removed.top();
    removed.pop();
}
IteratorPostordine::IteratorPostordine(const AB &_ab) : ab(_ab) {
    //CF = CM = CD = Θ(1)
    current = nullptr;
}

void IteratorPostordine::prim() {
    //CF = CM = CD = Θ(h)
    while (not s.empty()) s.pop();
    current = ab.root;
    while (current != nullptr) {
        s.emplace(current, false);
        current = current->left;
    }
}


bool IteratorPostordine::valid() {
    //CF = CM = CD = Θ(1)
    return current != nullptr;
}

TElem IteratorPostordine::element() {
    //CF = CM = CD = Θ(1)
    if (not valid())
        throw std::exception();
    return current->val;
}

void IteratorPostordine::urmator() {
    //CF = Θ(1), CM = CD = Θ(h)
    if (not valid()) {
        throw std::exception();
    }

    auto top = s.top();
    s.pop();

    if (top.k) {
        current = nullptr;
        if (!s.empty()) {
            current = s.top().p;
        }
    } else {
        s.emplace(top.p, true);
        if (top.p->right != nullptr) {
            current = top.p->right;
            while (current != nullptr) {
                s.emplace(current, false);
                current = current->left;
            }
        } else {
            current = nullptr;
        }
    }
}

IteratorLatime::IteratorLatime(const AB &_ab) : ab(_ab) {
    //CF = CM = CD = Θ(1)
    current = ab.root;
}

void IteratorLatime::prim() {
    //CF = CM = CD = Θ(1)
    while (!q.empty()) q.pop();

    current = ab.root;
    q.push(current);
}


bool IteratorLatime::valid() {
    //CF = CM = CD = Θ(1)
    return current != nullptr;
}

TElem IteratorLatime::element() {
    //CF = CM = CD = Θ(1)
    return current->val;
}

void IteratorLatime::urmator() {
    //CF = CM = CD = Θ(1)
    if (not valid())
        throw std::exception();
    auto top = q.front();
    q.pop();
    if (top->left != nullptr)
        q.push(top->left);
    if (top->right != nullptr)
        q.push(top->right);
    if (q.empty()) {
        current = nullptr;
    } else {
        current = q.front();
    }
}
