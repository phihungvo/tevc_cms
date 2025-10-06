import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/PayRoll/PayRoll.module.scss';
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
import { Form, message, Tag, DatePicker } from 'antd';
import { calculatePayroll, getAllPayroll, processPayroll, updatePayroll } from '~/service/admin/payroll';
import { getAllEmployeesNoPaging } from '~/service/admin/employee';
import { exportExcelFile } from '~/service/admin/export_service';
import dayjs from 'dayjs';

const cx = classNames.bind(styles);

function Payroll() {
    const [payRollSource, setPayRollSource] = useState([]);
    const [employeeSource, setEmployeeSource] = useState([]);
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);
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
            align: 'center',
        },
        {
            title: 'Kỳ Lương',
            dataIndex: 'period',
            key: 'period',
            width: 100,
            align: 'center',
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            key: 'status',
            width: 100,
            align: 'center',
            render: (status) => (
                <Tag color={statusPayroll[status] || 'default'}>{status}</Tag>
            ),
        },
        {
            title: 'Lương cơ bản',
            dataIndex: 'basicSalary',
            key: 'basicSalary',
            width: 100,
            align: 'center',
            render: (baseSalary) => baseSalary ? `${baseSalary.toLocaleString()}` : 'N/A',
        },
        {
            title: 'Làm thêm giờ',
            dataIndex: 'overtime',
            key: 'overtime',
            align: 'center',
            width: 100,
        },
        {
            title: 'Thưởng',
            dataIndex: 'bonus',
            key: 'bonus',
            align: 'center',
            width: 100,
        },
        {
            title: 'Phụ cấp',
            dataIndex: 'allowances',
            key: 'allowances',
            align: 'center',
            width: 100,
        },
        {
            title: 'Khấu trừ',
            dataIndex: 'deductions',
            key: 'deductions',
            align: 'center',
            width: 100,
        },
        {
            title: 'Thuế',
            dataIndex: 'tax',
            key: 'tax',
            align: 'center',
            width: 100,
            render: (baseSalary) => baseSalary ? `${baseSalary.toLocaleString()}` : 'N/A',
        },
        {
            title: 'Bảo hiểm',
            dataIndex: 'insurance',
            key: 'insurance',
            align: 'center',
            width: 100,
            render: (baseSalary) => baseSalary ? `${baseSalary.toLocaleString()}` : 'N/A',
        },
        {
            title: 'Lương ròng',
            dataIndex: 'netSalary',
            key: 'netSalary',
            align: 'center',
            width: 100,
            render: (baseSalary) => baseSalary ? `${baseSalary.toLocaleString()}` : 'N/A',
        },
        {
            title: 'Ngày xử lý',
            dataIndex: 'processedDate',
            key: 'processedDate',
            width: 150,
            align: 'center',
            render: (date) =>
                date && moment(date, moment.ISO_8601, true).isValid()
                    ? moment(date).format('DD/MM/YYYY HH:mm:ss')
                    : 'N/A',
        },
        {
            title: 'Ngày thanh toán',
            dataIndex: 'paidDate',
            key: 'paidDate',
            width: 150,
            align: 'center',
            render: (date) =>
                date && moment(date, moment.ISO_8601, true).isValid()
                    ? moment(date).format('DD/MM/YYYY HH:mm:ss')
                    : 'N/A',
        },
        {
            title: 'Actions',
            fixed: 'right',
            width: 130,
            render: (_, record) => (
                <>
                    <SmartButton
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={50}
                        onClick={() => handleEditPayroll(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={50}
                        // onClick={() => handleDeleteTrailer(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const userModelFields = [
        {
            label: 'Nhân viên',
            name: 'employeeId',
            type: 'select',
            options: employeeSource,
            rules: [{ required: true, message: 'Nhân viên bắt buộc chọn!' }],
        },
        {
            label: 'Kì hạn',
            name: 'period',
            type: 'date',
            render: () => <DatePicker format="YYYY-MM" picker="month" style={{ width: '100%' }} />,
            rules: [{ required: true, message: 'Kỳ hạn bắt buộc!' }],
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
            label: 'Nhân viên',
            name: 'employeeId',
            type: 'select',
            readOnly: true,
            disabled: true,
        },
        {
            label: 'Kỳ hạn',
            name: 'period',
            type: 'text',
            rules: [{ required: true, message: 'Kì hạn là bắt buộc!' }],
        },
        {
            label: 'Lương cơ bản',
            name: 'basicSalary',
            type: 'text',
        },
        {
            label: 'Làm thêm giờ',
            name: 'overtime',
            type: 'text',
        },
        {
            label: 'Thưởng',
            name: 'bonus',
            type: 'text',
        },
        {
            label: 'Phụ cấp',
            name: 'allowances',
            type: 'text',
        },
        {
            label: 'Khấu trừ',
            name: 'deductions',
            type: 'text',
        },
        {
            label: 'Thuế',
            name: 'tax',
            type: 'text',
        },
        {
            label: 'Bảo hiểm',
            name: 'insurance',
            type: 'text',
        },
        {
            label: 'Lương thực nhận',
            name: 'netSalary',
            type: 'text',
        },
        {
            label: 'Trạng thái',
            name: 'status',
            type: 'select',
            options: Object.keys(statusPayroll).map(key => ({
                label: key,
                value: key,
            }))
        },
        {
            label: 'Ngày xử lý',
            name: 'processedDate',
            type: 'date',
            render: () => <DatePicker format="DD/MM/YYYY HH:mm:ss" showTime style={{ width: '100%' }} />,
        },
        {
            label: 'Ngày thanh toán',
            name: 'paidDate',
            type: 'date',
            render: () => <DatePicker format="DD/MM/YYYY HH:mm:ss" showTime style={{ width: '100%' }} />,
        },
    ];

    const handleGetAllEmployees = async () => {
        try {
            const response = await getAllEmployeesNoPaging();

            const employeesData = response.map((employee) => ({
                label: `${employee.firstName} ${employee.lastName}`.trim(),
                value: employee.id,
            }));

            console.log('Employee data: ', employeesData)
            setEmployeeSource(employeesData);
        } catch (error) {
            console.error('Lỗi khi lấy danh sách nhân viên:', error);
            message.error('Không thể lấy danh sách nhân viên');
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
                        processedDate: payRoll.processedDate ? payRoll.processedDate : null,
                        paidDate: payRoll.paidDate ? payRoll.paidDate : null,
                    };
                });
                setPayRollSource(transformedPayrolls);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements || 0,
                });
            } else {
                console.error('Định dạng dữ liệu không hợp lệ:', response);
                setPayRollSource([]);
            }
        } catch (error) {
            console.error('Lỗi khi lấy danh sách bảng lương:', error);
            message.error('Không thể lấy danh sách bảng lương');
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
            console.error('Error when process payrolls!');
            message.error('Không thể xử lý bảng lương');
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
        setIsModalOpen(false);
    };

    const handleEditPayroll = (record) => {
        setSelectedPayroll(record);
        setModalMode('edit');

        form.setFieldsValue({
            ...record,
            processedDate: record.processedDate && moment(record.processedDate, moment.ISO_8601, true).isValid()
                ? moment(record.processedDate)
                : null,
            paidDate: record.paidDate && moment(record.paidDate, moment.ISO_8601, true).isValid()
                ? moment(record.paidDate)
                : null,
        });
        setIsModalOpen(true);
    };

    const handleCallUpdatePayroll = async (formData) => {
        const formattedData = {
            ...formData,
            processedDate: formData.processedDate ? moment(formData.processedDate).format('YYYY-MM-DDTHH:mm:ss') : null,
            paidDate: formData.paidDate ? moment(formData.paidDate).format('YYYY-MM-DDTHH:mm:ss') : null,
        };
        try {
            await updatePayroll(selectedPayroll.id, formattedData);
            message.success('Bảng lương đã được cập nhật thành công');
            handleGetAllPayRolls();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật bảng lương: ${error.response?.data?.message || error.message}`);
        }
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
                return 'Thêm bảng lương';
            case 'edit':
                return 'Chỉnh sửa bảng lương';
            case 'delete':
                return 'Xoá bảng lương';
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
                    icon={<SearchOutlined />}
                />
                <div className={cx('features')}>
                    <SmartButton
                        title="Add new"
                        icon={<PlusOutlined />}
                        type="primary"
                        onClick={handleAddPayroll}
                    />
                    <SmartButton
                        title="Process"
                        icon={<CheckCircleOutlined />}
                        type="primary"
                        onClick={handleProcessPayrolls}
                        disabled={selectedRowKeys.length === 0}
                        style={{ marginLeft: '8px' }}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
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
                            ? userModelFields
                            : updateModalFields
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
