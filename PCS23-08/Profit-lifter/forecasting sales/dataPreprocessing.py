import pandas as pd

def DataLoad():
    return pd.read_csv(r'D:\AI ML DL\Profit Lifter\forecasting sales\New Data\train.csv')

    """Returns a dataframe where each row represents total sales for a given
    month. Columns include 'date' by month and 'sales'.
    """
def monthly_sales(data):
    monthly_data = data.copy()
    monthly_data.date = monthly_data.date.apply(lambda x: str(x)[:-3])
    monthly_data = monthly_data.groupby('date')['sales'].sum().reset_index()
    monthly_data.date = pd.to_datetime(monthly_data.date)
    return monthly_data
    """Returns the dataframe with a column for sales difference between each
    month. Results in a stationary time series dataframe. Prior EDA revealed
    that the monthly data was not stationary as it had a time-dependent mean.
    """
def get_diff(data):
    data['sales_diff'] = data.sales.diff()
    data = data.dropna()
    
    data.to_csv('stationary_df.csv')
    return data


    """Generates a csv file where each row represents a month and columns
    include sales, the dependent variable, and prior sales for each lag. Based
    on EDA, 12 lag features are generated. Data is used for regression modeling.
    Output df:
    month1  sales  lag1  lag2  lag3 ... lag11 lag12
    
    """
def generate_supervised(data):
    supervised_df = data.copy()
    
    #create column for each lag
    for i in range(1,13):
        col_name = 'lag_' + str(i)
        supervised_df[col_name] = supervised_df['sales_diff'].shift(i)
    
    #drop null values
    supervised_df = supervised_df.dropna().reset_index(drop=True)
    
    supervised_df.to_csv('model_df.csv', index=False)
    
    return supervised_df

def generate_arima_data(data):
    df_data = data.set_index('date').drop('sales', axis=1)
    df_data.dropna(axis=0)
    df_data.to_csv('arima.csv')
    return df_data   

def main():
    sales_data= DataLoad()
    monthly_df=monthly_sales(sales_data)
    stationary_df=get_diff(monthly_df)

    generate_supervised(stationary_df)
    generate_arima_data(stationary_df)
main()   





