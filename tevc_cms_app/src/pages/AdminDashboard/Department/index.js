import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Department/Department.module.scss';
import { useState, useEffect } from 'react';
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
import { Form, message, Tag } from 'antd';
import { getAllDepartments, createDepartment } from '~/service/admin/department';
import { getAllByTitle } from '~/service/admin/position';

const cx = classNames.bind(styles);

function User() {
    const [employeeSource, setDepartmentSource] = useState([]);
    const [managerPositionSource, setManagerPositionSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedRole, setSelectedRole] = useState(null);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Department Name',
            dataIndex: 'name',
            key: 'name',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Created At',
            dataIndex: 'createdAt',
            key: 'createdAt',
            render: (date) =>
                date ? new Date(date).toLocaleString('vi-VN') : 'N/A',
        },
        {
            title: 'Actions',
            fixed: 'right',
            width: 250,
            render: (_, record) => (
                <>
                    <SmartButton
                        title="Edit"
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={80}
                        onClick={() => handleEditRole(record)}
                    />
                    <SmartButton
                        title="Delete"
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={80}
                        // onClick={() => handleDeleteTrailer(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'Department Name',
            name: 'employeeCode',
            type: 'text',
            rules: [{ required: true, message: 'Employee Code is required!' }],
        },
        {
            label: 'Description',
            name: 'description',
            type: 'text',
        },
       
        {
            label: 'Manager',
            name: 'managerId',
            type: 'select',
            options: managerPositionSource,
        },
        {
            label: 'Number of Employee',
            name: 'employeeCount',
            type: 'number',
        },
    ];

    useEffect(() => {
        handleGetManagerPosition('manager');
        handleGetAllDepartments();
    }, []);

    const handleGetManagerPosition = async (title) => {
        try {
            const response = await getAllByTitle(title);
            const mappedPosition = response.map(position => ({
                value: position.id, // Giả sử id là UUID của vị trí
                label: position.title, // Hiển thị title trong select
            }));
            setManagerPositionSource(mappedPosition);
            console.log('Manager position:', mappedPosition);
        } catch (error) {
            console.error('Error fetching manager positions:', error);
            setManagerPositionSource([]);
        }
    };

    const handleGetAllDepartments = async (page = 1, pageSize = 5) => {
        setLoading(true);
        try {
            const response = await getAllDepartments({ page: page - 1, pageSize });
            if (response && Array.isArray(response.content)) {
                const mappedDepartments = response.content.map(employee => ({
                    ...employee,
                    isActive: employee.active,
                }));
                setDepartmentSource(mappedDepartments);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Invalid data format:', response);
                setDepartmentSource([]);
            }
        } catch (error) {
            console.error('Error fetching employees:', error);
            setDepartmentSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddRole = () => {
        setModalMode('create');
        setSelectedRole(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreatePermission = async (formData) => {
        try {
            await createDepartment(formData); 
            message.success('Employee created successfully!');
            handleGetAllDepartments();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error creating employee: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditRole = (record) => {
        setSelectedRole(record);
        setModalMode('edit');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreatePermission(formData);
        } else if (modalMode === 'edit') {
            // Thêm logic update nếu cần
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllDepartments(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Add New Employee';
            case 'edit':
                return 'Edit Employee';
            case 'delete':
                return 'Delete Employee';
            default:
                return 'Employee Details';
        }
    };

    return (
        <div className={cx('trailer-wrapper')}>
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
                        onClick={handleAddRole}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
                    <SmartButton title="Excel" icon={<CloudUploadOutlined />} />
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={employeeSource}
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
                initialValues={selectedRole}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default User;