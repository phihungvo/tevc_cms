import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import moment from 'moment';
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
import { Form, message, Tag, DatePicker } from 'antd';
import {
    createAttandance,
    getAllAttendancesWithPagination,
    deleteAttendance,
    updateAttandance,
} from '~/service/admin/attendance';
import { getAllEmployeesNoPaging } from '~/service/admin/employee';
import 'moment/locale/vi';

moment.locale('vi');

const cx = classNames.bind(styles);

function Attendance() {
    const [attendanceSource, setAttendanceSource] = useState([]);
    const [employeeSource, setEmployeeSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5, // Đặt mặc định là 5 dòng
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedAttendance, setSelectedAttendance] = useState(null);
    const [form] = Form.useForm();

    const statusAttendance = {
        PRESENT: 'success',
        ABSENT: 'error',
        LATE: 'processing',
    };

    const columns = [
        {
            title: 'Nhân viên',
            dataIndex: 'employeeName',
            key: 'employeeName',
        },
        {
            title: 'Check In',
            dataIndex: 'checkIn',
            key: 'checkIn',
            render: (date) =>
                date && moment(date, moment.ISO_8601, true).isValid()
                    ? moment(date).format('DD/MM/YYYY HH:mm:ss')
                    : 'N/A',
        },
        {
            title: 'Check Out',
            dataIndex: 'checkOut',
            key: 'checkOut',
            render: (date) =>
                date && moment(date, moment.ISO_8601, true).isValid()
                    ? moment(date).format('DD/MM/YYYY HH:mm:ss')
                    : 'N/A',
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            key: 'status',
            render: (status) => (
                <Tag color={statusAttendance[status] || 'default'}>
                    {status}
                </Tag>
            ),
        },
        {
            title: 'Ghi chú',
            dataIndex: 'notes',
            key: 'notes',
        },
        {
            title: 'Hành động',
            key: 'actions',
            render: (_, record) => (
                <>
                    <SmartButton
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={50}
                        onClick={() => handleEditAttendance(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={50}
                        onClick={() => handleDeleteAttendance(record.id)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const attendanceModalFields = [
        {
            label: 'Tên nhân viên',
            name: 'employeeId',
            type: 'select',
            options: employeeSource,
            rules: [{ required: true, message: 'Vui lòng chọn nhân viên!' }],
        },
        {
            label: 'Trạng thái điểm danh',
            name: 'status',
            type: 'select',
            options: ['PRESENT', 'ABSENT', 'LATE'],
        },
        {
            label: 'Check In',
            name: 'checkIn',
            type: 'date',
            render: () => (
                <DatePicker
                    format="DD/MM/YYYY HH:mm:ss"
                    showTime
                    style={{ width: '100%' }}
                />
            ),
            rules: [{ required: true, message: 'Vui lòng chọn thời gian Check In!' }],
        },
        {
            label: 'Check Out',
            name: 'checkOut',
            type: 'date',
            render: () => (
                <DatePicker
                    format="DD/MM/YYYY HH:mm:ss"
                    showTime
                    style={{ width: '100%' }}
                />
            ),
        },
        {
            label: 'Ghi chú',
            name: 'notes',
            type: 'text',
        },
    ];

    useEffect(() => {
        handleGetAllEmployees();
        fetchAttendances();
    }, []);

    const fetchAttendances = async (page = 1, pageSize = 5) => {
        setLoading(true);
        try {
            const response = await getAllAttendancesWithPagination({ page: page - 1, pageSize });
            if (response && Array.isArray(response.content)) {
                const mappedAttendances = response.content.map((attendance) => ({
                    ...attendance,
                    checkIn: attendance.checkIn ? attendance.checkIn : null,
                    checkOut: attendance.checkOut ? attendance.checkOut : null,
                }));
                setAttendanceSource(mappedAttendances);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements || 0,
                });
            } else {
                console.error('Định dạng dữ liệu không hợp lệ:', response);
                setAttendanceSource([]);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: 0,
                });
            }
        } catch (error) {
            console.error('Lỗi khi lấy danh sách điểm danh:', error);
            message.error('Không thể lấy danh sách điểm danh');
            setAttendanceSource([]);
            setPagination({
                current: page,
                pageSize: pageSize,
                total: 0,
            });
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
            console.error('Lỗi khi lấy danh sách nhân viên:', error);
            message.error('Không thể lấy danh sách nhân viên');
        }
    };

    const handleAddAttendance = () => {
        setModalMode('create');
        setSelectedAttendance(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreateAttendance = async (formData) => {
        try {
            const formattedData = {
                ...formData,
                checkIn: formData.checkIn ? moment(formData.checkIn).format('YYYY-MM-DDTHH:mm:ss') : null,
                checkOut: formData.checkOut ? moment(formData.checkOut).format('YYYY-MM-DDTHH:mm:ss') : null,
            };
            await createAttandance(formattedData);
            message.success('Điểm danh được tạo thành công');
            fetchAttendances();
            setIsModalOpen(false);
        } catch (error) {
            message.error('Không thể tạo điểm danh');
        }
    };

    const handleDeleteAttendance = async (id) => {
        try {
            await deleteAttendance(id);
            message.success('Điểm danh đã được xóa thành công');
            fetchAttendances(pagination.current, pagination.pageSize);
        } catch (error) {
            message.error('Không thể xóa điểm danh');
        }
    };

    const handleEditAttendance = (record) => {
        setSelectedAttendance(record);
        setModalMode('edit');
        form.setFieldsValue({
            ...record,
            checkIn: record.checkIn && moment(record.checkIn, moment.ISO_8601, true).isValid()
                ? moment(record.checkIn)
                : null,
            checkOut: record.checkOut && moment(record.checkOut, moment.ISO_8601, true).isValid()
                ? moment(record.checkOut)
                : null,
        });
        setIsModalOpen(true);
    };

    const handleCallUpdateAttendance = async (formData) => {
        try {
            const formattedData = {
                ...formData,
                checkIn: formData.checkIn ? moment(formData.checkIn).format('YYYY-MM-DDTHH:mm:ss') : null,
                checkOut: formData.checkOut ? moment(formData.checkOut).format('YYYY-MM-DDTHH:mm:ss') : null,
            };
            await updateAttandance(selectedAttendance.id, formattedData);
            message.success('Điểm danh đã được cập nhật thành công');
            fetchAttendances();
            setIsModalOpen(false);
        } catch (error) {
            message.error('Không thể cập nhật điểm danh');
        }
    };

    const handleTableChange = (pagination) => {
        const { current, pageSize } = pagination;
        const newCurrent = pageSize !== pagination.pageSize ? 1 : current;
        fetchAttendances(newCurrent, pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Điểm Danh Mới';
            case 'edit':
                return 'Chỉnh Sửa Điểm Danh';
            case 'delete':
                return 'Xóa Điểm Danh';
            default:
                return 'Chi Tiết Điểm Danh';
        }
    };

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreateAttendance(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateAttendance(formData);
        } else if (modalMode === 'delete') {
            // handleCallDeleteAttendance();
        }
    };

    return (
        <div className={cx('attendance-wrapper')}>
            <div className={cx('sub_header')}>
                <SmartInput
                    size="large"
                    placeholder="Tìm kiếm"
                    icon={<SearchOutlined />}
                />
                <div className={cx('features')}>
                    <SmartButton
                        title="Thêm mới"
                        icon={<PlusOutlined />}
                        type="primary"
                        onClick={handleAddAttendance}
                    />
                    <SmartButton
                        title="Xử lý"
                        icon={<CheckCircleOutlined />}
                        type="primary"
                        style={{ marginLeft: '8px' }}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
                    <SmartButton
                        title="Excel"
                        icon={<CloudUploadOutlined />}
                    />
                </div>
            </div>
            <div className={cx('attendance-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={attendanceSource}
                    loading={loading}
                    pagination={{
                        current: pagination.current,
                        pageSize: pagination.pageSize,
                        total: pagination.total,
                        showSizeChanger: true,
                        pageSizeOptions: ['5', '10', '20', '50'],
                        showTotal: (total, range) => `${range[0]}-${range[1]} của ${total} bản ghi`,
                    }}
                    onTableChange={handleTableChange}
                />
            </div>

            <PopupModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                title={getModalTitle()}
                fields={modalMode === 'delete' ? [] : attendanceModalFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedAttendance}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default Attendance;
