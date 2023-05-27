# Modeling techniques include:
#  -- Linear Regression
#  -- Random Forest Regression
#  -- XGBoost
#  -- Long Short Term Memory (artifical recurrent neural network)
#  -- ARIMA Time Series Forecasting
import pickle

import pandas as pd
import numpy as np

import matplotlib.pyplot as plt
import seaborn as sns

from sklearn.preprocessing import MinMaxScaler
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, mean_absolute_error, r2_score
from sklearn.ensemble import RandomForestRegressor
from xgboost.sklearn import XGBRegressor

import keras
from keras.layers import Dense
from keras.models import Sequential
from keras.layers import LSTM

import statsmodels.api as sm

def load_data(file_name):
    return pd.read_csv(file_name)

model_scores={}

def splitdata(data):
    data = data.drop(['sales','date'],axis=1)
    train,test = data[0:-12].values, data[-12:].values

    return train,test

def scale_data(train_set, test_set):
    #apply Min Max Scaler
    scaler = MinMaxScaler(feature_range=(-1, 1))
    scaler = scaler.fit(train_set)
    
    # reshape training set
    train_set = train_set.reshape(train_set.shape[0], train_set.shape[1])
    train_set_scaled = scaler.transform(train_set)
    
    # reshape test set
    test_set = test_set.reshape(test_set.shape[0], test_set.shape[1])
    test_set_scaled = scaler.transform(test_set)
    
    X_train, y_train = train_set_scaled[:, 1:], train_set_scaled[:, 0:1].ravel()
    X_test, y_test = test_set_scaled[:, 1:], test_set_scaled[:, 0:1].ravel()
    
    return X_train, y_train, X_test, y_test, scaler

def undo_scaling(y_pred, x_test, scaler_obj, lstm=False):  
    #reshape y_pred
    y_pred = y_pred.reshape(y_pred.shape[0], 1, 1)
    
    if not lstm:
        x_test = x_test.reshape(x_test.shape[0], 1, x_test.shape[1])
    
    #rebuild test set for inverse transform
    pred_test_set = []
    for index in range(0,len(y_pred)):
        pred_test_set.append(np.concatenate([y_pred[index],x_test[index]],axis=1))
        
    #reshape pred_test_set
    pred_test_set = np.array(pred_test_set)
    pred_test_set = pred_test_set.reshape(pred_test_set.shape[0], pred_test_set.shape[2])
    
    #inverse transform
    pred_test_set_inverted = scaler_obj.inverse_transform(pred_test_set)
    
    return pred_test_set_inverted

def predict_df(unscaled_predictions, original_df):
    result_list = []
    sales_dates = list(original_df[-13:].date)
    act_sales = list(original_df[-13:].sales)
    
    for index in range(0,len(unscaled_predictions)):
        result_dict = {}
        result_dict['pred_value'] = int(unscaled_predictions[index][0] + act_sales[index])
        result_dict['date'] = sales_dates[index+1]
        result_list.append(result_dict)
        
    df_result = pd.DataFrame(result_list)
    
    return df_result

def get_scores(unscaled_df, original_df, model_name):
    rmse = np.sqrt(mean_squared_error(original_df.sales[-12:], unscaled_df.pred_value[-12:]))
    mae = mean_absolute_error(original_df.sales[-12:], unscaled_df.pred_value[-12:])
    r2 = r2_score(original_df.sales[-12:], unscaled_df.pred_value[-12:])
    model_scores[model_name] = [rmse, mae, r2]

    print(f"RMSE: {rmse}")
    print(f"MAE: {mae}")
    print(f"R2 Score: {r2}")

def plot_results(results, original_df, model_name):

    fig, ax = plt.subplots(figsize=(15,5))
    sns.lineplot(original_df.date, original_df.sales, data=original_df, ax=ax, 
                 label='Original', color='mediumblue')
    sns.lineplot(results.date, results.pred_value, data=results, ax=ax, 
                 label='Predicted', color='Red')
    
    ax.set(xlabel = "Date",
           ylabel = "Sales",
           title = f"{model_name} Sales Forecasting Prediction")
    
    ax.legend()
    
    sns.despine()
    
    plt.savefig("_forecast.png")

def regressive_model(train_data, test_data,model, model_name):
    X_train, y_train, X_test,y_test, scaler_object = scale_data(train_data, test_data)
    # run models from Sklearn

    mod = model
    mod.fit(X_train,y_train)
    predictions = mod.predict(X_test)

    original_df = load_data('../forecasting sales/monthly_data.csv')
    unscaled = undo_scaling(predictions, X_test, scaler_object)
    unscaled_df = predict_df(unscaled, original_df)
    get_scores(unscaled_df,original_df,model_name)
    plot_results(unscaled_df,original_df,model_name)

def LSTM_model(tarin_data,test_data):
    X_train,y_train,X_test,y_test,scaler_object=scale_data(tarin_data,test_data)
    X_train = X_train.reshape(X_train.shape[0],1,X_train.shape[1])
    X_test = X_test.reshape(X_test.shape[0],1,X_test.shape[1])

    model=Sequential()
    model.add(LSTM(4, batch_input_shape=(1,X_train.shape[1],X_train.shape[2]),stateful=True))
    model.add(Dense(1))
    model.add(Dense(1))
    model.compile(loss='mean_squared_error',optimizer='adam')
    model.fit(X_train,y_train,epochs=200,batch_size=1,verbose=1,shuffle=False)
    predictions=model.predict(X_test,batch_size=1)
    original_df=load_original_df()
    unscaled = undo_scaling(predictions, X_test, scaler_object, lstm=True)
    unscaled_df = predict_df(unscaled, original_df)
    
    get_scores(unscaled_df, original_df, 'LSTM')
    
    plot_results(unscaled_df, original_df, 'LSTM')


def sarimax_model(data):
    sar = sm.tsa.statespace.SARIMAX(data.sales_diff, order =(12,0,0),
    seasonal_order=(0,1,0,12),trend='c').fit()

    start, end, dynamic = 40,100,7
    data['pred_value'] = sar.predict(start=start, end=end, dynamic=dynamic)
    original_df = load_data('../forecasting/monthly_data.csv')
    unscaled_df = predict_df(data,original_df)

    get_scores(unscaled_df,original_df,'ARIMA')
    plot_results(unscaled_df,original_df,'ARIMA')

def main():
    model_df = load_data('../forecasting sales/model_df.csv')
    train,test = splitdata(model_df)

    # Sklearn
    regressive_model(train, test, LinearRegression(), 'LinearRegression')
    regressive_model(train, test, RandomForestRegressor(n_estimators=100,
                                                        max_depth=20),
                     'RandomForest')
    regressive_model(train, test, XGBRegressor(n_estimators=100,
                                               learning_rate=0.2,
                                               objective='reg:squarederror'),
                     'XGBoost')
    # Keras
    LSTM_model(train, test)

    ts_data=load_data('../forecasting sales/arima.csv').set_index('date')
    ts_data.index = pd.to_datetime(ts_data.index)

    sarimax_model(ts_data)
main()