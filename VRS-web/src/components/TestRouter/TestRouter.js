import React, { Component } from 'react';
import { Container } from 'reactstrap';
import { connect } from 'react-redux';
import { compose } from 'redux';
import { firebaseConnect, isLoaded, isEmpty } from 'react-redux-firebase';
import TodoItem from './TodoItem';

class TestRouter extends React.Component {
    constructor(props) {
        super(props);
        this.textInput = React.createRef();
    }
    pushSample() {
        const { firebase } = this.props;
        firebase.push('todos', {
            text: this.textInput.current.value,
            done: false
        });
    }

    render() {
        const { todos } = this.props;
        const todosList = !isLoaded(todos)
            ? 'Loading'
            : isEmpty(todos)
                ? 'Todo list is empty'
                : Object.keys(todos).map((key, id) => (
                      <TodoItem key={key} id={key} todo={todos[key]} />
                  ));
        return (
            <Container>
                <h1>Todos</h1>
                <ul>{todosList}</ul>
                <input type="text" ref={this.textInput} />
                <button onClick={() => this.pushSample()}>Add</button>
            </Container>
        );
    }
}

export default compose(
    firebaseConnect([
        'todos' // { path: '/todos' } // object notation
    ]),
    connect(({ firebase }) => ({
        todos: firebase.data.todos
        // profile: state.firebase.profile // load profile
    }))
)(TestRouter);
