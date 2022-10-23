import sys
import pymongo
import datetime
from pandas import DataFrame
import ssl
import crawling
import db_config
from bs4 import BeautifulSoup
from urllib import parse
import requests
import random
import url_filter
import re
from konlpy.tag import Okt
import pandas as pd
from keras_preprocessing.text import Tokenizer
from keras_preprocessing.sequence import pad_sequences


#아래 것들 때문에 java에서 ProcessBuilder가 안됨.
# import train as tr
# import check
# import result
# from tensorflow.python.keras.models import load_model
# import tensorflow

import tensorflow as tf

tf.keras.models.load_model('my_model')

#한글
def hi(arg1):
    with open("/Users/jungwoo/Desktop/dev/SpringBootApiServer/searchModule/file.txt", 'w') as target:
        target.write(arg1)
    return arg1


arg = sys.argv[1]

hi(arg)
