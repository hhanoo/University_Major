# Gachon University Machine Learning

### Description of each files

<details>
<summary>week1_Decision_Tree</summary>
</details>

<details>
<summary>week2_SVM</summary>
</details>

<details>
<summary>week3_clustering</summary>
</details>

<details>
<summary>week6_regularization</summary>
</details>

<details>
<summary>Lab1</summary>

## Lab1
<aside>
It is a function that runs 4 models, scaling, encoder, and kfold at once, and outputs information about the highest value of each model and the top 10 information of the whole.
</aside>

**Parameters:**

- **data: DataFrame**
    
  Allowed inputs is pandas dataframes.

  Data of an independent variable

- **model:** {‘DecisionTreeClassifier(), ‘LogisticREgression(), ‘SVC()}, default = None
   
  The type of models to test.
  
  If default=None, it is set to [DecisonTreeClassifier (crierion='enrotpy'), DecisonTreeClassifier (crierion='geni'), LogisticRegression(), and SVC()].

- **scaler:** {‘DecisionTreeClassifier(), ‘LogisticREgression(), ‘SVC()}, default = None
  
  The type of scalers to test <br>

  If default=None, it is set to [None, StandardScaler(), MinMaxScaler(), MaxAbsScaler(), RobustScaler(), Normalizer()].

- **encoders:** {False, LabelEncoder(), OrdinalEncoder()}, default = None
  
  The type of encoder to test. Only select ‘False’, ‘LabelEncoder()’, ‘OrdinalEncoder()’ <br>
  
  If default=None, it is set to [‘False’, ‘LabelEncoder()’, ‘OrdinalEncoder()’]

- **param:** dict, default = None

  The type of parameter according to the model may be selected.<br>
  It can be expressed in the form of a double dict.<br>
  If default=None, it is set to such dictionary data<br>
  <pre><code>
  {'decision_tree_entropy': {'max_depth': [None, 2, 3, 4],
                            'min_samples_split': [2, 3, 4]},
   'decision_tree_gini': {'max_depth': [None, 2, 3, 4],
                          'min_samples_split': [2, 3, 4]},
   'logistic_regress': {'C': [0.01, 0.1, 1, 10]},
   'support_vector': {'kernel': ['linear', 'rbf', 'poly', 'sigmoid'],
                      'C': [0.01, 0.1, 1],
                      'gamma': [0.001, 0.01, 0.1, 1, 10, 100]}
  }
  </code></pre>

- **k:** int, default = None
  Split dataset into k consecutive folds<br>
  Number of folds. Must be at least 2.<br>
  If default=None, it is set to [3, 5, 7, 10]

**Returns:**

- **best_score_models:** DataFrame
  It returns the information of scaler, encoder, k, and param, which represent the highest accuracy in each model, and the accuracy score in the form of a DataFrame.

- **top10_best:** DataFrame
  Among the information according to all models, scaler, encoder, k, and param, the top 10 information with the highest acuity score is returned in DataFrame format.
</details>

<details>
<summary>Lab2</summary>

## Lab2_autoML

<aside>
Python Library provides parameters for outputting clustering results through a combination of clustering models, encoders, and scalers for a dataset of selected features. As a result, plot, purity, and silhouette scores are output.

</aside>

**Methods:**

- **object_encoder**(dataframe, encoder, target_feature)
    
  Encoding target_feature, i.e. category feature, according to encoder method
    
- **data_scaling**(dataframe, scaling)
    
    Scales the data frame according to the scaling function of the numeric data.
    
- **model_kmeans**(X, y, [, k]=None)
    
    Use the K mean clustering model to predict k clusters.
    
- **model_gaussian**(X, y ,[, k]=None)
    
    Use the GaussianMixture(EM) clustering model to predict k clusters.
    
- **model_clarans**(X, y ,[, k]=None)
    
    Use the CLARANS clustering model to predict k clusters.
    
- **model_dbscan**(X, y)
    
    Use the DBSCAN clustering model to predict clusters.
    
- **model_meanshift**(X, y)
    
    Use the Mean Shift clustering model to predict clusters.
    
- **scores**(dataset, predict_y, true_y, k)
    
    Compare actual and predictive results to obtain purity and silhouette scores, and return information about them as strings.
    
- **compute_purity**(predict_y, true_y ,k)
    
    Compare actual and predicted results to obtain a purity score.
    
- **preprocessing** (dataset, encoder, scaling, encode_feature_list)
    
    Transform the dataset to match encoder and scaling.
    
- **auto_ml**(dataset, [, model],[, ecoder], [, scaling], [, select_feature_list]=None, [, k]=None )
    
    **Major Function.**
    Plot the scores and results for all combinations of model, encoder, scaling, and k.
    
---

## Lab2_autoML.auto_ml

<aside>
class **auto_ml** *(input_dataset, model_lists, encoder_lists, scaling_lists, select_feature_lists=None, k_lists=None):*

</aside>

**Major Function**.

The result of the combination of all parameters is shown through plot.

**Parameters:**

- **input_dataset: pd.DataFrame**
    
    Dataset with combined x,y for clustering
    
- **model_lists: list**
    
    List of models for clustering
    
- **encoder_lists: list**
    
    List of encoders for clustering
    
- **scaling_lists: list**
    
    List of scaliers for clustering
    
- **select_feature_lists: list, default = None**
    
    List of features to be extracted and used separately from ‘input_dataset’
    
- **k_lists: list, default = None**
    
    List of clustering counts (ex. [1,2,3] or [2,5,10])
    

**Attribute:**

- **dataset_x: pd.DataFrame**
    
    Data set excluding actual result data in input_dataset
    
- **dataset_y: pd.DataFrame**
    
    Actual result data to be compared with clustering results in input_dataset
    
- **data_category: list, default=None**
    
    List to store the name of the feature whose data is an object in input_dataset
    
- **dataset_x_encode: pd.DataFrame**
    
    Data set with preprocessing
    
- **count: int**
    
    An int-type variable to determine the number of all combinations
</details>
