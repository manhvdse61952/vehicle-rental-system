import React, { Component } from 'react';
import {
    Row,
    Col,
    Container,
    Button,
    Form,
    FormGroup,
    Label,
    Input,
    FormText
} from 'reactstrap';
import { LoginStyled } from './LoginStyled';
import { connect } from 'react-redux';
import { LOGIN } from './saga/loginSaga';
import { Redirect, Link } from 'react-router-dom';

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        };
    }

    loginInit() {
        const { login } = this.props;
        this.props.login(this.state.username, this.state.password);
    }

    render() {
        const { error, loginSuccess, user, loginFailed } = this.props;
        const { from } = this.props.location.state || {
            from: { pathname: '/' }
        };
        if (loginSuccess) {
            return <Redirect to={from} />;
        }
        return (
            <LoginStyled>
                <Form>
                    <FormGroup>
                        <Label for="username">Username</Label>
                        <Input
                            type="text"
                            name="username"
                            id="username"
                            onChange={e =>
                                this.setState({ username: e.target.value })
                            }
                        />
                    </FormGroup>
                    <FormGroup>
                        <Label for="password">Password</Label>
                        <Input
                            type="password"
                            name="password"
                            id="password"
                            onChange={e =>
                                this.setState({ password: e.target.value })
                            }
                        />
                    </FormGroup>
                    <Button onClick={() => this.loginInit()}>Login</Button>
                    <Link to="/SignUp">SignUp</Link>
                    <p>{error ? error : ''}</p>
                </Form>
            </LoginStyled>
        );
    }
}
const mapStateToProps = state => {
    const { loginReducer } = state;
    console.log(loginReducer);
    return { ...loginReducer };
};

const mapDispatchToProps = dispatch => ({
    login: (username, password) => dispatch({ type: LOGIN, username, password })
});

export default connect(mapStateToProps, mapDispatchToProps)(Login);
