# Gachon University Machine Learning

### Description of each files

<details>
<summary>week1_p99</summary>
</details>

<details>
<summary>week2_SVM_p30</summary>
</details>

<details>
<summary>week3_clustering_p27</summary>
</details>

<details>
<summary>week3_clustering_p28</summary>
</details>

<details>
<summary>Lab1</summary>
</details>

<details>
<summary>Lab2</summary>

## Lab2_autoML

<aside>
💡 Python Library provides parameters for outputting clustering results through a combination of clustering models, encoders, and scalers for a dataset of selected features. As a result, plot, purity, and silhouette scores are output.

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
💡 class **auto_ml** (*input_dataset, model_lists, encoder_lists, scaling_lists, select_feature_lists=None, k_lists=None):*

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
