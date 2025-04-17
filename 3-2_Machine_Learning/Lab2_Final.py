#!/usr/bin/env python
# coding: utf-8

import Lab2_autoML as autoML
import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import warnings

warnings.filterwarnings('ignore')

# read Dataset
df_origin = pd.read_csv('dataset/housing.csv')

# Check data information
df_origin.info()
df_origin.describe()

# Copy dataset
df = df_origin.copy()

# Drop null data
df.dropna(axis=0, inplace=True)

# Check df
df.describe()

# Check the correlation of dataset between target and features
plt.figure(figsize=(10, 6))
sns.heatmap(df.corr(), annot=True, fmt='0.2f')

# Check the correlation of dataset between target and features after encoding
plt.figure(figsize=(10, 6))
tmp_encoding_df = autoML.object_encoder(df, 'LabelEncoder', ['ocean_proximity'])
sns.heatmap(df.corr(), annot=True, fmt='0.2f')

# Select Feature (Select here ↓)
feature_list = ['longitude', 'latitude']

# List setting (Setting here ↓)
model_list = ['kmeans', 'em', 'clarans', 'dbscan', 'meanshift']
encoder_list = ['LabelEncoder', 'OrdinalEncoder']
scaling_list = ['StandardScaler', 'MinMaxScaler', 'MaxAbsScaler', 'RobustScaler', 'Normalizer']

# 20,000 datasets are too large to take long to calculate (especially CLARANS)
# So we will randomly extract only 10% of the total dataset and use it.
random_dataset = df.sample(n=int(df.shape[0] * 0.1), random_state=42)

# auto_ml parameters:
#   input_dataset, model_lists, encoder_lists, scaling_lists, select_feature_lists=None, k_lists=None
autoML.auto_ml(input_dataset=random_dataset, model_lists=model_list, encoder_lists=encoder_list,
               scaling_lists=scaling_list,
               select_feature_lists=feature_list)
