import { useState, useEffect, useRef, useCallback } from 'react';
import Moment from 'moment';
import axios from "axios";
import { Table, Button } from "react-bootstrap";




const NewUserList = ({ list, total }) => {

    const obsRef = useRef(null); 	//observer Element
    const [List, setList] = useState([]);	//Post List

    const [page, setPage] = useState(0); //현재 페이지
    const [load, setLoad] = useState(false); //로딩 스피너
    const preventRef = useRef(true); //옵저버 중복 실행 방지
    const endRef = useRef(false); //모든 글 로드 확인

    // const [totalElement, setTotalElement] = useState(null);


    useEffect(() => { //옵저버 생성
        const observer = new IntersectionObserver(obsHandler, { threshold: 0.5 });
        if (obsRef.current) observer.observe(obsRef.current);
        return () => { observer.disconnect(); }
    }, [])


    useEffect(() => {
        getUser();
    }, [page])


    const obsHandler = ((entries) => { //옵저버 콜백함수
        const target = entries[0];
        if (!endRef.current && target.isIntersecting && preventRef.current) { //옵저버 중복 실행 방지
            preventRef.current = false; //옵저버 중복 실행 방지
            setPage(prev => prev + 1); //페이지 값 증가
        }
    })

    const getUser = useCallback(async () => { //글 불러오기  

        setLoad(true); //로딩 시작

        // ---- Get Data Code ---
        // const res = await axios({ method: 'GET', url: `/members`});
        const res = await axios.get(`/members`, {
            params:{
                page: page
            },
        });
        if (res.data) {
            if (res.data.last) { //마지막 페이지일 경우
                endRef.current = true;
            }
            setList(prev => [...prev, ...res.data.content]); //리스트 추가
            preventRef.current = true;
        } else {
            console.log(res); //error
        }

        setLoad(false); //로딩 종료
    }, [page]);

    const createDateFormat = (date) => {
        return Moment(date).format('YYYY-MM-DD HH:mm:ss')
    }

    return (
        <div className="container">
            {/* <div style={{ marginBottom: "1%" }}>
                <h2>{this.state.boardName}</h2>
            </div>

            <div>
                <h5>{this.state.totalPages} 중 {this.state.currentPage + 1} 페이지</h5>
            </div> */}
            <div>
                <Table striped bordered hover size="sm">
                    <thead>
                        <tr style={{ textAlign: "center" }}>
                            <th style={{ width: "20%" }}>아이디</th>
                            <th style={{ width: "10%" }}>이름</th>
                            <th style={{ width: "40%" }}>가입일</th>
                            <th style={{ width: "10%" }}>권한</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            List &&
                            <>
                                {
                                    List.map((user) =>
                                        <tr>

                                            <td style={{ textAlign: "center" }}>
                                                {user.loginId}
                                            </td>
                                            <td style={{ textAlign: "center" }}>
                                                {user.name}
                                            </td>
                                            <td style={{ textAlign: "center" }}>
                                                {createDateFormat(user.createDate)}
                                            </td>
                                            <td style={{ textAlign: "center" }}>
                                                {user.role}
                                            </td>
                                        </tr>

                                    )
                                }
                            </>
                        }

                        {
                            load ?
                            <div>로딩중</div>
                            :
                            <></>
                        }

                    </tbody>
                </Table>

                <div className="py-3 text-center"  ref={obsRef}>...</div>
            </div>
            <div>
                
            </div>

        </div>

    );

}



export default NewUserList;