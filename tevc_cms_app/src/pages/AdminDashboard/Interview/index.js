import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/JobPosting/JobPosting.module.scss';
import {useState, useEffect} from 'react';
import dayjs from 'dayjs';
import {
    SearchOutlined,
    PlusOutlined,
    CloudUploadOutlined,
    CheckCircleOutlined,
    CloseOutlined,
    CalendarOutlined,
    ClearOutlined,
    UserOutlined
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import {
    Form,
    message,
    Pagination,
    Empty,
    Spin,
    DatePicker,
    Select,
    Row,
    Col,
    Statistic,
    ConfigProvider,
    Card,
    Space,
    Button
} from 'antd';
import {
    getAllInterviews,
    getInterviewsByJobPosting
} from "~/service/admin/interview";
import { exportExcelFile } from '~/service/admin/export_service';
import InterviewCard from "~/components/Layout/components/InterviewCard";
import useDebounce from '~/hooks/useDebounce';

const cx = classNames.bind(styles);
const { RangePicker } = DatePicker;
const { Option } = Select;

function Interview({ jobId = null, onResetFilter }) {
    const [interviews, setInterviews] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedInterview, setSelectedInterview] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const debouncedSearchTerm = useDebounce(searchTerm, 500);
    const [dateRange, setDateRange] = useState(null);
    const [statusFilter, setStatusFilter] = useState('ALL');
    const [typeFilter, setTypeFilter] = useState('ALL');
    const [statistics, setStatistics] = useState({
        completed: 0,
        cancelled: 0,
        scheduled: 0,
    });
    const [form] = Form.useForm();

    const interviewTypeOptions = [
        { value: 'ALL', label: 'Tất cả' },
        { value: 'ONLINE', label: 'Online' },
        { value: 'IN_PERSON', label: 'Trực tiếp' },
        { value: 'PHONE', label: 'Điện thoại' },
    ];

    const interviewModalFields = [
        {
            label: 'Tên ứng viên',
            name: 'candidateName',
            type: 'text',
            rules: [{ required: true, message: 'Vui lòng nhập tên ứng viên!' }],
        },
        {
            label: 'Vị trí ứng tuyển',
            name: 'jobPostingId',
            type: 'number',
            rules: [{ required: true, message: 'Vui lòng nhập ID vị trí!' }],
        },
        {
            label: 'Thời gian phỏng vấn',
            name: 'interviewDate',
            type: 'datetime',
            rules: [{ required: true, message: 'Vui lòng chọn thời gian!' }],
        },
        {
            label: 'Loại phỏng vấn',
            name: 'interviewType',
            type: 'select',
            options: [
                { value: 'ONLINE', label: 'Online' },
                { value: 'IN_PERSON', label: 'Trực tiếp' },
                { value: 'PHONE', label: 'Điện thoại' },
            ],
            rules: [{ required: true, message: 'Vui lòng chọn loại phỏng vấn!' }],
        },
        {
            label: 'Người phỏng vấn ID',
            name: 'interviewerId',
            type: 'number',
            rules: [{ required: true, message: 'Vui lòng nhập ID người phỏng vấn!' }],
        },
        {
            label: 'Đánh giá',
            name: 'rating',
            type: 'number',
            placeholder: '1-5',
        },
        {
            label: 'Nhận xét',
            name: 'feedback',
            type: 'textarea',
            placeholder: 'Nhập nhận xét về ứng viên',
        },
        {
            label: 'Trạng thái',
            name: 'interviewStatus',
            type: 'select',
            options: [
                { value: 'SCHEDULED', label: 'Đã lên lịch' },
                { value: 'COMPLETED', label: 'Hoàn thành' },
                { value: 'CANCELED', label: 'Hủy' },
            ],
        },
    ];

    useEffect(() => {
        handleFilterInterviews();
    }, []);

    useEffect(() => {
        setPagination(prev => ({ ...prev, current: 1 }));
        handleFilterInterviews();
    }, [debouncedSearchTerm, dateRange, statusFilter, typeFilter, jobId]);

    useEffect(() => {
        handleFilterInterviews();
    }, [pagination.current, pagination.pageSize]);

    // Transform API data to match component expectations
    const transformInterviewData = (apiData) => {
        return apiData.map(item => ({
            ...item,
            // Map API fields to component fields
            datetime: item.interviewDate ? dayjs(item.interviewDate) : null,
            location: item.interviewType === 'ONLINE' ? 'Online' :
                item.interviewType === 'IN_PERSON' ? 'Trực tiếp' :
                    item.interviewType === 'PHONE' ? 'Điện thoại' : 'N/A',
            status: item.interviewStatus,
            type: item.interviewType,
            // Add empty arrays to prevent undefined errors
            interviewers: item.interviewerId ? [{ id: item.interviewerId, name: `Interviewer ${item.interviewerId}` }] : [],
        }));
    };

    const calculateStatistics = (data) => {
        const stats = {
            completed: 0,
            cancelled: 0,
            scheduled: 0,
        };

        data.forEach(item => {
            switch (item.interviewStatus) {
                case 'COMPLETED':
                    stats.completed++;
                    break;
                case 'CANCELED':
                    stats.cancelled++;
                    break;
                case 'SCHEDULED':
                    stats.scheduled++;
                    break;
                default:
                    break;
            }
        });

        setStatistics(stats);
    };

    const handleFilterInterviews = async () => {
        setLoading(true);
        try {
            const params = {
                page: pagination.current - 1,
                size: pagination.pageSize,
            };

            if (debouncedSearchTerm) {
                params.candidateName = debouncedSearchTerm.trim();
            }

            if (dateRange && dateRange.length === 2) {
                params.startDate = dateRange[0].format('YYYY-MM-DD');
                params.endDate = dateRange[1].format('YYYY-MM-DD');
            }

            if (statusFilter !== 'ALL') {
                params.interviewStatus = statusFilter;
            }

            if (typeFilter && typeFilter !== 'ALL') {
                params.interviewType = typeFilter;
            }

            let response;
            if (jobId) {
                response = await getInterviewsByJobPosting(jobId, params);
            } else {
                response = await getAllInterviews(params);
            }

            if (response && Array.isArray(response.content)) {
                const transformedData = transformInterviewData(response.content);
                setInterviews(transformedData);
                setPagination({
                    current: pagination.current,
                    pageSize: pagination.pageSize,
                    total: response.totalElements ?? 0,
                });
                calculateStatistics(response.content);
            } else {
                console.error('Invalid response format:', response);
                setInterviews([]);
                setPagination(prev => ({ ...prev, total: 0 }));
            }
        } catch (error) {
            console.error('Error filtering interviews:', error);
            message.error('Lỗi khi lọc danh sách phỏng vấn');
            setInterviews([]);
            setPagination(prev => ({ ...prev, total: 0 }));
        } finally {
            setLoading(false);
        }
    };

    const handleResetCandidateFilter = () => {
        onResetFilter && onResetFilter();
        setSearchTerm('');
        setDateRange(null);
        setStatusFilter('ALL');
        setTypeFilter('ALL');
    };

    const handleAddInterview = () => {
        setModalMode('create');
        setSelectedInterview(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleEditInterview = (interview) => {
        setSelectedInterview(interview);
        setModalMode('edit');
        const formattedInterview = {
            ...interview,
            interviewDate: interview.interviewDate ? dayjs(interview.interviewDate) : null,
        };
        form.setFieldsValue(formattedInterview);
        setIsModalOpen(true);
    };

    const handleCompleteInterview = async (interview) => {
        try {
            // Call API to update status to COMPLETED
            // await updateInterviewStatus(interview.id, 'COMPLETED');
            message.success('Đã hoàn thành phỏng vấn!');
            handleFilterInterviews();
        } catch (error) {
            message.error('Lỗi khi cập nhật trạng thái');
        }
    };

    const handleCancelInterview = async (interview) => {
        try {
            // Call API to update status to CANCELED
            // await updateInterviewStatus(interview.id, 'CANCELED');
            message.success('Đã hủy lịch phỏng vấn!');
            handleFilterInterviews();
        } catch (error) {
            message.error('Lỗi khi hủy lịch phỏng vấn');
        }
    };

    const handleCallCreateInterview = async (formData) => {
        try {
            const dataToSubmit = {
                ...formData,
                interviewDate: formData.interviewDate.valueOf(), // Convert to timestamp
            };

            // await createInterview(dataToSubmit);
            message.success('Thêm lịch phỏng vấn thành công!');
            handleFilterInterviews();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi tạo phỏng vấn: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleCallUpdateInterview = async (formData) => {
        try {
            const dataToSubmit = {
                ...formData,
                interviewDate: formData.interviewDate.valueOf(), // Convert to timestamp
            };

            // await updateInterview(selectedInterview.id, dataToSubmit);
            message.success('Cập nhật lịch phỏng vấn thành công!');
            handleFilterInterviews();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('interviews');
            if (!response.headers['content-type'].includes('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `interviews_${new Date().toISOString().replace(/[-:]/g, '')}.xlsx`);
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
            handleCallCreateInterview(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateInterview(formData);
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
                return 'Thêm Lịch Phỏng Vấn Mới';
            case 'edit':
                return 'Chỉnh sửa Lịch Phỏng Vấn';
            default:
                return 'Lịch Phỏng Vấn';
        }
    };

    return (
        <ConfigProvider dateLib={dayjs}>
            <div className={cx('trailer-wrapper')}>
                {/* Statistics Cards */}
                <Row gutter={16} className={cx('stats-row')}>
                    <Col span={8}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Đã lên lịch"
                                value={statistics.scheduled}
                                valueStyle={{ color: '#1890ff' }}
                                prefix={<CalendarOutlined />}
                            />
                        </Card>
                    </Col>
                    <Col span={8}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Hoàn thành"
                                value={statistics.completed}
                                valueStyle={{ color: '#3f8600' }}
                                prefix={<CheckCircleOutlined />}
                            />
                        </Card>
                    </Col>
                    <Col span={8}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Đã hủy"
                                value={statistics.cancelled}
                                valueStyle={{ color: '#cf1322' }}
                                prefix={<CloseOutlined />}
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
                                placeholder="Tìm kiếm theo tên ứng viên..."
                                icon={<SearchOutlined />}
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
                                <Option value="SCHEDULED">Đã lên lịch</Option>
                                <Option value="COMPLETED">Hoàn thành</Option>
                                <Option value="CANCELED">Đã hủy</Option>
                            </Select>
                            <Select
                                size="large"
                                placeholder="Lọc theo loại"
                                value={typeFilter}
                                onChange={(value) => setTypeFilter(value)}
                                className={cx('type-select')}
                            >
                                {interviewTypeOptions.map((opt) => (
                                    <Option key={opt.value} value={opt.value}>
                                        {opt.label}
                                    </Option>
                                ))}
                            </Select>

                            {jobId && (
                                <Button
                                    type="default"
                                    icon={<ClearOutlined />}
                                    onClick={handleResetCandidateFilter}
                                    size="large"
                                >
                                    Xóa filter ứng viên
                                </Button>
                            )}

                            <SmartButton
                                title="Thêm mới"
                                icon={<PlusOutlined />}
                                type="primary"
                                onClick={handleAddInterview}
                            />
                            <SmartButton
                                title="Xuất Excel"
                                icon={<CloudUploadOutlined />}
                                onClick={handleExportFile}
                            />
                        </div>
                    </Space>
                </div>

                {/* Interview List */}
                <div className={cx('trailer-container')} style={{ padding: '20px' }}>
                    <div className={cx('pagination-wrapper')}>
                        <Pagination
                            current={pagination.current}
                            pageSize={pagination.pageSize}
                            total={pagination.total}
                            onChange={handlePaginationChange}
                            showSizeChanger
                            showTotal={(total) => `Tổng ${total} lịch phỏng vấn`}
                            pageSizeOptions={['5', '10', '20', '50']}
                        />
                    </div>

                    <Spin spinning={loading}>
                        {interviews.length === 0 ? (
                            <Empty description="Không có lịch phỏng vấn nào" />
                        ) : (
                            <div className={cx('interview-list')}>
                                {interviews.map((interview) => (
                                    <InterviewCard
                                        key={interview.id}
                                        interview={interview}
                                        onEdit={handleEditInterview}
                                        onComplete={handleCompleteInterview}
                                        onCancel={handleCancelInterview}
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
                    fields={interviewModalFields}
                    onSubmit={handleFormSubmit}
                    initialValues={selectedInterview}
                    formInstance={form}
                />
            </div>
        </ConfigProvider>
    );
}

export default Interview;