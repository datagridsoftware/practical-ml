package com.datagridsoftware.mlcourse

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.functions.{col, lit}
import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.{Imputer, MinMaxScaler, OneHotEncoder, StringIndexer, VectorAssembler, VectorIndexer}
import org.apache.spark.ml.linalg.SQLDataTypes.VectorType
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.sql.types.{BooleanType, ByteType, DoubleType, FloatType, StringType, StructField, StructType}

import scala.collection.JavaConverters._
import java.sql.Timestamp
import scala.reflect.io.File

object LogisticRegressionPipeline extends App with StrictLogging{

  val spark = SparkSession.builder.master("local[*]").appName("Spark Scala Playground").getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  val train_df = spark.read.format("csv")
    .option("sep", ",")
    .option("inferSchema", "true")
    .option("header", "true")
    .load("../data/titanic/train.csv")

  val test_df_no_label = spark.read.format("csv")
    .option("sep", ",")
    .option("inferSchema", "true")
    .option("header", "true")
    .load("../data/titanic/test.csv")

  val test_df_label = spark.read.format("csv")
    .option("sep", ",")
    .option("inferSchema", "true")
    .option("header", "true")
    .load("../data/titanic/gender_submission.csv")


  val imputer = new Imputer()
    .setInputCols(Array("Age", "Fare"))
    .setOutputCols(Array("Age_out", "Fare_out"))

  val indexer = new StringIndexer().setInputCols(Array( "Sex", "Cabin", "Embarked")).setOutputCols(Array("Sex_idx", "Cabin_idx", "Embarked_idx")).setHandleInvalid("keep")

  val ohe_str = new OneHotEncoder().setInputCols(Array("Sex_idx", "Cabin_idx", "Embarked_idx")).setOutputCols(Array("Sex_idx_ohe", "Cabin_idx_ohe", "Embarked_idx_ohe"))
  val va_numeric = new VectorAssembler().setInputCols(Array("Pclass", "SibSp", "Parch")).setOutputCol("numeric_features_asm")
  val vi_numeric = new VectorIndexer().setInputCol("numeric_features_asm").setOutputCol("numeric_cat").setHandleInvalid("keep")


  val va_scalers = new VectorAssembler().setInputCols(Array("Age_out", "Fare_out", "numeric_cat")).setOutputCol("numeric_va_out").setHandleInvalid("error")
  val scaler = new MinMaxScaler().setInputCol("numeric_va_out").setOutputCol("numeric_scaled")

  val va_features = new VectorAssembler().setInputCols(Array(
    "Sex_idx_ohe",
    "Cabin_idx_ohe",
    "Embarked_idx_ohe",
    "numeric_scaled")).setOutputCol("features")


  val lr = new LogisticRegression().setMaxIter(10).setRegParam(0.03).setFeaturesCol("features").setLabelCol("Survived")

  val pipeline = new Pipeline().setStages(Array(imputer,
    indexer,
    ohe_str,
    va_numeric,
    vi_numeric,
    va_scalers,
    scaler,
    va_features,
    lr))
  val pipeline_model = pipeline.fit(train_df)

  val scored_df = pipeline_model.transform(test_df_no_label)

  val scored_df_with_label = scored_df.join(test_df_label, Array("PassengerId"))

  scored_df_with_label.show()
  scored_df_with_label.printSchema()
//  scored_df_with_label.select("PassengerId",
//  "Pclass",
//  "Name",
//  "Sex",
//  "Age",
//  "SibSp",
//  "Parch",
//  "Ticket",
//  "Fare",
//  "Cabin",
//  "Embarked",
//  "Survived",
//  "prediction").write.mode("overwrite").format("csv").option("header", "true").csv("./scored_scala_test_titanic.csv")

  val predScoreDf = scored_df_with_label.select(col("prediction"), col("Survived").cast(DoubleType), lit(1.0)).rdd.map {
    case Row(rawPrediction: Vector, label: Double, weight: Double) =>
      (rawPrediction(1), label, weight)
    case Row(rawPrediction: Double, label: Double, weight: Double) =>
      (rawPrediction, label, weight)
  }

  val mlLibEvaluator = new BinaryClassificationMetrics(predScoreDf, 0)

  println(s"areaRoc: ${mlLibEvaluator.areaUnderROC}")
  println(s"areaUnderPR: ${mlLibEvaluator.areaUnderPR}")

  val mlEvaluator = new BinaryClassificationEvaluator().setLabelCol("Survived").setNumBins(0).setRawPredictionCol("prediction")
  println(s"ML areaRoc: ${mlEvaluator.evaluate(scored_df_with_label)}")
}
