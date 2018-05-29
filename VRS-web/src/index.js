import 'bootstrap/dist/css/bootstrap.min.css';
import '@coreui/coreui';
import 'perfect-scrollbar';
import './polyfill';

import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Switch, Redirect } from 'react-router-dom';
import { createStore, applyMiddleware, compose } from 'redux';
import createSagaMiddleware from 'redux-saga';
import { Provider } from 'react-redux';
import { reactReduxFirebase } from 'react-redux-firebase';
import firebase from 'firebase';

import './index.css';
import DefaultLayout from './containers/DefaultLayout/DefaultLayout';
import registerServiceWorker from './registerServiceWorker';

import './App.css';
// Styles
// CoreUI Icons Set
import '@coreui/icons/css/coreui-icons.min.css';
// Import Flag Icons Set
import 'flag-icon-css/css/flag-icon.min.css';
// Import Font Awesome Icons Set
import 'font-awesome/css/font-awesome.min.css';
// Import Simple Line Icons Set
import 'simple-line-icons/css/simple-line-icons.css';
// Import Main styles for this application
import './scss/style.css';
import 'react-virtualized/styles.css';

import Login from './routes/Login/Login';

import IndexReducer from './store/reducers';
import IndexSaga from './sagas/sagas';
import SignUp from './routes/Login/SignUp';

// create the saga middleware
const sagaMiddleware = createSagaMiddleware();

// dev tools middleware
const composeSetup =
    process.env.NODE_ENV !== 'production' &&
    typeof window === 'object' &&
    window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
        ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__
        : compose;

var firebaseConfig = {
    apiKey: 'AIzaSyB7OP_zZXcXstX_sMbeT5tDWTa5w8B-N7M',
    authDomain: 'rentcar-204101.firebaseapp.com',
    databaseURL: 'https://rentcar-204101.firebaseio.com',
    projectId: 'rentcar-204101',
    storageBucket: 'rentcar-204101.appspot.com',
    messagingSenderId: '859145769685'
};

firebase.initializeApp(firebaseConfig);

// create a redux store with our reducer above and middleware
const store = createStore(
    IndexReducer,
    {},
    composeSetup(
        reactReduxFirebase(firebase, { userProfile: 'users' }),
        applyMiddleware(sagaMiddleware)
    ) // allows redux devtools to watch sagas
);

// run the saga
sagaMiddleware.run(IndexSaga);

export const isAuthenticated = () => store.getState().loginReducer.loginSuccess;

export const PrivateRoute = ({ component: Component, ...rest }) => (
    <Route
        {...rest}
        render={props =>
            isAuthenticated() ? (
                <Component {...props} />
            ) : (
                <Redirect
                    to={{ pathname: '/Login', state: { from: props.location } }}
                    {...props}
                />
            )
        }
    />
);

ReactDOM.render(
    <Provider store={store}>
        <BrowserRouter>
            <Switch>
                <Route
                    exact
                    path="/"
                    render={props =>
                        isAuthenticated() ? (
                            <Redirect to="/home" />
                        ) : (
                            <Login {...props} />
                        )
                    }
                />

                <PrivateRoute path="/home" component={DefaultLayout} />
                <Route path="/Login" component={Login} />
                <Route path="/SignUp" component={SignUp} />
                <Redirect from="*" to="/" />
                {/* <Route path="/App" component={App}></Route> */}
            </Switch>
        </BrowserRouter>
    </Provider>,
    document.getElementById('root')
);
registerServiceWorker();
