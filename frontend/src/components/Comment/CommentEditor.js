import React, { Component } from "react";
import axios from "axios";
import $ from "jquery";
import {} from "jquery.cookie";
import cogoToast from 'cogo-toast';
import {} from "../../css/commentForm.css";

axios.defaults.withCredentials = true;


class CommentEditor extends Component {
  componentDidMount() {
  }

  createComment = () => {

    // console.log(props)

    if(!$.cookie('accessToken')) {
      cogoToast.error("로그인 후 가능합니다!!");
      // window.location.href = "/";   
    }
    let send_param;

    const CommentContent = this.comment.value;

    if (CommentContent === undefined || CommentContent === "") {
      cogoToast.warn("댓글 내용을 입력 해주세요.");
      CommentContent.focus();
    }
      send_param = {
        boardId: this.props.boardId,
        content: CommentContent
      };

      console.log(this.props.boardId);

    axios
      .post(`/comments`,send_param,
      {
        headers: {
          'Authorization': 'Bearer ' + $.cookie('accessToken')
        }
      })
      .then(response => {
        if(response.status == 201){
          cogoToast.success("댓글 작성 완료");
          //새로고침.
        }
      })
      .catch(err => {
        cogoToast.error("댓글 작성 실패");
      })

      window.location.reload();
  };

  onEnterPress = (e) => {
    if(e.keyCode === 13 && e.shiftKey === false) {
      e.preventDefault();
      this.createComment();
    }
  }


  render() {
    

    return (
      <div className="comment_div">
        <h4>Comment</h4>
          <div className='comment_write'>
            <textarea 
              rows='3'
              ref={ref => (this.comment = ref)}
              maxLength='100'
              placehoder='150자 이내의 댓글을 입력해주세요.'
              onKeyDown={this.onEnterPress}
            >
            </textarea>
            <input type='button' id='comment_submit_button' value='등록' onClick={this.createComment} /> 
          </div>
      </div>
    );
  }
}



export default CommentEditor;
