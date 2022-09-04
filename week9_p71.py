import numpy as np
import pandas as pd

data = pd.read_excel("data/Decision_Tree.xlsx")
features = data[["District", "House Type", "Income", "Previous Customer"]]
target = data["Outcome"]

print(data)
print()


# Entropy function
def entropy(target_col):
    elements, counts = np.unique(target_col, return_counts=True)
    entropy = -np.sum([(counts[i]/np.sum(counts)) * np.log2(counts[i]/np.sum(counts)) for i in range(len(elements))])
    return entropy

# Information Gain Function
def InfoGain(data, split_attribute_name, target_name):

    # Calculate total entropy
    total_entropy = entropy(data[target_name])

    # Calculate weighted entropy
    vals, counts = np.unique(data[split_attribute_name], return_counts=True)
    weighted_entropy = np.sum([(counts[i]/np.sum(counts))
                               * entropy(data.where(data[split_attribute_name]==vals[i]).dropna()[target_name])
                               for i in range(len(vals))])
    print('Entropy(', split_attribute_name, ') = ', round(weighted_entropy, 5))

    info_gain = total_entropy - weighted_entropy
    return info_gain


# Decision Tree Function
def dcTree(data, originaldata, features, target_attribute_name, parent_node_class = None):

    # if attribute has only one value, return that attribute value
    if len(np.unique(data[target_attribute_name])) <= 1:
        return np.unique(data[target_attribute_name])[0]

    # Tree
    else:
        # Parents node
        parent_node_class = np.unique(data[target_attribute_name]) \
            [np.argmax(np.unique(data[target_attribute_name], return_counts=True)[1])]

    # Select attribute for distributing value
    item_values=[InfoGain(data, feature, target_attribute_name) for feature in features]
    best_feature_index=np.argmax(item_values)
    best_feature=features[best_feature_index]

    # Tree structure
    tree={best_feature: {}}

    # Excepting progress
    features = [i for i in features if i != best_feature]

    # Tree
    for value in np.unique(data[best_feature]):
        sub_data=data.where(data[best_feature]==value).dropna()

        # Recursive call
        subtree=dcTree(sub_data, data, features, target_attribute_name, parent_node_class)
        tree[best_feature][value]=subtree

    return tree



print("\n******** Entropy & Infomation Gain ********")
# Print target entropy
print('Entropy( Outcome ) of Target = ', round(entropy(target), 4), '\n')
# Print infoGain value of each attributes
print('District InfoGain = ', round(InfoGain(data, "District", "Outcome"), 4), '\n')
print('House Type InfoGain = ', round(InfoGain(data, "House Type", "Outcome"), 4), '\n')
print('Income InfoGain = ', round(InfoGain(data, "Income", "Outcome"), 4), '\n')
print('Previous Customer InfoGain = ', round(InfoGain(data, "Previous Customer", "Outcome"), 4), '\n')
print("\n******************************************\n\n")


# Print Decision Tree
tree = dcTree(data, data, ["District", "House Type", "Previous Customer"], "Outcome")
from pprint import pprint
print("\n\n******** Decision Tree ********")
pprint(tree)
print("\n******************************************\n\n")


