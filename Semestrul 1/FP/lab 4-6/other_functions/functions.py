import math

def prim(n):
    if n < 2:
        return False
    if n == 2:
        return True
    if n % 2 == 0:
        return False
    for i in range(2, int(math.sqrt(n)) + 1):
        if n % i == 0:
            return False
    return True
def ends_check_valid(list, inceput, final):
    if inceput < 0 or final >= len(list):
        print("Valorile introduse sunt invalide. Alegeti o alta optiune")
        return False
    return True
def pos_check_valid(list, p):
    if p < 0 or p >= len(list):
        print("Valoare invalida")
        return False
    return True
def lists_compare(l1, l2):
    if len(l1) != len(l2):
        return False
    for i in range(0, len(l1)):
        if l1[i] != (l2[i]):
            return False
    return True
def list_print(list):
    for i in range(0, len(list)):
        print("[", list[i].real, " + ", list[i].imag, "i ]")
def check_existance(list, z):
    for i in range(0, len(list)):
        if list[i] == z:
            return True
    print("Numarul ales nu exista in lista")
    return False
def sign_check(sign):
    if not (sign == '=' or sign == '>' or sign == '<'):
        print("Semnul ales e invalid")
        return False
    return True
def del_pos_undo(list, p):
    res = []
    for i in range(0, len(list)):
        if i !=p:
            res.append((list[i]))

    return res

