# Major_DataScience_Term
가천대학교 AI·소프트웨어학부(소프트웨어전공) 3학년 1학기 데이터과학 텀프로젝트

코로나 이전 성적과 코로나 이후 성적의 변화량을 코로나 이전 성적으로 예측해보고함.

- File 'TermProject_cluster3.ipynb' about cluster k = 3 <br>
- File 'TermProject_cluster4.ipynb' about cluster k = 4

---
# 제목 없음

**Contents**

**1. End to End Process**

1-1) Business objective

1-2) Data exploration

1-3) Data preprocessing

1-4) Modeling (training of the learning models)

**2.** **Learning Experience**

2-1) Difficulties encountered and how we have solved them

2-2) What you have learned doing the project

2-3) Reason for failure

**3. Open Source SW**

3-1) Function definition (and description)

3-2) Architecture

# **End to End Process**

## 1-1) Business objective

As the COVID-19 situation occurred, non-face-to-face lectures increased a lot due to the characteristics of educational institutions where a large number of people gathered.

The question that always arises when conducting non-face-to-face lectures is whether proper learning has been achieved.

If you find out whether this is a proper learning through statistics and produce meaningful results, you will be able to recommend lectures to suitable students at various educational institutions.

Therefore, it is intended to find meaningful results so that face-to-face or non-face-to-face lectures can be recommended to suitable students.

## 1-2) Data exploration

![Untitled](https://user-images.githubusercontent.com/71388566/230099856-a8fdc7f9-ae92-4632-9045-2eed3385b523.png)

> 8400 rows x 18columns
> 

Columns

- StudentID: StudentID into Number assigned to student
- School: Wealthy School (0), Poor School (1)
- Grade Level: Determine grade level of child
- Gender: Female(F), Male(M)
- CovidposNull (0), Child had Covid (1)
- Householdincome: Household income for child
- Freelunch: Null (0), Takes free and reduced lunch (1)
- Numcomputers: Number of computers in child’s home
- Familysize: Defines size of family, parents and siblings
- Fathereduc, Mothereduc:
No HS diploma (0), HS diploma (1), Bachelor degree (2), Master’s degree (3), PhD(4)
- Reading score, Writing score, Math score:
Score for “reading”, “writing”, “math” test in school
- Reading scoreSL, Writing scoreSL, Math scores:
Score for “reading”, “writing”, “math” test at state level
- Timeperiod:
In-person learning (0,1,2), Online learning (3,4,5)

## 1-3) Data preprocessing

- Gender Labeling with Ordinal Encoder
- Average of each score before and after Corona
- Average each score before and after corona and add it to the data set
- Too many features drop to reduce the number of columns
- Increase the level of learning of data using the outlier.

## 1-4) Modeling (training of the learning models)

- Use clustering to create new features associated with multiple columns.
- Use selectKBest to find the most relevant feature.
- Use various scalers to linear regression.
- Determine which model is appropriate through MAE, MSE, and RMSE.

## 1-5) Learning model evaluation and analysis

- Use confusion matrix to evaluate what is judged to be the best model.
- Obtain precision and recall through confusion matrix and evaluate if the data is modeled according to the purpose.

# 2. **Learning Experience**

## 2-1) Difficulties encountered and how we have solved them

- Original Dataset Train-Test Score too Low
    
    → Using Scaling to reduce the influence on large numbers.
    
- Too **many** scores columns
    
    → All of the same kind of data (score) were combined into one average. The number of columns was reduced by making it an average.
    
- Do not know if the number of clusters is appropriate & Unable to determine if the applied feature is appropriate
    
    → All conditions were applied one by one and the scores were printed out and compared directly.
    

## 2-2) What you have learned doing the project

- The amount of data is very important. Too little data is very disadvantageous when training a model. If the test data is applied due to poor learning, it is difficult to output the correct results.
- Outlier data is easy to miss in dataset preprocessing. Outlier affects model learning more than you think. By excluding these unexpected data, the model can be trained to make relatively accurate predictions.
- Linear regression alone makes it difficult to learn a dataset model to which several variables are applied. The more variables there are, the more overfitting may occur, but it is thought that it is rather difficult to predict because several variables are not applied. If we learn several variables and supplement the problems of variables in the model, I think we can make a good model. Therefore, although I did not learn from the lecture on these datasets, I would have been more predictable if I applied multiple regression.
- I think clustering should check the shape of the data to be applied and apply it. Since clustering with a very good shape looks good apart from each other, I think the clustering we performed was too close to each other to achieve proper clustering. I think it is necessary to check in advance by outputting a graph of the corresponding data before clustering.

## 2-3) Reason for failure

- Use modeling methods that were not appropriate
- A statistical lie
- Until the professor told us, I focused only on statistical numbers and did not think of errors that were invisible to statistics.
- Even if the student's score is between 0 and 100, if the difficulty of the test changes, it is not appropriate to compare the test scores from other periods.
- Since these statistical errors were put into the model without thinking about them, it may be statistically predicted, but in reality, it is very likely to make false predictions.

# 3. **Open Source SW**

## 3-1) Function definition (and description)

1. Function ‘outliars'
    
    
    | Parameters : | data: Dataframe<br>    > Data to be calculated<br><br>    column: np.array<br>    > Array of columns to be applied to the calculation |
    | --- | --- |
    | Return | outliers_index: int<br>    > Outlier data’s index numbers |
2. Function ‘clustering_scaling'
    
    
    | Parameters : | Data: Dataframe<br>    > Dataset list’s index number<br><br>    k: int<br>    > cluster’s k size<br><br>    scaler: sklearn.preprocessing<br>    > Using scaler<br><br>    relationCol: np.array<br>    > Array of column’s name to applied scaling<br><br>    clusterName: String<br>    > Cluster name<br><br>    xCol: String<br>    > Name xlabel<br><br>    yCol: String<br>    > Name ylabel |
    | --- | --- |
    | Return | plt: plot chart<br>    > Result plot chart<br><br>    data.groupby(clusterName).size : Dataframe<br>    > Count data each cluster types |
3. Function ‘selectKBest_scaling'
    
    
    | Parameters : | data: Dataframe<br>    > Using Dataset<br><br>    scaler: sklearn.preprocessing<br>    > Using scaler<br><br>    xCol: np.array<br>    > Array of column’s name to use<br><br>    yCol: String<br>    > Cluster name |
    | --- | --- |
    | Return: | featureScores.nlargest(10, 'Score’): Datafame<br>    > 10 datas with the largest value |
4. Function ‘linearRegScaling'
    
    
    | Parameters : | sclar: sklearn.preprocessing<br>    > Using scaler<br><br>    k: int<br>> cluster’s k size<br><br>    data: dataframe<br>    > Data to be calculated<br><br>    testSize: float<br>    > define test size<br><br>    largeColumns: <br>    > Array of column’s name to apply scaling<br><br>    target: String<br>    > Cluster name |
    | --- | --- |
    | Return: | resultTrainScore: float<br>    > Train score data<br><br>    resultTestScore: float<br>    > Test score data |
5. Function ‘modelEvaluation'
    
    
    | Parameters : | type: int<br>    > The index number of the dataset declared in the array named data_transform_list<br><br>    largeColumns: <br>    > Array of column’s name to apply scaling<br><br>    target: String<br>    > Cluster name<br><br>    testSize: float<br>    > define test size |
    | --- | --- |
    | Return: | mae: float<br>    > Result MAE score<br><br>    mse: float<br>    > Result MSE score<br><br>    rmse: float<br>    > Result RMSE score |
6. Function ‘precisionRecall'
    
    
    | Parameters : | matrix: dataframe<br>    > Result of confusion matrix<br><br>    columns: list<br>    > list of matrix’s column name |
    | --- | --- |
    | Return: | precision: np.array<br>    > Result precision data each label<br><br>    recall: np.array<br>    > Result recall data each label |
