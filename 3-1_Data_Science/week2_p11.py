import numpy as np

b = 2. * np.ones((2, 2), float)  # overloaded
print(b)
"""
[[2. 2.]
 [2. 2.]]
"""

b = b + 1  # Addition of a scalar is
print(b)  # element by element
"""
[[3. 3.]
 [3. 3.]]
"""

c = 2. * b  # Multiplication by a scalar is
print(c)  # element by element
"""
[[6. 6.]
 [6. 6.]]
"""
