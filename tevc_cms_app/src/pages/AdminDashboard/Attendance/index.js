import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from './Attendance.module.scss';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartTable from '~/components/Layout/components/SmartTable';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import {
    SearchOutlined,
    PlusOutlined,
    FilterOutlined,
    CloudUploadOutlined,
    EditOutlined,
    DeleteOutlined,
    CheckCircleOutlined,
} from '@ant-design/icons';
import { Form, message, Tag } from 'antd';
import {
    createAttandance,
    getAllAttendancesWithPagination,
    deleteAttendance,
} from '~/service/admin/attendance';
import { getAllEmployeesNoPaging } from '~/service/admin/employee';

const cx = classNames.bind(styles);

function Attendance() {
    const [attendanceSource, setAttendanceSource] = useState([]);
    const [employeeSource, setEmployeeSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedPayroll, setSelectedPayroll] = useState(null);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Employee',
            dataIndex: 'employeeName',
            key: 'employeeName',
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

    const attendanceModalFields = [
        {
            label: 'Employee Name',
            name: 'employeeId',
            type: 'select',
            options: employeeSource,
            rules: [{ required: true, message: 'Employee is required!' }],
        },
        {
            label: 'Attendance Status',
            name: 'status',
            type: 'select',
            options: ['PRESENT', 'ABSENT', 'LATE'],
        },

        {
            label: 'Check In',
            name: 'checkIn',
            type: 'date',
        },
        {
            label: 'Check Out',
            name: 'checkOut',
            type: 'date',
        },
        {
            label: 'Notes',
            name: 'notes',
            type: 'text',
        },
    ];

    useEffect(() => {
        handleGetAllEmployees();
        fetchAttendances();
    }, []);

    const fetchAttendances = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllAttendancesWithPagination(
                page - 1,
                pageSize,
            );
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

    const handleGetAllEmployees = async () => {
        try {
            const response = await getAllEmployeesNoPaging();

            const employeesData = response.map((employee) => ({
                label: `${employee.firstName} ${employee.lastName}`.trim(),
                value: employee.id,
            }));

            setEmployeeSource(employeesData);
        } catch (error) {
            return { success: false, error: error.message };
        }
    };

    const handleAddAttendance = () => {
        setModalMode('create');
        setSelectedPayroll(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreateAttendance = async (formData) => {
        await createAttandance(formData);
        fetchAttendances();
        setIsModalOpen(false);
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

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Add New Attendances';
            case 'edit':
                return 'Edit Attendances';
            case 'delete':
                return 'Delete Attendances';
            default:
                return 'Attendances Details';
        }
    };

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreateAttendance(formData);
        } else if (modalMode === 'edit') {
            // handleCallUpdateLeave(formData);
        } else if (modalMode === 'delete') {
            // handleCallDeleteLeave();
        }
    };

    return (
        <div className={cx('attendance-wrapper')}>
            <div className={cx('sub_header')}>
                <SmartInput
                    size="large"
                    placeholder="Search"
                    icon={<SearchOutlined />}
                />
                <div className={cx('features')}>
                    <SmartButton
                        title="Add new"
                        icon={<PlusOutlined />}
                        type="primary"
                        onClick={handleAddAttendance}
                    />
                    <SmartButton
                        title="Process"
                        icon={<CheckCircleOutlined />}
                        type="primary"
                        // onClick={handleProcessPayrolls}
                        // disabled={selectedRowKeys.length === 0}
                        style={{ marginLeft: '8px' }}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
                    <SmartButton
                        title="Excel"
                        icon={<CloudUploadOutlined />}
                        // onClick={handleExportFile}
                    />
                </div>
            </div>
            <div className={cx('attendance-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={attendanceSource}
                    loading={loading}
                    pagination={pagination}
                    onTableChange={handleTableChange}
                />
            </div>

            <PopupModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                title={getModalTitle()}
                fields={modalMode === 'delete' ? [] : attendanceModalFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedPayroll}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default Attendance;
