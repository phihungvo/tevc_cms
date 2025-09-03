import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Permission/Permission.module.scss';
import SmartTable from '~/components/Layout/components/SmartTable';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import { Form, message } from 'antd';
import { getAllRoles, createRole, assignPermissionsToRole, getAllPermissions } from '~/service/admin/permission';

const cx = classNames.bind(styles);

function PermissionPage() {
    const [roles, setRoles] = useState([]);
    const [permissions, setPermissions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Role Name',
            dataIndex: 'name',
            key: 'name',
        },
        {
            title: 'Description',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: 'Actions',
            key: 'actions',
            render: (_, record) => (
                <SmartButton
                    title="Assign Permissions"
                    type="primary"
                    onClick={() => handleAssignPermissions(record)}
                />
            ),
        },
    ];

    const fetchRoles = async () => {
        setLoading(true);
        try {
            const response = await getAllRoles();
            if (Array.isArray(response)) {
                setRoles(response);
            } else {
                console.error('Invalid data format for roles:', response);
                setRoles([]);
            }
        } catch (error) {
            message.error('Failed to fetch roles');
        } finally {
            setLoading(false);
        }
    };

    const fetchPermissions = async () => {
        try {
            const response = await getAllPermissions();
            if (Array.isArray(response)) {
                setPermissions(response);
            } else {
                console.error('Invalid data format for permissions:', response);
                setPermissions([]);
            }
        } catch (error) {
            message.error('Failed to fetch permissions');
        }
    };

    const handleCreateRole = async (formData) => {
        try {
            await createRole(formData);
            message.success('Role created successfully');
            fetchRoles();
        } catch (error) {
            message.error('Failed to create role');
        }
    };

    const handleAssignPermissions = (role) => {
        form.setFieldsValue({ roleId: role.id, permissions: [] });
        setIsModalOpen(true);
    };

    const handleFormSubmit = async (formData) => {
        try {
            await assignPermissionsToRole(formData.roleId, formData.permissions);
            message.success('Permissions assigned successfully');
            setIsModalOpen(false);
            fetchRoles();
        } catch (error) {
            message.error('Failed to assign permissions');
        }
    };

    useEffect(() => {
        fetchRoles();
        fetchPermissions();
    }, []);

    return (
        <div className={cx('permission-wrapper')}>
            <div className={cx('header')}>
                <SmartInput placeholder="Search Roles" />
                <SmartButton title="Create Role" type="primary" onClick={() => setIsModalOpen(true)} />
            </div>
            <SmartTable columns={columns} dataSources={roles} loading={loading} />
            <PopupModal
                isModalOpen={isModalOpen}
                setIsModalOpen={setIsModalOpen}
                title="Assign Permissions to Role"
                fields={[
                    {
                        label: 'Permissions',
                        name: 'permissions',
                        type: 'select',
                        options: permissions.map((perm) => ({ label: `${perm.resource}:${perm.action}`, value: perm.id })),
                        mode: 'multiple',
                        rules: [{ required: true, message: 'Please select permissions' }],
                    },
                ]}
                onSubmit={handleFormSubmit}
                formInstance={form}
            />
        </div>
    );
}

export default PermissionPage;
