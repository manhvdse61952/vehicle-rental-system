import React, { Component } from 'react';
import { Container, Row, Col } from 'reactstrap';
import Header from './components/Header/Header';
import LeftNav from './components/LeftNav/LeftNav';
import Map from './routes/Map/Map';
import TestRouter from './components/TestRouter/TestRouter';
import { Switch, Route, Redirect } from 'react-router-dom';
import { PrivateRoute } from './index';

class App extends Component {
    render() {
        return (
            <Container fluid={true}>
                <Row>
                    <Col>
                        <Header />
                    </Col>
                </Row>
                <Row>
                    <Col xs="3">
                        <LeftNav />
                    </Col>
                    <Col xs="9">
                        <Switch>
                            <PrivateRoute path="/App/Map" component={Map} />
                            <PrivateRoute
                                path="/App/TestRouter"
                                component={TestRouter}
                            />
                        </Switch>
                    </Col>
                </Row>
            </Container>
        );
    }
}
export default App;
