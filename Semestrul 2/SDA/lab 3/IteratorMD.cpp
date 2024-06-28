#include "IteratorMD.h"
#include "MD.h"

using namespace std;

IteratorMD::IteratorMD(const MD& _md): md(_md) {
    /**
     * Complexitate: T(n) ∈ θ(1)
     */
    current = _md.head;
}

TElem IteratorMD::element() const{
    /**
     * Complexitate: T(n) ∈ θ(1)
     */
	return current->data;
}

bool IteratorMD::valid() const {
    /**
     * Complexitate: T(n) ∈ θ(1)
     */
    return current != nullptr;
}

void IteratorMD::urmator() {
    /**
     * Complexitate: T(n) ∈ θ(1)
     */
	current = current->next;
}

void IteratorMD::prim() {
    /**
     * Complexitate: T(n) ∈ θ(1)
     */
	current = md.head;
}

