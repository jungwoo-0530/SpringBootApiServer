import React, { Component } from "react";
import CKEditor from "ckeditor4-react-advanced";
import { Button, Form} from "react-bootstrap";
import axios from "axios";
import $ from "jquery";
import {} from "jquery.cookie";
import cogoToast from 'cogo-toast';
axios.defaults.withCredentials = true;


class PostUpdateForm extends Component {
  constructor(props){
    super(props);
    this.state= {
      content:"",
      title: ""
    }
  }
  

    componentDidMount(){
        this.setState({
            content: this.props.location.data.content,
            title: this.props.location.data.title
        });
        console.log(this.props);
        console.log(this.state);
    };

  updatePost = () => {

    if(!$.cookie('accessToken')) {
      cogoToast.error("로그인 후 가능합니다!!");
      window.location.href = "/";   
    }


    if (this.state.title === undefined || this.state.title === "") {
      cogoToast.warn("글 제목을 입력 해주세요.");
      this.state.title.focus();
      return;
    } else if (this.state.content === undefined || this.state.content === "") {
      cogoToast.warn("글 내용을 입력 해주세요.");
      return;
    }
   
    const send_param = {
        title: this.state.title,
        content: this.state.content
    }
    console.log(this.props);
      axios
        .put(`/boards/${this.props.location.data.id}`, send_param,
          {
            headers: {
              'Authorization': 'Bearer ' + $.cookie('accessToken'),
            }
          })
        //정상 수행
        .then(response => {
          if (response.status == 200) {
            cogoToast.success(response.data.message);
            this.props.history.goBack();
          } else {
            cogoToast.error("글 수정 실패");
          }
        })
        //에러
        .catch(err => {
          console.log(err);
        });

    
  };

  onEditorChange = (e) => {
    this.setState({
      content: e.editor.getData()
    });
  };

  onChangeTitle = (e) => {
    this.setState({
      title: e.target.value
    })
  }

  render() {
    const divStyle = {
      marginTop:"3%",
      minWidth:"70%", 
      minHeight:"700px"
    };
    const titleStyle = {
      marginBottom: 5
    };
    const buttonStyle = {
      marginTop: 5
    };

    return (
      <div className="container" style={divStyle}>
        <h2>글 수정</h2>
        <Form.Control
          type="text"
          style={titleStyle}
          placeholder="글 제목"
          value = {this.state.title}
          onChange={this.onChangeTitle}
        />
        <CKEditor
          placeholder = "내용을 입력하세요."
          data={this.props.location.data.content}
          onChange={this.onEditorChange}
        />
        <Button style={buttonStyle} onClick={this.updatePost} block>
          수정
        </Button>
      </div>
    );
  }
}

export default PostUpdateForm;
