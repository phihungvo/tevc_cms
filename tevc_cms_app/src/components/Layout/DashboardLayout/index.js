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
    UsergroupAddOutlined,
    ApartmentOutlined, 
    SolutionOutlined,
    ClockCircleOutlined,
    BarChartOutlined, 
    BookOutlined, 
    ProjectOutlined, 
    CalendarOutlined, 
    DollarOutlined, 
    CoffeeOutlined, 
} from '@ant-design/icons';

const cx = classNames.bind(styles);


const sideBar = [
    {
        title: 'Dashboard',
        color: '#40c4ff', 
        icon: <HomeOutlined />,
        url: '/admin/dashboard',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'User Management',
        color: '#1890ff', 
        icon: <TeamOutlined />,
        url: '/admin/user-management',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Employee',
        color: '#ab47bc', 
        icon: <UsergroupAddOutlined />,
        url: '/admin/employee',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Department',
        color: '#ec407a',
        icon: <ApartmentOutlined />,
        url: '/admin/department',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Position',
        color: '#7e57c2', 
        icon: <SolutionOutlined />,
        url: '/admin/position',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Leave',
        color: '#ff7043', 
        icon: <CoffeeOutlined />, 
        url: '/admin/leave',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'PayRoll',
        color: '#d4a017', 
        icon: <DollarOutlined />, 
        url: '/admin/payroll',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Team',
        color: '#0baedb',
        icon: <TeamOutlined />,
        url: '/admin/team',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Attendance',
        color: '#26a69a',
        icon: <ClockCircleOutlined />, 
        url: '/admin/attendance',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Performance',
        color: '#ffa726', 
        icon: <BarChartOutlined />, 
        url: '/admin/performance',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Training',
        color: '#66bb6a', 
        icon: <BookOutlined />,
        url: '/admin/training',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Project',
        color: '#ef5350',
        icon: <ProjectOutlined />, 
        url: '/admin/project',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'TimeSheet',
        color: '#29b6f6',
        icon: <CalendarOutlined />, 
        url: '/admin/timesheet',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />,
    },
    {
        title: 'Notifications',
        color: '#ff4d4f', 
        icon: <BellOutlined />,
        role: 'USER',
        permissions: ['USER:READ'],
    },
    {
        title: 'Setting',
        color: '#3f51b5', 
        icon: <SettingFilled />,
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
    },
    {
        title: 'Store',
        color: '#ffab00',
        icon: <ShopOutlined />,
        role: 'USER',
        permissions: ['USER:READ'],
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