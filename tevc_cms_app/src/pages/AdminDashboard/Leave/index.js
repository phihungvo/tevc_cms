import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Leave/Leave.module.scss';
import {useState, useEffect} from 'react';
import moment from 'moment';
import SmartTable from '~/components/Layout/components/SmartTable';
import {
    SearchOutlined,
    PlusOutlined,
    FilterOutlined,
    CloudUploadOutlined,
    EditOutlined,
    DeleteOutlined,
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import {Form, message, Tag} from 'antd';
import {getAllLeaves, createLeave, updateLeave, deleteLeave} from '~/service/admin/leave';
import {getAllEmployees} from '~/service/admin/employee';

const cx = classNames.bind(styles);

function Leave() {
    const [leaveSource, setLeaveSource] = useState([]);
    const [employeeSource, setEmployeeSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedLeave, setSelectedLeave] = useState(null);
    const [form] = Form.useForm();

    const leaveTypeStyles = {
        ANNUAL: {color: 'green', label: 'Annual'},
        SICK: {color: 'volcano', label: 'Sick'},
        UNPAID: {color: 'gold', label: 'Unpaid'},
        MATERNITY: {color: 'pink', label: 'Maternity'},
        PATERNTIY: {color: 'blue', label: 'Paternity'},
        BEREAVEMENT: {color: 'purple', label: 'Bereavement'},
        COMPASSIONATE: {color: 'cyan', label: 'Compassionate'},
    };

    const statusStyles = {
        PENDING: {color: 'orange', label: 'Pending'},
        APPROVED: {color: 'green', label: 'Approved'},
        REJECTED: {color: 'red', label: 'Rejected'},
    };

    const columns = [
        {
            title: 'Employee Name',
            dataIndex: 'employeeName',
            key: 'employeeName',
            width: 150,
        },
        {
            title: 'Thời gian nghĩ phép',
            dataIndex: 'leaveDuration',
            key: 'leaveDuration',
            width: 180,
            render: (_, record) => {
                const start = record.startDate ? new Date(record.startDate).toLocaleDateString('vi-VN') : 'N/A';
                const end = record.endDate ? new Date(record.endDate).toLocaleDateString('vi-VN') : 'N/A';
                return `${start} ⇒ ${end}`;
            }
        },
        {
            title: 'Type',
            dataIndex: 'leaveType',
            key: 'leaveType',
            width: 100,
            render: (leaveType) => {
                const style = leaveTypeStyles[leaveType] || {color: 'default', label: leaveType || 'N/A'};
                return <Tag color={style.color}>{style.label}</Tag>;
            },
        },
        {
            title: 'Reason',
            dataIndex: 'reason',
            key: 'reason',
        },
        {
            title: 'Status',
            dataIndex: 'leaveStatus',
            key: 'leaveStatus',
            render: (status) => {
                const style = statusStyles[status] || {color: 'default', label: status || 'N/A'};
                return <Tag color={style.color}>{style.label}</Tag>;
            },
        },
        {
            title: 'Approver Comments',
            dataIndex: 'approverComments',
            key: 'approverComments',
        },
        {
            title: 'Actions',
            fixed: 'right',
            width: 130,
            render: (_, record) => (
                <>
                    <SmartButton
                        type="primary"
                        icon={<EditOutlined/>}
                        buttonWidth={50}
                        onClick={() => handleEditLeave(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined/>}
                        buttonWidth={50}
                        onClick={() => handleDeleteLeave(record)}
                        style={{marginLeft: '8px'}}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'Employee Name',
            name: 'employeeId',
            type: 'select',
            options: employeeSource,
            rules: [{required: true, message: 'Employee is required!'}],
        },
        ...(modalMode === 'edit'
            ? [{
                label: 'Leave Status',
                name: 'leaveStatus',
                type: 'select',
                options: ['PENDING', 'APPROVED', 'REJECTED'],
            }]
            : []),
        {
            label: 'Leave Type',
            name: 'leaveType',
            type: 'select',
            options: [
                'ANNUAL',
                'SICK',
                'UNPAID',
                'MATERNITY',
                'PATERNITY',
                'BEREAVEMENT',
                'COMPASSIONATE',
            ],
        },
        {
            label: 'Start Date',
            name: 'startDate',
            type: 'date',
        },
        {
            label: 'End Date',
            name: 'endDate',
            type: 'date',
        },
        {
            label: 'Reason',
            name: 'reason',
            type: 'text',
        },
        {
            label: 'Approver Comments',
            name: 'approverComments',
            type: 'text',
        },
    ];

    useEffect(() => {
        handleGetAllEmployees();
        handleGetAllLeaves();
    }, []);

    const handleGetAllEmployees = async (page = 1, pageSize = 10) => {
        try {
            const response = await getAllEmployees({page: page - 1, pageSize});

            if (!response || !Array.isArray(response.content)) {
                throw new Error('Invalid response: employees data is missing or not an array');
            }

            const employeesData = response.content.map(employee => ({
                label: `${employee.firstName} ${employee.lastName}`.trim(),
                value: employee.id,
            }));

            setEmployeeSource(employeesData);

        } catch (error) {
            return {success: false, error: error.message};
        }
    };

    const handleGetAllLeaves = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllLeaves({page: page - 1, pageSize});
            if (response && Array.isArray(response.content)) {
                const mappedLeaves = response.content.map((leave) => ({
                    ...leave,
                }));
                setLeaveSource(mappedLeaves);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                setLeaveSource([]);
            }
        } catch (error) {
            setLeaveSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddRole = () => {
        setModalMode('create');
        setSelectedLeave(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreatePermission = async (formData) => {
        try {
            await createLeave(formData);
            handleGetAllLeaves();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error creating leave: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditLeave = (record) => {
        setSelectedLeave(record);
        setModalMode('edit');

        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdateLeave = async (formData) => {
        await updateLeave(selectedLeave.id, formData);
        handleGetAllLeaves();
        setIsModalOpen(false);
    }

    const handleDeleteLeave = async (record) => {
        setModalMode('delete');
        setSelectedLeave(record.id);
        setIsModalOpen(true);
    }

    const handleCallDeleteLeave = async () => {
        await deleteLeave(selectedLeave);
        handleGetAllLeaves();
        setIsModalOpen(false);
    }

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreatePermission(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateLeave(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteLeave();
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllLeaves(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Add New Leave';
            case 'edit':
                return 'Edit Leave';
            case 'delete':
                return 'Delete Leave';
            default:
                return 'Leave Details';
        }
    };

    return (
        <div className={cx('trailer-wrapper')}>
            <div className={cx('sub_header')}>
                <SmartInput
                    size="large"
                    placeholder="Search"
                    icon={<SearchOutlined/>}
                />
                <div className={cx('features')}>
                    <SmartButton
                        title="Add new"
                        icon={<PlusOutlined/>}
                        type="primary"
                        onClick={handleAddRole}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined/>}/>
                    <SmartButton title="Excel" icon={<CloudUploadOutlined/>}/>
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={leaveSource}
                    loading={loading}
                    pagination={pagination}
                    onTableChange={handleTableChange}
                />
            </div>

            <PopupModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                title={getModalTitle()}
                fields={modalMode === 'delete' ? [] : userModalFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedLeave}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default Leave;