import moment from 'moment';
export function getAccessToken() {
    return localStorage.getItem('access_token');
}

export function getExpiration() {
    return localStorage.getItem('expires_at');
}

export function isExpired() {
    const expireAt = getExpiration();
    return (new Date().getTime() > expireAt);
}

export function setAccessToken(authResult) {
    const expiresAt = moment(authResult.expirationDateTime).valueOf();
    localStorage.setItem('access_token', authResult.token);
    localStorage.setItem('expires_at', expiresAt);
}

export function removeAccessToken() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('expires_at');
}