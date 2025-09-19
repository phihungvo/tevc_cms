import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Position/Position.module.scss';
import SmartTable from '~/components/Layout/components/SmartTable';
import {
    SearchOutlined,
    PlusOutlined,
    FilterOutlined,
    CloudUploadOutlined,
    EditOutlined,
    DeleteOutlined,
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import { Form, message, Tag } from 'antd';
import { getAllPositions, createPosition, updatePosition, deletePosition, getAllByEmployeePaged } from '~/service/admin/position';

const cx = classNames.bind(styles);

function Position({ employeeId }) { // Nhận prop employeeId
    const [positionSource, setPositionSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedPosition, setSelectedPosition] = useState(null);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Title',
            dataIndex: 'title',
            key: 'title',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'Position Type',
            dataIndex: 'positionType',
            key: 'positionType',
            width: 150,
            render: (positionType) => (
                <Tag color="blue">{positionType || 'N/A'}</Tag>
            ),
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
            width: 150,
        },
        {
            title: 'Base Salary',
            dataIndex: 'baseSalary',
            key: 'baseSalary',
            width: 150,
            render: (baseSalary) => baseSalary ? `${baseSalary.toLocaleString()} VND` : 'N/A',
        },
        {
            title: 'Actions',
            fixed: 'right',
            width: 80,
            render: (_, record) => (
                <>
                    <SmartButton
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={50}
                        onClick={() => handleEditPosition(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={50}
                        onClick={() => handleDeletePosition(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'Position Title',
            name: 'title',
            type: 'text',
            rules: [{ required: true, message: 'Position Title is required!' }],
        },
        {
            label: 'Description',
            name: 'description',
            type: 'text',
        },
        {
            label: 'Base Salary',
            name: 'baseSalary',
            type: 'number', // Đổi thành type number để phù hợp với baseSalary
            rules: [{ type: 'number', min: 0, message: 'Base Salary must be a positive number!' }],
        },
        {
            label: 'Position Type',
            name: 'positionType',
            type: 'select',
            options: [
                'INTERN',
                'JUNIOR',
                'MID',
                'SENIOR',
                'LEADER',
                'MANAGER',
                'DIRECTOR',
                'EXECUTIVE',
            ],
            rules: [{ required: true, message: 'Position Type is required!' }],
        },
    ];

    useEffect(() => {
        handleGetPositions();
    }, [employeeId]); // Thêm employeeId vào dependency array

    const handleGetPositions = async (page = 1, pageSize = 5) => {
        setLoading(true);
        try {
            const response = employeeId
                ? await getAllByEmployeePaged(employeeId, { page: page - 1, pageSize })
                : await getAllPositions({ page: page - 1, pageSize });

            if (response && Array.isArray(response.content)) {
                const mappedPositions = response.content.map((position) => ({
                    ...position,
                }));
                setPositionSource(mappedPositions);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                setPositionSource([]);
                message.error('Dữ liệu vị trí không hợp lệ');
            }
        } catch (error) {
            message.error(`Lỗi khi lấy danh sách vị trí: ${error.response?.data?.message || error.message}`);
            setPositionSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddPosition = () => {
        setModalMode('create');
        setSelectedPosition(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreatePosition = async (formData) => {
        try {
            await createPosition(formData);
            handleGetPositions();
            setIsModalOpen(false);
            message.success('Tạo vị trí thành công');
        } catch (error) {
            message.error(`Lỗi khi tạo vị trí: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditPosition = (record) => {
        setSelectedPosition(record);
        setModalMode('edit');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdatePosition = async (formData) => {
        try {
            await updatePosition(selectedPosition.id, formData);
            handleGetPositions();
            setIsModalOpen(false);
            message.success('Cập nhật vị trí thành công');
        } catch (error) {
            message.error(`Lỗi khi cập nhật vị trí: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleDeletePosition = (record) => {
        setModalMode('delete');
        setSelectedPosition(record.id);
        setIsModalOpen(true);
    };

    const handleCallDeletePosition = async () => {
        try {
            await deletePosition(selectedPosition);
            handleGetPositions();
            setIsModalOpen(false);
            message.success('Xóa vị trí thành công');
        } catch (error) {
            message.error(`Lỗi khi xóa vị trí: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreatePosition(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdatePosition(formData);
        } else if (modalMode === 'delete') {
            handleCallDeletePosition();
        }
    };

    const handleTableChange = (pagination) => {
        handleGetPositions(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm vị trí mới';
            case 'edit':
                return 'Chỉnh sửa vị trí';
            case 'delete':
                return 'Xóa vị trí';
            default:
                return 'Chi tiết vị trí';
        }
    };

    return (
        <div className={cx('trailer-wrapper')}>
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
                        onClick={handleAddPosition}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
                    <SmartButton title="Excel" icon={<CloudUploadOutlined />} />
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={positionSource}
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
                initialValues={selectedPosition}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default Position;