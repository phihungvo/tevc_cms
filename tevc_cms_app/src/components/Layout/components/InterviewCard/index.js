import React from 'react';
import { Card, Tag, Button } from 'antd';
import {
    CalendarOutlined,
    EnvironmentOutlined,
    TeamOutlined,
    CheckCircleOutlined,
    CloseCircleOutlined,
    EditOutlined,
} from '@ant-design/icons';
import classNames from 'classnames/bind';
import styles from './InterviewCard.module.scss';
import moment from 'moment';

const cx = classNames.bind(styles);

const InterviewCard = ({ interview, onEdit, onComplete, onCancel }) => {
    const getStatusTag = (status) => {
        const config = {
            COMPLETED: {
                color: 'green',
                text: 'Hoàn thành',
                icon: <CheckCircleOutlined />
            },
            CANCELLED: {
                color: 'red',
                text: 'Hủy',
                icon: <CloseCircleOutlined />
            },
            SCHEDULED: {
                color: 'blue',
                text: 'Đã lên lịch',
                icon: <CalendarOutlined />
            },
        };
        const s = config[status] || { color: 'default', text: status, icon: null };
        return (
            <Tag color={s.color} className={cx('status-tag')} icon={s.icon}>
                {s.text}
            </Tag>
        );
    };

    const getTypeTag = (type) => {
        return (
            <Tag color="purple" className={cx('type-tag')}>
                {type}
            </Tag>
        );
    };

    const handleEdit = () => onEdit && onEdit(interview);
    const handleComplete = () => onComplete && onComplete(interview);
    const handleCancel = () => onCancel && onCancel(interview);

    return (
        <Card className={cx('interview-card')} hoverable>
            <div className={cx('card-content')}>
                {/* Left Section - Avatar/Icon */}
                <div className={cx('avatar-section')}>
                    <div className={cx('avatar-icon')}>
                        <CalendarOutlined />
                    </div>
                </div>

                {/* Main Info Section */}
                <div className={cx('info-section')}>
                    <div className={cx('candidate-info')}>
                        <h3 className={cx('candidate-name')}>{interview.candidateName}</h3>
                        <span className={cx('position')}>{interview.position}</span>
                    </div>

                    <div className={cx('details-row')}>
                        <div className={cx('detail-item')}>
                            <CalendarOutlined className={cx('icon')} />
                            <span>{moment(interview.datetime).format('DD/MM/YYYY - HH:mm')}</span>
                        </div>

                        <div className={cx('detail-item')}>
                            <EnvironmentOutlined className={cx('icon')} />
                            <span>{interview.location}</span>
                        </div>

                        <div className={cx('detail-item')}>
                            <TeamOutlined className={cx('icon')} />
                            <span>{interview.interviewerName}</span>
                        </div>

                        <div className={cx('detail-item')}>
                            {getTypeTag(interview.type)}
                            <span>Thời lượng: {interview.durationMinutes} phút</span>
                        </div>
                    </div>
                </div>

                {/* Right Section - Status and Actions */}
                <div className={cx('action-section')}>
                    <div className={cx('status-wrapper')}>
                        {getStatusTag(interview.status)}
                    </div>

                    <div className={cx('button-group')}>
                        {interview.status === 'COMPLETED' && (
                            <Button
                                type="primary"
                                icon={<CheckCircleOutlined />}
                                className={cx('btn-complete')}
                            >
                                Hoàn thành
                            </Button>
                        )}
                        {interview.status === 'CANCELLED' && (
                            <Button
                                danger
                                icon={<CloseCircleOutlined />}
                                className={cx('btn-cancel')}
                            >
                                Hủy
                            </Button>
                        )}
                        {interview.status === 'SCHEDULED' && (
                            <>
                                <Button
                                    type="primary"
                                    icon={<CheckCircleOutlined />}
                                    onClick={handleComplete}
                                    className={cx('btn-complete')}
                                >
                                    Hoàn thành
                                </Button>
                                <Button
                                    danger
                                    icon={<CloseCircleOutlined />}
                                    onClick={handleCancel}
                                    className={cx('btn-cancel')}
                                >
                                    Hủy
                                </Button>
                            </>
                        )}
                        <Button
                            icon={<EditOutlined />}
                            onClick={handleEdit}
                            className={cx('btn-edit')}
                        >
                            Sửa
                        </Button>
                    </div>
                </div>
            </div>
        </Card>
    );
};

export default InterviewCard;