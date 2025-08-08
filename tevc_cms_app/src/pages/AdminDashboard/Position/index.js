import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Position/Position.module.scss';
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
import { getAllPositions, createPosition, updatePosition, deletePosition } from '~/service/admin/position';

const cx = classNames.bind(styles);

function Position() {
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
        },
        {
            title: 'Employee Count',
            dataIndex: 'employeeCount',
            key: 'employeeCount',
            width: 150,
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
                        onClick={() => handleEditPosition(record)}
                    />
                    <SmartButton
                        title="Delete"
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={80}
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
            label: 'Desctiontion',
            name: 'description',
            type: 'text',
        },
        {
            label: 'Base Salary',
            name: 'baseSalary',
            type: 'text',
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
                'EXECUTIVE'
            ],
        },
    ];

    useEffect(() => {
        handleGetAllPositions();
    }, []);

    const handleGetAllPositions = async (page = 1, pageSize = 5) => {
        setLoading(true);
        try {
            const response = await getAllPositions();
            if (response && Array.isArray(response.content)) {
                const mappedPositions = response.content.map(employee => ({
                    ...employee,
                    isActive: employee.active,
                }));
                setPositionSource(mappedPositions);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Invalid data format:', response);
                setPositionSource([]);
            }
        } catch (error) {
            console.error('Error fetching employees:', error);
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
            handleGetAllPositions();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error creating position: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditPosition = (record) => {
        setSelectedPosition(record);
        setModalMode('edit');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdatePosition = async (formData) => {
        await updatePosition(selectedPosition.id, formData);
        handleGetAllPositions();
        setIsModalOpen(false);
    };

    const handleDeletePosition = (record) => {
        setModalMode('delete');
        setSelectedPosition(record.id)
        setIsModalOpen(true);
    };

    const handleCallDeleteUser = async () => {
        await deletePosition(selectedPosition);
        handleGetAllPositions();
        setIsModalOpen(false);
    };

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreatePosition(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdatePosition(formData);
        } else {
            handleCallDeleteUser();
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllPositions(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Add New Position';
            case 'edit':
                return 'Edit Position';
            case 'delete':
                return 'Delete Position';
            default:
                return 'Position Details';
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