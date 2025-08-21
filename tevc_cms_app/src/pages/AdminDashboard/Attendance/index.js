import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from './Attendance.module.scss';
import SmartTable from '~/components/Layout/components/SmartTable';
import SmartButton from '~/components/Layout/components/SmartButton';
import { getAllAttendancesWithPagination, deleteAttendance } from '~/service/admin/attendance';
import { message, Tag } from 'antd';
import { EditOutlined, DeleteOutlined } from '@ant-design/icons';

const cx = classNames.bind(styles);

function Attendance() {
    const [attendanceSource, setAttendanceSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });

    const columns = [
        {
            title: 'Employee ID',
            dataIndex: 'employeeId',
            key: 'employeeId',
        },
        {
            title: 'Check In',
            dataIndex: 'checkIn',
            key: 'checkIn',
            render: (date) => (date ? new Date(date).toLocaleString() : 'N/A'),
        },
        {
            title: 'Check Out',
            dataIndex: 'checkOut',
            key: 'checkOut',
            render: (date) => (date ? new Date(date).toLocaleString() : 'N/A'),
        },
        {
            title: 'Status',
            dataIndex: 'status',
            key: 'status',
            render: (status) => <Tag>{status}</Tag>,
        },
        {
            title: 'Notes',
            dataIndex: 'notes',
            key: 'notes',
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (_, record) => (
                <>
                    <SmartButton
                        title="Edit"
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={80}
                        onClick={() => handleEditAttendance(record)}
                    />
                    <SmartButton
                        title="Delete"
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={80}
                        onClick={() => handleDeleteAttendance(record.id)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    useEffect(() => {
        fetchAttendances();
    }, []);

    const fetchAttendances = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllAttendancesWithPagination(page - 1, pageSize);
            setAttendanceSource(response.content || []);
            setPagination({
                current: page,
                pageSize: pageSize,
                total: response.totalElements,
            });
        } catch (error) {
            message.error('Failed to fetch attendances');
        } finally {
            setLoading(false);
        }
    };

    const handleDeleteAttendance = async (id) => {
        try {
            await deleteAttendance(id);
            message.success('Attendance deleted successfully');
            fetchAttendances(pagination.current, pagination.pageSize);
        } catch (error) {
            message.error('Failed to delete attendance');
        }
    };

    const handleEditAttendance = (record) => {
        // Implement edit functionality here
    };

    const handleTableChange = (pagination) => {
        fetchAttendances(pagination.current, pagination.pageSize);
    };

    return (
        <div className={cx('attendance-wrapper')}>
            <div className={cx('attendance-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={attendanceSource}
                    loading={loading}
                    pagination={pagination}
                    onTableChange={handleTableChange}
                />
            </div>
        </div>
    );
}

export default Attendance;
