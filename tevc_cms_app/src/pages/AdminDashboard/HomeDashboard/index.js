import styles from './HomeDashboard.module.scss';
import React from 'react';
import {
    ArrowDownOutlined,
    ArrowUpOutlined,
} from '@ant-design/icons';
import {
    Col,
    Row,
    Statistic,
    Card,
    Space,
    
} from 'antd';

const { Countdown } = Statistic;

const cx = (classNames) => styles[classNames];

function HomeDashboard() {
    const deadline = Date.now() + 6000 * 72 * 43 * 24 * 2 + 1000 * 30;
    const onFinish = () => {
        console.log('finished!');
    };

    return (
        <div className={cx('dashboard-wrapper')}>
            <div className={cx('comment-contailer')}>
                <Row gutter={16} style={{ marginBottom: '24px' }}>
                    <Col span={8}>
                        <Card variant="borderless">
                            <Statistic
                                title="Tổng số người dùng"
                                value={11.28}
                                precision={2}
                                valueStyle={{ color: '#3f8600' }}
                                prefix={<ArrowUpOutlined />}
                                suffix="%"
                            />
                        </Card>
                    </Col>
                    <Col span={8}>
                        <Card variant="borderless">
                            <Statistic
                                title="Lượt xem phim hôm nay"
                                value={9.3}
                                precision={2}
                                valueStyle={{ color: '#cf1322' }}
                                prefix={<ArrowDownOutlined />}
                                suffix="%"
                            />
                        </Card>
                    </Col>
                    <Col span={8}>
                        <Card variant="borderless">
                            <Countdown
                                title="Thời gian còn lại cho sự kiện"
                                value={deadline}
                                onFinish={onFinish}
                            />
                        </Card>
                    </Col>
                </Row>
            </div>
        </div>
    );
}

export default HomeDashboard;
