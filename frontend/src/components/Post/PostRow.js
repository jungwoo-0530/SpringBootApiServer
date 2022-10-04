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
import { render } from "@testing-library/react";
import { NavLink } from "react-router-dom";


axios.defaults.withCredentials = true;
const headers = { withCredentials: true };

const bucket = 'https://s3.ap-northeast-2.amazonaws.com/cidsprofileimg/';


const PostRow = ({ PostRow }) => {

    const [Post, setPost] = useState(null);

    //useEffect에서 commentRow들어간 곳이 deps임.
    //여기에 지정한 값이 변경 될때, useEffect()가 호출됨.
    //여기서는 페이징으로 새로운 댓글이 보일때마다 setContent호출해야하므로.
    useEffect(() => {

        // setContent(commentRow.content);
        setPost(PostRow);

    }, [PostRow])




    const createDateFormat = (date) => {
        return Moment(date).format('YYYY-MM-DD HH:mm:ss')
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
            <tr>
                <td style={{ textAlign: "center" }}>
                    {PostRow.id}
                </td>
                <td>
                    <NavLink style={{ color: "black" }}
                        to={{
                            pathname: `/posts/${PostRow.id}`, query: {}
                        }}
                    >
                        {PostRow.title}
                    </NavLink>
                </td>
                <td style={{ textAlign: "center" }}>
                    {PostRow.author}
                </td>
                <td style={{ textAlign: "center" }}>
                    {createDateFormat(PostRow.createDate)}
                </td>
                <td style={{ textAlign: "center" }}>
                    {PostRow.hit}
                </td>
            </tr>

        </>
    );


};


export default PostRow;

