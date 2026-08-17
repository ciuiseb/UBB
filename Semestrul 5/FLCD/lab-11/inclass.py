import os
class Gramatica:
    def __init__(self):
        self.reguli = []
    def is_neterminal(self, char):
        return char.isupper()
    def is_terminal(self, char):
        return not char.isupper()
    def read_file(self, file):
        try:
            with open(file, 'r') as f:
                for linie in f:
                    parti = linie.strip().split()
                    if len(parti) >= 2:
                        stanga = parti[0]
                        dreapta = parti[1]
                        self.reguli.append((stanga, dreapta))
            return True
        except Exception as e:
            print(e)
            return False
    def is_fnc(self):
        valid = True
        for st, dr in self.reguli:
            length = len(dr)
            if length == 1:
                if not self.is_terminal(dr[0]):
                    valid = False
            elif length == 2:
                if not (self.is_neterminal(dr[0]) and self.is_neterminal(dr[1])):
                    valid = False
            else:
                valid = False
            if not valid:
                break
        return valid
def run():
    g = Gramatica()
    g.read_file("gram.txt")
    if g.is_fnc():
        print("Gramatica este FNC.\n")
    else:
        print("Gramatica nu este FNC.\n")

if __name__ == '__main__':
    run()