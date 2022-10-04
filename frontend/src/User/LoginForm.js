import React, { Component } from "react";
import moment from 'moment-timezone';
import { Form, Button, Modal } from "react-bootstrap";
import axios from "axios";
import $ from "jquery";
import {} from "jquery.cookie";
import cogoToast from "cogo-toast"; //로그인, 로그아웃 처리 (쿠키값이 있으면 게시글 보여주고, 쿠키값 없으면 회원가입, 로그인 보여줌)
import { RepeatOneSharp } from "@material-ui/icons";
axios.defaults.withCredentials = true; // node.js server와 통신 구현 -> 동일 기원(host)가 아니여도 접근할 수 있도록 구현 
// axios.defaults.headers.common['x-access-token'] = `test`;
// const instance = axios.create({
// });


class LoginForm extends Component {
  
  state = {
    show: false,
  }
  
  componentDidMount() {

  }
  

  //로그인
  login = () => {
    const loginId = this.loginId.value;
    const loginPw = this.loginPw.value;

    if (loginId === "" || loginId === undefined) {
      cogoToast.warn("아이디를 입력해주세요.");
      this.loginId.focus();
      return;
    } else if (loginPw === "" || loginPw === undefined) {
      cogoToast.warn("비밀번호를 입력해주세요.");
      this.loginPw.focus();
      return;
    }

    const send_param = {
      //headers,
      loginId: this.loginId.value,
      password: this.loginPw.value
    };
    axios
      .post("/login", send_param)
      //정상 수행
      .then(response => {
        if (response.status == 200) {
          
          const expiresTime = new Date(response.data.data.tokenExpired);
          $.cookie("accessToken", response.data.data.accessToken, { expires: expiresTime });
          const a = new Date();
          
          cogoToast.success("로그인에 성공했습니다.");
          setTimeout(function() {
            window.location.href="/";
          }, 1000);
        } 
        else {
          cogoToast.error(response.data.reason);
          this.loginId.value = "";
          this.loginPw.value = "";
        }
      })
      //에러
      .catch(err => {
        console.log(err);
      });
  };

  handleKeyPress = (e) => {
    if (e.key === "Enter") {
      this.login();
    }
  };

  render() {
    const buttonStyle = {
      marginTop: 10
    };

    return (
      <Modal show={this.props.show} onHide={this.props.showHandler}>
      <Modal.Header closeButton>
      <Modal.Title>로그인</Modal.Title>
      </Modal.Header>
      <Modal.Body>
      <Form className="container">
        <Form.Group>
          <Form.Label>ID</Form.Label>
          <Form.Control
            type="id"
            maxLength="20"
            ref={ref => (this.loginId = ref)}
            placeholder="Enter ID"
            onKeyPress={this.handleKeyPress}
          />
          <br/>
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            maxLength="20"
            ref={ref => (this.loginPw = ref)}
            placeholder="Password"
            onKeyPress={this.handleKeyPress}
          />
          <Button
            style={buttonStyle}
            onClick={this.login}
            variant="dark"
            type="button"
            block
          >
            로그인
          </Button>
        </Form.Group>
      </Form>
      </Modal.Body>
      </Modal>
     
    );
  }
}

export default LoginForm;
