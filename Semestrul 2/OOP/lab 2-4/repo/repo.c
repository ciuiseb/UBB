#include "repo.h"

struct BillsRepository{
    Vector* elements;
};
struct BillsRepository* create_repo(){
    struct BillsRepository* new = (struct BillsRepository*) malloc(sizeof(struct BillsRepository));

    new->elements = create_vector();

    return new;
}

struct Bill** get_all(struct BillsRepository* repo){
    return repo->elements->data;
}
Vector*get_vector(struct BillsRepository* repo){
    return repo->elements;
}

void add_to_repo(struct BillsRepository*repo, struct Bill* element){
    push_back(&repo->elements, element);
}
void delete_from_repo(struct BillsRepository*repo, int index){
    pop(repo->elements, index);
}

int get_repo_len(struct BillsRepository*repo){
    return (repo->elements->size);
}
void destroy_repo(struct BillsRepository*repo) {
    destroy_vector(repo->elements);
    free(repo);
}