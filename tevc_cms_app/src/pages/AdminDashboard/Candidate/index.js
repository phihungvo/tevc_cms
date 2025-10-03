import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Department/Department.module.scss';
import {useState, useEffect} from 'react';
import moment from 'moment';
import {
    SearchOutlined,
    PlusOutlined,
    FilterOutlined,
    CloudUploadOutlined,
    EditOutlined,
    DeleteOutlined,
    EyeOutlined,
    MailOutlined,
    PhoneOutlined,
    CalendarOutlined,
    FileTextOutlined,
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import {Form, message, Tag, Pagination, Card, Avatar, Space, Empty, Spin} from 'antd';
import {getAllCandidates} from '~/service/admin/candidate';
import {getAllJobPostings} from '~/service/admin/job-posting';
import {exportExcelFile} from '~/service/admin/export_service';
import CandidateCard from "~/components/Layout/components/CandidateCard";

const cx = classNames.bind(styles);

function Candidate() {
    const [candidateSource, setCandidateSource] = useState([]);
    const [jobPostingSource, setJobPostingSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 6,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedCandidate, setSelectedCandidate] = useState(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [form] = Form.useForm();

    const candidateModalFields = [
        {
            label: 'First Name',
            name: 'firstName',
            type: 'text',
            rules: [{required: true, message: 'First name is required!'}],
        },
        {
            label: 'Last Name',
            name: 'lastName',
            type: 'text',
            rules: [{required: true, message: 'Last name is required!'}],
        },
        {
            label: 'Email',
            name: 'email',
            type: 'email',
            rules: [
                {required: true, message: 'Email is required!'},
                {type: 'email', message: 'Please enter a valid email!'}
            ],
        },
        {
            label: 'Phone',
            name: 'phone',
            type: 'text',
            rules: [{required: true, message: 'Phone is required!'}],
        },
        {
            label: 'Resume URL',
            name: 'resumeUrl',
            type: 'text',
        },
        {
            label: 'Application Date',
            name: 'applicationDate',
            type: 'date',
        },
        {
            label: 'Status',
            name: 'status',
            type: 'select',
            options: [
                {value: 'NEW', label: 'Mới'},
                {value: 'SCREENING', label: 'Sàng lọc'},
                {value: 'INTERVIEW', label: 'Phỏng vấn'},
                {value: 'OFFERED', label: 'Đã offer'},
                {value: 'HIRED', label: 'Đã tuyển'},
                {value: 'REJECTED', label: 'Từ chối'},
            ],
        },
        {
            label: 'Job Postings',
            name: 'jobPostingIds',
            type: 'multiselect',
            options: jobPostingSource,
        },
    ];

    useEffect(() => {
        handleGetAllJobPostings();
        handleGetAllCandidates();
    }, []);

    useEffect(() => {
        if (searchTerm) {
            handleSearch();
        } else {
            handleGetAllCandidates();
        }
    }, [searchTerm]);

    const handleGetAllJobPostings = async (page = 0, size = 10) => {
        try {
            const response = await getAllJobPostings({page, size});
            const mappedJobPostings = response.map(job => ({
                value: job.id,
                label: job.title,
            }));
            setJobPostingSource(mappedJobPostings);
        } catch (error) {
            console.error('Error fetching job postings:', error);
            setJobPostingSource([]);
        }
    };

    const handleGetAllCandidates = async (page = 1, size = 10) => {
        setLoading(true);
        try {
            const response = await getAllCandidates({page: page - 1, size});
            if (response && Array.isArray(response.content)) {
                setCandidateSource(response.content);
                setPagination({
                    current: page,
                    pageSize: size,
                    total: response.totalElements ?? 0,
                });
            } else {
                console.error('Invalid data format:', response);
                setCandidateSource([]);
            }
        } catch (error) {
            console.error('Error fetching candidates:', error);
            setCandidateSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async () => {
        setLoading(true);
        try {
            const response = await getAllCandidates({
                page: 0,
                pageSize: pagination.pageSize,
                search: searchTerm
            });
            if (response && Array.isArray(response.content)) {
                setCandidateSource(response.content);
                setPagination({
                    current: 1,
                    pageSize: pagination.pageSize,
                    total: response.totalElements,
                });
            }
        } catch (error) {
            console.error('Error searching candidates:', error);
            setCandidateSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddCandidate = () => {
        setModalMode('create');
        setSelectedCandidate(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreateCandidate = async (formData) => {
        try {
            // await createCandidate(formData);
            message.success('Thêm ứng viên thành công!');
            handleGetAllCandidates();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error creating candidate: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleViewCandidate = (record) => {
        setSelectedCandidate(record);
        setModalMode('view');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleEditCandidate = (record) => {
        setSelectedCandidate(record);
        setModalMode('edit');
        form.setFieldsValue({
            ...record,
            jobPostingIds: record.jobPostings?.map(job => job.id) || []
        });
        setIsModalOpen(true);
    };

    const handleCallUpdateCandidate = async (formData) => {
        try {
            // await updateCandidate(selectedCandidate.id, formData);
            message.success('Cập nhật ứng viên thành công!');
            handleGetAllCandidates();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error updating candidate: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleDeleteCandidate = (record) => {
        setSelectedCandidate(record);
        setModalMode('delete');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallDeleteCandidate = async () => {
        try {
            // await deleteCandidate(selectedCandidate.id);
            message.success('Xóa ứng viên thành công!');
            handleGetAllCandidates();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error deleting candidate: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('candidate');
            if (!response.headers['content-type'].includes('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `candidates_${new Date().toISOString().replace(/[-:]/g, '')}.xlsx`);
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
            handleCallCreateCandidate(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateCandidate(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteCandidate();
        }
    };

    const handlePaginationChange = (page, pageSize) => {
        handleGetAllCandidates(page, pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Ứng viên Mới';
            case 'edit':
                return 'Chỉnh sửa Ứng viên';
            case 'delete':
                return 'Xóa Ứng viên';
            case 'view':
                return 'Chi tiết Ứng viên';
            default:
                return 'Ứng viên';
        }
    };

    const getStatusTag = (status) => {
        const statusConfig = {
            NEW: {color: 'blue', text: 'Mới'},
            SCREENING: {color: 'cyan', text: 'Sàng lọc'},
            INTERVIEW: {color: 'orange', text: 'Phỏng vấn'},
            OFFERED: {color: 'green', text: 'Đã offer'},
            HIRED: {color: 'success', text: 'Đã tuyển'},
            REJECTED: {color: 'red', text: 'Từ chối'},
        };
        const config = statusConfig[status] || statusConfig.NEW;
        return <Tag color={config.color}>{config.text}</Tag>;
    };

    const getInitials = (firstName, lastName) => {
        return `${firstName?.charAt(0) || ''}${lastName?.charAt(0) || ''}`.toUpperCase();
    };

    return (
        <div className={cx('trailer-wrapper')}>
            <div className={cx('sub_header')}>
                <SmartInput
                    size="large"
                    placeholder="Tìm kiếm ứng viên theo tên, email..."
                    icon={<SearchOutlined/>}
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
                <div className={cx('features')}>
                    <SmartButton
                        title="Thêm mới"
                        icon={<PlusOutlined/>}
                        type="primary"
                        onClick={handleAddCandidate}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined/>}/>
                    <SmartButton
                        title="Excel"
                        icon={<CloudUploadOutlined/>}
                        onClick={handleExportFile}
                    />
                </div>
            </div>

            <div className={cx('trailer-container')} style={{padding: '20px'}}>
                <Spin spinning={loading}>
                    {candidateSource.length === 0 ? (
                        <Empty description="Không có ứng viên nào"/>
                    ) : (
                        <div style={{
                            display: 'grid',
                            gridTemplateColumns: 'repeat(auto-fill, minmax(500px, 1fr))',
                            gap: '20px'
                        }}>
                            {candidateSource.map((candidate) => (
                                <CandidateCard
                                    key={candidate.id}
                                    candidate={candidate}
                                    onView={handleViewCandidate}
                                    onEdit={handleEditCandidate}
                                    onDelete={handleDeleteCandidate}
                                />
                            ))}
                        </div>
                    )}

                    {/* Phần Pagination giữ nguyên */}
                    <div style={{marginTop: '24px', display: 'flex', justifyContent: 'center'}}>
                        <Pagination
                            current={pagination.current}
                            pageSize={pagination.pageSize}
                            total={pagination.total}
                            onChange={handlePaginationChange}
                            showSizeChanger
                            showTotal={(total) => `Tổng ${total} ứng viên`}
                            pageSizeOptions={['6', '12', '18', '24']}
                        />
                    </div>
                </Spin>
            </div>


            <PopupModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                title={getModalTitle()}
                fields={modalMode === 'delete' || modalMode === 'view' ? [] : candidateModalFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedCandidate}
                isDeleteMode={modalMode === 'delete'}
                isViewMode={modalMode === 'view'}
                formInstance={form}
            />
        </div>
    );
}

export default Candidate;