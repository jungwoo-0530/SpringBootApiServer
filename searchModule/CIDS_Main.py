from bson import ObjectId

import crawling
import db_config
import train as tr
import check
import result
from tensorflow.python.keras.models import load_model
import sys
import pymongo
import datetime
from pandas import DataFrame
import ssl
import os


loaded_model = load_model('my_model')
keyword = {}

db_info = db_config.db_info()

connection = pymongo.MongoClient(db_config.db_info()['db_address'])

global flag

global db
db = connection['cids_db']
global keywords_col
keywords_col = db['keywords']
global urls_col
urls_col = db['urls']
global words_col
words_col = db['words']
global sites_col
sites_col = db['sites']
global results_col
results_col = db['results']
global train_col
train_col = db['train_words']
global count_col
count_col = db['countdomains']

ssl._create_default_https_context = ssl._create_unverified_context


def main(keyword_id):

    keyword_doc = keywords_col.find_one({'_id': ObjectId(keyword_id)})
    target_keyword = str(keyword_doc['keyword'])

    # train_data 콜렉션 전처리
    train_data = DataFrame(list(train_col.find({})))
    train_data = train_data.drop(['_id'], axis=1)
    tr.preprocess(train_data)
    flag = 0
    current_url = ""
    for i in range(2):
        if flag == 0 or flag == 1:
            result_url_list, current_url = crawling.craurl(target_keyword, flag, current_url)
            for url in result_url_list:
                pre_check_result = check.site_pre_check(url)

                if pre_check_result['result'] is True:
                    result.result_insert(keyword_id, url, pre_check_result['label'])
                    if pre_check_result['label'] == 0:
                        flag += 2
                elif pre_check_result['result'] is False:

                    # if check.video_check(url) is True:
                    #     result.result_insert(_keyword_id, url, 0)
                    #     flag += 2
                    # else:
                    #
                    #     if crawling.banner_crawling_function(url):
                    #         if result.result_insert(_keyword_id, url) is True:
                    #             flag += 2
                    #
                    #             continue
                    if crawling.banner_crawling_function(url):
                        if result.result_insert(keyword_id, url) is True:
                            flag += 2

                            continue
        flag += 1

    update_keywords = {"$set": {'status': 0, 'updateDate': datetime.datetime.utcnow()}}
    keywords_col.update_one(keyword_doc, update_keywords)

    print(True)

    return keyword_id



#
# if __name__ == "__main__":
#     # execute only if run as a script
#     main()
