import { useState, useEffect, useRef, useCallback } from 'react';
import Moment from 'moment';
import axios from "axios";
import $ from "jquery";
import { Table, Button } from "react-bootstrap";
import cogoToast from 'cogo-toast';




const SearchMember = ({ list, total, searchWords }) => {

    const obsRef = useRef(null); 	//observer Element
    const [List, setList] = useState([]);	//Post List

    const [page, setPage] = useState(0); //현재 페이지
    const [load, setLoad] = useState(false); //로딩 스피너
    const preventRef = useRef(true); //옵저버 중복 실행 방지
    const endRef = useRef(false); //모든 글 로드 확인

    const [role, setRole] = useState("");

    const [search, setSearch] = useState("");

    // const [totalElement, setTotalElement] = useState(null);


    useEffect(() => { //옵저버 생성
        const observer = new IntersectionObserver(obsHandler, { threshold: 0.5 });
        if (obsRef.current) observer.observe(obsRef.current);
        return () => { observer.disconnect(); }
    }, [])


    useEffect(() => {
        getUser();
    }, [page])


    //권한 변경
    const handleRole = (memberId, loginID) => (e) => {
        if (window.confirm("권한을 변경 하시겠습니까?")) {
            setRole(e.target.value);
            updateUserRole(memberId, e.target.value);
        }

    }

    const obsHandler = ((entries) => { //옵저버 콜백함수
        const target = entries[0];
        if (!endRef.current && target.isIntersecting && preventRef.current) { //옵저버 중복 실행 방지
            preventRef.current = false; //옵저버 중복 실행 방지
            setPage(prev => prev + 1); //페이지 값 증가
        }
    })


    const updateUserRole = (memberId, role) => {

        console.log(role);
        console.log(memberId);
        const send_param = {
            id: memberId,
            role: role
        }


        axios
            .put(`/members/${memberId}`, send_param,
                {
                    headers: {
                        'Authorization': 'Bearer ' + $.cookie('accessToken'),
                    }
                })
            .then(response => {
                if (response.status == 200) {
                    cogoToast.success(response.data.message);
                } else {
                    cogoToast.error("role 수정 실패");
                }
            })
            .catch(err => {
                console.log(err);
            });

    };

    const getUser = useCallback(async () => { //글 불러오기  

        setLoad(true); //로딩 시작

        // ---- Get Data Code ---
        // const res = await axios({ method: 'GET', url: `/members`});
        const res = await axios.get(`/members/search`, {
            params: {
                searchWord: searchWords,
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





    ///////////////////검색창


    const handleSearch = (e) => {
        getUser(e.target.value);
    }

    const Search = (e) => {

        if (search == null || search == '') {
            axios
                .get(`/member/search`, {
                    params: {
                        searchWord: search
                    }
                })
        }


    }


    return (

        <div className="container">
            {/* <div style={{ marginBottom: "1%" }}>
                <h2>{this.state.boardName}</h2>
            </div>

            <div>
                <h5>{this.state.totalPages} 중 {this.state.currentPage + 1} 페이지</h5>
            </div> */}
            <form onSubmit={e => Search(e)}>
                <input type="text" value={search} placeholder="" onChange={handleSearch} />
                <Button type='submit'>검색</Button>
            </form>
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
                                            <td>
                                                <select value={user.role} onChange={handleRole(user.id, user.loginId)}>
                                                    <option value="ADMIN">
                                                        ADMIN
                                                    </option>
                                                    <option value="SUBSCRIBER">
                                                        SUBSCRIBER
                                                    </option>
                                                    <option value="MEMBER">
                                                        MEMBER
                                                    </option>

                                                </select>
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

                <div className="py-3 text-center" ref={obsRef}>...</div>
            </div>
            <div>

            </div>

        </div>

    );

}



export default SearchMember;