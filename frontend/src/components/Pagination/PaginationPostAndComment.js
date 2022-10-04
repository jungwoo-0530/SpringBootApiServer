import { Pagination } from 'react-bootstrap';
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
import CommentReply from '../Comment/CommentReply';
import { render } from "@testing-library/react";
import ReactPaginate from 'react-paginate';


// const PaginationPostAndComment = ({commentsPerPage, totalComments, paginate}) =>{
const PaginationPostAndComment = ({ totalPage , paginate }) => {

    const [totalPages, setTotalPages] = useState('');
    const pageNumber = [];

    // for(let i = 1; i <= Math.ceil(totalComments / commentsPerPage); i++){
    //     pageNumber.push(i);
    // }

    useEffect(() => {

        // setTotalPages(totalPage);

        console.log(totalPage);

        for (let i = 1; i <= totalPage; i++) {
            console.log(i)
            pageNumber.push(i);
        }

        // console.log(totalPages);
    }, [])

    const changePage  = () =>{

    }

    return (
        <div>
            <ul className="pagination">
                {pageNumber.map((pageNum) => (
                    <li>{pageNum}</li>
                ))}
            </ul>

            <ReactPaginate
                previousLabel={'이전'}
                nextLabel={'다음'}
                breakLabel={'...'}//페이지수가 많을 경우 건너 뛸 수 있는 버튼
                breakClassName={'break-me'}
                pageCount={(totalPage)}//총 게시글의 수
                marginPagesDisplayed={0}
                pageRangeDisplayed={totalPage}//한페이지에 표시할 게시글의 수
                onPageChange={paginate}//페이지버튼을 눌럿을때 일어나는 이벤트 이를 이용해 페이지 증감
                containerClassName={'pagination'}//css적용할 때 사용
                subContainerClassName={'pages pagination'}
                activeClassName={"pagination"}
                previousClassName={"pagination"}
                nextClassName={"pagination"}
                previousLinkClassName={"pagination"}
                nextLinkClassName={"pagination"}
            />


        </div>
    );

};


export default PaginationPostAndComment;