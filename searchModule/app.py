from flask import Flask, request
from flask_restx import Api, Resource
import CIDS_Main as Cm

from threading import Thread

#Flask 객체 선언, 파라미터로 어플리케이션 패키지의 이름을 넣어줌.
app = Flask(__name__)

# api = Api(app, version='1.0', title='flask api 문서', decorators='문서', doc="/api-docs")


@app.route('/api/detect', methods=['GET'])
def detect_service():

    args = request.args

    return Cm.main(args.get("keyword"))


@app.route('/test/flask', methods=['GET'])
def test():
    return "hi"


if __name__ == "__main__":
    # app.run(debug=True, host='0.0.0.0', port=5000)
    app.run(host="0.0.0.0", port=9900, debug=True)


# werkzeug                  2.2.2              pyhd8ed1ab_0    conda-forge
# flask                     2.2.2              pyhd8ed1ab_0    conda-forge
# flask-restx               0.5.1              pyhd8ed1ab_0    conda-forge
