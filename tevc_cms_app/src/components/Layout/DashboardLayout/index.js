import { useState } from 'react';
import classNames from 'classnames/bind';
import styles from './DashboardLayout.module.scss';
import Sidebar from '../components/Sidebar';
import Header from '../Header';
import {
    HomeOutlined,
    ProfileOutlined,
    BellOutlined,
    ShopOutlined,
    SettingFilled,
    SafetyOutlined,
    LockOutlined,
    TeamOutlined,
    KeyOutlined,
} from '@ant-design/icons';

const cx = classNames.bind(styles);

const sideBar = [
    {
        title: 'Dashboard',
        color: '#1890ff',
        icon: <HomeOutlined />,
        url: '/admin/dashboard',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'User',
        color: '#8c8c8c',
        icon: <ProfileOutlined />,
        url: '/admin/user',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Role',
        color: '#8c8c8c',
        icon: <TeamOutlined />,
        url: '/admin/role',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Permission',
        color: '#8c8c8c',
        icon: <KeyOutlined />,
        url: '/admin/permission',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Notifications',
        color: '#f5222d',
        icon: <BellOutlined />,
        role: 'USER',
        permissions: ['USER:READ']
    },
    {
        title: 'Setting',
        color: '#2f54eb',
        icon: <SettingFilled />,
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE']
    },
    {
        title: 'Store',
        color: '#faad14',
        icon: <ShopOutlined />,
        role: 'USER',
        permissions: ['USER:READ']
    },
];

function DashboardLayout({ children, pageTitle }) {
    const [collapsed, setCollapsed] = useState(false);
    const title = pageTitle || 'Dashboard';

    const toggleCollapsed = () => {
        setCollapsed(!collapsed);
    };

    return (
        <div className={cx('wrapper')}>
            <Sidebar 
                hiddenLogo={true} 
                dataSource={sideBar} 
                collapsed={collapsed}
                onCollapse={toggleCollapsed}
            />
            <div className={cx('right-container', { collapsed })}>
                <Header title={title} />
                <div className={cx('content')}>{children}</div>
            </div>
        </div>
    );
}

export default DashboardLayout;
