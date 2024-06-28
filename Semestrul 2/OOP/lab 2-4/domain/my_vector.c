#include "my_vector.h"

Vector* create_vector(){
    Vector*v = (Vector*) malloc(sizeof(Vector));
    v->size = 0;
    v->capacity = INITIAL_CAPACITY;
    v->data = (struct Bill**) malloc(v->capacity * sizeof(struct Bill*));

    return v;
}

void destroy_vector(Vector*v){
    if(v != NULL) {
        for (int i = 0; i < v->size; ++i) {
            destroy_bill(v->data[i]);
        }
        free(v->data);
        free(v);
    }
}

void resize_vector(Vector** v_ptr){
    Vector* v = *v_ptr;
    Vector* temp = create_vector();
    temp->capacity = 2 * v->capacity;
    temp->size = v->size;
    for(int i = 0; i < v->size; ++i){
        temp->data[i] = v->data[i];
    }
    destroy_vector(v);
    *v_ptr = temp;
}


void push_back(Vector**v_ptr, struct Bill*element){
    Vector*v = *v_ptr;
    if(v->size >= v->capacity){
        resize_vector(v_ptr);
        v = *v_ptr;
    }
    v->data[v->size++] = element;
}

void pop(Vector*v, int index){
    if(index < 0 || index >= (v->size)){
        return;
    }
    for(int i = index; i < (v->size - 1);++i){
        v->data[i] = v->data[i+1];
    }

    --v->size;
}


