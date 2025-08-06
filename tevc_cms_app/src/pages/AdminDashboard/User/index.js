import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/User/User.module.scss';
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
import { getAllUser, createUser, updateUser, deleteUser } from '~/service/admin/user';

const cx = classNames.bind(styles);

function User() {
    const [userSource, setUserSource] = useState([]);
    const [selectedRowKeys, setSelectedRowKeys] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 10,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'User Name',
            dataIndex: 'userName',
            key: 'userName',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'Email',
            dataIndex: 'email',
            key: 'email',
            width: 150,
        },
        {
            title: 'Role',
            dataIndex: 'roles',
            key: 'roles',
            width: 100,
        },
        {
            title: 'Address',
            dataIndex: 'address',
            key: 'address',
            width: 150,
            render: (add) => (add ? add : 'Viet Nam'),
        },
        {
            title: 'Phone Number',
            dataIndex: 'phoneNumber',
            key: 'phoneNumber',
            width: 200,
            render: (phone) => (phone ? phone : 'N/A'),
        },
        {
            title: 'Enable',
            dataIndex: 'enabled',
            key: 'enabled',
            width: 50,
            render: (enabled) => {
                const icon = enabled ? '✔️' : '❌';
                const color = enabled ? 'green' : 'red';
                return <Tag color={color}>{icon}</Tag>;
            },
        },
        {
            title: 'Create At',
            dataIndex: 'createAt',
            key: 'createAt',
            width: 150,
            render: (date) =>
                date ? new Date(date).toLocaleString('vi-VN') : 'N/A',
        },
        {
            title: 'Update At',
            dataIndex: 'updateAt',
            key: 'updateAt',
            width: 150,
            render: (date) =>
                date ? new Date(date).toLocaleString('vi-VN') : 'N/A',
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
                        onClick={() => handleEditUser(record)}
                    />
                    <SmartButton
                        title="Delete"
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={80}
                        onClick={() => handleDeleteUser(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'User Name',
            name: 'userName',
            type: 'text',
            rules: [{ required: true, message: 'User Name is required!' }],
        },
        {
            label: 'First Name',
            name: 'firstName',
            type: 'text',
        },
        {
            label: 'Last Name',
            name: 'lastName',
            type: 'text',
        },
        {
            label: 'Email',
            name: 'email',
            type: 'text',
            rules: [{ required: true, message: 'Email is required!' }],
        },
        {
            label: 'Address',
            name: 'address',
            type: 'text',
        },
        {
            label: 'Password',
            name: 'password',
            type: 'text',
        },
        {
            label: 'Phone Number',
            name: 'phoneNumber',
            type: 'text',
        },
        {
            label: 'Profile Picture',
            name: 'profilePicture',
            type: 'text',
        },
        {
            label: 'Role',
            name: 'roles',
            type: 'select',
            options: ['USER', 'ADMIN', 'MODERATOR'],
        },
        {
            label: 'Permission',
            name: 'permissions',
            type: 'select',
            options: ['ADMIN:MANAGE', 'ADMIN:CREATE', 'ADMIN:UPDATE', 'ADMIN:READ', 'ADMIN:DELETE'],
        },
        {
            label: 'Enable',
            name: 'enabled',
            type: 'yesno',
        },
        {
            label: 'Bio',
            name: 'bio',
            type: 'textarea',
        },
    ];

    const handleGetAllUsers = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllUser({ page: page - 1, pageSize });
            const userList = response.content;

            if (response && Array.isArray(userList)) {
                const transformedTrailers = userList.map((user) => {
                    return {
                        ...user,
                        keyDisplay: user.key,
                    };
                });

                setUserSource(transformedTrailers);
                setPagination((prev) => ({
                    ...prev,
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                }));
            } else {
                console.error('Invalid data users: ', response);
                setUserSource([]);
            }
        } catch (error) {
            console.error('Error fetching user', error);
            setUserSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddUser = () => {
        setModalMode('create');
        setSelectedUser(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreateUser = async (formData) => {
        await createUser(formData);
        handleGetAllUsers();
    };

    const handleEditUser = (record) => {
        setSelectedUser(record);
        setModalMode('edit');

        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdateUser = async (formData) => {
        console.log('selected user: ', selectedUser)
        await updateUser(selectedUser.id, formData);
        handleGetAllUsers();
        setIsModalOpen(false);
    };

    const handleDeleteUser = (record) => {
        setModalMode('delete');
        setSelectedRowKeys([record.id]);
        setIsModalOpen(true);
    };

    const handleCallDeleteUser = async () => {
        console.log('selected row key: ', selectedRowKeys);
        await deleteUser(selectedRowKeys);
        handleGetAllUsers();
        setIsModalOpen(false);
    };

    const handleSelectChange = (newSelectedRowKeys, selectedRows) => {
        setSelectedRowKeys(newSelectedRowKeys);
    };

    useEffect(() => {
        handleGetAllUsers();
    }, []);

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreateUser(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateUser(formData);
        }else if (modalMode === 'delete') {
            handleCallDeleteUser();
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllUsers(pagination.current, pagination.pageSize);
    };
    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Add New User';
            case 'edit':
                return 'Edit User';
            case 'delete':
                return 'Delete User';
            default:
                return 'User Details';
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
                        onClick={handleAddUser}
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
                    selectedRowKeys={selectedRowKeys}
                    onSelectChange={handleSelectChange}
                />
            </div>

            <PopupModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                title={getModalTitle()}
                fields={modalMode === 'delete' ? [] : userModalFields}
                onSubmit={handleFormSubmit}
                initialValues={selectedUser}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default User;
