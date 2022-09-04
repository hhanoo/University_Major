import numpy as np
import matplotlib.pyplot as plt

wt = np.array(50 * np.random.random(100) + 40)
ht = np.array(np.random.randint(140, 200, 100))

BMI = np.round(wt / ((ht / 100) ** 2), 1)
status = ['Underweight', 'Healthy', 'Overweight', 'Obese']

# Bar Chart
BMI_count = [0, 0, 0, 0]

for i in BMI:
    if i < 18.5:
        BMI_count[0] = BMI_count[0] + 1
    elif i < 24.9:
        BMI_count[1] = BMI_count[1] + 1
    elif i < 29.9:
        BMI_count[2] = BMI_count[2] + 1
    else:
        BMI_count[3] = BMI_count[3] + 1

plt.bar(status, BMI_count)
plt.title("Bar Chart of BMI")
plt.show()

# Histogram
plt.hist(BMI, bins=[10, 18.5, 25.0, 30.0, 46.0])
plt.xticks([10, 18.5, 25.0, 30.0, 46.0])
plt.title("Histogram of BMI")
plt.xlabel("BMI Level")
plt.ylabel("Student distribution")
plt.show()

# Pie Chart
plt.pie(BMI_count, labels=status, autopct='%1.2f%%')
plt.title("Pie Chart")
plt.show()

# Scatter Plot

plt.scatter(ht, wt, color='b')
plt.title("Scatter plot of BMI")
plt.xlabel("Height")
plt.ylabel("Weight")
plt.show()
