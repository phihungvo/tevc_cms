import classNames from 'classnames/bind';
import styles from './Sidebar.module.scss';
import logo from '../../../../assets/images/logo.png';
import NavItem from '../NavItem';

const cx = classNames.bind(styles);

function Sidebar({ hiddenLogo = false, dataSource }) {
    return (
        <div className={cx('side-bar')}>
            <div className={cx('sidebar-inner')}>
                {hiddenLogo && (
                    <div className={cx('sidebar-logo')}>
                        <div className={cx('logo')}>
                            <img src={logo} alt="logo" />
                        </div>
                        <div className={cx('logo-text')}>Adminator</div>
                    </div>
                )}

                <div className={cx('sidebar-menu')}>
                    {dataSource.map((item, index) => (
                        <NavItem
                            key={index}
                            title={item.title}
                            color={item.color}
                            icon={item.icon}
                            children={item.children}
                            url={item.url}
                        />
                    ))}
                </div>
            </div>
        </div>
    );
}

export default Sidebar;
