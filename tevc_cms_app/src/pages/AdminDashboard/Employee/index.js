import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Employee/Employee.module.scss';
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
import { getAllEmployees, createEmployee } from '~/service/admin/employee';
import { getAllDepartments } from '~/service/admin/department';
import {  getAllPositions } from '~/service/admin/position';

const cx = classNames.bind(styles);

function Employee() {
    const [employeeSource, setEmployeeSource] = useState([]);
    const [departmentOptions, setDepartmentOptions] = useState([]);
    const [positionOptions, setPositionOptions] = useState([]);
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

    const genderStyles = {
        Male: { color: 'processing', label: 'Male' }, 
        Female: { color: 'error', label: 'Female' },
    };

    const columns = [
        {
            title: 'Employee Code',
            dataIndex: 'employeeCode',
            key: 'employeeCode',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'First Name',
            dataIndex: 'firstName',
            key: 'firstName',
            width: 150,
        },
        {
            title: 'Last Name',
            dataIndex: 'lastName',
            key: 'lastName',
            width: 150,
        },
        {
            title: 'Date of Birth',
            dataIndex: 'dateOfBirth',
            key: 'dateOfBirth',
            width: 150,
            render: (date) =>
                date ? new Date(date).toLocaleString('vi-VN') : 'N/A',
        },
        {
            title: 'Gender',
            dataIndex: 'gender',
            key: 'gender',
            width: 100,
            render: (gender) => {
                const style = genderStyles[gender] || { color: 'default', label: gender || 'N/A' };
                return <Tag color={style.color}>{style.label}</Tag>;
            },
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: 'Address',
            dataIndex: 'address',
            key: 'address',
        },
        {
            title: 'Phone',
            dataIndex: 'phone',
            key: 'phone',
        },
        {
            title: 'Hire Date',
            dataIndex: 'hireDate',
            key: 'hireDate',
            render: (date) =>
                date ? new Date(date).toLocaleString('vi-VN') : 'N/A',
        },
        {
            title: 'Is Active',
            dataIndex: 'isActive',
            key: 'isActive',
        },
        {
            title: 'Created At',
            dataIndex: 'createdAt',
            key: 'createdAt',
            render: (date) =>
                date ? new Date(date).toLocaleString('vi-VN') : 'N/A',
        },
        {
            title: 'Updated At',
            dataIndex: 'updatedAt',
            key: 'updatedAt',
            render: (date) =>
                date ? new Date(date).toLocaleString('vi-VN') : 'N/A',
        },
        {
            title: 'Actions',
            fixed: 'right',
            width: 180,
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
            label: 'First Name',
            name: 'firstName',
            type: 'text',
        },
        {
            label: 'Last Name',
            name: 'lastName',
            type: 'text',
        },
        {
            label: 'Date of Birth',
            name: 'dateOfBirth',
            type: 'date',
        },
        {
            label: 'Gender',
            name: 'gender',
            type: 'select',
            options: ['Male', 'Female', 'Other'],
        },
        {
            label: 'Email',
            name: 'email',
            type: 'text',
        },
        {
            label: 'Phone',
            name: 'phone',
            type: 'text',
        },
        {
            label: 'Address',
            name: 'address',
            type: 'text',
        },
        {
            label: 'Hire Date',
            name: 'hireDate',
            type: 'date',
        },
        {
            label: 'Department',
            name: 'departmentId',
            type: 'select',
            options: departmentOptions, // Sử dụng state trực tiếp
        },
        {
            label: 'Position',
            name: 'positionId',
            type: 'select',
            options: positionOptions, // Sử dụng state trực tiếp
        },
        {
            label: 'Active',
            name: 'isActive',
            type: 'yesno',
        },
    ];

    const fetchDepartmentAndPositionOptions = async () => {
        try {
            // Fetch departments
            const deptResponse = await getAllDepartments({ page: 0, pageSize: 100 });
            if (deptResponse && Array.isArray(deptResponse.content)) {
                const departments = deptResponse.content.map(dept => ({
                    label: dept.name, 
                    value: dept.id,
                }));
                setDepartmentOptions(departments);
            }

            // Fetch positions
            const posResponse = await getAllPositions();
            if (posResponse && Array.isArray(posResponse.content)) {
                const positions = posResponse.content.map(pos => ({
                    label: pos.title, // Hoặc pos.positionTitle tùy API
                    value: pos.id,
                }));
                setPositionOptions(positions);
            }
        } catch (error) {
            console.error('Error fetching options:', error);
        }
    };

    useEffect(() => {
        fetchDepartmentAndPositionOptions();
        handleGetAllEmployees();
    }, []);

    const handleGetAllEmployees = async (page = 1, pageSize = 100) => {
        setLoading(true);
        try {
            const response = await getAllEmployees({ page: page - 1, pageSize });
            if (response && Array.isArray(response.content)) {
                const mappedEmployees = response.content.map(employee => ({
                    ...employee,
                    isActive: employee.active,
                }));
                setEmployeeSource(mappedEmployees);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Invalid data format:', response);
                setEmployeeSource([]);
            }
        } catch (error) {
            console.error('Error fetching employees:', error);
            setEmployeeSource([]);
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
            await createEmployee(formData);
            handleGetAllEmployees();
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
        handleGetAllEmployees(pagination.current, pagination.pageSize);
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

export default Employee;