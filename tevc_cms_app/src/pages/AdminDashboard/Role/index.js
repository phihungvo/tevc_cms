import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Role/Role.module.scss';
import { useState, useEffect } from 'react';
import moment from 'moment';
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
import { getAllPermissionsNoPaging } from '~/service/admin/permission';
import { getAllRoles, createRole, updateRole, deleteRole} from '~/service/admin/role';

const cx = classNames.bind(styles);

function Role() {
    const [userSource, setUserSource] = useState([]);
     const [permissionOptions, setPermissionOptions] = useState([]);
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
            title: 'Role Name',
            dataIndex: 'name',
            key: 'name',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
            // width: 150,
        },
        {
            title: 'Actions',
            fixed: 'right',
            width: 180,
            render: (_, record) => (
                <>
                    <SmartButton
                        title="Edit"
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={80}
                        onClick={() => handleEditRole(record)}
                    />
                    <SmartButton
                        title="Delete"
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={80}
                        onClick={() => handleDeleteRole(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'Role Name',
            name: 'name',
            type: 'text',
            rules: [{ required: true, message: 'Role Name is required!' }],
        },
        {
            label: 'Description',
            name: 'description',
            type: 'text',
        },
        {
            label: 'Permission',
            name: 'permissions',
            type: 'select',
            multiple: true,
            options: permissionOptions,
        },
    ];

    const handleGetAllRoles = async (page = 1, pageSize = 5) => {
        setLoading(true);
        try {
            const response = await getAllRoles({ page: page - 1, pageSize });
            if (response && response.content) {
                setUserSource(response.content);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Invalid data format:', response);
                setUserSource([]);
            }
        } catch (error) {
            console.error('Error fetching roles:', error);
            setUserSource([]);
        } finally {
            setLoading(false);
        }
    };

    const fetchPermissionOptions = async () => {
        try {
            const perResponse = await getAllPermissionsNoPaging();
            console.log('Permission Response:', perResponse);
            if (perResponse && Array.isArray(perResponse)) {
                const permissions = perResponse.map((per) => ({
                    label: per.name + ' ' + '(' + per.description + ')',
                    value: per.id,
                }));
                setPermissionOptions(permissions);
            } else {
                setPermissionOptions([]); // Đặt rỗng nếu dữ liệu không hợp lệ
                console.error('Invalid permission response:', perResponse);
            }
        } catch (error) {
            console.error('Error fetching permissions:', error);
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
        await createRole(formData);
        handleGetAllRoles();
    };

    const handleEditRole = (record) => {
        setSelectedRole(record);
        setModalMode('edit');

        form.setFieldsValue({
            ...record,
            permissions: record.permissions ? record.permissions.map(p => p.id) : [],
        });
        setIsModalOpen(true);
    };

    const handleCallUpdateRole = async (formData) => {
        await updateRole(selectedRole.id, formData);
        handleGetAllRoles();
        setIsModalOpen(false);
    };

    const handleDeleteRole = (record) => {
        setModalMode('delete');
        setSelectedRole(record);
        setIsModalOpen(true);
    };

    const handleCallDeleteRole = async () => {
        console.log('role id: ', selectedRole.id)
        await deleteRole(selectedRole.id);
        handleGetAllRoles();
        setIsModalOpen(false);
    }


    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreateRole(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateRole(formData);
        }else if (modalMode === 'delete') {
            handleCallDeleteRole();
        }
    };

    useEffect(() => {
        fetchPermissionOptions();
        handleGetAllRoles();
    }, []);

    const handleTableChange = (pagination) => {
        handleGetAllRoles(pagination.current, pagination.pageSize);
    };
    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Add New Role';
            case 'edit':
                return 'Edit Role';
            case 'delete':
                return 'Delete Role';
            default:
                return 'Role Details';
        }
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
                        onClick={handleAddRole}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
                    <SmartButton title="Excel" icon={<CloudUploadOutlined />} />
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={userSource}
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
                initialValues={selectedRole}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default Role;
