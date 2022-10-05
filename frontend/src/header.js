import React, {Component} from 'react';
import { Navbar, Nav } from "react-bootstrap";
import { NavLink } from "react-router-dom";
import LoginForm from "./User/LoginForm";
import axios from "axios";
import $ from "jquery";
import {} from "jquery.cookie";
import cogoToast from 'cogo-toast';
axios.defaults.withCredentials = true;

class Header extends Component{
  
  constructor(props){
    super(props);
    this.state = {
      loginState: false,
      loginFormShow: false,
    }
  }

  componentDidMount(){
    if ($.cookie("accessToken")){
      this.setState({
        loginState: true
      
      });
    } else{
      this.setState({
        loginState: false      
      });
    }
  }

  logout = () => {
    // axios
    //   .post("/logout", {
    //         headers: {
    //           'Authorization': 'Bearer ' + $.cookie('accessToken'),
    //         }
    //       })
    //   .then( (response) => {
    //     if(response.data.message){
    //       $.removeCookie("accessToken");
    //       // $.removeCookie("tokenType");
    //       cogoToast.success("로그아웃에 성공 했습니다.");
    //       setTimeout(function() {
    //         window.location.href="/";
    //       }, 1000);
    //     }
    //   });

    $.removeCookie("accessToken");
    cogoToast.success("로그아웃에 성공했습니다.");
    window.location.href="/";
  };

  handleLoginFormShow = () =>{
    this.setState({
      loginFormShow: !this.state.loginFormShow
    });
    
  }

  render(){
    
    const categoryStyle = {
      color:"white",
      textDecoration: "none",
      padding: "8px" 
    }

    return(
    <> 
    <Navbar style={{backgroundColor: "#090707"}} expand="lg"  variant="dark">
      <Navbar.Brand href="/">
        <img
        alt=""
        src="/img/logo_mini2.png"
        width="30"
        height="30"
        className="d-inline-block align-top"
        />
        <Navbar.Brand style={{ marginLeft:"10px"}}>CIDS</Navbar.Brand>
      </Navbar.Brand>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="mr-auto" style={categoryStyle}>
          <NavLink to="/dashboard" style={categoryStyle}>대시보드 </NavLink>
          <NavLink to="/notice" style={categoryStyle} onClick={() => {window.location.href="/notice"}}>공지사항 </NavLink>
          <NavLink to="/qna" style={categoryStyle} onClick={() => {window.location.href="/qna"}}> Q & A </NavLink>
          <NavLink to={{pathname:"/ranking"}} style={categoryStyle} >
            의심 도메인
          </NavLink>
          <NavLink to="/service" style={categoryStyle}>CIDS 서비스 </NavLink>

          <NavLink to={{pathname:"/userlist"}} style={categoryStyle} >
            회원들
          </NavLink>


        </Nav>
        {this.state.loginState ? 
        <Nav inline="true">
          <NavLink to="/user/edit" style={categoryStyle}>회원정보</NavLink>
          <Nav.Link variant="dark" onClick={this.logout} style={categoryStyle}>로그아웃</Nav.Link>
        </Nav>          
        :
        <Nav inline="true">
          <NavLink to="/user/join" style={categoryStyle}>회원가입</NavLink>
          <Nav.Link variant="dark" onClick={this.handleLoginFormShow} style={categoryStyle}>로그인</Nav.Link>
          <LoginForm show = {this.state.loginFormShow} showHandler={this.handleLoginFormShow}/>
        </Nav>
        }

        

      </Navbar.Collapse>
    </Navbar>
    
    </>
    )
  }

}

export default Header;