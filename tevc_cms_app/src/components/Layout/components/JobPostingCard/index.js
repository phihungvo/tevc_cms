import React from 'react';
import {Card, Tag, message} from 'antd';
import {
    EnvironmentOutlined,
    DollarOutlined,
    CalendarOutlined,
    TeamOutlined,
    EyeOutlined,
    EditOutlined, UserOutlined,
} from '@ant-design/icons';
import classNames from 'classnames/bind';
import styles from './JobPostingCard.module.scss';
import moment from 'moment';
import SmartButton from '~/components/Layout/components/SmartButton';

const cx = classNames.bind(styles);

const JobPostingCard = ({job, onView, onEdit, onApplicantClick }) => {
    const getStatusTag = (status) => {
        const config = {
            OPEN: {color: 'green', text: 'Đang mở'},
            CLOSED: {color: 'red', text: 'Đã đóng'},
            DRAFT: {color: 'blue', text: 'Nháp'},
        };
        const s = config[status] || {color: 'default', text: status};
        return (
            <Tag color={s.color} className={cx('status-tag')}>
                {s.text}
            </Tag>
        );
    };

    const handleView = () => onView && onView(job);
    const handleEdit = () => onEdit && onEdit(job);
    const handleApplicantClick = () => {
        if (job.applicantCount > 0) {
            onApplicantClick && onApplicantClick(job.id);
        } else {
            message.info('Vị trí này chưa có ứng viên nào ứng tuyển.');
        }
    };

    return (
        <Card className={cx('card')} hoverable>
            <div className={cx('card-header')}>
                <h3 className={cx('job-title')}>{job.title}</h3>
                {getStatusTag(job.jobPostingStatus)}
            </div>

            <div className={cx('card-body')}>
                <div className={cx('info-row')}>
                    <EnvironmentOutlined className={cx('icon')}/>
                    <span>{job.location}</span>
                </div>
                <div className={cx('info-row')}>
                    <DollarOutlined className={cx('icon')}/>
                    <span>{job.salaryRange}</span>
                </div>
                <div className={cx('info-row')}>
                    <CalendarOutlined className={cx('icon')}/>
                    <span>
                        Đăng: {moment(job.postingDate).format('YYYY-MM-DD')} - Hết hạn:{' '}
                        {moment(job.closingDate).format('YYYY-MM-DD')}
                    </span>
                </div>
                <div className={cx('info-row')} style={{ cursor: 'pointer' }} onClick={handleApplicantClick}>
                    <UserOutlined className={cx('icon')} style={{ color: '#1890ff' }} />
                    <span style={{ color: '#1890ff', fontWeight: 'bold' }}>
                        {job.applicantCount} ứng viên
                    </span>
                </div>
            </div>

            <div className={cx('card-footer')}>
                <SmartButton
                    type="default"
                    icon={<EyeOutlined/>}
                    onClick={handleView}
                >
                    Xem chi tiết
                </SmartButton>
                <SmartButton
                    type="primary"
                    icon={<EditOutlined/>}
                    onClick={handleEdit}
                >
                    Chỉnh sửa
                </SmartButton>
            </div>
        </Card>
    );
};

export default JobPostingCard;