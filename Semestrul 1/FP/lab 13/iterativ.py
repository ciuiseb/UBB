class SubSecv:
    def __init__(self):
        self.lista_initiala = self.get_int_list_form_input()
        # self.lista_initiala = [1, 2, 7, 3, 4]
        self.sol = []

    def get_int_list_form_input(self):
        return [int(self.sol) for self.sol in input("Introduceti lista: ").split(" ")]

    def print_solution(self):
        res = []
        for element in self.sol:
            res.append(self.lista_initiala[element])
        print(res)

    def consistent(self):
        for i in range(1, len(self.sol)):
            if (self.lista_initiala[self.sol[i]] <= self.lista_initiala[self.sol[i - 1]] or
                    self.sol[i] - 1 != self.sol[i - 1]):
                return False
        return True

    def valid(self):
        if len(self.sol) < 2:
            return False
        return True

    def bkt_iterativ(self):
        self.sol = [-1]
        while len(self.sol) > 0:
            chosen = False
            while not chosen and self.sol[-1] < len(self) - 1:
                self.sol[-1] = self.sol[-1] + 1
                chosen = self.consistent()
            if chosen:
                if self.valid():
                    self.print_solution()
                self.sol.append(-1)
            else:
                self.sol = self.sol[:-1]

    def __len__(self):
        return len(self.lista_initiala)


run = SubSecv()
run.bkt_iterativ()
