def makeDict(K, V):
    D = {}
    for i in range(0, len(K)):
        D[K[i]] = V[i]
    return D


Key = ["Korean", "Mathematics", "English"]
Value = (90.3, 85.5, 92.7)
print(makeDict(Key, Value).items())