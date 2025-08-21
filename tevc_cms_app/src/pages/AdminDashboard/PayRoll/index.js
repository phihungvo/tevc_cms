import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/PayRoll/PayRoll.module.scss';
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
import {calculatePayroll, getAllPayroll, processPayroll, updatePayroll} from '~/service/admin/payroll';
import {getAllEmployees, getAllEmployeesNoPaging} from '~/service/admin/employee';
import {exportExcelFile} from '~/service/admin/export_service';
import dayjs from 'dayjs';

const cx = classNames.bind(styles);

function Payroll() {
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
    const [selectedPayroll, setSelectedPayroll] = useState(null);
    const [form] = Form.useForm();

    const statusPayroll = {
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
                <Tag color={statusPayroll[status] || 'default'}>{status}</Tag>
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
                        onClick={() => handleEditPayroll(record)}
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

    const updateModalFields = [
        {
            label: 'ID',
            name: 'id',
            type: 'text',
            readOnly: true,
            disabled: true,
        },
        {
            label: 'Employee',
            name: 'employeeId',
            type: 'select',
            options: employeeSource,
            rules: [{ required: true, message: 'Employee is required!' }],
        },
        {
            label: 'Period',
            name: 'period',
            type: 'text',
            rules: [{ required: true, message: 'period is required!' }],
        },
        {
            label: 'Basic Salary',
            name: 'basicSalary',
            type: 'text',
        },
        {
            label: 'Overtime',
            name: 'overtime',
            type: 'text',
        },
        {
            label: 'Bonus',
            name: 'bonus',
            type: 'text',
        },
        {
            label: 'Allowances',
            name: 'allowances',
            type: 'text',
        },
        {
            label: 'Deductions',
            name: 'deductions',
            type: 'text',
        },
        {
            label: 'Tax',
            name: 'tax',
            type: 'text',
        },
        {
            label: 'Insurance',
            name: 'insurance',
            type: 'text',
        },
        {
            label: 'NetSalary',
            name: 'netSalary',
            type: 'text',
        },
        {
            label: 'Status',
            name: 'status',
            type: 'select',
            options: Object.keys(statusPayroll).map(key => ({
                label: key,
                value: key,
            }))
        },
        {
            label: 'Processed Date',
            name: 'processedDate',
            type: 'date',
        },
        {
            label: 'Paid Date',
            name: 'paidDate',
            type: 'date',
        },
    ];

    const handleGetAllEmployees = async () => {
        try {
            const response = await getAllEmployeesNoPaging();

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

    const handleGetAllPayRolls = async (page = 1, pageSize = 10) => {
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
            console.error('Error when process oayrolls!');
        } finally {
            setLoading(false);
        }
    }

    const handleAddPayroll = () => {
        setModalMode('create');
        setSelectedPayroll(null);
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

    const handleEditPayroll = (record) => {
        setSelectedPayroll(record);
        setModalMode('edit');

        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdatePayroll = async (formData) => {
        await updatePayroll(selectedPayroll.id, formData);
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
            handleCallUpdatePayroll(formData);
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
                fields={
                    modalMode === 'delete'
                    ? []
                    : modalMode === 'create'
                        ? userModelFields : updateModalFields
                }
                onSubmit={handleFormSubmit}
                initialValues={selectedPayroll}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default Payroll;
