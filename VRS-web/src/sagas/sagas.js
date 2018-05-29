import { all } from 'redux-saga/effects';
import loginSaga from '../routes/Login/saga/loginSaga';
export default function* IndexSaga() {
    yield ([
        loginSaga()
    ]);
}