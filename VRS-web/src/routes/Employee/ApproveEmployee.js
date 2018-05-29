import React, { Component } from 'react';
import { Button } from 'reactstrap';
import {
    Table,
    Column,
    SortDirection,
    SortIndicator,
    AutoSizer
} from 'react-virtualized';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

class ApproveEmployee extends Component {
    constructor(props) {
        super(props);
        const sortBy = 'index';
        const sortDirection = SortDirection.ASC;
        this.stompClient = null;
        this.state = {
            disableHeader: false,
            headerHeight: 30,
            height: 270,
            hideIndexRow: false,
            overscanRowCount: 10,
            rowHeight: 40,
            rowCount: 1000,
            scrollToIndex: undefined,
            sortBy,
            sortDirection,
            useDynamicRowHeight: false,
            myTableData: [
                { name: 'Rylan', role: false },
                { name: 'Amelia', role: true },
                { name: 'Estevan', role: true },
                { name: 'Florence', role: true },
                { name: 'Tressa', role: false }
            ]
        };
    }
    componentDidMount() {
        this.initializeWebSocketConnection();
    }

    initializeWebSocketConnection() {
        const serverUrl = 'http://localhost:8080/my-websocket';
        const title = 'Test ws';
        let socket = new SockJS(serverUrl);
        this.stompClient = Stomp.over(socket);
        let that = this;
        this.stompClient.connect({}, frame => {
            that.stompClient.subscribe('/accountlist', message => {
                if (message.body) {
                    const data = this.state.myTableData;
                    const received = JSON.parse(message.body).body;
                    received['name'] = received['fullname'];
                    received['role'] = false;
                    delete received['fullname'];
                    data.push(received);
                    this.setState({ myTableData: data });
                }
            });
        });
    }
    sendMessage() {
        const message = {
            fullname: 'khai',
            password: '123789',
            username: 'provt',
            email: 'zx@gmail.com',
            role: 'ROLE_OWNER'
        };
        this.stompClient.send(
            '/app/account/add',
            {
                'Content-Type': 'application/json'
            },
            JSON.stringify(message)
        );
    }
    render() {
        return (
            <div>
                <Table
                    height={300}
                    rowHeight={30}
                    rowCount={this.state.myTableData.length}
                    headerHeight={20}
                    width={300}
                    rowGetter={({ index }) => this.state.myTableData[index]}
                    style={{ backgroundColor: 'white', paddingTop: '30px' }}
                >
                    <Column label="Name" dataKey="name" width={100} />
                    <Column
                        label="Role"
                        dataKey="role"
                        width={150}
                        cellRenderer={props =>
                            this.state.myTableData[props.rowIndex].role ? (
                                <Button color="success">Accepted</Button>
                            ) : (
                                <Button color="danger">Unauthorized</Button>
                            )
                        }
                    />
                </Table>
                <Button onClick={() => this.sendMessage()}>Test send</Button>
            </div>
        );
    }
}

export default ApproveEmployee;
