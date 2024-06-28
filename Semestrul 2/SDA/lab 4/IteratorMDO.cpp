#include "IteratorMDO.h"
#include "MDO.h"

IteratorMDO::IteratorMDO(const MDO& d) : dict(d){
	prim();
}

void IteratorMDO::prim(){
	current = dict.prim;
}

void IteratorMDO::urmator(){
	current = dict.urm[current];
}

bool IteratorMDO::valid() const{
	return (current != -1);
}

TElem IteratorMDO::element() const{
    if(valid())
        return dict.elems[current];
	return TElem (-1, -1);
}


