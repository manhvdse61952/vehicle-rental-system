import { all, takeLatest, put, call } from 'redux-saga/effects';
import Api from '../../../../src/api/apiUtils';
import { setAccessToken } from './../../../api/authentication';

export const LOGIN = 'LOGIN';
export const LOGIN_REQUEST = 'LOGIN_REQUEST';
export const LOGIN_SUCCESS = 'LOGIN_SUCCESS';
export const LOGIN_FAILED = 'LOGIN_FAILED';

export const SIGN_UP = 'SIGN_UP';
export const SIGN_UP_REQUEST = 'SIGN_UP_REQUEST';
export const SIGN_UP_SUCCESS = 'SIGN_UP_SUCCESS';
export const SIGN_UP_FAILED = 'SIGN_UP_FAILED';

export const LOGOUT = 'LOGOUT';
export const LOGOUT_REQUEST = 'LOGOUT_REQUEST';
export const LOGOUT_SUCCESS = 'LOGOUT_SUCCESS';
export const LOGOUT_FAILURE = 'LOGOUT_FAILURE';

// const getLoginState = state => state.loginReducer;

function* fetchLogin({ username, password }) {
    try {
        yield put({
            type: LOGIN_REQUEST
        });
        const { data, status } = yield call(Api.fetchLogin, username, password);

        setAccessToken(data);
        yield put({
            type: LOGIN_SUCCESS,
            data
        });
        localStorage.setItem('access_token', data.access_token);
        localStorage.setItem('expires_at', data.expirationDateTime);
    } catch (error) {
        yield put({
            type: LOGIN_FAILED,
            error: error.message
        });
    }
}

function* fetchSignUp({ username, password, email, fullname }) {
    try {
        yield put({
            type: SIGN_UP_REQUEST
        });
        const { data, status } = yield call(
            Api.signUp,
            username,
            password,
            email,
            fullname
        );
        yield put({
            type: SIGN_UP_SUCCESS,
            data
        });
    } catch (error) {
        yield put({
            type: SIGN_UP_FAILED,
            error: error.message
        });
    }
}

function* watchLogin() {
    yield takeLatest(LOGIN, fetchLogin);
}

function* watchSignUp() {
    yield takeLatest(SIGN_UP, fetchSignUp);
}

export default function* loginFlow() {
    yield all([watchLogin(), watchSignUp()]);
}
