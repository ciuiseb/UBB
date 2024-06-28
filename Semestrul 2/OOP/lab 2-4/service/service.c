#include "service.h"

struct BillService {
    struct BillsRepository *repo;
};


struct BillService *create_service() {
    struct BillService *service = (struct BillService *) malloc(sizeof(struct BillService));
    service->repo = create_repo();

    return service;
}

struct Bill **get_all_s(struct BillService *service) {
    return get_all(service->repo);
}
Vector* get_vector_s(struct BillService*service){
    return get_vector(service->repo);
}
int get_service_len(struct BillService *service) {
    return get_repo_len(service->repo);
}

void add_to_service(struct BillService *service, struct Bill *element) {
        add_to_repo(service->repo, element);
}

int find_bill(struct BillService *service, struct Bill *bill) {
    for (int i = 0; i < get_repo_len(service->repo); ++i) {
        if (get_ap_number(bill) == get_ap_number(get_all_s(service)[i]) &&
            get_sum(bill) == get_sum(get_all_s(service)[i])
            && strcmp(get_type(bill), get_type(get_all_s(service)[i])) == 0) {
            return i;
        }
    }
    return -1;
}

void delete_from_service(struct BillService *service, int index) {
    delete_from_repo(service->repo, index);
}


void modify_bill(struct BillService *service, int index, char *attribute, char *new_attribute) {
    if (strcmp(attribute, "nr") == 0) {
        int nr = atoi(new_attribute);
        set_ap_number(get_all_s(service)[index], nr);
    } else if (strcmp(attribute, "suma") == 0) {
        int sum = atoi(new_attribute);
        set_sum(get_all_s(service)[index], sum);

    } else if (strcmp(attribute, "tip") == 0) {
        set_type(get_all_s(service)[index], new_attribute);
    } else {
        return;
    }
}

bool cmp(struct Bill *x, struct Bill *y, char *key, bool reverse) {

    int rev = (reverse == 1) ? -1 : 1;

    if (strcmp(key, "nr") == 0) {
        return (rev * get_ap_number(x) >
                rev * get_ap_number(y));
    } else if (strcmp(key, "suma") == 0) {
        return (rev * get_sum(x) >
                rev * get_sum(y));
    } else {
        return 0;
    }
}
Vector* filter_by(struct BillService*service, char*attribute, char*value){
    Vector*res = create_vector();

    for (int i = 0; i < get_service_len(service); ++i) {

        if(strcmp(attribute, "nr") == 0){
            if(get_ap_number(get_all_s(service)[i]) == atoi(value))
                push_back(&res, get_all_s(service)[i]);
        }
        else if(strcmp(attribute, "suma") == 0){
            if(get_sum(get_all_s(service)[i]) < atoi(value))
                push_back(&res, get_all_s(service)[i]);
        }
        else if(strcmp(attribute, "tip") == 0){
            if(strcmp(get_type(get_all_s(service)[i]),value) == 0)
                push_back(&res, get_all_s(service)[i]);
        }
    }
    return res;
}
void sort_by(struct BillService *service, char *attribute, char*mode) {
    bool reverse = (strcmp(mode, "da") == 0);
    struct Bill **arr = get_all_s(service);
    int n = get_service_len(service);

    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (cmp(arr[j], arr[j + 1], attribute, reverse)) {
                struct Bill *temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

void destroy_service(struct BillService *service) {
    destroy_repo(service->repo);
    free(service);

}