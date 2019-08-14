import React, { Suspense, lazy } from 'react';
import ReactDOM from 'react-dom';

import Nav from 'react-bootstrap/Nav';

import { LinkContainer } from 'react-router-bootstrap';
import { BrowserRouter, Route, Switch, NavLink } from 'react-router-dom';

import './index.css';
import '@fortawesome/fontawesome-free/css/all.css';

const DeviceManagement = lazy(() => import('./views/device-management'));
const ResourceManagement = lazy(() => import('./views/resource-management'));
const ResourceTypeManagement = lazy(() => import('./views/resource-type-management'));
const StationManagement = lazy(() => import('./views/station-management'));
const UserManagement = lazy(() => import('./views/user-management'));

class RootLayout extends React.Component {

    render() {
        return (
            <BrowserRouter>
                <div id="header-bar">
                    <div className="title">iDispatch Admin</div>
                    <label htmlFor="toggle-navigation-bar-button" className="toggle-navigation-bar-icon"><i className="fas fa-bars"></i></label>
                </div>
                <input type="checkbox" className="toggle-navigation-bar-button" id="toggle-navigation-bar-button"/>
                <div className="navigation-bar">
                    <div className="header">Access Control</div>
                    <NavLink to="/devices">Devices</NavLink>
                    <NavLink to="/users">Users</NavLink>
                    
                    <div className="header">Resources</div>
                    <NavLink to="/stations">Stations</NavLink>
                    <NavLink to="/resourceTypes">Resource Types</NavLink>
                    <NavLink to="/resources">Resources</NavLink>
                </div>
                <div id="view-container">
                    <Suspense fallback={<div>Loading...</div>}>
                        <Switch>
                            <Route path="/devices" component={DeviceManagement} />
                            <Route path="/resources" component={ResourceManagement} />
                            <Route path="/resourceTypes" component={ResourceTypeManagement} />
                            <Route path="/stations" component={StationManagement} />
                            <Route path="/users" component={UserManagement} />
                        </Switch>
                    </Suspense>
                </div>
            </BrowserRouter>
        );
    }
}

ReactDOM.render(
    <RootLayout />,
    document.getElementById('root')
);