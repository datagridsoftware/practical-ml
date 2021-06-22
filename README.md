# Practical Machine Learning 

Support materials for the Practical Machine Learning course.

## Getting ready for the course
1. Clone this repository
1. Install the [Prerequisites](#Prerequisites)
1. Open a system terminal (Mac, Linux)
    1. For Windows open an **Anaconda Prompt** instead
1. In the terminal go to the repository folder
1. Create the [SKLearn](#SKLearn) environment
1. Activate the SkLearn environment and start the **Jupyter Notebooks** from the terminal
    ```
    conda activate sklearn-env
    jupyter-notebook
    ```
1. In the Jupyter Notebook UI open `notebooks/00_Test.ipynb`
1. Run the test notebook

### Prerequisites
- Anaconda (https://www.anaconda.com/products/individual)
- Miniconda (https://docs.conda.io/en/latest/miniconda.html) - alternative to Anaconda
- Java 8 or higher version - required for Spark

### Notebook Environments

Conda environments supporting the various frameworks used in the exercises.

#### SKLearn
- Conda environment setup
  ```
  conda create -n sklearn-env python=3.8
  conda activate sklearn-env
  pip install -r ./pip/requirements-sklearn.txt
  ```

  For Windows
  ```
  conda install pywin32
  ```

#### Keras / Tensorflow
- Conda environment setup
  ```
  conda create -n tf-env python=3.8
  conda activate tf-env
  pip install -r ./pip/requirements-tf.txt
  ``` 
- Graphviz installation 
  - Please follow instructions specific for your OS from https://graphviz.gitlab.io/download/

#### Pyspark

Pyspark is a python layer over a Spark installation.

- Conda environment setup
  ```
  conda create -n pyspark-env python=3.8
  conda activate pyspark-env 
  pip install -r ./pip/requirements-pyspark.txt
  ```
- Spark installation
  - Download spark archive with hadoop support from https://spark.apache.org/downloads.html
    ```
    wget https://downloads.apache.org/spark/spark-3.1.2/spark-3.1.2-bin-hadoop3.2.tgz
    ```
  - Unpack spark archive
    ``` 
    mkdir ~/APPS/
    cd ~/APPS
    tar -xvzf spark-3.1.2-bin-hadoop3.2.tgz
    ```
  - Setup environment variable
    ```
    export SPARK_HOME=$HOME/APPS/spark-3.1.2-bin-hadoop3.2
    export PATH=$SPARK_HOME/bin:$SPARK_HOME/sbin:$PATH
    export SPARK_SUBMIT_OPTS="--illegal-access=permit -Dio.netty.tryReflectionSetAccessible=true"
    ```
    - Note: The last parameter is needed only in case you are running with Java version equal or higher than 11
    
    - Troubleshooting:
      - (Windows) In case you notice and error specifying `Python worker failed to connect back`, set up the following environment variables on your system:
      ```
      PYSPARK_DRIVER_PYTHON=jupyter
      PYSPARK_DRIVER_PYTHON_OPTS=notebook
      PYSPARK_PYTHON=python
      ```

#### Useful Conda commands

- List conda environments
  ```
  conda env list
  ```
  
- Create conda environment
  ```
  conda create -n <env_name>
  ```
  
- Create conda environment with specific python version
  ```
  conda create -n <env_name> python=<python_version>
  ```
  e.g.
  ```
  conda create -n <env_name> python=3.8
  ```

- Remove conda environment
  ```
  conda env remove -n <env_name>
  ```
  
- Activate conda environment
  ```
  conda activate <env_name>
  ```

## Data sources
This is not meant as an exhaustive dataset listing. The datasets listed here are the ones used in the this course

| Name      | Source | License     |
|:--- |:---   |:--- |
| auto-mpg.data      | http://archive.ics.uci.edu/ml/machine-learning-databases/auto-mpg/auto-mpg.data       | CC0 1.0: Public Domain   |
| auto-mpg.csv   | https://www.kaggle.com/uciml/autompg-dataset        | CC0 1.0: Public Domain      |
| dataset_191_wine.csv   | https://www.openml.org/d/187        | Public Domain Mark 1.0       |
| titanic | https://www.kaggle.com/c/titanic/data/  or https://www.openml.org/d/40945| Public Domain Mark 1.0 |
| MNIST | http://yann.lecun.com/exdb/mnist/ | Public |
| ImageNet | https://image-net.org/| Request access |
