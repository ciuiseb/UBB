"""
Se dă o listă de numere întregi a1,...an, determinați toate sub-secvențele (ordinea
elementelor este menținută) strict crescătoare.
Pentru [1, 2, 7, 2, 7]:
[1, 2]
[1, 2, 7]
[2, 7]
[3, 4]
1 2 7 3 4
"""


class SubSecv:
    def __init__(self):
        self.lista_initiala = self.get_int_list_form_input()
        # self.lista_initiala = [1,2,7,3,4]
        self.sol = []
        self.sol_position = -1
        self.max = max(self.lista_initiala)

    def get_int_list_form_input(self):
        return [int(x) for x in input("Introduceti lista: ").split(" ")]

    def print_solution(self):
        res = []
        for element in self.sol:
            res.append(element[0])
        print(res)

    def consistent(self):
        for i in range(1, len(self.sol)):
            if (self.sol[i][0] <= self.sol[i - 1][0] or
                    self.sol[i][1] - 1 != self.sol[i - 1][1]):
                return False
        return True

    def valid(self):
        if len(self.sol) < 2:
            return False
        return True

    def bkt(self, pozitie):
        for i in range(pozitie, len(self.lista_initiala)):
            self.sol.append([self.lista_initiala[i], i])
            if self.consistent():
                if self.valid():
                    self.print_solution()
                self.bkt(pozitie + 1)
            if self.sol:
                self.sol.pop()


run = SubSecv()
run.bkt(0)
"""
Spatiu de cautare: A^n
    *A = lista initiala
    *n = len(A)
*daca nu mai exista nr mai mari
"""
