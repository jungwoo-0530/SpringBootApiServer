import React, { Component, useEffect, useState } from "react";
import { Image, Table, Row, Col } from "react-bootstrap";
import { Input } from 'semantic-ui-react';
import axios from "axios";
import Moment from 'moment';
import ReactPaginate from 'react-paginate';
import { } from '../css/pagination.css';
import { setIn } from "immutable";


axios.defaults.withCredentials = true;


const DomainRow = ({ DomainRow, index, currentPage, pageSize }) => {

    const [Domain, setDomain] = useState('');
    const [Index, setIndex] = useState('');
    

    useEffect(() => {

        setIndex((currentPage*pageSize)+index);
        setDomain(DomainRow);
    
        
    }, [DomainRow])



    const createDateFormat = (date) => {
        return Moment(date).format('YYYY-MM-DD HH:mm:ss')
    }


    const showRanking = () => {
        if (Index === 1) {
          return (<Image src="/img/ranking/1.png" width="25" height="30" />)
        }
        else if (Index === 2) {
          return (<Image src="/img/ranking/2.png" width="25" height="30" />)
        }
        else if (Index === 3) {
          return (<Image src="/img/ranking/3.png" width="25" height="30" />)
        }
        else {
          return Index;
        }
      }
    


    return (
        <>
            <tr style={{ fontWeight: "600" }}>
                <td style={{ textAlign: "center" }}>
                    {showRanking()}
                </td>

                <td style={{ textAlign: "center", marginTop: "10px", paddingTop: "17px" }}>
                    {DomainRow.domain}
                </td>
                <td style={{ textAlign: "center", marginTop: "10px", paddingTop: "17px" }}>
                    {createDateFormat(DomainRow.updateDate)}
                </td>
                <td style={{ textAlign: "center", marginTop: "10px", paddingTop: "17px" }}>
                    {DomainRow.hit}
                </td>
            </tr>
        </>
    );





};

export default DomainRow;