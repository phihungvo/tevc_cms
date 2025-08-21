import React from 'react';
import { TeamOutlined } from '@ant-design/icons';

const sidebarItems = [
    {
        key: 'dashboard',
        icon: <TeamOutlined />,
        label: 'Dashboard',
        path: '/admin-dashboard',
    },
    {
        key: 'attendance',
        icon: <TeamOutlined />,
        label: 'Attendance',
        path: '/admin/attendance',
    },
];

export default sidebarItems;