# 8. Pentru un numar natural n dat gasiti numarul natural maxim m format cu aceleasi cifre. Ex. n=3658, m=8653.
def creareNumar(lista):
    x=0
    for i in range(9,-1,-1):
        while lista[i]:
            x*=10
            x+=i
            lista[i]-=1;
    print(x)

def creareLista(n, lista):
    while n != 0:
        lista[int(n%10)] += 1
        n = int(n / 10)

n = int(input())
lista = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

creareLista(n,lista)
creareNumar(lista)
