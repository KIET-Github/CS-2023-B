from cgitb import text
import pickle
model=pickle.load(open("model.pkl",'rb'))
def predict(string):
    return model.predict(string)