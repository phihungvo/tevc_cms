import React, {useState, useEffect} from 'react';
import classNames from 'classnames/bind';
import styles from './Contract.module.scss';
import SmartTable from '~/components/Layout/components/SmartTable';
import {
    SearchOutlined,
    PlusOutlined,
    FilterOutlined,
    CloudUploadOutlined,
    EditOutlined,
    DeleteOutlined,
    EyeOutlined,
    FileTextOutlined,
    DownloadOutlined,
    UploadOutlined,
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import {Form, message, Tag, Modal, Tabs, Button, Upload, Empty, Progress, Spin} from 'antd';
import {
    createContract,
    getAllByEmployeePaged,
    uploadFilesForContract,
    // updateContract,
    // getFilesForContract,
} from "~/service/admin/contract";
import {getAllPositions} from '~/service/admin/position'
import {exportExcelFile} from '~/service/admin/export_service';
import {getAllDepartmentsNoPaging} from "~/service/admin/department";

const cx = classNames.bind(styles);
const {TabPane} = Tabs;

function Contract({employeeId}) {
    const [contractSource, setContractSource] = useState([]);
    const [positionOptions, setPositionOptions] = useState([]);
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);
    const [selectedRows, setSelectedRows] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedContract, setSelectedContract] = useState(null);
    const [isViewModalOpen, setIsViewModalOpen] = useState(false);
    const [currentFiles, setCurrentFiles] = useState([]);
    const [uploadingFiles, setUploadingFiles] = useState([]);
    const [uploadProgress, setUploadProgress] = useState({});
    const [form] = Form.useForm();

    const getStatusTag = (status) => {
        const statusConfig = {
            'ACTIVE': {color: 'success', text: 'Đang hiệu lực'},
            'PENDING': {color: 'warning', text: 'Chờ duyệt'},
            'EXPIRED': {color: 'error', text: 'Hết hạn'},
            'TERMINATED': {color: 'default', text: 'Đã chấm dứt'},
        };
        const config = statusConfig[status] || {color: 'default', text: status};
        return <Tag color={config.color}>{config.text}</Tag>;
    };

    const baseColumns = [
        {
            title: 'Loại hợp đồng',
            dataIndex: 'contractType',
            key: 'contractType',
            width: 150,
            fixed: 'left',
            render: (text) => <strong>{text}</strong>,
        },
        {
            title: 'Thời hạn hợp đồng',
            dataIndex: 'contractDuration',
            key: 'contractDuration',
            width: 200,
            render: (_, record) => {
                const start = record.startDate ? new Date(record.startDate).toLocaleDateString('vi-VN') : 'N/A';
                const end = record.endDate ? new Date(record.endDate).toLocaleDateString('vi-VN') : 'N/A';
                return (
                    <div className={cx('date-range')}>
                        <span className={cx('date-start')}>{start}</span>
                        <span className={cx('date-arrow')}>→</span>
                        <span className={cx('date-end')}>{end}</span>
                    </div>
                );
            }
        },
        {
            title: 'Lương cơ bản',
            dataIndex: 'basicSalary',
            key: 'basicSalary',
            width: 150,
            render: (value) =>
                value != null ? (
                    <span className={cx('salary-value')}>
                        {value.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'})}
                    </span>
                ) : 'N/A',
        },
        {
            title: 'Vị trí',
            dataIndex: 'positionId',
            key: 'positionId',
            width: 150,
        },
        {
            title: 'Trạng thái',
            dataIndex: 'status',
            key: 'status',
            width: 120,
            render: (status) => getStatusTag(status),
        },
        {
            title: 'Ngày ký',
            dataIndex: 'signedDate',
            key: 'signedDate',
            width: 120,
            render: (date) => (date ? new Date(date).toLocaleDateString('vi-VN') : 'N/A'),
        },
        {
            title: 'Tệp đính kèm',
            dataIndex: 'fileIds',
            key: 'fileIds',
            width: 120,
            align: 'center',
            render: (fileIds) => {
                const fileCount = fileIds?.length || 0;
                return fileCount > 0 ? (
                    <Tag color="blue" icon={<FileTextOutlined/>}>
                        {fileCount} file
                    </Tag>
                ) : (
                    <Tag color="default">Không có</Tag>
                );
            },
        },
        {
            title: 'Hành động',
            fixed: 'right',
            width: 140,
            render: (_, record) => (
                <div className={cx('action-buttons')}>
                    <SmartButton
                        type="default"
                        icon={<EyeOutlined/>}
                        buttonWidth={40}
                        onClick={() => handleViewContract(record)}
                    />
                    <SmartButton
                        type="primary"
                        icon={<EditOutlined/>}
                        buttonWidth={40}
                        onClick={() => handleEditContract(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined/>}
                        buttonWidth={40}
                        onClick={() => handleDeleteContract(record)}
                    />
                </div>
            ),
        },
    ];

    const contractModalFields = [
        {
            label: 'Loại hợp đồng',
            name: 'contractType',
            type: 'select',
            rules: [{required: true, message: 'Vui lòng nhập loại hợp đồng!'}],
            options: [
                {value: 'FREELANCE', label: 'Tự do'},
                {value: 'FULL_TIME', label: 'Toàn thời gian'},
                {value: 'INTERNSHIP', label: 'Thực tập'},
                {value: 'PART_TIME', label: 'Bán thời gian'},
                {value: 'PROBATION', label: 'Thử việc'},
                {value: 'TEMPORARY', label: 'Tạm thời'},
            ]
        },
        {
            label: 'Vị trí',
            name: 'positionId',
            type: 'select',
            options: positionOptions,
        },
        {
            label: 'Ngày bắt đầu',
            name: 'startDate',
            type: 'date',
            //rules: [{required: true, message: 'Vui lòng chọn ngày bắt đầu!'}],
        },
        {
            label: 'Ngày kết thúc',
            name: 'endDate',
            type: 'date',
        },
        {
            label: 'Lương cơ bản',
            name: 'basicSalary',
            type: 'number',
            rules: [{required: true, message: 'Vui lòng nhập lương cơ bản!'}],
        },
        {
            label: 'Trạng thái',
            name: 'status',
            type: 'select',
            options: [
                {value: 'ACTIVE', label: 'Đang hiệu lực'},
                {value: 'PENDING', label: 'Chờ duyệt'},
                {value: 'EXPIRED', label: 'Hết hạn'},
                {value: 'TERMINATED', label: 'Đã chấm dứt'},
            ],
        },
        {
            label: 'Ngày ký',
            name: 'signedDate',
            type: 'date',
        },
        {
            label: 'Thời gian thử việc (tháng)',
            name: 'probationPeriod',
            type: 'number',
        },
        {
            label: 'File đính kèm',
            name: 'files',
            type: 'upload',
            multiple: true,
            rules: [{required: false}],
            uploadProps: {
                multiple: true,
                beforeUpload: (file) => {
                    const isLt10M = file.size / 1024 / 1024 < 10;
                    if (!isLt10M) {
                        message.error('File phải nhỏ hơn 10MB!');
                        return false;
                    }
                    return false;  // Không upload ngay, chờ submit
                },
                onChange: (info) => {
                    form.setFieldsValue({files: info.fileList});
                },
            },
        }
    ];

    const handleGetAllContracts = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllByEmployeePaged(employeeId, {page: page - 1, pageSize});
            const contractList = response.content;

            if (response && Array.isArray(contractList)) {
                const transformedContracts = contractList.map((contract) => ({
                    ...contract,
                    key: contract.id,
                }));

                setContractSource(transformedContracts);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Invalid data contracts: ', response);
                setContractSource([]);
            }
        } catch (error) {
            console.error('Error fetching contracts', error);
            setContractSource([]);
        } finally {
            setLoading(false);
        }
    };

    const fetchPositionOptions = async () => {
        try {
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

    const handleViewContract = async (record) => {
        setSelectedContract(record);
        setLoading(true);
        try {
            // const files = await getFilesForContract(record.id);
            // setCurrentFiles(files.map(file => ({
            //     id: file.id,
            //     name: file.originalName,
            //     url: file.presignedUrl || `${window.location.origin}/api/files/download/${file.fileName}`,  // Giả sử presignedUrl hoặc download endpoint
            //     type: file.contentType.split('/')[1],
            //     size: (file.size / 1024 / 1024).toFixed(1) + ' MB',
            // })));
        } catch (error) {
            message.error('Lỗi tải files');
            setCurrentFiles([]);
        } finally {
            setLoading(false);
        }
        setIsViewModalOpen(true);
    };

    const handleAddContract = () => {
        setModalMode('create');
        setSelectedContract(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreateContract = async (formData) => {
        try {
            await createContract(formData);
            message.success('Tạo hợp đồng thành công!');
            handleGetAllContracts();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi tạo hợp đồng: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditContract = async (record) => {
        setSelectedContract(record);
        setModalMode('edit');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdateContract = async (formData) => {
        try {
            // await updateContract(selectedContract.id, formData);
            message.success('Cập nhật hợp đồng thành công!');
            handleGetAllContracts();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleDeleteContract = (record) => {
        setModalMode('delete');
        setSelectedRowKeys([record.id]);
        setSelectedRows([record]);
        setIsModalOpen(true);
    };

    const handleCallDeleteContract = async () => {
        try {
            // await deleteContract(selectedRowKeys);
            message.success('Xóa hợp đồng thành công!');
            handleGetAllContracts();
            setIsModalOpen(false);
            setSelectedRowKeys([]);
            setSelectedRows([]);
        } catch (error) {
            message.error(`Lỗi khi xóa: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('contract');
            if (!response.headers['content-type'].includes('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `contract_${new Date().toISOString().replace(/[-:]/g, '')}.xlsx`);
            link.click();
            window.URL.revokeObjectURL(url);
            message.success('Tải file Excel thành công!');
        } catch (error) {
            console.error('Lỗi khi xuất file Excel:', error);
            message.error('Không thể tải file Excel');
        }
    };

    const handleSelectChange = (newSelectedRowKeys, newSelectedRows) => {
        setSelectedRowKeys(newSelectedRowKeys);
        setSelectedRows(newSelectedRows);
    };

    const handleFormSubmit = async (formData) => {
        setLoading(true);
        try {
            let contractId = selectedContract?.id;

            const payload = {
                ...formData,
                employeeId: Number(employeeId),
            };

            if (modalMode === 'create') {
                const createdContract = await createContract(payload);
                // message.success('Tạo hợp đồng thành công!');
            } else if (modalMode === 'edit') {
                // const updatedContract = await updateContract(selectedContract.id, payload);
                // contractId = updatedContract.id;
                message.success('Cập nhật hợp đồng thành công!');
            }

            // Upload files nếu có (multiple)
            const files = payload.files;
            if (files && files.length > 0) {
                await uploadFilesForContract(Array.from(files), contractId);
                message.success(`Upload ${files.length} file thành công!`);
            }

            // Refresh danh sách
            handleGetAllContracts();
            setIsModalOpen(false);
            form.resetFields();
        } catch (error) {
            message.error(`Lỗi: ${error.response?.data?.message || error.message}`);
        } finally {
            setLoading(false);
        }
    };

    const handleTableChange = (pagination, filters, sorter) => {
        handleGetAllContracts(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Hợp Đồng Mới';
            case 'edit':
                return 'Chỉnh Sửa Hợp Đồng';
            case 'delete':
                return 'Xóa Hợp Đồng';
            default:
                return 'Chi Tiết Hợp Đồng';
        }
    };

    const handleDownloadFile = (file) => {
        window.open(file.url, '_blank');
        message.success(`Đang tải xuống ${file.name}`);
    };

    useEffect(() => {
        handleGetAllContracts();
        fetchPositionOptions();
    }, []);

    return (
        <div className={cx('contract-wrapper')}>
            <div className={cx('sub-header')}>
                <SmartInput
                    size="large"
                    placeholder="Tìm kiếm hợp đồng..."
                    icon={<SearchOutlined/>}
                    className={cx('search-input')}
                />
                <div className={cx('features')}>
                    <SmartButton
                        title="Thêm mới"
                        icon={<PlusOutlined/>}
                        type="primary"
                        onClick={handleAddContract}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined/>}/>
                    <SmartButton title="Excel" icon={<CloudUploadOutlined/>} onClick={handleExportFile}/>
                </div>
            </div>

            <div className={cx('contract-container')}>
                <SmartTable
                    columns={baseColumns}
                    dataSources={contractSource}
                    loading={loading}
                    pagination={pagination}
                    onTableChange={handleTableChange}
                    selectedRowKeys={selectedRowKeys}
                    onSelectChange={handleSelectChange}
                />
            </div>

            {/* Edit/Create/Delete Modal */}
            <PopupModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                title={getModalTitle()}
                fields={modalMode === 'delete' ? [] : contractModalFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedContract}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
                loading={loading}
            />

            {/* View Contract Modal */}
            <Modal
                title={null}
                open={isViewModalOpen}
                onCancel={() => setIsViewModalOpen(false)}
                footer={null}
                width="90%"
                style={{top: 20, maxWidth: 1400}}
                className={cx('view-modal')}
                destroyOnClose
            >
                <div className={cx('modal-header')}>
                    <div className={cx('modal-title')}>
                        <FileTextOutlined className={cx('title-icon')}/>
                        <h2>Chi tiết Hợp đồng</h2>
                    </div>
                </div>

                {selectedContract && (
                    <div className={cx('contract-info-section')}>
                        <div className={cx('info-grid')}>
                            <div className={cx('info-item')}>
                                <span className={cx('info-label')}>Loại hợp đồng</span>
                                <span className={cx('info-value')}>{selectedContract.contractType}</span>
                            </div>
                            <div className={cx('info-item')}>
                                <span className={cx('info-label')}>Trạng thái</span>
                                <span className={cx('info-value')}>{getStatusTag(selectedContract.status)}</span>
                            </div>
                            <div className={cx('info-item')}>
                                <span className={cx('info-label')}>Ngày bắt đầu</span>
                                <span className={cx('info-value')}>
                                    {selectedContract.startDate ? new Date(selectedContract.startDate).toLocaleDateString('vi-VN') : 'N/A'}
                                </span>
                            </div>
                            <div className={cx('info-item')}>
                                <span className={cx('info-label')}>Ngày kết thúc</span>
                                <span className={cx('info-value')}>
                                    {selectedContract.endDate ? new Date(selectedContract.endDate).toLocaleDateString('vi-VN') : 'N/A'}
                                </span>
                            </div>
                            <div className={cx('info-item')}>
                                <span className={cx('info-label')}>Lương cơ bản</span>
                                <span className={cx('info-value', 'salary')}>
                                    {selectedContract.basicSalary?.toLocaleString('vi-VN', {
                                        style: 'currency',
                                        currency: 'VND'
                                    })}
                                </span>
                            </div>
                            <div className={cx('info-item')}>
                                <span className={cx('info-label')}>Vị trí</span>
                                <span className={cx('info-value')}>{selectedContract.positionId || 'N/A'}</span>
                            </div>
                        </div>
                    </div>
                )}

                <div className={cx('files-section')}>
                    <Spin spinning={loading}>
                        {currentFiles.length === 0 ? (
                            <Empty description="Không có file đính kèm"/>
                        ) : (
                            <Tabs defaultActiveKey="1" className={cx('file-tabs')}>
                                {currentFiles.map((file, index) => (
                                    <TabPane
                                        tab={
                                            <span className={cx('tab-title')}>
                                                <FileTextOutlined/>
                                                {file.name}
                                            </span>
                                        }
                                        key={file.id || index + 1}
                                    >
                                        <div className={cx('file-viewer')}>
                                            <div className={cx('file-header')}>
                                                <div className={cx('file-info')}>
                                                    <h3>{file.name}</h3>
                                                    <span className={cx('file-size')}>{file.size}</span>
                                                </div>
                                                <Button
                                                    type="primary"
                                                    icon={<DownloadOutlined/>}
                                                    onClick={() => handleDownloadFile(file)}
                                                >
                                                    Tải xuống
                                                </Button>
                                            </div>
                                            <div className={cx('pdf-container')}>
                                                <iframe
                                                    src={file.url}
                                                    className={cx('pdf-iframe')}
                                                    title={file.name}
                                                />
                                            </div>
                                        </div>
                                    </TabPane>
                                ))}
                            </Tabs>
                        )}
                    </Spin>
                </div>
            </Modal>
        </div>
    );
}

export default Contract;