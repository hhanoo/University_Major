import math


def isPrime(n):
    for i in range(2, int(math.sqrt(n)) + 1):
        if n % i == 0:
            return 0
    return 1


num = int(input())
if isPrime(num):
    print(str(num) + " is prime\n")
else:
    print(str(num) + " is not prime\n")
