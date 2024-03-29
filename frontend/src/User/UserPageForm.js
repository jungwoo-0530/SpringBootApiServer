import React, {Component} from "react";
import {} from "react-bootstrap";
import {Button, Grid,Card,Form, Segment, Image, Placeholder  } from 'semantic-ui-react';
import axios from "axios";
import $ from "jquery";
import {} from "jquery.cookie";
import PwChangeModalForm from "./PwChangeModal";
import ImageUploader from "react-images-upload";
import cogoToast from "cogo-toast";
axios.defaults.withCredentials = true;

class UserPageForm extends Component {

  constructor(props){
    super(props);
    this.state = {
      name: '',
      loginId: '',
      role: '',
      telephone: '',
      email: '',
      pictures: [],
      imgPath: '',
      s3_url: 'https://s3.ap-northeast-2.amazonaws.com/cidsprofileimg/',
      pwChangeModalShow: false,
      userDeleteModalShow: false,
      hidden: '',
    }
    this.onDrop = this.onDrop.bind(this);
  }

  //비번 변경 modal
  handlePwChangeModalShow = () =>{
    this.setState({
      pwChangeModalShow: !this.state.pwChangeModalShow
    });
  
  }

  //사진 업로드 Func
  async onDrop(pictureFiles, pictureDataURLs) {
    try{
      await this.setState({
        pictures: this.state.pictures.concat(pictureFiles)
      });
      this.fileUploadHandler();
    }
    catch(err){
      console.log(err);
    }
    
  }

  //사진 업로드
  fileUploadHandler = async () => {
    try{
      const data = new FormData();
      await data.append('file',this.state.pictures[0]); //key value 저장 후 전송
      // await data.append('filename',this.state.pictures[0].name);
      console.log(data);

      axios.post(`/test/image/upload`, data, {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': 'Bearer ' + $.cookie('accessToken')
        }})
      .then( response => { //응답받은 url 저장
          this.setState({
            imgPath: this.state.s3_url + response.data.img_path
          });
          this.setState({
            pictures: []
          });
          
      });
      return;
    } catch (err){

    }
   
  }

  componentDidMount(){
    this.getUserInfo();
  }

  //user 정보 가져오기
  getUserInfo = () => {

    axios
      .get("/members/me", {     
            headers: {
              'Authorization': 'Bearer ' + $.cookie('accessToken')
            },
            withCredentials: true
          })
      .then((response) =>{
        if(response.status==200){
          let user = response.data.data;
          this.setState({
            name: user.name,
            role: user.role,
            telephone: user.telephone,
            email: user.email,
            loginId: user.loginId
            // imgPath: this.state.s3_url + user.img_path
          });          
        }
        else{
          cogoToast.error("유저정보 조회가 실패했습니다.");
          window.location.href = '/';
        }
        
      })
      .catch( err => {
        console.log(err);
        window.location.href = '/';
      })
  }

  //change 체크
  handleTelephoneChange = (e) =>{
    this.setState({
      telephone: e.target.value
    });
  }

  handleEmailChange = (e) =>{
    this.setState({
      email: e.target.value
    })
  }

  handleHidden = (e) =>{
    this.setState({
      hidden: e.target.value
    })
  }

  handleNameChange = (e) =>{
    this.setState({
      name: e.target.value
    })
  }
  userInfoUpdate = () =>{

    const telePhoneRegExp = /^\d{3}-\d{3,4}-\d{4}$/;
    const emailRegExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
    
    if (this.state.email === "") {
      cogoToast.warn("이메일 주소를 입력해주세요.");
      return;
    } 
    else if (this.state.email.match(emailRegExp) === null ||this.state.email.match(emailRegExp) === undefined) {
      cogoToast.warn("이메일 형식에 맞게 입력해주세요.");
      this.setState({
        email: ""
      });
      return;
    }

    if(this.state.telephone.match(telePhoneRegExp) === null || this.state.telephone.match(telePhoneRegExp) === undefined){
      cogoToast.warn("핸드폰 형식을 맞게 입력해주세요.");
      this.setState({
        telephone: ""
      });
      return;
    }
    
    const send_param = {
      name: this.state.name,
      telephone: this.state.telephone,
      email: this.state.email,
      loginId: this.state.loginId,
      role: this.state.role
    }

    axios
      .put(`/members/me/update`, send_param,
      {
        headers: {
          'Authorization': 'Bearer ' + $.cookie('accessToken')
        }
      }
        )
        .then((response) => {
          if(response.status == 200){
            cogoToast.success(response.data.message,{
              onClick:()=>{
                window.location.href = '/user/edit';
              }
            });
          }
          else{
            cogoToast.warn(response.data.message);
          }
        })
        .catch( (err) => {
          console.log(err);
          cogoToast.error("유저정보 업데이트에 실패했습니다.");
          window.location.href = '/';
        });
  }

  //유저 탈퇴 루틴 
  userDeleteCheck = () =>{
    if(window.confirm('정말 회원탈퇴를 하시겠습니까?')){
      this.userDelete();
    }
    else{
      return;
    }
  }

  userDelete = () => {
    axios
      .delete(`/uesr/me`)
        .then((response) => {
          if(response.data.message){
            $.removeCookie("login_id");
            $.removeCookie("token");
            cogoToast.success("회원탈퇴에 성공했습니다.");
            setTimeout(function() {
              window.location.href="/";
            }, 1000);
          }
        })
        .catch( (err) => {
          //console.log(err);
          cogoToast.error("회원탈퇴에 실패했습니다.");
          return;
        });

  }

  onChange = (e) => {
    this.setState({
      retained_star: e.target.value
    })
  }

  render(){
    return (
      <div className="container">
      
        <center>
        <Segment style={{marginBottom: "20px", marginTop: "20px", width: "80%" }}>
      
                <Grid doubling columns={2} style={{marginBottom:"3%",marginTop:"3%"}}>
                  <Grid.Column>
                  <Card key={this.state.name} style={{width:"280px"}} >
                    <Image src={this.state.imgPath} style={{height: "280px", width: "280px"}}/>
                    <Placeholder>
                      <Card.Description>User Image</Card.Description>
                    </Placeholder>
                    <Card.Content>
                      <ImageUploader
                              withIcon={false}
                              buttonText="유저 이미지 변경"
                              onChange={this.onDrop}
                              imgExtension={[".jpg", ".gif", ".png", ".gif"]}
                              maxFileSize={5242880}
                              withLabel={false}
                              singleImage ={true}
                            />
                    </Card.Content>
                     
                  </Card>
                  </Grid.Column>
                  <Grid.Column>
                    <Form>
                      <Form.Group widths={2}>
                        <Form.Input label="name" name="name"type="text" value={this.state.name} onChange={this.handleNameChange}/>
                      </Form.Group>
                      <Form.Group widths={2}>
                        <Form.Input  label="email" name="email" type="text" value={this.state.email} onChange={this.handleEmailChange} />
                        <Form.Input label="role" name="role" type="text" value={this.state.role} readOnly={true}/>
                      </Form.Group>
                <Form.Group widths={2}>
                  <Form.Input label="telephone" name="telephone" type="text"  value={this.state.telephone} onChange={this.handleTelephoneChange}/>
                </Form.Group>
                <Button onClick={this.handlePwChangeModalShow} active>
                  비밀번호 변경
                </Button>
                <Button onClick={this.userInfoUpdate} active>
                  회원정보 수정
                </Button>
                <Button onClick={this.userDeleteCheck} active>
                  회원 탈퇴
                </Button>
                </Form>
                </Grid.Column>
                </Grid>
          </Segment>
          <PwChangeModalForm show = {this.state.pwChangeModalShow} showHandler={this.handlePwChangeModalShow}/>
        </center>
      </div>
    );
  };
}
export default UserPageForm;
