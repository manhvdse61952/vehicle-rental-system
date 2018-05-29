import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { firebase } from 'react-redux-firebase';

import './Todo.css';

class TodoItem extends Component {
    static propTypes = {
        todo: PropTypes.object,
        id: PropTypes.string
    };

    render() {
        const { firebase, todo, id } = this.props;

        const deleteTodo = event => {
            firebase.remove(`/todos/${id}`);
        };
        const toggleDone = event => {
            firebase.set(`/todos/${id}/done`, !todo.done);
        };
        return (
            <li className="Todo">
                <label>
                    <input
                        className="Todo-Input"
                        type="checkbox"
                        checked={todo.done}
                        onChange={toggleDone}
                    />
                    <span>{todo.text || todo.name}</span>
                </label>
                <button className="Todo-Button" onClick={deleteTodo}>
                    Delete
                </button>
            </li>
        );
    }
}
export default firebase()(TodoItem);
