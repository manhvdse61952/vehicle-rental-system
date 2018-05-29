import {
    LOGIN,
    LOGIN_REQUEST,
    LOGIN_FAILED,
    LOGIN_SUCCESS,
    SIGN_UP,
    SIGN_UP_FAILED,
    SIGN_UP_REQUEST,
    SIGN_UP_SUCCESS,
    LOGOUT
} from '../saga/loginSaga';

export default function loginReducer(
    state = {
        user: null,
        isFetching: false,
        error: null,
        loginSuccess: localStorage.getItem('access_token') ? true : false
    },
    action
) {
    switch (action.type) {
        case LOGIN_REQUEST: {
            return Object.assign({}, state, {
                isFetching: true,
                loginSuccess: false,
                error: null
            });
        }
        case LOGIN_SUCCESS: {
            console.log(action.data);
            return Object.assign({}, state, {
                isFetching: false,
                error: null,
                loginSuccess: true,
                user: action.data
            });
        }
        case LOGIN_FAILED: {
            return Object.assign({}, state, {
                isFetching: false,
                loginSuccess: false,
                error: action.error
            });
        }
        case SIGN_UP_REQUEST: {
            return Object.assign({}, state, {
                isFetching: true,
                error: null
            });
        }
        case SIGN_UP_SUCCESS: {
            console.log(action.data);
            return Object.assign({}, state, {
                isFetching: false,
                error: null,
                user: action.data
            });
        }
        case SIGN_UP_FAILED: {
            return Object.assign({}, state, {
                isFetching: false,
                error: action.error
            });
        }
        case LOGOUT: {
            return Object.assign({}, state, {
                loginSuccess: false
            });
        }
        default:
            return state;
    }
}
