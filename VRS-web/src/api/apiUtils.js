import axios from 'axios';
import { getAccessToken, isExpired } from './authentication';
import { isAuthenticated } from './../index';

const ENDPOINT = 'http://localhost:8080';

axios.interceptors.request.use(_config => {
    // if (isAuthenticated() && isExpired()) {

    // }
    const config = Object.assign({}, _config);
    const accessToken = getAccessToken() || null;
    if (!accessToken) config.headers.Authorization = `Bearer ${accessToken}`;
    return config;
});

axios.interceptors.response.use(
    response => response,
    error => {
        // if (error && error.response && error.response.status === 401) {

        // }
        return Promise.reject(error);
    }
);

function fetchLogin(usernameOrEmail, password) {
    return axios.post(`${ENDPOINT}/api/auth/signin`, {
        usernameOrEmail,
        password
    });
}

function signUp(username, password, email, fullname) {
    return axios.post(`${ENDPOINT}/api/auth/signup`, {
        username,
        password,
        email,
        name: fullname
    });
}
function demo() {
    return axios.get(`${ENDPOINT}/demo/hotel`);
}
function getAllAccount() {
    return axios.get(`${ENDPOINT}/account`);
}

const Api = {
    fetchLogin,
    signUp,
    demo,
    getAllAccount
};
export default Api;
