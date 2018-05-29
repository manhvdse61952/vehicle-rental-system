import { all, takeLatest, put, call } from 'redux-saga/effects';
import Api from '../../../../src/api/apiUtils';

export const FETCH_ACCOUNT = 'FETCH_ACCOUNT';
export const ACCOUNT_REQUEST = 'ACCOUNT_REQUEST';
export const ACCOUNT_SUCCESS = 'ACCOUNT_SUCCESS';
export const ACCOUNT_FAILED = 'ACCOUNT_FAILED';

function* fetchAccount() {
    try {
        yield put({
            type: ACCOUNT_REQUEST
        });
        const { data } = yield call(Api.getAllAccount);
        yield put({
            type: ACCOUNT_SUCCESS,
            data
        });
    } catch (error) {
        yield put({
            type: ACCOUNT_FAILED,
            error: error.message
        });
    }
}

function* watchFetchAccount() {
    yield takeLatest(FETCH_ACCOUNT, fetchAccount);
}
