import React from 'react';
import { Card, Avatar, Tag } from 'antd';
import {
    MailOutlined,
    PhoneOutlined,
    CalendarOutlined,
    FileTextOutlined,
    EyeOutlined,
    EditOutlined,
    DeleteOutlined,
} from '@ant-design/icons';
import moment from 'moment';
import classNames from 'classnames/bind';
import styles from './Candidate.module.scss';

const cx = classNames.bind(styles);

const CandidateCard = ({ candidate, onView, onEdit, onDelete }) => {
    const getStatusTag = (status) => {
        const statusConfig = {
            NEW: { color: 'blue', text: 'Mới' },
            SCREENING: { color: 'cyan', text: 'Sàng lọc' },
            INTERVIEW: { color: 'orange', text: 'Phỏng vấn' },
            OFFERED: { color: 'green', text: 'Đã offer' },
            HIRED: { color: 'success', text: 'Đã tuyển' },
            REJECTED: { color: 'red', text: 'Từ chối' },
        };
        const config = statusConfig[status] || statusConfig.NEW;
        return <Tag color={config.color}>{config.text}</Tag>;
    };

    const getInitials = (firstName, lastName) =>
        `${firstName?.charAt(0) || ''}${lastName?.charAt(0) || ''}`.toUpperCase();

    const jobTitles = candidate.jobTitles?.join(', ') || 'Chưa có';

    return (
        <Card hoverable className={cx('candidate-card')}>
            <div className={cx('candidate-card-container')}>
                <div className={cx('candidate-info')}>
                    <Avatar size={48} className={cx('candidate-avatar')}>
                        {getInitials(candidate.firstName, candidate.lastName)}
                    </Avatar>
                    <div className={cx('candidate-details')}>
                        <h3 className={cx('candidate-name')}>
                            {candidate.firstName} {candidate.lastName}
                        </h3>
                        <div className={cx('candidate-contact')}>
                            <span>
                                <MailOutlined /> {candidate.email}
                            </span>
                            <span>
                                <PhoneOutlined /> {candidate.phone}
                            </span>
                            <span>
                                <CalendarOutlined /> {moment(candidate.applicationDate).format('DD/MM/YYYY')}
                            </span>
                            {candidate.resumeUrl && (
                                <span>
                                    <FileTextOutlined />
                                    <a
                                        href={candidate.resumeUrl}
                                        target="_blank"
                                        rel="noopener noreferrer"
                                        className={cx('resume-link')}
                                    >
                                        Xem CV
                                    </a>
                                </span>
                            )}
                        </div>
                        <div className={cx('candidate-status')}>
                            {getStatusTag(candidate.status)}
                            <span>Ứng tuyển: {jobTitles}</span>
                        </div>
                    </div>
                </div>
                <div className={cx('candidate-actions')}>
                    <button
                        className={cx('candidate-action-btn', 'view')}
                        onClick={() => onView(candidate)}
                    >
                        <EyeOutlined style={{ fontSize: '16px' }} />
                    </button>
                    <button
                        className={cx('candidate-action-btn', 'edit')}
                        onClick={() => onEdit(candidate)}
                    >
                        <EditOutlined style={{ fontSize: '16px' }} />
                    </button>
                    <button
                        className={cx('candidate-action-btn', 'delete')}
                        onClick={() => onDelete(candidate)}
                    >
                        <DeleteOutlined style={{ fontSize: '16px' }} />
                    </button>
                </div>
            </div>
        </Card>
    );
};

export default CandidateCard;
