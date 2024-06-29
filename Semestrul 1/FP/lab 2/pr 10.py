#10. Pentru un numar natural n dat gasiti numarul natural minim m format cu aceleasi cifre.
def creareNumar(l):
    m = 0
    for i in range(0,10):
        while(l[i]):
            m*=10
            m+=i
            l[i]-=1
    print(m)
def creareLista(n, l):
    while n!=0:
        l[n%10]+=1
        n = int(n/10)  

n = int(input())
l = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

creareLista(n,l)
creareNumar(l)
