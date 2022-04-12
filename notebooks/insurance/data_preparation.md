# Data preparation
 
 There are several ways to prepare the data for consumption, and there are countlesss resources dedicated to this. We will refer only to the ones that are used in this notebook, even though this is not a "defenitive guide to data prep", but is enough to get you started.

1. Missing values.
    To print the statistica about the missing values for a dataframe 

        Example:
        dataset_nulls.isnull().sum()
        

    Most training algorithms do not work well with missing values in the dataset.  There are several ways of dealing with missing data, some of which are:
    - **Remove columns/rows that contain missing data**
        
        Example:

        ```
        dataframe.dropna(inplace=True)
        ```

    - **Interpolation** 

        It is using  a number of methods to calculate the value of  the missing data, such:``` ‘linear’, ‘time’, ‘index’, ‘values’, ‘nearest’, ‘zero’, ‘slinear’, ‘quadratic’, ‘cubic’, ‘barycentric’, ‘krogh’, ‘polynomial’, ‘spline’, ‘piecewise_polynomial’, ‘from_derivatives’, ‘pchip’, ‘akima’```. Each of these can be used on different types of data. Usually, interpolation is used when there is a dependence between the values in the data (dates, pacient temperature value)
        
        More on using interpolation with Pandas [here](https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.interpolate.html)
        
        Example:
        ```
        dataframe.interpolate(method="linear")
        ```
        
      
    
    - **Imputation**

        if there is not a strong dependency between the values in a feature, a simpler mathematical formula can be used to fill the missing values, like mean, mode, median, etc. 

        Example (using pandas):

        ```
        datasource['Age'].fillna(datasource['Age'].mean())

        ```

        Example (with Scikit):
        
        ```
        simple_imputer = SimpleImputer(missing_values=np.NaN,strategy='most_frequent') #  possible values for strategy are mean, media, most_frequent and constant
        simple_imputer_arr = imputer_simple.fit_transform(dataframe)
        simple_imputer_datasource = pd.DataFrame(simple_imputer_arr, columns=dataframe.columns)
        ```

2. Data conversion 

    Most of the algorithms used for training a model require data to be of numerical format, so in order to get the most out of the data, a conversion to number is needed.this 

    - **Label encoding**
        It is used to convert cathegorical data in numeric data by assigning a number for each cathegory entry.
        
        Example:
        ```
        labelEncoder = LabelEncoder()
        labelEncoder.fit_transform(dataframe[data_column])
        ```
        
        For example values "male"/"female" will become 0/1
    
    - **Ordinal variable encoding**
        
        In case of ordinal variables, where there is a relation between the values of a feature, that oreder needs to be preserved. In this a mappring between a categorical value and a number is created.
        Example:

        ```
        week_array =['Monday','Tuesday','Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
        dataframe['Week'].replace(week_array, [1,2,3,4,5,6,7], inplace=True)

        ```
        That will replace the name of the week with a number and keep the original ordering, as this may be important. Scikit learn OrdinalEncoder can be also used for this.

    - **Nominal variable encoding (One Hot Encoder)**

        Not in all cases the order is important, and in some cases result in poor performing models. To avoid that we can use One Hot Encoder techinique, which will create so called "dummy" variables.



        **Before OHE:**

        | Color         |
        | ------------- |
        | Red  |  
        | Blue |
        | Green |

        **After OHE:**

        | red|green|blue|
        | ---|---- |----|
        | 1  |  0  | 0  |
        | 0  |  0  | 1  |
        | 0  |  1  | 0  |