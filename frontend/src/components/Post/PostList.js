import React, { Component } from "react";
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


class PostList extends Component {
    state = {
        posts: [],
        postType: "",
        boardName: "",

        currentPage: 0,
        totalPages: 0,

        search: false,

        searchOption: "",
        keyword: ""

    }

    Category = () => {
        if (this.props.match.url === '/notice') {
            this.setState({
                postType: "notification",
                boardName: "공지사항"
            });
            return "notification";
        } else if (this.props.match.url === '/qna') {
            console.log("현재 /qna입니다.");
            this.setState({
                postType: "qna",
                boardName: "Q & A"
            }, () => {
                console.log(this.state.postType)
            });
            return "qna";
        }
    }

    componentDidMount() {
        // this.Category();
        if(!this.state.search){
            this.getPosts(this.state.currentPage);
        }

        
        
    }




    getPosts = (pageNumber) => {

        
        console.log(this.state.searchOption);
        console.log(this.state.search);
        console.log(this.state.keyword);
        console.log(this.state.boardName);
        console.log(this.state.postType);
        // const send_param = this.Category();
        let returnType = this.Category();
        let send_params;
        let send_body;
        let url;
        if(this.state.search == true){
            url = `/boards/search`;
            send_params = {
                boardType: returnType,
                page: pageNumber,
                
            };
            send_body ={
                option: this.state.searchOption,
                keyword: this.state.keyword
            };
        }
        
            url = `/boards`;
            send_params = {
                boardType: returnType,
                page: pageNumber
            };


        console.log(send_params.boardType)

        /* 권한 체크ajax */
        axios
            .get(
                axios // 실제 post List 가져오기
                    .get(url, {
                        params: send_params,
                        send_body,
                        withCredentials: true
                    })
                    .then(returnData => {
                        const totalPages = returnData.data.totalPages;

                        this.setState({ totalPages: totalPages })

                        if (returnData.data.numberOfElements > 0) {
                            const returnedPosts = returnData.data.content;
                            this.setState({
                                posts: returnedPosts
                            })

                        } else {
                            //게시글 못 찾은 경우
                            this.state.posts.push([
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

    whitePost = () => {
        axios
            .get(`/boards/authUser/${this.state.postType}`,
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
                        query: { postType: this.state.postType }
                    });

                    console.log("글 쓸 수 있는 권한 있음");


                }
            }).catch(err => {
                console.log(err);
                cogoToast.error("글 쓰기 권한이 없습니다.");
            });

    }

    ///////////////////////////////////////////////
    paginate = (pageNumber) => {
        this.setState({
            currentPage: pageNumber.selected
        })

        console.log(this.state.currentPage);
        console.log(pageNumber);

            this.getPosts(pageNumber.selected);
            this.forceUpdate();
        
        
        // window.location.reload();
    }


    /////////////////검색
    handleSearch = (e) =>{
       this.setState({
            keyword: e.target.value
       })
        
    }


    onSubmit = () =>{
        this.setState({
            search: true
        })
        this.getPosts(0);
    }

    handleSearchOption = (e) =>{
        this.setState({
            searchOption: e.target.value
        })

    }


    render() {
        const divStyle = {
            marginTop: "3%",
            minWidth: "70%",
            minHeight: "700px",
        };


        return (
            <div className="container" style={divStyle}>
                <div style={{ marginBottom: "1%" }}>
                    <h2>{this.state.boardName}</h2>
                </div>

                <div>
                    <h5>{this.state.totalPages} 중 {this.state.currentPage + 1} 페이지</h5>
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
                            {this.state.posts.map((post) => (
                                <PostRow PostRow={post} />
                            ))}
                        </tbody>
                    </Table>
                    <Button variant="secondary" onClick={this.whitePost}>게시글 작성</Button>
                </div>
                <div>
                    <PaginationPostAndComment totalPage={this.state.totalPages} paginate={this.paginate} />
                </div>
                <div>
                <form>
                    <select value={this.state.searchOption} onChange={this.handleSearchOption}>
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
                    
                    <input type="text" placeholder="" onChange={this.handleSearch}/>
                    <div>
                        <Button type="submit" onClick={this.onSubmit}>검색</Button>
                    </div>
                    </form>

                    
                </div>

            </div>

        );
    }
}

export default PostList;
