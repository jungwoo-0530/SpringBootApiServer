import React, { Component, useState, useEffect } from "react";
import { Table, Button } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import axios from "axios";
import Moment from 'moment';
import ReactPaginate from 'react-paginate';
import $ from "jquery";
import { } from "jquery.cookie";
import cogoToast from "cogo-toast";
import { } from '../../css/pagination.css';
import qs from 'qs';
import { toast } from "react-toastify";
import { AlternateEmail } from "@material-ui/icons";

import PostRow from "./PostRow";
import PaginationPostAndComment from "../Pagination/PaginationPostAndComment";

axios.defaults.withCredentials = true;

//post 하나당 출력

//////////////////////////////////////////////

///////////////////////////////////////////


const SearchPosts = ({ ppostType, psearchOption, pkeyword, boardName }) => {

    const [keyword, setKeyword] = useState(pkeyword);
    const [searchOption, setSearchOption] = useState(psearchOption);

    const [posts, setPosts] = useState([]);
    const [currentPage, setCurrentPage] = useState("");
    const [totalPages, setTotalPages] = useState(0);


    const getPosts = (pageNumber) => {


        url = `/boards/search`;
        send_params = {
            boardType: ppostType,
            page: pageNumber,

        };
        send_body = {
            option: psearchOption,
            keyword: keyword
        };



        /* 권한 체크ajax */
        axios
            .get(
                axios // 실제 post List 가져오기
                    .get(url, {
                        params: send_params,
                        send_body
                    })
                    .then(returnData => {
                        const totalPages = returnData.data.totalPages;

                        setTotalPages(totalPages)
                        if (returnData.data.numberOfElements > 0) {
                            const returnedPosts = returnData.data.content;
                            setPosts(returnedPosts);


                        } else {
                            //게시글 못 찾은 경우
                            posts.push([
                                <tr>
                                    <td colSpan="5" style={{ textAlign: "center" }}>게시글이 존재하지 않습니다.</td>
                                </tr>
                            ])

                        }
                    })
                    .catch(err => {
                        console.log(err);
                    })
            );


    };

    const whitePost = () => {
        axios
            .get(`/boards/authUser/${ppostType}`,
                {
                    headers: {
                        'Authorization': 'Bearer ' + $.cookie('accessToken'),
                        'Content-Type': 'application/json'
                    }
                })
            .then(response => {
                console.log(response.status);
                //   console.log(this.state.postType);
                if (response.status === 200) {
                    this.props.history.push({
                        pathname: "/posts/write",
                        query: { postType: ppostType }
                    });

                    console.log("글 쓸 수 있는 권한 있음");


                }
            }).catch(err => {
                console.log(err);
                cogoToast.error("글 쓰기 권한이 없습니다.");
            });

    }

    ///////////////////////////////////////////////
    const paginate = (pageNumber) => {

        setCurrentPage(pageNumber.selected);


        getPosts(pageNumber.selected);
        // this.forceUpdate();


        // window.location.reload();
    }


    /////////////////검색
    const handleSearch = (e) => {
        
        setKeyword(e.target.value);

    }


    const onSubmit = () => {
    
        getPosts(0);
    }

    const handleSearchOption = (e) => {

        setSearchOption(e.target.value);
    }


        const divStyle = {
            marginTop: "3%",
            minWidth: "70%",
            minHeight: "700px",
        };


        return (
            <div className="container" style={divStyle}>
                <div style={{ marginBottom: "1%" }}>
                    <h2>{boardName}</h2>
                </div>

                <div>
                    <h5>{totalPages} 중 {currentPage + 1} 페이지</h5>
                </div>
                <div>
                    <Table striped bordered hover size="sm">
                        <thead>
                            <tr style={{ textAlign: "center" }}>
                                <th style={{ width: "10%" }}>#</th>
                                <th style={{ width: "40%" }}>제목</th>
                                <th>작성자</th>
                                <th>작성일</th>
                                <th>조회</th>
                            </tr>
                        </thead>
                        <tbody>
                            {posts.map((post) => (
                                <PostRow PostRow={post} />
                            ))}
                        </tbody>
                    </Table>
                    <Button variant="secondary" onClick={whitePost}>게시글 작성</Button>
                </div>
                <div>
                    <PaginationPostAndComment totalPage={totalPages} paginate={paginate} />
                </div>
                <div>
                    <form>
                        <select value={searchOption} onChange={handleSearchOption}>
                            <option value="title">
                                제목
                            </option>
                            <option value="titleAndContent">
                                제목 + 내용
                            </option>
                            <option value="author">
                                글쓴이
                            </option>
                        </select>

                        <input type="text" placeholder="" onChange={handleSearch} />
                        <div>
                            <Button type="submit" onClick={onSubmit}>검색</Button>
                        </div>
                    </form>


                </div>

            </div>

        );
}

export default SearchPosts;
