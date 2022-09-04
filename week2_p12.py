import numpy as np

a = np.zeros((3, 3), float) + 2. * np.identity(3)

print(np.linalg.inv(a))
"""
[[0.5 0.  0. ]
 [0.  0.5 0. ]
 [0.  0.  0.5]]
"""

print(np.linalg.det(np.linalg.inv(a)))
"""
0.12500000000000003
"""

print(np.diagonal(a))
"""
[2. 2. 2.]
"""

print(np.diagonal(a, 1))
"""
[0. 0.]
"""