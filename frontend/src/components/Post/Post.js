import React, { Component } from "react";
import { ButtonGroup, Button, Jumbotron, Image } from "react-bootstrap";
import CommentEditor from "../Comment/CommentEditor";
import UserProfileModal from "../../User/UserProfileModal";
import { NavLink } from "react-router-dom";
import axios from "axios";
import Moment from "moment";
import $ from 'jquery';
import { } from '../../css/userProfile.css';
import cogoToast from 'cogo-toast';
import PostUpdateForm from "./PostUpdateForm"
import InvalidPage from "../../404Page"

/////////////////////////////
import PaginationPostAndComment from "../Pagination/PaginationPostAndComment";
import CommentsRow from "../Comment/CommentsRow";
import { Comment } from 'semantic-ui-react';
///////////////////////////////

import "../../css/NavigaionGuardModalStyle.scss"

axios.defaults.withCredentials = true;


const bucket = 'https://s3.ap-northeast-2.amazonaws.com/cidsprofileimg/';

class Post extends Component {
  state = {
    post: [],
    postType: "",
    boardName: "",
    user: '',
    userProfileModalShow: false,
    available: 1,
    editable: false,

    comments: [],
    currentPage: 0,
    totalPages: 0
  };

 


  componentWillMount() {
    if (this.props.match.params.id !== undefined) {
      this.getShow();
      this.getComments(0);

    } else {
      window.location.href = "/";
    }

    console.log(this.props);


  }



  DateFormat = (date) => {
    return Moment(date).format('YYYY-MM-DD hh:mm:ss');
  }

  //게시글 삭제
  deletePost = () => {


    if (window.confirm("정말 삭제하시겠습니까?")) {
      axios
        .delete(`/boards/${this.props.match.params.id}`,
          {
            headers: {
              'Authorization': 'Bearer ' + $.cookie('accessToken'),
              'Content-Type': 'application/json'
            }
          })
        //정상 수행
        .then(response => {
          cogoToast.success("게시글이 삭제 되었습니다.");
          this.goBack();
        })
        //에러
        .catch(err => {
          console.log(err);
          cogoToast.error("글 삭제 실패");
        });
    }

  };


  //메뉴 분기
  Category = (post_type) => {

    if (post_type === 'notification') {
      this.setState({
        postType: "notification",
        boardName: "공지사항"
      });
    }
    else if (post_type === 'qna') {

      this.setState({
        postType: "qna",
        boardName: "Q & A"
      });
    }

  }
  //유저 프로필 show 컨트롤
  handleUserProfileShow = () => {

    this.setState({
      userProfileModalShow: !this.state.userProfileModalShow
    });
  }

  //뒤로가기
  goBack = () => {
    this.props.history.goBack();
  }


  //post render
  getShow = () => {
    axios
      .get(`/boards/${this.props.match.params.id}`,
        {
          headers: {
            'Authorization': 'Bearer ' + $.cookie('accessToken'),
            'Content-Type': 'application/json'
          }
        })
      //정상 수행
      .then(response => {

        if (response.status === 200) {
          console.log("성공");
          cogoToast.success(this.props.match.params.id);
          this.setState({ editable: response.data.data.editable })
          console.log(this.state.editable);
          this.Category(response.data.data.type);
          const post = (
            <div>
              <h1>{response.data.data.title}</h1>
              <div className="row">
                <div onClick={this.handleUserProfileShow}  >
                  {/* <Image className="userProfile" src={bucket + response.data.writer.img_path} width="25" height="25" style={{ border:"3px solid #fff", boxShadow: "0 0 16px rgb(221,221,221",marginLeft:"15px", cursor:"pointer"}} roundedCircle/> */}
                  <Image className="userProfile" src={bucket + response.data.author} width="25" height="25" style={{ border: "3px solid #fff", boxShadow: "0 0 16px rgb(221,221,221", marginLeft: "15px", cursor: "pointer" }} roundedCircle />
                </div>
                <h6 style={{ marginTop: "5px", marginLeft: "10px", fontWeight: "600" }}>{response.data.data.author}( {response.data.data.email} )</h6>
                <h6 style={{ display: "inline-block", marginLeft: ".5em", color: "rgba(0,0,0,.4)", fontSize: ".875em", marginTop: "6px" }}>{this.DateFormat(response.data.data.updateTime)}</h6>
              </div>
              <hr />
              <div style={{ minHeight: "300px" }}>
                <p dangerouslySetInnerHTML={{ //ckeditor의 형식이 html이기 때문에 html형식으로 넣어줘야함
                  __html: response.data.data.content
                }}>
                </p>
              </div>
              {/* {response.data.author === $.cookie('login_id') || $.cookie('login_id') === 'admin'?  // 글을 쓴 유저거나 admin일때만 btn 활성화 */}
              {/* this.editable === true? */}
              <ButtonGroup aria-label="Basic example">
                <Button variant="secondary" onClick={this.goBack}>뒤로가기</Button>
                <NavLink
                  to={{
                    pathname: `/posts/update`,
                    data: {
                      id: this.props.match.params.id,
                      title: response.data.data.title,
                      content: response.data.data.content
                    }
                  }}
                >
                  <Button disabled={this.state.editable === true ? "" : "false"} variant="secondary">
                    글 수정
                  </Button>
                </NavLink>
                <Button disabled={this.state.editable === true ? "" : "false"} variant="secondary" onClick={this.deletePost}>글 삭제</Button>
              </ButtonGroup>

              <hr />

            </div>
          );
          this.setState({
            post: post,
            user: response.data.data.author,
            available: response.data.data.available
          });
        }
        else {
          console.log("error");

        }

      })
      //에러
      .catch(err => {
        console.log("error 발생");
        console.log(err);
        this.setState({
          available: 0
        })

      });
  };

  //////////////////////////////////////////////////////////////////////
  
  paginate = (pageNumber) => {
    this.setState({
      currentPage: pageNumber.selected
    })

    console.log(this.state.currentPage);
    console.log(pageNumber);

    this.getComments(pageNumber.selected);
    this.forceUpdate();
    // window.location.reload();
  }


  getComments = (pageNum) => {
    const send_param = { boardId: this.props.match.params.id };
    console.log(this.props.match.params.id);
    axios
      .get(`/comments`, {
        headers: {
          'Authorization': 'Bearer ' + $.cookie('accessToken'), 'Content-Type': 'application/json'
        },
        params: {
          boardId: send_param.boardId,
          page: pageNum
        }
      })
      .then(response => {
        
        console.log(response.data.totalPages);
        

        const totalPages = response.data.totalPages;
        // const itemsCountPerPage = response.data.size;
        // const totalItemsCount = response.data.totalElements;
        // const editable = response.data.content.editable;
        
        this.setState({totalPages: response.data.totalPages});

        console.log(this.state.totalPages);

        
        if (response.data.numberOfElements > 0) {
          const returnedComments = response.data.content;
          console.log("here");
          console.log(returnedComments.length);
          // console.log(response.data.content)
          this.setState({
            comments: returnedComments
          })
          
        } else {
          this.state.comments.push([
            <tr>
              <td colSpan="5" style={{ textAlign: "center" }}>댓글이 존재하지 않습니다.</td>
            </tr>
          ])
          
        }

      })
      .catch(err => {
        console.log(err);
      });
  };

  //////////////////////////////////////////////////////////////////////

  render() {
    
    const divStyle = {
      marginTop: "3%",
      minWidth: "70%",
      minHeight: "700px",
    };

    if (this.state.available) {
      return (
        <div className="container" style={{ marginTop: "3%" }}>
          <Jumbotron style={{ backgroundColor: "#f0f2f4" }}>
            <h6 style={{ color: "green" }} >[{this.state.boardName}]</h6>
            {this.state.post}
          </Jumbotron>

           
          <CommentEditor boardId={this.props.match.params.id} />

          <div style={divStyle}>
           <Comment.Group size='large'>
            {this.state.comments.map((comment) =>(
              <CommentsRow commentRow = {comment} comments={this.state.comments} boardId={this.props.match.params.id}/>
            ))}
          </Comment.Group>
          </div>
          
          {/* <PaginationPostAndComment commentsPerPage={this.state.commentsPerPage} totalComments={this.state.comments.length} paginate={this.paginate}/> */}


          <PaginationPostAndComment totalPage = {this.state.totalPages} paginate={this.paginate} />


          {/* {this.state.user?
          <UserProfileModal user={this.state.user} show ={this.state.userProfileModalShow} showHandler={this.handleUserProfileShow}/>
          :<></>} */}

          
        </div>

      )
    }
    else {
      return <InvalidPage />
    }
  }
}

export default Post;
