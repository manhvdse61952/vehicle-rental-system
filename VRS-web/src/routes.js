import React from 'react';
import Loadable from 'react-loadable';

import DefaultLayout from './containers/DefaultLayout/DefaultLayout';
function Loading() {
    return <div>Loading...</div>;
}

const Map = Loadable({
    loader: () => import('./routes/Map/Map'),
    loading: Loading
});

const TestRouter = Loadable({
    loader: () => import('./components/TestRouter/TestRouter'),
    loading: Loading
});
const ApproveEmployee = Loadable({
    loader: () => import('./routes/Employee/ApproveEmployee'),
    loading: Loading
});

const routes = [
    { path: '/home', exact: true, name: 'Home', component: DefaultLayout },
    { path: '/home/map', name: 'Map', component: Map },
    {
        path: '/home/approve-employee',
        name: 'Approve Employee',
        component: ApproveEmployee
    },
    {
        path: '/home/testrouter',
        exact: true,
        name: 'Test Router',
        component: TestRouter
    }
];

export default routes;
