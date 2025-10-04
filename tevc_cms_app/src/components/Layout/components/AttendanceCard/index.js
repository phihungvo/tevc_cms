import React from 'react';
import classNames from 'classnames/bind';
import cardStyles from './AttendanceCard.module.scss';
import { Card, Avatar, Space, Tag } from 'antd';
import {
    ClockCircleOutlined,
    CheckCircleOutlined,
    CloseCircleOutlined,
    CalendarOutlined,
    UserOutlined,
    FieldTimeOutlined,
    EyeOutlined,
    EditOutlined,
    DeleteOutlined,
} from '@ant-design/icons';
import moment from 'moment';
import SmartButton from '~/components/Layout/components/SmartButton';

const cx = classNames.bind(cardStyles);

const AttendanceCard = ({ attendance, onView, onEdit, onDelete }) => {
    const getStatusTag = (status) => {
        const statusConfig = {
            PRESENT: { color: 'success', text: 'Có mặt', icon: <CheckCircleOutlined /> },
            ABSENT: { color: 'error', text: 'Vắng mặt', icon: <CloseCircleOutlined /> },
            LATE: { color: 'warning', text: 'Đi muộn', icon: <ClockCircleOutlined /> },
        };
        const config = statusConfig[status] || statusConfig.PRESENT;
        return (
            <Tag className={cx('status-tag', `status-${status?.toLowerCase()}`)} icon={config.icon}>
                {config.text}
            </Tag>
        );
    };

    const getInitials = (firstName, lastName) => {
        return `${firstName?.charAt(0) || ''}${lastName?.charAt(0) || ''}`.toUpperCase();
    };

    const calculateWorkHours = (checkIn, checkOut) => {
        if (!checkIn || !checkOut) return 'N/A';
        const start = moment(checkIn, 'HH:mm:ss');
        const end = moment(checkOut, 'HH:mm:ss');
        const duration = moment.duration(end.diff(start));
        const hours = Math.floor(duration.asHours());
        const minutes = duration.minutes();
        return `${hours}h ${minutes}m`;
    };

    return (
        <Card
            hoverable
            className={cx('card')}
        >
            <div className={cx('card-content')}>
                <Avatar
                    size={56}
                    className={cx('avatar')}
                    icon={<UserOutlined />}
                >
                    {getInitials(attendance.employeeName)}
                </Avatar>

                <div className={cx('info-section')}>
                    <div className={cx('employee-header')}>
                        <h3 className={cx('employee-name')}>
                            {attendance.employeeName}
                        </h3>
                        <span className={cx('employee-code')}>
                            {attendance.employeeCode}
                        </span>
                    </div>

                    <Space direction="vertical" size={12} className={cx('details-space')}>
                        <div className={cx('date-row')}>
                            <CalendarOutlined className={cx('icon', 'date-icon')} />
                            <span className={cx('date-text')}>
                                {moment(attendance.attendanceDate).format('DD/MM/YYYY - dddd')}
                            </span>
                        </div>

                        <div className={cx('time-row')}>
                            <div className={cx('time-item', 'check-in')}>
                                <ClockCircleOutlined className={cx('icon')} />
                                <span className={cx('time-label')}>Vào: <strong>{attendance.checkIn ? moment(attendance.checkIn).format('HH:mm') : 'N/A'}</strong></span>
                            </div>
                            <div className={cx('time-item', 'check-out')}>
                                <ClockCircleOutlined className={cx('icon')} />
                                <span className={cx('time-label')}>Ra: <strong>{attendance.checkOut ? moment(attendance.checkOut).format('HH:mm') : 'N/A'}</strong></span>
                            </div>
                        </div>

                        <div className={cx('work-hours-row')}>
                            <FieldTimeOutlined className={cx('icon')} />
                            <span className={cx('work-hours-label')}>Giờ công: <strong>{attendance.workHours}</strong></span>
                        </div>

                        {attendance.notes && (
                            <div className={cx('notes-section')}>
                                📝 {attendance.notes}
                            </div>
                        )}
                    </Space>

                    <div className={cx('status-section')}>
                        {getStatusTag(attendance.status)}
                    </div>
                </div>
            </div>

            <div className={cx('action-section')}>
                <SmartButton
                    type="default"
                    icon={<EyeOutlined />}
                    buttonWidth={40}
                    onClick={() => onView(attendance)}
                />
                <SmartButton
                    type="primary"
                    icon={<EditOutlined />}
                    buttonWidth={40}
                    onClick={() => onEdit(attendance)}
                />
                <SmartButton
                    type="danger"
                    icon={<DeleteOutlined />}
                    buttonWidth={40}
                    onClick={() => onDelete(attendance)}
                />
            </div>
        </Card>
    );
};

export default AttendanceCard;