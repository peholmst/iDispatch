import React from 'react';

export default class DeviceManagement extends React.Component {
    render() {
        return (
            <div className="view">
                <div className="toolbar">
                    <h1>Device Management</h1>
                    <div class="group">
                        <button className="success">New Device</button>
                        <input type="text" placeholder="Search" />
                    </div>
                </div>
                <div className="table-container">
                    <table className="view-table">
                        <thead>
                            <tr>
                                <th>Device ID</th>
                                <th>Type</th>
                                <th>Description</th>                            
                                <th>Assigned to hardware</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>cf38as92</td>
                                <td>Runboard</td>
                                <td>Brandkårshuset, nedre våningen</td>
                            </tr>
                            <tr>
                                <td>jf10ls92</td>
                                <td>Mobile</td>
                                <td>PG31</td>
                            </tr>
                            <tr>
                                <td>ty91xs12</td>
                                <td>Mobile</td>
                                <td>NA11</td>
                            </tr>
                            <tr>
                                <td>uw91ux83</td>
                                <td>Dispatcher</td>
                                <td>Dispatcher 1</td>
                            </tr>
                            <tr>
                                <td>da62tx45</td>
                                <td>Commander</td>
                                <td>ÖvningP3</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        );
    }
}