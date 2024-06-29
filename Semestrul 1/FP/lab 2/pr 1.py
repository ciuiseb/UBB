import math
# 1.Gasiti primul numar prim mai mare decat un numar dat
def prim(x):
    if x < 2:
        return False
    if  x == 2:
        return True
    if x % 2 == 0:
        return False
    for d in range(3,int(math.sqrt(x)),2):
        if x % d == 0:
            return False
    return True

def test_prim():
    assert prim(-1) == False
    assert prim(2) == True
    assert prim(3) == True
    assert prim(4) == False
    assert prim(29) == True
    assert prim(49) == False
    
def func(n):
    n+=1
    while prim(n) == 0:
     n+=1
    print(n)

n = int(input())
func(n)
    
    
