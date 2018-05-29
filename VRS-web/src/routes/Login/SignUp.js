import React, { Component } from 'react';
import { Row, Col, Container, Button, Form, FormGroup, Label, Input, FormText } from 'reactstrap';
import { SIGN_UP } from './saga/loginSaga';
import {connect} from 'react-redux';
import {Link} from 'react-router-dom'

class SignUp extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            email: '',
            fullname: ''
        }
    };
    register = () => {
        const { username, password, email, fullname } = this.state;
        this.props.register(username, password, email, fullname);

    }
    render() {
        return (
            <div style={{ margin: '0 auto', width: '400px' }}>
                <Form>
                    <FormGroup>
                        <Label for='username'>Username</Label>
                        <Input type='text' name='username' id='username' onChange={(e) => this.setState({ username: e.target.value })}></Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for='password'>Password</Label>
                        <Input type='password' name='password' id='password' onChange={(e) => this.setState({ password: e.target.value })}></Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for='email'>Email</Label>
                        <Input type='email' name='email' id='email' onChange={(e) => this.setState({ email: e.target.value })}></Input>
                    </FormGroup>
                    <FormGroup>
                        <Label for='fullname'>Full name</Label>
                        <Input type='text' name='fullname' id='fullname' onChange={(e) => this.setState({ fullname: e.target.value })}></Input>
                    </FormGroup>
                    <Button onClick={() => this.register()}>Sign Up</Button>
                    <Link to="/Login">Login</Link>
                </Form>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
    const { loginReducer } = state;
    return { ...loginReducer };
};

const mapDispatchToProps = dispatch => ({
    register: (username, password, email, fullname) => dispatch({ type: SIGN_UP, username, password, email, fullname })
});

export default connect(mapStateToProps, mapDispatchToProps)(SignUp);
