import React, { useEffect, useState } from 'react';
import $ from "jquery";
import { } from "jquery.cookie";
import cogoToast from 'cogo-toast';
import { } from "../../css/commentForm.css";
import axios from 'axios';

function CommentReply(props) {

  const [isReplyOrUpdate, setIsReplyOrUpdate] = useState(props.isReplyOrUpdate);//리플은 true, 수정은 false

  const [boardId, setBoardId] = useState("");

  const [parentCommentId, setParentComentId] = useState("");


  const [commentContent, setCommentContent] = useState("");

  const [content, setContent] = useState(props.content);

  useEffect(() => {

    console.log(props.boardId);
    console.log(props.parentCommentId)
    setBoardId(props.boardId);
    setParentComentId(props.parentCommentId);
    setIsReplyOrUpdate(props.isReplyOrUpdate);
    setContent(props.content);

  }, [])



  const createComment = () => {

    if (!$.cookie('accessToken')) {
      cogoToast.error("로그인 후 가능합니다!!");
    }

    let send_param;

    // const CommentContent = comment.value;

    if (commentContent === undefined || commentContent === "") {
      cogoToast.warn("댓글 내용을 입력 해주세요.");
      commentContent.focus();
    }
    send_param = {
      boardId: boardId,
      content: commentContent,
      parentId: parentCommentId
    };


    axios
      .post('/comments', send_param,
        {
          headers: {
            'Authorization': 'Bearer ' + $.cookie('accessToken')
          }
        })
      .then(response => {
        if (response.status === 201) {
          cogoToast.success("대댓글 작성 완료");
        }
      })
      .catch(err => {
        cogoToast.error("대댓글 작성 실패")
      })

      window.location.reload();

  };

  const updateComment = () => {
    if (window.confirm('정말 수정하시겠습니까?')) {
        const send_param = {
            content: content
        }
        axios
            .put(`/comments/${props.commentId}`, send_param,
                {
                    headers: {
                        'Authorization': 'Bearer ' + $.cookie('accessToken')
                    }
                })
            .then(response => {
                if (response.status === 200) {
                    this.setState({
                        //modified_date: response.data.comment.modified_date
                    })
                    cogoToast.success("댓글이 수정되었습니다.");
                }
                else {
                    cogoToast.error("댓글 수정에 실패했습니다.");
                }
            })
            .catch(err => {
                //console.log(err);
                cogoToast.error("댓글 수정에 실패했습니다.");
            })
    }

    window.location.reload();
}



  const textChange = (e) => {
    setCommentContent(e.target.value);
  }

  const onEnterPress = (e) => {
    if (e.keyCode === 13 && e.shiftKey === false) {
      e.preventDefault();
      createComment();
    }
  };

  const handleChange = (e) => {
    setContent(e.target.value);
}


  return (
    <>
      {isReplyOrUpdate ?
        <div className="comment_div">
          <div className='comment_write'>
            <textarea
              rows='2'
              // ref={ref => (comment = ref)}
              maxLength='100'
              placehoder='100자 이내의 댓글을 입력해주세요.'
              onChange={textChange}
              onKeyDown={onEnterPress}
            >
            </textarea>
            <input type='button' id='comment_submit_button' value='등록' onClick={createComment} />
          </div>
        </div>
    
      :
      <div className="comment_div">
        <div className='comment_write'>
          <textarea
            rows='2'
            maxLength='100'
            value={content}
            onChange={handleChange}
            onKeyDown={onEnterPress}
          >
          </textarea>
          <input type='button' id='comment_submit_button' value='수정' onClick={updateComment} />
        </div>
      </div>
}
    </>
  );
}

export default CommentReply;