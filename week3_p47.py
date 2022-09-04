import pandas as pd
import numpy as np

np_array = ([3., '?', 2., 5.], ['*', 4., 5., 6.], ['+', 3., 2., '&'], [5., '?', 7., '!'])

# Create a Pandas (4,4) DataFrame from the following Numpy array
df = pd.DataFrame(np_array)


"""
df = pd.DataFrame(
    {'col_1': [3., '?', 2., 5.],
     'col_2': ['*', 4., 5., 6.],
     'col_3': ['+', 3., 2., '&'],
     'col_4': [5., '?', 7., '!']})
"""

# Display the DataFrame
print(df)
print()

# Replace any non-numeric value with NaN
df.replace(['?', '*', '+', '&', '!'], np.NaN, inplace=True)

# Display the DataFrame
print(df)
print()

# Apply the following functions one at a time in sequence to the DataFrame
print("df.isna().any()")
print(df.isna().any())
print()

print("df.isna().sum()")
print(df.isna().sum())
print()

print("df.dropna(how='any')")
print(df.dropna(how='any'))
print()

print("df.dropna(how='all')")
print(df.dropna(how='all'))
print()

print("df.dropna(thresh=1)")
print(df.dropna(thresh=1))
print()

print("df.dropna(thresh=2)")
print(df.dropna(thresh=3))
print()

print("df.fillna(100)")
print(df.fillna(100))
print()

print("df.fillna(df.mean())")
print(df.fillna(df.mean()))
print()

print("df.fillna(df.median())")
print(df.fillna(df.median()))
print()

print("df.fillna(method='ffill')")
print(df.fillna(method='ffill'))
print()

print("df.fillna(method='bfill')")
print(df.fillna(method='bfill'))
