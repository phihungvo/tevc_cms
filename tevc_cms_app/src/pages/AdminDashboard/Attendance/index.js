import React from 'react';
import classNames from 'classnames/bind';
import styles from './Attendance.module.scss';
import {useState, useEffect} from 'react';
import dayjs from 'dayjs';
import {
    SearchOutlined,
    PlusOutlined,
    CloudUploadOutlined,
    ClockCircleOutlined,
    CheckCircleOutlined,
    CloseCircleOutlined,
    FieldTimeOutlined,
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import {
    Form,
    message,
    Tag,
    Pagination,
    Card,
    Avatar,
    Space,
    Empty,
    Spin,
    DatePicker,
    Select,
    Row,
    Col,
    Statistic,
    ConfigProvider
} from 'antd';
import {getAllAttendances, filterAttendances, createAttendance, updateAttendance} from '~/service/admin/attendance';
import {getAllEmployeesNoPaging} from '~/service/admin/employee';
import {exportExcelFile} from '~/service/admin/export_service';
import AttendanceCard from "~/components/Layout/components/AttendanceCard";
import useDebounce from '~/hooks/useDebounce';

const cx = classNames.bind(styles);
const {RangePicker} = DatePicker;
const {Option} = Select;

function Attendance() {
    const [attendanceSource, setAttendanceSource] = useState([]);
    const [employeeSource, setEmployeeSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 9,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedAttendance, setSelectedAttendance] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const debouncedSearchTerm = useDebounce(searchTerm, 0); // use 1000 to enable useDebounce
    const [dateRange, setDateRange] = useState(null);
    const [statusFilter, setStatusFilter] = useState('ALL');
    const [statistics, setStatistics] = useState({
        present: 0,
        absent: 0,
        late: 0,
        earlyLeave: 0,
    });
    const [form] = Form.useForm();

    const attendanceModalFields = [
        {
            label: 'Nhân viên',
            name: 'employeeId',
            type: 'select',
            options: employeeSource,
            rules: [{required: true, message: 'Vui lòng chọn nhân viên!'}],
        },
        {
            label: 'Ngày',
            name: 'attendanceDate',
            type: 'date',
            rules: [{required: true, message: 'Vui lòng chọn ngày!'}],
        },
        {
            label: 'Giờ vào',
            name: 'checkIn',
            type: 'time',
            rules: [{required: true, message: 'Vui lòng nhập giờ vào!'}],
        },
        {
            label: 'Giờ ra',
            name: 'checkOut',
            type: 'time',
        },
        {
            label: 'Trạng thái',
            name: 'status',
            type: 'select',
            rules: [{required: true, message: 'Vui lòng chọn trạng thái!'}],
            options: [
                {value: 'PRESENT', label: 'Có mặt'},
                {value: 'ABSENT', label: 'Vắng mặt'},
                {value: 'LATE', label: 'Đi muộn'},
            ],
        },
        {
            label: 'Ghi chú',
            name: 'notes',
            type: 'textarea',
        },
    ];

    useEffect(() => {
        handleGetAllEmployees();
        handleGetAllAttendances();
    }, []);

    useEffect(() => {
        handleFilterAttendances();
    }, [debouncedSearchTerm, dateRange, statusFilter, pagination.current, pagination.pageSize]);

    const handleGetAllEmployees = async () => {
        try {
            const response = await getAllEmployeesNoPaging();
            const mappedEmployees = response.map(emp => ({
                value: emp.id,
                label: `${emp.firstName} ${emp.lastName} - ${emp.employeeCode}`,
            }));
            setEmployeeSource(mappedEmployees);
        } catch (error) {
            console.error('Error fetching employees:', error);
            setEmployeeSource([]);
        }
    };

    const handleGetAllAttendances = async (page = 1, pageSize = 9) => {
        setLoading(true);
        try {
            const response = await getAllAttendances({page: page - 1, pageSize});
            if (response && Array.isArray(response.content)) {
                setAttendanceSource(response.content);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
                calculateStatistics(response.content);
            } else {
                console.error('Invalid data format:', response);
                setAttendanceSource([]);
            }
        } catch (error) {
            console.error('Error fetching attendances:', error);
            setAttendanceSource([]);
        } finally {
            setLoading(false);
        }
    };

    const calculateStatistics = (data) => {
        const stats = {
            present: 0,
            absent: 0,
            late: 0,
            earlyLeave: 0,
        };

        data.forEach(item => {
            switch (item.status) {
                case 'PRESENT':
                    stats.present++;
                    break;
                case 'ABSENT':
                    stats.absent++;
                    break;
                case 'LATE':
                    stats.late++;
                    break;
                case 'EARLY_LEAVE':
                    stats.earlyLeave++;
                    break;
                default:
                    break;
            }
        });

        setStatistics(stats);
    };

    const handleFilterAttendances = async () => {
        setLoading(true);
        try {
            const params = {
                page: pagination.current - 1,
                pageSize: pagination.pageSize,
            };

            if (debouncedSearchTerm) {
                params.employeeName = debouncedSearchTerm; // dùng debounced value
            }

            if (dateRange && dateRange.length === 2) {
                params.startDate = dateRange[0].format('YYYY-MM-DD');
                params.endDate = dateRange[1].format('YYYY-MM-DD');
            }

            if (statusFilter !== 'ALL') {
                params.status = statusFilter;
            }

            const response = await filterAttendances(params);
            if (response && Array.isArray(response.content)) {
                setAttendanceSource(response.content);
                setPagination({
                    current: pagination.current,
                    pageSize: pagination.pageSize,
                    total: response.totalElements,
                });
                calculateStatistics(response.content);
            } else {
                setAttendanceSource([]);
            }
        } catch (error) {
            console.error('Error filtering attendances:', error);
            setAttendanceSource([]);
        } finally {
            setLoading(false);
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
            await createAttendance(formData);
            message.success('Thêm chấm công thành công!');
            handleGetAllAttendances();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi thêm chấm công: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleViewAttendance = (record) => {
        setSelectedAttendance(record);
        setModalMode('view');
        const formattedRecord = {
            ...record,
            attendanceDate: record.attendanceDate ? dayjs(record.attendanceDate) : null,
            checkIn: record.checkIn ? dayjs(record.checkIn) : null,
            checkOut: record.checkOut ? dayjs(record.checkOut) : null,
        };
        form.setFieldsValue(formattedRecord);
        setIsModalOpen(true);
    };

    const handleEditAttendance = (record) => {
        setSelectedAttendance(record);
        setModalMode('edit');
        const formattedRecord = {
            ...record,
            attendanceDate: record.attendanceDate ? dayjs(record.attendanceDate) : null,
            checkIn: record.checkIn ? dayjs(record.checkIn) : null,
            checkOut: record.checkOut ? dayjs(record.checkOut) : null,
        };
        form.setFieldsValue(formattedRecord);
        setIsModalOpen(true);
    };

    const handleCallUpdateAttendance = async (formData) => {
        try {
            await updateAttendance(selectedAttendance.id, formData);
            message.success('Cập nhật chấm công thành công!');
            handleGetAllAttendances();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleDeleteAttendance = (record) => {
        setSelectedAttendance(record);
        setModalMode('delete');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallDeleteAttendance = async () => {
        try {
            // await deleteAttendance(selectedAttendance.id);
            message.success('Xóa chấm công thành công!');
            handleGetAllAttendances();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi xóa: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('attendance');
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
                `attendance_${new Date()
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

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreateAttendance(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateAttendance(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteAttendance();
        }
    };

    const handlePaginationChange = (page, pageSize) => {
        setPagination(prev => ({
            ...prev,
            current: page,
            pageSize,
        }));
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Chấm công Mới';
            case 'edit':
                return 'Chỉnh sửa Chấm công';
            case 'delete':
                return 'Xóa Chấm công';
            case 'view':
                return 'Chi tiết Chấm công';
            default:
                return 'Chấm công';
        }
    };

    return (
        <ConfigProvider dateLib={dayjs}>
            <div className={cx('wrapper')}>
                {/* Statistics Cards */}
                <Row gutter={16} className={cx('stats-row')}>
                    <Col span={6}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Có mặt"
                                value={statistics.present}
                                valueStyle={{color: '#3f8600'}}
                                prefix={<CheckCircleOutlined/>}
                            />
                        </Card>
                    </Col>
                    <Col span={6}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Vắng mặt"
                                value={statistics.absent}
                                valueStyle={{color: '#cf1322'}}
                                prefix={<CloseCircleOutlined/>}
                            />
                        </Card>
                    </Col>
                    <Col span={6}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Đi muộn"
                                value={statistics.late}
                                valueStyle={{color: '#faad14'}}
                                prefix={<ClockCircleOutlined/>}
                            />
                        </Card>
                    </Col>
                    <Col span={6}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Về sớm"
                                value={statistics.earlyLeave}
                                valueStyle={{color: '#fa8c16'}}
                                prefix={<FieldTimeOutlined/>}
                            />
                        </Card>
                    </Col>
                </Row>

                {/* Filter Section */}
                <div className={cx('filter-section')}>
                    <Space direction="vertical" size="middle" className={cx('filter-space')}>
                        <div className={cx('filter-inputs')}>
                            <SmartInput
                                size="large"
                                placeholder="Tìm kiếm theo tên nhân viên, mã NV..."
                                icon={<SearchOutlined/>}
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className={cx('search-input')}
                            />
                            <RangePicker
                                size="large"
                                placeholder={['Từ ngày', 'Đến ngày']}
                                format="DD/MM/YYYY"
                                onChange={(dates) => setDateRange(dates)}
                                className={cx('date-picker')}
                            />
                            <Select
                                size="large"
                                placeholder="Lọc theo trạng thái"
                                value={statusFilter}
                                onChange={(value) => setStatusFilter(value)}
                                className={cx('status-select')}
                            >
                                <Option value="ALL">Tất cả</Option>
                                <Option value="PRESENT">Có mặt</Option>
                                <Option value="ABSENT">Vắng mặt</Option>
                                <Option value="LATE">Đi muộn</Option>
                            </Select>

                            <SmartButton
                                title="Thêm mới"
                                icon={<PlusOutlined/>}
                                type="primary"
                                onClick={handleAddAttendance}
                            />
                            <SmartButton
                                title="Excel"
                                icon={<CloudUploadOutlined/>}
                                onClick={handleExportFile}
                            />
                        </div>
                    </Space>
                </div>

                {/* Attendance Container with Pagination on Top */}
                <div className={cx('container')}>
                    <div className={cx('pagination-wrapper')}>
                        <Pagination
                            current={pagination.current}
                            pageSize={pagination.pageSize}
                            total={pagination.total}
                            onChange={handlePaginationChange}
                            showSizeChanger
                            showTotal={(total) => `Tổng ${total} bản ghi chấm công`}
                            pageSizeOptions={['9', '18', '27', '36']}
                        />
                    </div>
                    <Spin spinning={loading}>
                        {attendanceSource.length === 0 ? (
                            <Empty description="Không có dữ liệu chấm công"/>
                        ) : (
                            <div className={cx('cards-grid')}>
                                {attendanceSource.map((attendance) => (
                                    <AttendanceCard
                                        key={attendance.id}
                                        attendance={attendance}
                                        onView={handleViewAttendance}
                                        onEdit={handleEditAttendance}
                                        onDelete={handleDeleteAttendance}
                                    />
                                ))}
                            </div>
                        )}
                    </Spin>
                </div>

                <PopupModal
                    isModalOpen={isModalOpen}
                    setIsModalOpen={setIsModalOpen}
                    title={getModalTitle()}
                    fields={modalMode === 'delete' || modalMode === 'view' ? [] : attendanceModalFields}
                    onSubmit={handleFormSubmit}
                    initialValues={selectedAttendance}
                    isDeleteMode={modalMode === 'delete'}
                    isViewMode={modalMode === 'view'}
                    formInstance={form}
                />
            </div>
        </ConfigProvider>
    );
}

export default Attendance;