import React, {Component, useEffect, useState} from "react";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import {CKEditor} from "@ckeditor/ckeditor5-react";
import {Button, Form} from "react-bootstrap";
import axios from "axios";
import $ from "jquery";
import {} from "jquery.cookie";
import cogoToast from 'cogo-toast';
import ImageUploadAdapter from "./ImageUploadAdapter";
import {Prompt} from 'react-router-dom'
import usePreventLeave from "../usePreventLeave";




axios.defaults.withCredentials = true;




function PostEditor(props){


    const [content, setContent] = useState("");
    const [postType, setPostType] = useState("");
    const [title, setTitle] = useState("");

    const [imageUploaded, setImageUploaded] = useState(false);



    //ComponentDidMount
    useEffect(()=>{
        setPostType(props.location.query.postType);
        setPostType(props)
        console.log(props.location.query.postType)
        console.log(postType);
        return ()=>{
            if(imageUploaded){
                console.log(props);
                deleteTempImage();
                console.log(imageUploaded);
            }
            console.log('componentWillUnmount')
        }

    },[])

    //ComponentDidUpdate
    //사용시 close tap, refresh만 작동.
    // useEffect(()=>{
    //     console.log('component updated!');
    //     if (shouldBlockNavigation) {
    //         window.onbeforeunload = () => true
    //     }else{
    //         window.onbeforeunload = undefined;
    //     }
    // })

    //ComponentWillUnmount
    // useEffect(()=>{
    //     return ()=>{
    //         deleteTempImage();
    //         console.log('componentWillUnmount')
    //     }
    // })




    const deleteTempImage = () =>{
        axios.post(`/image/delete`,null,{
            headers: {
                'Authorization': 'Bearer ' + $.cookie('accessToken')
            }
        })
            .then(response => {
                if(response.status == 201){
                    cogoToast.success(response.data.message);
                }
            })
            .catch(err =>{
                console.log(err);
            })


    }

    const createPost = () => {

        console.log(props);

        if (!$.cookie('accessToken')) {
            cogoToast.error("로그인 후 가능합니다!!");
            // window.location.href = "/";
        }

        let url;
        let send_param;

        // const [title, setTitle] = useState(title);
        const titleCopy = title;
        const contentCopy = content;

        if (titleCopy === undefined || titleCopy === "") {
            cogoToast.warn("글 제목을 입력 해주세요.");
            titleCopy.focus();
            return;
        } else if (contentCopy === undefined || contentCopy === "") {
            cogoToast.warn("글 내용을 입력 해주세요.");
            return;
        }

        console.log(postType);
        url = `/boards`;
        send_param = {
            title: titleCopy,
            type: props.location.query.postType,
            content: contentCopy
        };
        axios
            .post(`/boards`, send_param,
                {
                    headers: {
                        'Authorization': 'Bearer ' + $.cookie('accessToken')
                    }
                })
            //정상 수행
            .then(response => {
                if (response.status == 201) {
                    // cogoToast.success(response.data.message);
                    history.goBack();
                } else {
                    cogoToast.error("글쓰기 실패");
                }
            })
            //에러
            .catch(err => {
                console.log(err);
            });

    };


    const onChangeTitle = (e) => {
        setTitle(e.target.value);
    }

    const handleCkeditorState = (event, editor) => {
        const data = editor.getData();
        setContent(data);
            // content: event.editor.getData()



        console.log(data);
    }



        const { history } = props

        const custom_config ={
            extraPlugins: [ MyCustomUploadAdapterPlugin ],
            toolbar: {
                items: [
                    'heading',
                    '|',
                    'bold',
                    'italic',
                    'link',
                    'bulletedList',
                    'numberedList',
                    '|',
                    'blockQuote',
                    'insertTable',
                    '|',
                    'imageUpload',
                    'undo',
                    'redo'
                ]
            },
            table: {
                contentToolbar: [ 'tableColumn', 'tableRow', 'mergeTableCells' ]
            }
        }
        const divStyle = {
            marginTop: "3%",
            minWidth: "70%",
            minHeight: "700px"
        };
        const titleStyle = {
            marginBottom: 5
        };
        const buttonStyle = {
            marginTop: 5
        };

    return (
        <div className="container" style={divStyle}>
            <h2>글쓰기</h2>
            <Form.Control
                type="text"
                style={titleStyle}
                placeholder="글 제목"
                value={title}
                onChange={onChangeTitle}
            />
            <CKEditor
                editor={ClassicEditor}
                onInit={editor => {

                }}
                config={custom_config}
                data={content}
                onChange={handleCkeditorState}
            />

            <Button style={buttonStyle} onClick={createPost} block>
                저장하기
            </Button>

        </div>
    );
}


function MyCustomUploadAdapterPlugin(editor) {
    editor.plugins.get( 'FileRepository' ).createUploadAdapter = (loader) => {
        return new ImageUploadAdapter(loader)
    }
}


export default PostEditor;
