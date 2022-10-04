import React, { Component } from "react";
// import CKEditor from "ckeditor4-react-advanced";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import ckeditor, { CKEditor } from "@ckeditor/ckeditor5-react";
import { Button, Form} from "react-bootstrap";
import axios from "axios";
import $ from "jquery";
import {} from "jquery.cookie";
import cogoToast from 'cogo-toast';
// import multipart from 'connect-multiparty'

axios.defaults.withCredentials = true;

// const MultipartyMiddleware = multipart({uploadDir: './images'});

// app.post('/uploads', MultipartyMiddleware, (req,res) =>{
//     console.log(req.files);
// })


class upload extends Component {
 
    
    componentDidMount(){
        console.log("hi");
        this.uploadImage();
    }

    uploadImage = () =>{


        axios
        .post(`/test/image/upload`,
          {
            headers: {
            'Authorization': 'Bearer ' + $.cookie('accessToken')
            }
          })
        //정상 수행
        .then(response => {
          if (response.status == 201) {
            cogoToast.success(response.data.message);
            this.props.history.goBack();
          } else {
            cogoToast.error("글쓰기 실패");
          }
        })
        //에러
        .catch(err => {
          console.log(err);
        });

  };

    


}

export default upload;
