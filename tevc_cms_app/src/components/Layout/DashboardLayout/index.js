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
} from '@ant-design/icons';

const cx = classNames.bind(styles);

const sideBar = [
    {
        title: 'Dashboard',
        color: '#1890ff',
        icon: <HomeOutlined />,
        url: '/admin/dashboard',
    },
    {
        title: 'User',
        color: '#8c8c8c',
        icon: <ProfileOutlined />,
        url: '/admin/user',
    },
    {
        title: 'Notifications',
        color: '#f5222d',
        icon: <BellOutlined />,
    },
    {
        title: 'Setting',
        color: '#2f54eb',
        icon: <SettingFilled />,
    },
    {
        title: 'Store',
        color: '#faad14',
        icon: <ShopOutlined />,
    },
];

function DashboardLayout({ children, pageTitle }) {
    
    const title = pageTitle || 'Dashboard';

    return (
        <div className={cx('wrapper')}>
            <Sidebar hiddenLogo={true} dataSource={sideBar} />
            <div className={cx('right-container')}>
                <Header title={title} />
                <div className={cx('content')}>{children}</div>
            </div>
        </div>
    );
}

export default DashboardLayout;
