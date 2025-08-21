import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/PayRole/PayRole.module.scss';
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
    CheckCircleOutlined,
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import {Form, message, Tag} from 'antd';
import {calculatePayroll, getAllPayroll, processPayroll} from '~/service/admin/payroll';
import {getAllEmployees} from '~/service/admin/employee';
import {exportExcelFile} from '~/service/admin/export_service';
import dayjs from 'dayjs';

const cx = classNames.bind(styles);

function User() {
    const [payRollSource, setPayRollSource] = useState([]);
    const [employeeSource, setEmployeeSource] = useState([]);
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [form] = Form.useForm();

    const statusColors = {
        PENDING: 'warning',
        PROCESSED: 'success',
        PAID: 'processing',
        CANCELLED: 'error',
        ERROR: 'error',
    };

    const columns = [
        {
            title: 'Tên nhân viên',
            dataIndex: 'employeeName',
            key: 'userName',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'Kỳ Lương',
            dataIndex: 'period',
            key: 'period',
            width: 100,
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            key: 'status',
            width: 100,
            render: (status) => (
                <Tag color={statusColors[status] || 'default'}>{status}</Tag>
            ),
        },
        {
            title: 'Lương cơ bản',
            dataIndex: 'basicSalary',
            key: 'basicSalary',
            width: 100,
        },
        {
            title: 'Làm thêm giờ',
            dataIndex: 'overtime',
            key: 'overtime',
            width: 100,
        },
        {
            title: 'Thưởng',
            dataIndex: 'bonus',
            key: 'bonus',
            width: 100,
        },
        {
            title: 'Phụ cấp',
            dataIndex: 'allowances',
            key: 'allowances',
            width: 100,
        },
        {
            title: 'Khấu trừ',
            dataIndex: 'deductions',
            key: 'deductions',
            width: 100,
        },
        {
            title: 'Thuế',
            dataIndex: 'tax',
            key: 'tax',
            width: 100,
        },
        {
            title: 'Bảo hiểm',
            dataIndex: 'insurance',
            key: 'insurance',
            width: 100,
        },
        {
            title: 'Lương ròng',
            dataIndex: 'netSalary',
            key: 'netSalary',
            width: 100,
        },
        {
            title: 'Ngày xử lý',
            dataIndex: 'processedDate',
            key: 'processedDate',
            width: 150,
            render: (date) =>
                date ? new Date(date).toLocaleString('vi-VN') : 'N/A',
        },
        {
            title: 'Ngày thanh toán',
            dataIndex: 'paidDate',
            key: 'paidDate',
            width: 150,
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
                        icon={<EditOutlined/>}
                        buttonWidth={80}
                        onClick={() => handleEditUser(record)}
                    />
                    <SmartButton
                        title="Delete"
                        type="danger"
                        icon={<DeleteOutlined/>}
                        buttonWidth={80}
                        // onClick={() => handleDeleteTrailer(record)}
                        style={{marginLeft: '8px'}}
                    />
                </>
            ),
        },
    ];

    const userModelFields = [
        {
            label: 'Employee',
            name: 'employeeId',
            type: 'select',
            options: employeeSource,
            rules: [{required: true, message: 'Employee is required!'}],
        },
        {
            label: 'period',
            name: 'period',
            type: 'date',
            rules: [{required: true, message: 'period is required!'}],
        },
    ];

    // const userModalFields = [
    //     {
    //         label: 'Employee',
    //         name: 'employeeId',
    //         type: 'select',
    //         options: employeeSource,
    //         rules: [{ required: true, message: 'Employee is required!' }],
    //     },
    //     {
    //         label: 'period',
    //         name: 'period',
    //         type: 'text',
    //         rules: [{ required: true, message: 'period is required!' }],
    //     },
    //     {
    //         label: 'basicSalary',
    //         name: 'basicSalary',
    //         type: 'text',
    //     },
    //     {
    //         label: 'overtime',
    //         name: 'overtime',
    //         type: 'text',
    //     },
    //     {
    //         label: 'bonus',
    //         name: 'bonus',
    //         type: 'text',
    //     },
    //     {
    //         label: 'allowances',
    //         name: 'allowances',
    //         type: 'text',
    //     },
    //     {
    //         label: 'deductions',
    //         name: 'deductions',
    //         type: 'text',
    //     },
    //     {
    //         label: 'tax',
    //         name: 'tax',
    //         type: 'text',
    //     },
    //     {
    //         label: 'insurance',
    //         name: 'insurance',
    //         type: 'text',
    //     },
    //     {
    //         label: 'Permission',
    //         name: 'permissions',
    //         type: 'select',
    //         options: ['ADMIN:MANAGE', 'ADMIN:CREATE', 'ADMIN:UPDATE', 'ADMIN:READ', 'ADMIN:DELETE'],
    //     },
    //     {
    //         label: 'Enable',
    //         name: 'enabled',
    //         type: 'yesno',
    //     },
    //     {
    //         label: 'Bio',
    //         name: 'bio',
    //         type: 'textarea',
    //     },
    // ];

    const handleGetAllEmployees = async (page = 1, pageSize = 5) => {
        try {
            const response = await getAllEmployees({
                page: page - 1,
                pageSize,
            });

            if (!response || !Array.isArray(response.result)) {
                throw new Error(
                    'Invalid response: employees data is missing or not an array',
                );
            }

            const employeesData = response.result.map((employee) => ({
                label: `${employee.firstName} ${employee.lastName}`.trim(),
                value: employee.id,
            }));

            console.log('Employee data: ', employeesData)
            setEmployeeSource(employeesData);
        } catch (error) {
            return {success: false, error: error.message};
        }
    };

    const handleGetAllPayRolls = async (page = 1, pageSize = 5) => {
        setLoading(true);
        try {
            const response = await getAllPayroll({page: page - 1, pageSize});
            const payRollList = response.content;

            if (response && Array.isArray(payRollList)) {
                const transformedPayrolls = payRollList.map((payRoll) => {
                    return {
                        ...payRoll,
                    };
                });
                console.log('tranfer payroll: ', transformedPayrolls);
                setPayRollSource(transformedPayrolls);
                setPagination((prev) => ({
                    ...prev,
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                }));
            } else {
                setPayRollSource([]);
            }
        } catch (error) {
            setPayRollSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleProcessPayrolls = async () => {
        if (selectedRowKeys.length === 0) {
            message.warning('Please select at least one payroll to process');
            return;
        }
        setLoading(true);
        try {
            await processPayroll(selectedRowKeys);
            handleGetAllPayRolls();
            setSelectedRowKeys([]);
        } catch (error) {
            message.error('Error when process oayrolls!');
        } finally {
            setLoading(false);
        }
    }

    const handleAddPayroll = () => {
        setModalMode('create');
        setSelectedUser(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreateUser = async (formData) => {
        const formattedFormData = {
            ...formData,
            period: dayjs(formData.period).format('YYYY-MM'),
        };

        console.log('formated data: ', formattedFormData)
        await calculatePayroll(formattedFormData.employeeId, formattedFormData.period);
        handleGetAllPayRolls();
        form.resetFields();
    };

    const handleEditUser = (record) => {
        setSelectedUser(record);
        setModalMode('edit');

        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdateUser = async (formData) => {
        // await updateUser(selectedUser.id, formData);
        handleGetAllPayRolls();
        setIsModalOpen(false);
    };

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('payroll');
            if (!response.headers['content-type'].includes('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `payroll_${new Date().toISOString().replace(/[-:]/g, '')}.xlsx`);
            link.click();
            window.URL.revokeObjectURL(url);
            message.success('Tải file Excel thành công!');
        } catch (error) {
            console.error('Lỗi khi xuất file Excel:', error);
            message.error('Không thể tải file Excel');
        }
    };
    useEffect(() => {
        handleGetAllEmployees();
        handleGetAllPayRolls();
    }, []);

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreateUser(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateUser(formData);
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllPayRolls(pagination.current, pagination.pageSize);
    };
    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Add New Payroll';
            case 'edit':
                return 'Edit Payroll';
            case 'delete':
                return 'Delete Payroll';
            default:
                return 'Payroll Details';
        }
    };

    const handleSelectChange = (newSelectedRowKeys, selectedRows) => {
        setSelectedRowKeys(newSelectedRowKeys);
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
                        onClick={handleAddPayroll}
                    />
                    <SmartButton
                        title="Process"
                        icon={<CheckCircleOutlined/>}
                        type="primary"
                        onClick={handleProcessPayrolls}
                        disabled={selectedRowKeys.length === 0}
                        style={{marginLeft: '8px'}}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined/>}/>
                    <SmartButton
                        title="Excel"
                        icon={<CloudUploadOutlined/>}
                        onClick={handleExportFile}
                    />
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={payRollSource}
                    loading={loading}
                    pagination={pagination}
                    onTableChange={handleTableChange}
                    selectedRowKeys={selectedRowKeys}
                    onSelectChange={handleSelectChange}
                />
            </div>

            <PopupModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                title={getModalTitle()}
                fields={modalMode === 'delete' ? [] : userModelFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedUser}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default User;
