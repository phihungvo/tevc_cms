// ~/pages/AdminDashboard/UserManagement/RoleList.js
import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Role/Role.module.scss';
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
import { Form, message } from 'antd';
import { getAllPermissionsNoPaging } from '~/service/admin/permission';
import { getAllRoles, createRole, updateRole, deleteRole } from '~/service/admin/role';

const cx = classNames.bind(styles);

/**
 * Component RoleList
 * Quản lý danh sách vai trò (Role) với các tính năng: hiển thị, thêm, sửa, xóa, chọn nhiều dòng.
 */
function RoleList() {
    const [roleSource, setRoleSource] = useState([]);
    const [permissionOptions, setPermissionOptions] = useState([]);
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);
    const [selectedRows, setSelectedRows] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedRole, setSelectedRole] = useState(null);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Tên Vai Trò',
            dataIndex: 'name',
            key: 'name',
            width: 150,
            fixed: 'left',
            align: 'center',
            onFilter: (value, record) => record.name.toLowerCase().startsWith(value.toLowerCase()),
        },
        {
            title: 'Mô Tả',
            dataIndex: 'description',
            key: 'description',
            width: 750,
            render: (text) =>
                text
                    ? text
                        .split(/\d+\.\s/)
                        .filter(Boolean)
                        .map((item, idx) => (
                            <div key={idx}>
                                {`${idx + 1}. ${item.trim()}`}
                            </div>
                        ))
                    : null,
        },
        {
            title: 'Hành Động',
            fixed: 'right',
            width: 120,
            render: (_, record) => (
                <>
                    <SmartButton
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={50}
                        onClick={() => handleEditRole(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={50}
                        onClick={() => handleDeleteRole(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const roleModalFields = [
        {
            label: 'Tên Vai Trò',
            name: 'name',
            type: 'text',
            rules: [{ required: true, message: 'Tên vai trò là bắt buộc!' }],
        },
        {
            label: 'Mô Tả',
            name: 'description',
            type: 'textarea',
        },
        {
            label: 'Quyền',
            name: 'permissionIds',
            type: 'select',
            multiple: true,
            fullWidth: true,
            options: permissionOptions,
        },
    ];

    const handleGetAllRoles = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllRoles({ page: page - 1, pageSize });
            if (response && response.content) {
                setRoleSource(response.content);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Định dạng dữ liệu không hợp lệ:', response);
                setRoleSource([]);
            }
        } catch (error) {
            console.error('Lỗi khi lấy danh sách vai trò:', error);
            setRoleSource([]);
        } finally {
            setLoading(false);
        }
    };

    const fetchPermissionOptions = async () => {
        try {
            const perResponse = await getAllPermissionsNoPaging();
            if (perResponse && Array.isArray(perResponse)) {
                const permissions = perResponse.map((per) => ({
                    label: `${per.name} (${per.description})`,
                    value: per.id,
                }));
                setPermissionOptions(permissions);
            } else {
                setPermissionOptions([]);
                console.error('Phản hồi quyền không hợp lệ:', perResponse);
            }
        } catch (error) {
            console.error('Lỗi khi lấy quyền:', error);
            setPermissionOptions([]);
        }
    };

    const handleAddRole = () => {
        setModalMode('create');
        setSelectedRole(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreateRole = async (formData) => {
        try {
            await createRole(formData);
            handleGetAllRoles();
        } catch (error) {
            message.error(`Lỗi khi tạo vai trò: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditRole = (record) => {
        setSelectedRole(record);
        setModalMode('edit');
        form.setFieldsValue({
            ...record,
            permissionIds: record.permissions ? record.permissions.map((p) => p.id) : [],
        });
        setIsModalOpen(true);
    };

    const handleCallUpdateRole = async (formData) => {
        try {
            await updateRole(selectedRole.id, formData);

            handleGetAllRoles();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật vai trò: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleDeleteRole = (record) => {
        setModalMode('delete');
        setSelectedRowKeys([record.id]);
        setSelectedRows([record]);
        setIsModalOpen(true);
    };

    const handleCallDeleteRole = async () => {
        try {
            await deleteRole(selectedRowKeys[0]);
            message.success('Xóa vai trò thành công!');
            handleGetAllRoles();
            setIsModalOpen(false);
            setSelectedRowKeys([]);
            setSelectedRows([]);
        } catch (error) {
            message.error(`Lỗi khi xóa vai trò: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreateRole(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateRole(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteRole();
        }
    };

    const handleSelectChange = (newSelectedRowKeys, newSelectedRows) => {
        setSelectedRowKeys(newSelectedRowKeys);
        setSelectedRows(newSelectedRows);
    };

    const handleTableChange = (pagination) => {
        handleGetAllRoles(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Vai Trò Mới';
            case 'edit':
                return 'Chỉnh Sửa Vai Trò';
            case 'delete':
                return 'Xóa Vai Trò';
            default:
                return 'Chi Tiết Vai Trò';
        }
    };

    useEffect(() => {
        fetchPermissionOptions();
        handleGetAllRoles();
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
                        onClick={handleAddRole}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
                    <SmartButton title="Excel" icon={<CloudUploadOutlined />} />
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={roleSource}
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
                fields={modalMode === 'delete' ? [] : roleModalFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedRole}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default RoleList;