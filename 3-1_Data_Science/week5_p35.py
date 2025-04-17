import pandas as pd
import numpy as np
import featuretools as ft
from woodwork.logical_types import Categorical

# load dataFrame
clients_df = pd.read_csv("data/clients.csv")
loans_df = pd.read_csv("data/loans.csv")
payments_df = pd.read_csv("data/payments.csv")

# Create new entity-set
es = ft.EntitySet(id="clients")

# --------------------------------------------------
# Attribute 'entity_from_dataframe' is OLD version
# Attribute 'add_dataframe' is NEW version
# Add an entity to entity-set
es = es.add_dataframe(dataframe_name="clients",
                      dataframe=clients_df,
                      index="client_id",
                      time_index="joined")

es = es.add_dataframe(dataframe_name="loans",
                      dataframe=loans_df,
                      logical_types={"repaid": Categorical},
                      index="loan_id",
                      time_index="loan_start")

es = es.add_dataframe(dataframe_name="payments",
                      dataframe=payments_df,
                      logical_types={"missed": Categorical},
                      make_index=True,
                      index="payment_id",
                      time_index="payment_date")

# --------------------------------------------------
# Aggregation
# Group loans by client id and calculate total of loans
stats = loans_df.groupby('client_id')['loan_amount'].agg(['sum'])
stats.columns = ['total_loan_amount']

# Merge with the clients dataframe
stats = clients_df.merge(stats, left_on='client_id', right_index=True, how="left")

# --------------------------------------------------
# Relationships Between Entities
# Between clients and previous loans
r_client_previous = ft.Relationship(es, 'clients', 'client_id', 'loans', 'client_id')

# Between previous loans and previous payments
r_payments = ft.Relationship(es, 'loans', 'loan_id', 'payments', 'loan_id')

# Add the relationship to the entity set
es = es.add_relationship(relationship=r_client_previous)
es = es.add_relationship(relationship=r_payments)

# --------------------------------------------------
# Create new features using specified primitives
features, feature_names = ft.dfs(entityset=es,
                                 target_dataframe_name="clients",
                                 agg_primitives=["sum"],
                                 trans_primitives=['year', 'month'])

print(features.head())
