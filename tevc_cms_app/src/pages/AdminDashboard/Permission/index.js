// ~/pages/AdminDashboard/UserManagement/PermissionList.js
import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Permission/Permission.module.scss';
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
import { getAllPermissions, createPermission, updatePermission, deletePermission } from '~/service/admin/permission';

const cx = classNames.bind(styles);

/**
 * Component PermissionList
 * Quản lý danh sách quyền (Permission) với các tính năng: hiển thị, thêm, sửa, xóa, chọn nhiều dòng.
 */
function PermissionList() {
    const [permissionSource, setPermissionSource] = useState([]); // Dữ liệu nguồn của bảng quyền
    const [selectedRowKeys, setSelectedRowKeys] = useState([]); // Danh sách ID của các quyền được chọn
    const [selectedRows, setSelectedRows] = useState([]); // Danh sách bản ghi quyền được chọn
    const [loading, setLoading] = useState(false); // Trạng thái loading của bảng
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    }); // Cấu hình phân trang
    const [modalMode, setModalMode] = useState('create'); // Chế độ modal: create, edit, delete
    const [isModalOpen, setIsModalOpen] = useState(false); // Trạng thái mở/đóng modal
    const [selectedPermission, setSelectedPermission] = useState(null); // Quyền đang được chỉnh sửa/xóa
    const [form] = Form.useForm(); // Form instance từ Ant Design

    // Định nghĩa màu sắc cho các phương thức HTTP
    const httpMethod = {
        GET: 'green',
        POST: 'blue',
        PUT: 'orange',
        DELETE: 'red',
        HEAD: 'gray',
        OPTIONS: 'gray',
        PATCH: 'gray',
        TRACE: 'gray',
    };

    // Cấu hình các cột của bảng
    const columns = [
        {
            title: 'Tên Quyền',
            dataIndex: 'name',
            width: 300,
            fixed: 'left',
            onFilter: (value, record) => record.name.toLowerCase().startsWith(value.toLowerCase()),
        },
        {
            title: 'Phương Thức HTTP',
            dataIndex: 'httpMethod',
            render: (method) => (
                <Tag color={httpMethod[method] || 'default'}>{method}</Tag>
            ),
        },
        {
            title: 'API Endpoint',
            dataIndex: 'apiEndpoint',
            width: 350,
        },
        {
            title: 'Resource Pattern',
            dataIndex: 'resourcePattern',
            width: 350,
        },
        {
            title: 'Mô Tả',
            dataIndex: 'description',
            width: 500,
        },
        {
            title: 'Hành Động',
            fixed: 'right',
            width: 180,
            render: (_, record) => (
                <>
                    <SmartButton
                        title="Sửa"
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={80}
                        onClick={() => handleEditPermission(record)}
                    />
                    <SmartButton
                        title="Xóa"
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={80}
                        onClick={() => handleDeletePermission(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    // Cấu hình các trường trong modal
    const permissionModalFields = [
        {
            label: 'Tên Quyền',
            name: 'name',
            type: 'text',
            rules: [{ required: true, message: 'Tên quyền là bắt buộc!' }],
        },
        {
            label: 'Mô Tả',
            name: 'description',
            type: 'text',
        },
        {
            label: 'API Endpoint',
            name: 'apiEndpoint',
            type: 'text',
        },
        {
            label: 'Resource Pattern',
            name: 'resourcePattern',
            type: 'text',
        },
        {
            label: 'Phương Thức HTTP',
            name: 'httpMethod',
            type: 'select',
            options: ['DELETE', 'GET', 'HEAD', 'OPTIONS', 'PATCH', 'POST', 'PUT', 'TRACE'],
        },
    ];

    // Lấy danh sách quyền từ API
    const handleGetAllPermissions = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllPermissions({ page: page - 1, pageSize });
            if (response && response.content) {
                setPermissionSource(response.content);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Định dạng dữ liệu không hợp lệ:', response);
                setPermissionSource([]);
            }
        } catch (error) {
            console.error('Lỗi khi lấy danh sách quyền:', error);
            setPermissionSource([]);
        } finally {
            setLoading(false);
        }
    };

    // Mở modal để thêm quyền mới
    const handleAddPermission = () => {
        setModalMode('create');
        setSelectedPermission(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    // Gọi API để tạo quyền mới
    const handleCallCreatePermission = async (formData) => {
        try {
            await createPermission(formData);
            message.success('Tạo quyền thành công!');
            handleGetAllPermissions();
        } catch (error) {
            message.error(`Lỗi khi tạo quyền: ${error.response?.data?.message || error.message}`);
        }
    };

    // Mở modal để chỉnh sửa quyền
    const handleEditPermission = (record) => {
        setSelectedPermission(record);
        setModalMode('edit');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    // Gọi API để cập nhật quyền
    const handleCallUpdatePermission = async (formData) => {
        try {
            await updatePermission(selectedPermission.id, formData);
            message.success('Cập nhật quyền thành công!');
            handleGetAllPermissions();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật quyền: ${error.response?.data?.message || error.message}`);
        }
    };

    // Mở modal xác nhận xóa quyền
    const handleDeletePermission = (record) => {
        setModalMode('delete');
        setSelectedRowKeys([record.id]);
        setSelectedRows([record]);
        setIsModalOpen(true);
    };

    // Gọi API để xóa quyền
    const handleCallDeletePermission = async () => {
        try {
            await deletePermission(selectedRowKeys[0]); // Xóa một quyền duy nhất
            message.success('Xóa quyền thành công!');
            handleGetAllPermissions();
            setIsModalOpen(false);
            setSelectedRowKeys([]);
            setSelectedRows([]);
        } catch (error) {
            message.error(`Lỗi khi xóa quyền: ${error.response?.data?.message || error.message}`);
        }
    };

    // Xử lý submit form trong modal
    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreatePermission(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdatePermission(formData);
        } else if (modalMode === 'delete') {
            handleCallDeletePermission();
        }
    };

    // Xử lý thay đổi trạng thái chọn dòng
    const handleSelectChange = (newSelectedRowKeys, newSelectedRows) => {
        setSelectedRowKeys(newSelectedRowKeys);
        setSelectedRows(newSelectedRows);
    };

    // Xử lý thay đổi bảng (phân trang)
    const handleTableChange = (pagination) => {
        handleGetAllPermissions(pagination.current, pagination.pageSize);
    };

    // Lấy tiêu đề modal dựa trên chế độ
    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Quyền Mới';
            case 'edit':
                return 'Chỉnh Sửa Quyền';
            case 'delete':
                return 'Xóa Quyền';
            default:
                return 'Chi Tiết Quyền';
        }
    };

    // Khởi tạo dữ liệu khi component mount
    useEffect(() => {
        handleGetAllPermissions();
    }, []);

    return (
        <div className={cx('trailer-wrapper')}>
            <div className={cx('sub_header')}>
                <SmartInput size="large" placeholder="Tìm kiếm" icon={<SearchOutlined />} />
                <div className={cx('features')}>
                    <SmartButton
                        title="Thêm mới"
                        icon={<PlusOutlined />}
                        type="primary"
                        onClick={handleAddPermission}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
                    <SmartButton title="Excel" icon={<CloudUploadOutlined />} />
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={permissionSource}
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
                fields={modalMode === 'delete' ? [] : permissionModalFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedPermission}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default PermissionList;