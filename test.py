from IPython.core.pylabtools import figsize #import libraries
import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from sklearn.preprocessing import RobustScaler
from sklearn.preprocessing import MinMaxScaler
from sklearn import linear_model
from sklearn import preprocessing
from sklearn.linear_model import LinearRegression
import seaborn as sns; sns.set()
#get excel file
df = pd.read_excel("data/bmi_data_phw3.xlsx")

#print data exploration
print(df.describe())
print(df.columns)
print(df.info())

#print height & weight histogram for each BMI value using for loop
f, axes = plt.subplots(2, 5, figsize=(20, 10), sharex=False, sharey=False)
for i in range(0, 5):
  condition1 = df['BMI'] == i
  filtered_df = df.loc[condition1, ['Height (Inches)']]
  sns.histplot(filtered_df['Height (Inches)'], ax = axes[0, i], bins=10, label="BMI 1")

for i in range(0, 5):
  condition_weight = df['BMI'] == i
  filtered_df_weight = df.loc[condition_weight, ['Weight (Pounds)']]
  sns.histplot(filtered_df_weight['Weight (Pounds)'], ax = axes[1, i], bins = 10)

#standard scaler
print("Standard scaler")
df_stdScaler = pd.read_excel("data/bmi_data_phw3.xlsx")
df_stdScaler = df_stdScaler.drop(['Sex'], axis = 1)
df_stdScaler = df_stdScaler.drop(['Age'], axis = 1)
df_stdScaler = df_stdScaler.drop(['BMI'], axis = 1)
scaler = StandardScaler()
scaled_df = scaler.fit_transform(df_stdScaler)
scaled_df = pd.DataFrame(scaled_df, columns=['Height (Inches)',   'Weight (Pounds)'])
print(scaled_df)
sns.kdeplot(scaled_df['Height (Inches)'])
sns.kdeplot(scaled_df['Weight (Pounds)'])
#MinMaxScaler
print("MinMaxScaler")
df_mmScaler = pd.read_excel("data/bmi_data_phw3.xlsx")
df_mmScaler = df_mmScaler.drop(['Sex'], axis = 1)
df_mmScaler = df_mmScaler.drop(['Age'], axis = 1)
df_mmScaler = df_mmScaler.drop(['BMI'], axis = 1)


scalerMinMax = MinMaxScaler()
scaled_df_MinMax = scalerMinMax.fit_transform(df_mmScaler) #df was scaled
df_scaled_df_MinMax = pd.DataFrame(scaled_df_MinMax, columns=['Height (Inches)',   'Weight (Pounds)']) #change data type array to data frame
print(df_scaled_df_MinMax)
sns.kdeplot(df_scaled_df_MinMax['Height (Inches)'])
sns.kdeplot(df_scaled_df_MinMax['Weight (Pounds)'])

#robust scaler
print("Robust scaler")
df_rbScaler = pd.read_excel("data/bmi_data_phw3.xlsx")
df_rbScaler = df_rbScaler.drop(['Sex'], axis = 1)
df_rbScaler = df_rbScaler.drop(['Age'], axis = 1)
df_rbScaler = df_rbScaler.drop(['BMI'], axis = 1)

scalerRobust = RobustScaler()
scaled_df_Robust = scalerRobust.fit_transform(df_rbScaler) #df was scaled
df_scaled_df_Robust = pd.DataFrame(scaled_df_Robust, columns=['Height (Inches)',   'Weight (Pounds)']) #change data type array to data frame
print(df_scaled_df_Robust)
sns.kdeplot(df_scaled_df_Robust['Height (Inches)'])
sns.kdeplot(df_scaled_df_Robust['Weight (Pounds)'])


#linear regression
dfLinear = pd.read_excel("data/bmi_data_phw3.xlsx") #get excel file
X = dfLinear['Height (Inches)']
y = dfLinear['Weight (Pounds)']

E = linear_model.LinearRegression()
E.fit(X.values.reshape(-1, 1), y) #reshape 2d array

dataFrame = pd.DataFrame()

dataFrame['Weight (Pounds) Linear regression'] = E.predict(X.values.reshape(-1, 1))  #reshape 2d array

#print(dataFrame)
e = pd.DataFrame()
e = dfLinear['Weight (Pounds)'] - dataFrame['Weight (Pounds) Linear regression'] #calculate ze
#print(e.size)

ze = ((e - np.mean(e, axis = 0)) / np.std(e, axis = 0))
ze.columns = ['ze']
#print(ze)
#ze histogram
plt.title("Normalize the e values")
plt.xlabel("ze")
plt.ylabel("frequency")
plt.hist(ze, bins = 10)
plt.show()

#make zeBMI array
zeBMI = [0 for i in range(100)]

#get bmi value by ze data
# [-1.5 , -0.75, 0.75, 1,5] -> BMI 0, 1, 2, 3, 4
for i in range(0, len(ze)):
  if(ze[i]<-1.5):
    zeBMI[i] = 0
  if(ze[i]>=-1.5 and ze[i]<-0.75):
    zeBMI[i] = 1
  if(ze[i]>=-0.75 and ze[i]<0.75):
    zeBMI[i] = 2
  if(ze[i]>=0.75 and ze[i]<1.5):
    zeBMI[i] = 3
  if(ze[i]>=1.5):
    zeBMI[i] = 4

#print(zeBMI)
zeBMI_df = pd.DataFrame(zeBMI)
zeBMI_df.columns = ['BMI']
#print(zeBMI_df)
#merger ze and zeBMI_df data frame
zeResult = pd.concat([ze, zeBMI_df], axis = 1)
zeResult = pd.concat([df, zeResult], axis = 1)
#concat ze data frame and original data frame for compare my BMI estimate
print("Final Result")
print(zeResult)

#male
df_male = pd.read_excel("data/bmi_data_phw3.xlsx")
df_male = df_male.loc[df_male['Sex'] == 'Male']
df_male = df_male.reset_index(drop = True)
df_male
X_male = df_male['Height (Inches)']
y_male = df_male['Weight (Pounds)']

E_male = linear_model.LinearRegression()
E_male.fit(X_male.values.reshape(-1, 1), y_male)

dataFrame_male = pd.DataFrame()

dataFrame_male['Weight (Pounds) Linear regression'] = E_male.predict(X_male.values.reshape(-1, 1))

e_male = pd.DataFrame()
e_male = df_male['Weight (Pounds)'] - dataFrame_male['Weight (Pounds) Linear regression']

ze_male = (e_male - np.mean(e_male, axis = 0)) / (np.std(e_male, axis = 0))
#print(ze_male.size)
ze_male.columns = ['ze_male']
plt.title("Normalize the e_male values")
plt.xlabel("ze_male")
plt.ylabel("frequency_male")
plt.hist(ze, bins = 10)
plt.show()
ze_male

zeBMI_male = [0 for i in range(0, len(ze_male))]
#get bmi value by ze data
# [-1.5 , -0.75, 0.75, 1,5] -> BMI 0, 1, 2, 3, 4
for i in range(0, len(zeBMI_male)):

  if(ze_male[i] < -1.5):
    zeBMI_male[i] = 0

  if(ze_male[i] >= -1.5 and ze_male[i] < -0.75):
    zeBMI_male[i] = 1

  if(ze_male[i] >= -0.75 and ze_male[i] < 0.75):
    zeBMI_male[i] = 2

  if(ze_male[i] >= 0.75 and ze_male[i] < 1.5):
    zeBMI_male[i] = 3

  if(ze_male[i] >= 1.5):
    zeBMI_male[i] = 4

zeBMI_male
#print(zeBMI)
zeBMI_df_male = pd.DataFrame(zeBMI_male)
zeBMI_df_male.columns = ['BMI_male']
#print(zeBMI_df)
#merger ze and zeBMI_df data frame
zeResult_male = pd.concat([ze_male, zeBMI_df_male], axis = 1)
zeResult_male = pd.concat([df_male, zeResult_male], axis = 1)
#concat ze_male data frame and original data frame for compare my BMI estimate
print("Final Result")
print(zeResult_male)


#female
df_female = pd.read_excel("data/bmi_data_phw3.xlsx")
df_female = df_female.loc[df_female['Sex'] == 'Female']
df_female = df_female.reset_index(drop = True)
df_female
X_female = df_female['Height (Inches)']
y_female = df_female['Weight (Pounds)']

E_female = linear_model.LinearRegression()
E_female.fit(X_female.values.reshape(-1, 1), y_female)

dataFrame_female = pd.DataFrame()

dataFrame_female['Weight (Pounds) Linear regression'] = E_female.predict(X_female.values.reshape(-1, 1))

e_female = pd.DataFrame()
e_female = df_female['Weight (Pounds)'] - dataFrame_female['Weight (Pounds) Linear regression']

ze_female = (e_female - np.mean(e_female, axis = 0)) / (np.std(e_female, axis = 0))
#print(ze_female.size)
ze_female.columns = ['ze_female']
plt.title("Normalize the e_female values")
plt.xlabel("ze_female")
plt.ylabel("frequency_female")
plt.hist(ze, bins = 10)
plt.show()
ze_female

zeBMI_female = [0 for i in range(0, len(ze_female))]
#get bmi value by ze data
# [-1.5 , -0.75, 0.75, 1,5] -> BMI 0, 1, 2, 3, 4
for i in range(0, len(zeBMI_female)):
  if(ze_female[i]<-1.5):
    zeBMI_female[i] = 0
  if(ze_female[i]>=-1.5 and ze_female[i]<-0.75):
    zeBMI_female[i] = 1
  if(ze_female[i]>=-0.75 and ze_female[i]<0.75):
    zeBMI_female[i] = 2
  if(ze_female[i]>=0.75 and ze_female[i]<1.5):
    zeBMI_female[i] = 3
  if(ze_female[i]>=1.5):
    zeBMI_female[i] = 4

zeBMI_female
#print(zeBMI)
zeBMI_df_female = pd.DataFrame(zeBMI_female)
zeBMI_df_female.columns = ['BMI_female']
#print(zeBMI_df)
#merger ze and zeBMI_df data frame
zeResult_female = pd.concat([ze_female, zeBMI_df_female], axis = 1)
zeResult_female = pd.concat([df_female, zeResult_female], axis = 1)
#concat ze_female data frame and original data frame for compare my BMI estimate
print("Final Result")
print(zeResult_female)