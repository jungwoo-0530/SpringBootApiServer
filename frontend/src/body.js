import React, {Component} from "react";
import JoinForm from "./User/JoinForm";
import LoginForm from "./User/LoginForm";
import Post from "./components/Post/Post";
import UserPageForm from "./User/UserPageForm";
import HomepageForm from "./HomePage/HomePage";
import {Route, Switch} from "react-router-dom";
import BoardList from "./components/Post/BoardList"
import RankingIndex from "./Service/RankingIndex";
import ServicePage from "./Service/ServicePage";
import DashboardPage from './Service/DashboardPage'
import {CssBaseline} from "@material-ui/core";
import LoginPage from "./LoginPage";
import InvalidPage from "./404Page";
import PostUpdateForm from "./components/Post/PostUpdateForm"
import PostEditor from "./components/Post/PostEditor"
import Upload from "./upload"

import PostList from "./components/Post/PostList";

import UserList from './User/NewUserList';

// import PostSearchList from './Post/SearchPosts';


class Body extends Component {
    render() {
        return (
            <CssBaseline>
                <div>
                    <Switch>
                        <Route path="/boardList" component={BoardList}/>


                        <Route exact path="/posts/update" component={PostUpdateForm}/>
                        <Route exact path="/posts/write" component={PostEditor}/>
                        <Route path="/posts/:id" component={Post}/>
                        
                        <Route exact path="/qna" component={PostList}/>
                        <Route exact path="/notice" component={PostList}/>
                        <Route path="/uploads" component={Upload}/>
                        
                        {/* <Route exact path="/posts/search" component={PostSearchList}/> */}


                        <Route path="/userlist" component={UserList}/>
         


                        <Route path="/user/edit" component={UserPageForm}/>
                        <Route path="/user/login" component={LoginForm}/>
                        <Route path="/user/join" component={JoinForm}/>
                        <Route path="/dashboard" component={DashboardPage}/>
                        <Route path="/ranking" component={RankingIndex}/>
                        <Route path="/login" component={LoginPage}/>

                        <Route path="/service" component={ServicePage}/>

                        <Route exact path="/" component={HomepageForm}/>

                        <Route path="/*" component={InvalidPage}/>
                    </Switch>
                </div>
            </CssBaseline>
        );
    }
}

export default Body;
