import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/JobPosting/JobPosting.module.scss';
import {useState, useEffect} from 'react';
import dayjs from 'dayjs';
import {
    SearchOutlined,
    PlusOutlined,
    FilterOutlined,
    CloudUploadOutlined,
    EyeOutlined,
    CloseOutlined,
    FileOutlined,
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
    ConfigProvider, Card, Space
} from 'antd';
import {getAllJobPostings, filterJobPostings} from '~/service/admin/job-posting';
import {exportExcelFile} from '~/service/admin/export_service';
import JobPostingCard from "~/components/Layout/components/JobPostingCard";
import useDebounce from '~/hooks/useDebounce';
import {getAllDepartmentsNoPaging} from "~/service/admin/department";
import {getAllPositions} from "~/service/admin/position";

const cx = classNames.bind(styles);
const {RangePicker} = DatePicker;
const {Option} = Select;

function JobPosting({ onApplicantClick }) {
    const [jobPostings, setJobPostings] = useState([]);
    const [loading, setLoading] = useState(false);
    const [departmentOptions, setDepartmentOptions] = useState([]);
    const [positionOptions, setPositionOptions] = useState([]);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 6,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedJobPosting, setSelectedJobPosting] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const debouncedSearchTerm = useDebounce(searchTerm, 500);
    const [dateRange, setDateRange] = useState(null);
    const [statusFilter, setStatusFilter] = useState('ALL');
    const [locationFilter, setLocationFilter] = useState(null);
    const [statistics, setStatistics] = useState({
        open: 0,
        closed: 0,
        draft: 0,
    });
    const [form] = Form.useForm();

    const locationOptions = [
        { value: 'ALL', label: 'Tất cả' },
        { value: 'Hà Nội', label: 'Hà Nội' },
        { value: 'TP.HCM', label: 'TP. Hồ Chí Minh' },
        { value: 'Cần Thơ', label: 'Cần Thơ' },
        { value: 'Đà Nẵng', label: 'Đà Nẵng' },
        { value: 'Hải Phòng', label: 'Hải Phòng' },
    ];

    const jobPostingModalFields = [
        {
            label: 'Tiêu đề',
            name: 'title',
            type: 'text',
            rules: [{ required: true, message: 'Vui lòng nhập tiêu đề!' }],
        },
        {
            label: 'Địa điểm',
            name: 'location',
            type: 'text',
            rules: [{ required: true, message: 'Vui lòng nhập địa điểm!' }],
        },
        {
            label: 'Mô tả công việc',
            name: 'description',
            type: 'textarea',
            rules: [{ required: true, message: 'Vui lòng nhập mô tả công việc!' }],
        },
        {
            label: 'Yêu cầu',
            name: 'requirements',
            type: 'textarea',
        },
        {
            label: 'Mức lương',
            name: 'salaryRange',
            type: 'text',
            fullWidth: true,
        },
        {
            label: 'Ngày đăng tuyển',
            name: 'postingDate',
            type: 'date',
        },
        {
            label: 'Hạn nộp hồ sơ',
            name: 'closingDate',
            type: 'date',
        },
        {
            label: 'Phòng ban',
            name: 'departmentId',
            type: 'select',
            options: departmentOptions,
        },
        {
            label: 'Vị trí tuyển dụng',
            name: 'positionId',
            type: 'select',
            options: positionOptions,
        },
        {
            label: 'Trạng thái',
            name: 'jobPostingStatus',
            type: 'select',
            options: [
                { value: 'OPEN', label: 'Mở' },
                { value: 'CLOSED', label: 'Đóng' },
                { value: 'DRAFT', label: 'Nháp' },
            ],
        },
    ];

    useEffect(() => {
        fetchDepartmentAndPositionOptions();
        handleFilterJobPostings();
    }, []);

    useEffect(() => {
        setPagination(prev => ({ ...prev, current: 1 }));
        handleFilterJobPostings();
    }, [debouncedSearchTerm, dateRange, statusFilter, locationFilter]);

    useEffect(() => {
        handleFilterJobPostings();
    }, [pagination.current, pagination.pageSize]);

    const handleGetAllJobPostings = async (page = 1, pageSize = 6) => {
        setLoading(true);
        try {
            const response = await getAllJobPostings({page: page - 1, size: pageSize});
            if (response && Array.isArray(response.content)) {
                setJobPostings(response.content);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
                calculateStatistics(response.content);
            } else {
                console.error('Invalid data format:', response);
                setJobPostings([]);
            }
        } catch (error) {
            console.error('Error fetching job postings:', error);
            setJobPostings([]);
        } finally {
            setLoading(false);
        }
    };

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

    const calculateStatistics = (data) => {
        const stats = {
            open: 0,
            closed: 0,
            draft: 0,
        };

        data.forEach(item => {
            switch (item.jobPostingStatus) {
                case 'OPEN':
                    stats.open++;
                    break;
                case 'CLOSED':
                    stats.closed++;
                    break;
                case 'DRAFT':
                    stats.draft++;
                    break;
                default:
                    break;
            }
        });

        setStatistics(stats);
    };

    const handleFilterJobPostings = async () => {
        setLoading(true);
        try {
            const params = {
                page: pagination.current - 1,
                size: pagination.pageSize,
            };

            if (debouncedSearchTerm) {
                params.title = debouncedSearchTerm.trim();
            }

            if (dateRange && dateRange.length === 2) {
                params.startDate = dateRange[0].format('YYYY-MM-DD');
                params.endDate = dateRange[1].format('YYYY-MM-DD');
            }

            if (statusFilter !== 'ALL') {
                params.status = statusFilter;
            }

            if (locationFilter && locationFilter !== 'ALL') {
                params.location = locationFilter;
            }

            const response = await filterJobPostings(params);
            if (response && Array.isArray(response.content)) {
                setJobPostings(response.content);
                setPagination({
                    current: pagination.current,
                    pageSize: pagination.pageSize,
                    total: response.totalElements ?? 0,
                });
                calculateStatistics(response.content);
            } else {
                console.error('Invalid response format:', response);
                setJobPostings([]);
                setPagination(prev => ({ ...prev, total: 0 }));
            }
        } catch (error) {
            console.error('Error filtering job postings:', error);
            message.error('Lỗi khi lọc job postings');
            setJobPostings([]);
            setPagination(prev => ({ ...prev, total: 0 }));
        } finally {
            setLoading(false);
        }
    };

    const handleAddJobPosting = () => {
        setModalMode('create');
        setSelectedJobPosting(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleViewJobPosting = (record) => {
        setSelectedJobPosting(record);
        setModalMode('view');
        const formattedRecord = {
            ...record,
            postingDate: record.postingDate ? dayjs(record.postingDate) : null,
            closingDate: record.closingDate ? dayjs(record.closingDate) : null,
        };
        form.setFieldsValue(formattedRecord);
        setIsModalOpen(true);
    };

    const handleEditJobPosting = (record) => {
        setSelectedJobPosting(record);
        setModalMode('edit');
        const formattedRecord = {
            ...record,
            postingDate: record.postingDate ? dayjs(record.postingDate) : null,
            closingDate: record.closingDate ? dayjs(record.closingDate) : null,
        };
        form.setFieldsValue(formattedRecord);
        setIsModalOpen(true);
    };

    const handleDeleteJobPosting = (record) => {
        setSelectedJobPosting(record);
        setModalMode('delete');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallCreateJobPosting = async (formData) => {
        try {
            // await createJobPosting(formData);
            message.success('Thêm job posting thành công!');
            handleFilterJobPostings();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error creating job posting: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleCallUpdateJobPosting = async (formData) => {
        try {
            // await updateJobPosting(selectedJobPosting.id, formData);
            message.success('Cập nhật job posting thành công!');
            handleFilterJobPostings();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error updating job posting: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleCallDeleteJobPosting = async () => {
        try {
            // await deleteJobPosting(selectedJobPosting.id);
            message.success('Xóa job posting thành công!');
            handleFilterJobPostings();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error deleting job posting: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('job-posting');
            if (!response.headers['content-type'].includes('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `job-postings_${new Date().toISOString().replace(/[-:]/g, '')}.xlsx`);
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
            handleCallCreateJobPosting(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateJobPosting(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteJobPosting();
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
                return 'Thêm Job Posting Mới';
            case 'edit':
                return 'Chỉnh sửa Job Posting';
            case 'delete':
                return 'Xóa Job Posting';
            case 'view':
                return 'Chi tiết Job Posting';
            default:
                return 'Job Posting';
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
                                title="Mở"
                                value={statistics.open}
                                valueStyle={{color: '#3f8600'}}
                                prefix={<EyeOutlined/>}
                            />
                        </Card>
                    </Col>
                    <Col span={8}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Đóng"
                                value={statistics.closed}
                                valueStyle={{color: '#cf1322'}}
                                prefix={<CloseOutlined/>}
                            />
                        </Card>
                    </Col>
                    <Col span={8}>
                        <Card className={cx('stat-card')}>
                            <Statistic
                                title="Nháp"
                                value={statistics.draft}
                                valueStyle={{color: '#1890ff'}}
                                prefix={<FileOutlined/>}
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
                                placeholder="Tìm kiếm theo title, location..."
                                icon={<SearchOutlined/>}
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                                className={cx('search-input')}
                            />
                            <RangePicker
                                size="large"
                                placeholder={['Từ ngày đăng', 'Đến ngày đăng']}
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
                                <Option value="OPEN">Mở</Option>
                                <Option value="CLOSED">Đóng</Option>
                                <Option value="DRAFT">Nháp</Option>
                            </Select>
                            <Select
                                size="large"
                                placeholder="Lọc theo địa điểm"
                                value={locationFilter}
                                onChange={(value) => setLocationFilter(value)}
                                className={cx('location-select')}
                            >
                                {locationOptions.map((opt) => (
                                    <Option key={opt.value} value={opt.value}>
                                        {opt.label}
                                    </Option>
                                ))}
                            </Select>

                            <SmartButton
                                title="Thêm mới"
                                icon={<PlusOutlined/>}
                                type="primary"
                                onClick={handleAddJobPosting}
                            />
                            <SmartButton
                                title="Excel"
                                icon={<CloudUploadOutlined/>}
                                onClick={handleExportFile}
                            />
                        </div>
                    </Space>
                </div>

                <div className={cx('trailer-container')} style={{padding: '20px'}}>
                    <div className={cx('pagination-wrapper')}>
                        <Pagination
                            current={pagination.current}
                            pageSize={pagination.pageSize}
                            total={pagination.total}
                            onChange={handlePaginationChange}
                            showSizeChanger
                            showTotal={(total) => `Tổng ${total} tin tuyển dụng`}
                            pageSizeOptions={['6', '12', '18', '24']}
                        />
                    </div>
                    <Spin spinning={loading}>
                        {jobPostings.length === 0 ? (
                            <Empty description="Không có job posting nào"/>
                        ) : (
                            <div style={{
                                display: 'grid',
                                gridTemplateColumns: 'repeat(auto-fill, minmax(500px, 1fr))',
                                gap: '20px'
                            }}>
                                {jobPostings.map((job) => (
                                    <JobPostingCard
                                        key={job.id}
                                        job={job}
                                        onView={handleViewJobPosting}
                                        onEdit={handleEditJobPosting}
                                        onDelete={handleDeleteJobPosting}
                                        onApplicantClick={onApplicantClick}
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
                    fields={modalMode === 'delete' || modalMode === 'view' ? [] : jobPostingModalFields}
                    onSubmit={handleFormSubmit}
                    initialValues={selectedJobPosting}
                    isDeleteMode={modalMode === 'delete'}
                    isViewMode={modalMode === 'view'}
                    formInstance={form}
                />
            </div>
        </ConfigProvider>
    );
}

export default JobPosting;