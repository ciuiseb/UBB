import math
# 4. Dandu-se numarul natural n, determina numerele prime p1 si p2 astfel ca
# n = p1 + p2
# (verificarea ipotezei lui Goldbach).
def prim(x):
    if x < 2:
        return False
    if  x == 2:
        return True
    if x % 2 == 0:
        return False
    for d in range(3,int(math.sqrt(x) + 1),2):
        if x % d == 0:
            return False
    return True

def test_prim():
    assert prim(-2) == False
    assert prim(2) == True
    assert prim(3) == True
    assert prim(4) == False
    assert prim(25) == False
    assert prim(100) == False

def solve(n):
    for i in range(2,n-2):
        if prim(i) and prim(n-i):
            print(i, n-i)
            return
    print("nu exista astfel de numere")


n = int(input())
solve(n)
