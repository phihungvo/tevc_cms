import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Permission/Permission.module.scss';
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
import { getAllPermissions, createPermission } from '~/service/admin/permission';

const cx = classNames.bind(styles);

function User() {
    const [permissionSource, setPermissionSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedRole, setselectedRole] = useState(null);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Permission Action',
            dataIndex: 'action',
            key: 'action',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'Permission Resource',
            dataIndex: 'resource',
            key: 'resource',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Actions',
            fixed: 'right',
            width: 250,
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
                        // onClick={() => handleDeleteTrailer(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'Permission Action',
            name: 'action',
            type: 'text',
            rules: [{ required: true, message: 'Permission Action is required!' }],
        },
        {
            label: 'Permission Resource',
            name: 'resource',
            type: 'text',
            rules: [{ required: true, message: 'Permission Resource is required!' }],
        },
        {
            label: 'Description',
            name: 'description',
            type: 'text',
        },
    ];

    const handleGetAllPermissions = async (page = 1, pageSize = 5) => {
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
                console.error('Invalid data format:', response);
                setPermissionSource([]);
            }
        } catch (error) {
            console.error('Error fetching roles:', error);
            setPermissionSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddRole = () => {
        setModalMode('create');
        setselectedRole(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreatePermission = async (formData) => {
        await createPermission(formData);
        handleGetAllPermissions();
    };

    const handleEditRole = (record) => {
        setselectedRole(record);
        setModalMode('edit');

        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    // const handleCallUpdateRole = async (formData) => {
    //     await updateRole(selectedRole.id, formData);
    //     handleGetAllPermissions();
    //     setIsModalOpen(false);
    // };

    useEffect(() => {
        handleGetAllPermissions();
    }, []);

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreatePermission(formData);
        } else if (modalMode === 'edit') {
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllPermissions(pagination.current, pagination.pageSize);
    };
    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Add New Permission';
            case 'edit':
                return 'Edit Permission';
            case 'delete':
                return 'Delete Permission';
            default:
                return 'Permission Details';
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
                    dataSources={permissionSource}
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

export default User;
