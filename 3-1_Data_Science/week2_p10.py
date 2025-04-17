import numpy as np

a = np.ones((3, 3), float)
print(a)

"""
[[1. 1. 1.]
 [1. 1. 1.]
 [1. 1. 1.]]
"""

b = np.zeros((3, 3), float)
b = b + 2. * np.identity(3)  # "+" is overloaded
c = a + b
print(c)

"""
[[3. 1. 1.]
 [1. 3. 1.]
 [1. 1. 3.]]
"""
