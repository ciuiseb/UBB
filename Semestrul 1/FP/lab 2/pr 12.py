import math
#12.Determinati al n-lea element al sirului
# 1,2,3,2,5,2,3,7,2,3,2,5,...
# obtinut din sirul numerelor naturale prin inlocuirea numerelor compuse prin
# divizorii lor primi, fara a retine termenii sirului.
def prim(n):
    if n < 1:
        return False
    if n== 1 or n == 2:
        return True
    if n%2 == 0:
        return False
    for i in range(3, int(math.sqrt(n))+1):
        if n % i == 0 :
            return False
    return True

def test_prim():
    assert prim(1) == True
    assert prim(4) == False
    assert prim(7) == True
    assert prim(15) == False

test_prim()
n = int(input())
i = 1
while n:
    if prim(i) == True:
        if n == 1:
            print(i)
        n-=1
    else:
        m = i
        for j in range(2,m):
            if m % j == 0:
                n -= 1
                m /=  j
                if n == 0:
                    print(j)
                    break
    i += 1
