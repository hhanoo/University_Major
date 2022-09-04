import numpy as np

wt = np.array(50 * np.random.random(100) + 40)
ht = np.array(np.random.randint(140, 200, 100))

BMI = np.round(wt / ((ht / 100) ** 2), 1)

print(BMI)
