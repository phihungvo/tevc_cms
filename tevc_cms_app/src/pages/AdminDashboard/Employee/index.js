import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Employee/Employee.module.scss';
import moment from 'moment';
import SmartTable from '~/components/Layout/components/SmartTable';
import {
    SearchOutlined,
    PlusOutlined,
    FilterOutlined,
    CloudUploadOutlined,
    EditOutlined,
    DeleteOutlined,
    EyeOutlined, // Thêm icon View
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import { Form, message, Tag, DatePicker, Watermark } from 'antd';
import {
    getAllEmployees,
    createEmployee,
    updateEmployee,
    deleteEmployee,
} from '~/service/admin/employee';
import { getAllDepartmentsNoPaging } from '~/service/admin/department';
import { getAllPositions } from '~/service/admin/position';
import { exportExcelFile } from '~/service/admin/export_service';
import CustomTabs from '~/components/Layout/components/Tab';
import 'moment/locale/vi';
moment.locale('vi');

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
    const [selectedEmployee, setSelectedEmployee] = useState(null);
    const [form] = Form.useForm();
    const navigate = useNavigate();

    const genderStyles = {
        Male: { color: 'green', label: 'Male' },
        Female: { color: 'volcano', label: 'Female' },
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
            title: 'Last Name',
            dataIndex: 'lastName',
            key: 'lastName',
            width: 150,
        },
        {
            title: 'First Name',
            dataIndex: 'firstName',
            key: 'firstName',
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
            render: (status) => {
                const style = genderStyles[status] || {
                    color: 'default',
                    label: status || 'N/A',
                };
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
                date && moment(date, moment.ISO_8601, true).isValid()
                    ? moment(date).format('DD/MM/YYYY')
                    : 'N/A',
        },
        {
            title: 'Is Active',
            dataIndex: 'isActive',
            key: 'isActive',
            render: (isActive) => (isActive ? 'Có' : 'Không'),
        },
        {
            title: 'Created At',
            dataIndex: 'createdAt',
            key: 'createdAt',
            render: (date) =>
                date && moment(date, moment.ISO_8601, true).isValid()
                    ? moment(date).format('DD/MM/YYYY')
                    : 'N/A',
        },
        {
            title: 'Updated At',
            dataIndex: 'updatedAt',
            key: 'updatedAt',
            render: (date) =>
                date && moment(date, moment.ISO_8601, true).isValid()
                    ? moment(date).format('DD/MM/YYYY')
                    : 'N/A',
        },
        {
            title: 'Actions',
            fixed: 'right',
            width: 170,
            render: (_, record) => (
                <>
                    <SmartButton
                        // title="Xem"
                        type="default"
                        icon={<EyeOutlined />}
                        buttonWidth={50}
                        onClick={() => navigate(`/admin/employee/${record.id}`)} // Navigate sang detail
                    />
                    <SmartButton
                        // title="Sửa"
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={50}
                        onClick={() => handleEditRole(record)}
                        style={{ marginLeft: '8px' }}
                    />
                    <SmartButton
                        // title="Xóa"
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={50}
                        onClick={() => handleDeleteEmployee(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'Employee Code',
            name: 'employeeCode',
            type: 'text',
            readOnly: true,
            disabled: true,
        },
        {
            label: 'First Name',
            name: 'firstName',
            type: 'text',
            rules: [{ required: true, message: 'Tên là bắt buộc!' }],
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
            render: () => (
                <DatePicker format="DD/MM/YYYY" style={{ width: '100%' }} />
            ),
            rules: [{ required: true, message: 'Ngày sinh là bắt buộc!' }],
        },
        {
            label: 'Gender',
            name: 'gender',
            type: 'select',
            options: ['Male', 'Female', 'Other'],
            rules: [{ required: true, message: 'Giới tính là bắt buộc!' }],
        },
        {
            label: 'Email',
            name: 'email',
            type: 'text',
            rules: [
                { required: true, message: 'Email là bắt buộc!' },
                { type: 'email', message: 'Định dạng email không hợp lệ!' },
            ],
        },
        {
            label: 'Phone',
            name: 'phone',
            type: 'text',
            rules: [{ required: true, message: 'Số điện thoại là bắt buộc!' }],
        },
        {
            label: 'Address',
            name: 'address',
            type: 'text',
            rules: [{ required: true, message: 'Địa chỉ là bắt buộc!' }],
        },
        {
            label: 'Hire Date',
            name: 'hireDate',
            type: 'date',
            render: () => (
                <DatePicker format="DD/MM/YYYY" style={{ width: '100%' }} />
            ),
            rules: [{ required: true, message: 'Ngày vào làm là bắt buộc!' }],
        },
        {
            label: 'Department',
            name: 'departmentId',
            type: 'select',
            options: departmentOptions,
        },
        {
            label: 'Position',
            name: 'positionId',
            type: 'select',
            options: positionOptions,
        },
        {
            label: 'Active',
            name: 'isActive',
            type: 'yesno',
        },
    ];

    const fetchDepartmentAndPositionOptions = async () => {
        try {
            const deptResponse = await getAllDepartmentsNoPaging();
            if (deptResponse && Array.isArray(deptResponse)) {
                const departments = deptResponse.map((dept) => ({
                    label: dept.name,
                    value: dept.id,
                }));
                setDepartmentOptions(departments);
            }

            const posResponse = await getAllPositions();
            if (posResponse && Array.isArray(posResponse.content)) {
                const positions = posResponse.content.map((pos) => ({
                    label: pos.title,
                    value: pos.id,
                }));
                setPositionOptions(positions);
            }
        } catch (error) {
            console.error('Error fetching options:', error);
        }
    };

    const handleGetAllEmployees = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllEmployees({
                page: page - 1,
                pageSize,
            });
            if (response && Array.isArray(response.content)) {
                const mappedEmployees = response.content.map((employee) => ({
                    ...employee,
                    isActive: employee.active,
                    dateOfBirth: employee.dateOfBirth
                        ? moment(employee.dateOfBirth).format('YYYY-MM-DD')
                        : null,
                    hireDate: employee.hireDate
                        ? moment(employee.hireDate).format('YYYY-MM-DD')
                        : null,
                }));
                setEmployeeSource(mappedEmployees);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Định dạng dữ liệu không hợp lệ:', response);
                setEmployeeSource([]);
            }
        } catch (error) {
            console.error('Lỗi khi lấy danh sách nhân viên:', error);
            setEmployeeSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddRole = () => {
        setModalMode('create');
        setSelectedEmployee(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreatePermission = async (formData) => {
        try {
            const formattedData = {
                ...formData,
                dateOfBirth: formData.dateOfBirth
                    ? moment(formData.dateOfBirth).format('YYYY-MM-DD')
                    : null,
                hireDate: formData.hireDate
                    ? moment(formData.hireDate).format('YYYY-MM-DD')
                    : null,
            };
            await createEmployee(formattedData);
            handleGetAllEmployees();
            setIsModalOpen(false);
        } catch (error) {
            message.error(
                `Lỗi khi tạo nhân viên: ${
                    error.response?.data?.message || error.message
                }`,
            );
        }
    };

    const handleEditRole = (record) => {
        setSelectedEmployee(record);
        setModalMode('edit');
        form.setFieldsValue({
            ...record,
            dateOfBirth:
                record.dateOfBirth &&
                moment(record.dateOfBirth, moment.ISO_8601, true).isValid()
                    ? moment(record.dateOfBirth)
                    : null,
            hireDate:
                record.hireDate &&
                moment(record.hireDate, moment.ISO_8601, true).isValid()
                    ? moment(record.hireDate)
                    : null,
        });
        setIsModalOpen(true);
    };

    const handleCallUpdateEmployee = async (formData) => {
        try {
            const formattedData = {
                ...formData,
                dateOfBirth: formData.dateOfBirth
                    ? moment(formData.dateOfBirth).format('YYYY-MM-DD')
                    : null,
                hireDate: formData.hireDate
                    ? moment(formData.hireDate).format('YYYY-MM-DD')
                    : null,
            };
            await updateEmployee(selectedEmployee.id, formattedData);
            handleGetAllEmployees();
            setIsModalOpen(false);
        } catch (error) {
            message.error(
                `Lỗi khi cập nhật nhân viên: ${
                    error.response?.data?.message || error.message
                }`,
            );
        }
    };

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreatePermission(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateEmployee(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteEmployee();
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllEmployees(pagination.current, pagination.pageSize);
    };

    const handleDeleteEmployee = (record) => {
        setSelectedEmployee(record);
        setModalMode('delete');
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallDeleteEmployee = async () => {
        await deleteEmployee(selectedEmployee.id);
        handleGetAllEmployees();
        setIsModalOpen(false);
    };

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('employee');
            if (
                !response.headers['content-type'].includes(
                    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
                )
            ) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute(
                'download',
                `employee_${new Date()
                    .toISOString()
                    .replace(/[-:]/g, '')}.xlsx`,
            );
            link.click();
            window.URL.revokeObjectURL(url);
            message.success('Tải file Excel thành công!');
        } catch (error) {
            console.error('Lỗi khi xuất file Excel:', error);
            message.error('Không thể tải file Excel');
        }
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Nhân Viên Mới';
            case 'edit':
                return 'Chỉnh Sửa Nhân Viên';
            case 'delete':
                return 'Xóa Nhân Viên';
            default:
                return 'Chi Tiết Nhân Viên';
        }
    };

    useEffect(() => {
        fetchDepartmentAndPositionOptions();
        handleGetAllEmployees();
    }, []);

    const tabItems = [
        {
            key: '1',
            label: 'Employee List',
            children: (
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
                            <SmartButton
                                title="Bộ lọc"
                                icon={<FilterOutlined />}
                            />
                            <SmartButton
                                title="Excel"
                                icon={<CloudUploadOutlined />}
                                onClick={handleExportFile}
                            />
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
                        initialValues={selectedEmployee}
                        isDeleteMode={modalMode === 'delete'}
                        formInstance={form}
                    />
                </div>
            ),
        },
        {
            key: '2',
            label: 'Work History',
            children: (
                <>
                    <Watermark content="Ant Design">
                        <div style={{ height: 500 }} />
                    </Watermark>
                </>
            ),
        },
        {
            key: '3',
            label: 'Skills',
            children: (
                <>
                    <Watermark content="Ant Design">
                        <div style={{ height: 500 }} />
                    </Watermark>
                </>
            ),
        },
        {
            key: '4',
            label: 'Reports',
            children: (
                <>
                    <Watermark content="Ant Design">
                        <div style={{ height: 500 }} />
                    </Watermark>
                </>
            ),
        },
    ];

    return (
        <div>
            <CustomTabs
                items={tabItems}
                defaultActiveKey="1"
                tabPosition="top"
                onChange={(key) => console.log('Tab changed:', key)}
            />
        </div>
    );
}

export default Employee;
