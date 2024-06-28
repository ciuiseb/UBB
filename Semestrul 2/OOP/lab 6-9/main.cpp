#include "ui.h"
#include "tests.h"

int main() {
    all_tests();
    unique_ptr<AbstractRepo> repo = std::unique_ptr<OffersRepo>();
    Service service(*repo);
    UI ui(service);
    ui.main_menu();
    return 0;
}