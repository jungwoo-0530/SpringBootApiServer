import React, { Component } from "react";
import { Image, Table, Row, Col } from "react-bootstrap";
import { Input } from 'semantic-ui-react';
import axios from "axios";
import Moment from 'moment';
import ReactPaginate from 'react-paginate';
import { } from '../css/pagination.css';


import DomainRow from "./DomainRow";
import PaginationPostAndComment from "../components/Pagination/PaginationPostAndComment";

axios.defaults.withCredentials = true;

/* Ranking List */
class RankingIndex extends Component {
  state = {
    domains: [],

    currentPage: 0,
    totalPages: 0,

    pageSize: 0,

    startIndex: 0,



    keyword: ''
  }


  //component 시작하면 getRankingIndex 함수 실행
  componentDidMount() {
    // this.getRankingIndex('all');
    this.getRankingIndex(this.state.currentPage);
  }

  //rank List 가져오는 함수 
  getRankingIndex = (pageNumber) => {

    let send_params;
    send_params = {
      page: pageNumber
    }

    axios
      .get(`/domain/ranking`,
        {
          params: send_params
        })
      .then(res => {

        const totalPages =
          res.data.data.totalPages;

        const pageSize = res.data.data.pageSize;

        const currentPage = res.data.currentpage;


        this.setState({
          totalPages: totalPages,
          pageSize: pageSize,
          currentPage: res.data.data.currentPage
        })

        console.log(this.state.currentPage)

        if (res.data.data.numberOfElements > 0) {
          const returnedDomains = res.data.data.content;

          this.setState({
            domains: returnedDomains
          })
        } else {
          this.state.domains.push([
            <tr key={0}>
              <td colSpan="4" style={{ textAlign: "center" }}>도메인이 존재하지 않습니다.</td>
            </tr>
          ])

        }
      })
      .catch(err => {
        console.log(err);
      });
  };


  paginate = (pageNumber) => {
    this.setState({
      currentPage: pageNumber.selected
    })
    

    // console.log(this.state.currentPage);
    // console.log(pageNumber);
    this.forceUpdate();

    this.getRankingIndex(pageNumber.selected);
    this.forceUpdate();


    // window.location.reload();
  }

  // //search Change 반영
  // handleKeywordChange = (e) => {
  //   this.setState({
  //     keyword: e.target.value
  //   })
  // }

  // //enter 반영
  // handleKeyPress = (e) => {
  //   if (e.key === "Enter") {
  //     this.getRankingIndex(this.state.keyword);
  //   }
  // };

  render() {
    const divStyle = {
      marginTop: "3%",
      minWidth: "70%",
      minHeight: "700px",
    };


    return (
      <div className="container" style={divStyle} >
        <div style={{ marginBottom: "3%" }}>
          <Row>
            <Col sm={10}>
              <h2>탐색된 의심 도메인 순위</h2><br />
            </Col>
            {/* <Col sm={1}>
              <Input
                focus placeholder='Search...'
                value={this.state.keyword}
                onChange={this.handleKeywordChange}
                onKeyPress={this.handleKeyPress}
              />
            </Col> */}

          </Row>
        </div>
        <div>
          <Table striped bordered hover>
            <thead>
              <tr style={{ textAlign: "center" }}>
                <th style={{ width: "10%" }}>Ranking</th>
                <th style={{ width: "60%" }}>Domain</th>
                <th style={{ width: "20%" }}>Last Date</th>
                <th style={{ width: "10%" }}>Hit</th>
              </tr>
            </thead>
              <tbody>
              {this.state.domains.map((domain, index) => (
                <DomainRow DomainRow={domain} index={index+1} currentPage = {this.state.currentPage} pageSize = {this.state.pageSize}/>
              ))}
            </tbody>
          </Table>

        </div>
        <div>
          <PaginationPostAndComment totalPage={this.state.totalPages} paginate={this.paginate} />
        </div>
        {/* <br /><br /><br /> */}
      </div>
    );
  }
}

export default RankingIndex;
