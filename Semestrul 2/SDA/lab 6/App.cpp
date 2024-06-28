#include <iostream>
#include "Matrice.h"
#include "TestExtins.h"
#include "TestScurt.h"

using namespace std;


int main() {

	testAll();
    cout<<"Short tests OK\n";

	testAllExtins();

	cout<<"All tests OK";
}
