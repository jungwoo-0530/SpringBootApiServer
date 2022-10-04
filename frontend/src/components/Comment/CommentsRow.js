import React, { Component, useEffect, useState } from "react";
import axios from "axios";
import Moment from 'moment';
//https://react.semantic-ui.com/views/comment/#types-comment
import { Comment } from 'semantic-ui-react';
import 'semantic-ui-css/semantic.min.css';
import UserProfileModal from "../../User/UserProfileModal";
import { } from "../../css/NewCommentForm.css";
import $ from 'jquery';
import { } from 'jquery.cookie';
import { } from '../../css/userProfile.css';
import { } from '../../css/pagination.css';
import cogoToast from 'cogo-toast';
import CommentReply from './CommentReply';
import { render } from "@testing-library/react";

axios.defaults.withCredentials = true;
const headers = { withCredentials: true };

const bucket = 'https://s3.ap-northeast-2.amazonaws.com/cidsprofileimg/';


const CommentsRow = ({ commentRow, comments, boardId }) => {


    const [editForm, setEditForm] = useState(false);
    const [content, setContent] = useState("");
    const [replyVisible, setReplyVisible] = useState(false);
    const [editVisible, setEditVisible] = useState(false);


    //useEffect에서 commentRow들어간 곳이 deps임.
    //여기에 지정한 값이 변경 될때, useEffect()가 호출됨.
    //여기서는 페이징으로 새로운 댓글이 보일때마다 setContent호출해야하므로.
    useEffect(() => {

        setContent(commentRow.content);

    }, [commentRow])


    const deleteComment = (commentId) => {
        if (window.confirm("정말 삭제하시겠습니까?")) {
            axios
                .delete(`/comments/${commentRow.id}`,
                    {
                        headers: {
                            'Authorization': 'Bearer ' + $.cookie('accessToken'),
                            'Content-Type': 'application/json'
                        }
                    })
                //정상 수행
                .then(response => {
                    if (response.status === 200) {
                        cogoToast.success("댓글이 삭제 되었습니다.");

                        // this.props.deleteCheck(this.props.comment.id);
                    }
                    else {
                        cogoToast.error("댓글삭제에 실패했습니다.");
                    }
                })
                //에러
                .catch(err => {
                    console.log(err);
                    cogoToast.error("댓글 삭제 실패");
                });
        }
        else {
            //
        }

        window.location.reload();

    }


    const createDateFormat = (date) => {
        return Moment(date).format('YYYY-MM-DD HH:mm:ss')
    }

    const paddingLeftValue = (deep) => {

        if (deep === 2) {
            return "30px";
        } else if (deep === 3) {
            return "60px";
        } else if (deep > 3) {
            return "90px";
        } else {
            return "0px";
        }
    }


    const handleUserProfileShow = (deep) => {

        if (deep === 2) {
            return "30px";
        } else if (deep === 3) {
            return "60px";
        } else if (deep > 3) {
            return "90px";
        } else {
            return "0px";
        }
    }

    const imgStyle = {
        borderRadius: "50%",
        overflow: "hidden",
        marginLeft: "15px",
        cursor: "pointer",
        marginRight: "15px",
        border: "3px solid #fff",
        boxShadow: "0 0 16px rgb(221,221,221)"
    }


    return (
        <>

            <Comment style={{ marginBottom: 20, paddingLeft: paddingLeftValue(commentRow.deep) }}>
                <Comment.Avatar
                    onClick={handleUserProfileShow}
                    className="userProfile"
                    // src={bucket + this.state.user.img_path} 
                    style={imgStyle} />

                <Comment.Content>
                    <Comment.Author onClick={handleUserProfileShow} as='a'>{commentRow.author}</Comment.Author>

                    <>
                        <Comment.Metadata>
                            <div>{createDateFormat(commentRow.createDate)}</div>
                        </Comment.Metadata>
                        {commentRow.available ?
                            <Comment.Text>{commentRow.content}</Comment.Text>
                            :
                            <Comment.Text>#삭제된 댓글입니다#</Comment.Text>
                        }
                        {commentRow.editable
                            ?
                            <>
                                {commentRow.available
                                    ?
                                    <Comment.Actions style={{ paddingLeft: "15px" }}>
                                        <Comment.Action onClick={() => { setEditVisible(!editVisible) }}> 수정 </Comment.Action>
                                        {editVisible && <CommentReply parentCommentId={commentRow.id} boardId={boardId} isReplyOrUpdate={false} content={content} commentId={commentRow.id} />}
                                        <Comment.Action onClick={deleteComment}> 삭제 </Comment.Action>
                                        <Comment.Action onClick={() => { setReplyVisible(!replyVisible) }}>{replyVisible ? "리플끄기" : "리플"}</Comment.Action>
                                        {replyVisible && <CommentReply parentCommentId={commentRow.id} boardId={boardId} isReplyOrUpdate={true} />}

                                    </Comment.Actions>
                                    :
                                    <Comment.Actions style={{ paddingLeft: "15px" }}>
                                        <Comment.Action onClick={() => { setEditVisible(!editVisible) }}> 수정 </Comment.Action>
                                        {editVisible && <CommentReply parentCommentId={commentRow.id} boardId={boardId} isReplyOrUpdate={false} content={content} commentId={commentRow.id} />}
                                        <Comment.Action onClick={deleteComment}> 삭제 </Comment.Action>
                                        <Comment.Action onClick={() => { setReplyVisible(!replyVisible) }}>{replyVisible ? "리플끄기" : "리플"}</Comment.Action>
                                        {replyVisible && <CommentReply parentCommentId={commentRow.id} boardId={boardId} isReplyOrUpdate={true} />}

                                    </Comment.Actions>
                                }



                            </>
                            :
                            <>
                            <Comment.Actions style={{ paddingLeft: "15px" }}>
                                        <Comment.Action onClick={() => { setReplyVisible(!replyVisible) }}>{replyVisible ? "리플끄기" : "리플"}</Comment.Action>
                                        {replyVisible && <CommentReply parentCommentId={commentRow.id} boardId={boardId} isReplyOrUpdate={true} />}
                                    </Comment.Actions>
                            </>
                        }
                    </>

                </Comment.Content>
            </Comment>

        </>
    );


};


export default CommentsRow;

