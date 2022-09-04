import pandas as pd
import numpy as np
import seaborn as sns
import matplotlib.pyplot as plt

test_score = (28, 35, 26, 32, 28, 28, 35, 34, 46, 42, 37)
# test_score = (20, 15, 26, 32, 18, 28, 35, 14, 26, 22, 17)
df = pd.DataFrame(test_score)

print(df.describe())
