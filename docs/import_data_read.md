# Library imports and data read

The section is used to import libraries and data that will be used in the current notebook.  The libraries contain code and methods that are being used to read, manipulate and display information about the data, to build models, generate charts, etc.
Those libraries need to be installed before running the notebook. For that you need to run 
 - in case is a new environment 
    
    Create a new conda environment and install libraries
    ```
    conda create -n pyspark-env python=3.9
    conda activate pyspark-env 
    pip install -r ./pip/requirements-notebook.txt
    ```
- if an environement is already created, jut install the libraries
    ```    
    conda activate pyspark-env 
    pip install -r ./pip/requirements-notebook.txt
    ```

Data can be read by using the read method from pandas

        
        dataframe = pd.read_excel("..\..\data\insurance\car_insurance_fraud.xlsx")
        

There are several files that are being supported, like csv, json, parquet, etc. In case you need another format of the file, use the correspondent method like

        dataframe = pd.read_csv("path to the file/ files")
        dataframe = pd.read_json("path to the file / files")
        dataframe = pd.read_parquet("path to the files / files")

Files can be also loaded from an URL. In this case , as a parameter to the function a URL need to be passed

        dataframe = pd.read_csv('https://sample.com/file.csv')

More details on data reading can be found at [Data Input/Output with Pandas](https://pandas.pydata.org/pandas-docs/stable/reference/io.html)

