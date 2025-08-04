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
        color: '#40c4ff', // Xanh dương sáng
        icon: <HomeOutlined />,
        url: '/admin/dashboard',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'User',
        color: '#90a4ae', // Xám xanh nhạt
        icon: <TeamOutlined />,
        url: '/admin/user',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Role',
        color: '#90a4ae', // Xám xanh nhạt
        icon: <KeyOutlined />,
        url: '/admin/role',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Permission',
        color: '#90a4ae', // Xám xanh nhạt
        icon: <LockOutlined />,
        url: '/admin/permission',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Employee',
        color: '#ab47bc', // Tím nhạt
        icon: <UsergroupAddOutlined />,
        url: '/admin/employee',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Department',
        color: '#ec407a', // Tím nhạt
        icon: <ApartmentOutlined />,
        url: '/admin/department',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Position',
        color: '#7e57c2', // Tím nhạt
        icon: <SolutionOutlined />,
        url: '/admin/position',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Leave',
        color: '#ff7043', // Tím nhạt
        icon: <CoffeeOutlined />, // Icon nghỉ phép
        url: '/admin/leave',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Attendance',
        color: '#26a69a', // Tím nhạt
        icon: <ClockCircleOutlined />, // Icon đồng hồ cho chấm công
        url: '/admin/attendance',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Performance',
        color: '#ffa726', // Tím nhạt
        icon: <BarChartOutlined />, // Icon biểu đồ cho hiệu suất
        url: '/admin/performance',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Training',
        color: '#66bb6a', // Tím nhạt
        icon: <BookOutlined />, // Icon sách cho đào tạo
        url: '/admin/training',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Project',
        color: '#ef5350', // Tím nhạt
        icon: <ProjectOutlined />, // Icon dự án
        url: '/admin/project',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'TimeSheet',
        color: '#29b6f6', // Tím nhạt
        icon: <CalendarOutlined />, // Icon lịch cho bảng chấm công
        url: '/admin/timesheet',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'PayRoll',
        color: '#d4a017', // Tím nhạt
        icon: <DollarOutlined />, // Icon tiền lương
        url: '/admin/payroll',
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE'],
        roleIcon: <SafetyOutlined />,
        permissionIcon: <LockOutlined />
    },
    {
        title: 'Notifications',
        color: '#ff4d4f', // Đỏ nhạt
        icon: <BellOutlined />,
        role: 'USER',
        permissions: ['USER:READ']
    },
    {
        title: 'Setting',
        color: '#3f51b5', // Xanh tím đậm
        icon: <SettingFilled />,
        role: 'ADMIN',
        permissions: ['ADMIN:MANAGE']
    },
    {
        title: 'Store',
        color: '#ffab00', // Vàng cam
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