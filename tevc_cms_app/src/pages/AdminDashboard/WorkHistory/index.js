import React, { useState, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/User/User.module.scss';
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
import {DatePicker, Form, message, Tag} from 'antd';
import { getAllUser, createUser, updateUser, deleteUser } from '~/service/admin/user';
import { getAllByEmployeePaged } from '~/service/admin/work-history';
import { exportExcelFile } from '~/service/admin/export_service';

const cx = classNames.bind(styles);

function WorkHistory({employeeId}) {
    const [workHistory, setWorkHistorySource] = useState([]);
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
    const [selectedUser, setSelectedUser] = useState(null);
    const [form] = Form.useForm();
    const [dynamicColumns, setDynamicColumns] = useState([]);

    const baseColumns = [
        {
            title: 'Tên công ty',
            dataIndex: 'companyName',
            key: 'companyName',
            width: 200,
            fixed: 'left',
        },
        {
            title: 'Vị trí',
            dataIndex: 'position',
            key: 'position',
            width: 180,
        },
        {
            title: 'Thời gian làm việc',
            dataIndex: 'workDuration',
            key: 'workDuration',
            width: 180,
            render: (_, record) => {
                const start = record.startDate ? new Date(record.startDate).toLocaleDateString('vi-VN') : 'N/A';
                const end = record.endDate ? new Date(record.endDate).toLocaleDateString('vi-VN') : 'N/A';
                return `${start} ⇒ ${end}`;
            }
        },
        {
            title: 'Mô tả',
            dataIndex: 'description',
            key: 'description',
            width: 150,
        },
        {
            title: 'Địa chỉ công ty',
            dataIndex: 'companyAddress',
            key: 'companyAddress',
            width: 200,
        },
        {
            title: 'Lý do nghĩ việc',
            dataIndex: 'reasonForLeaving',
            key: 'reasonForLeaving',
            width: 200,
        },
        {
            title: 'Lương',
            dataIndex: 'salary',
            key: 'salary',
            width: 130,
            render: (value) =>
                value != null
                    ? value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
                    : 'N/A',
        },
        {
            title: 'Loại hợp đồng',
            dataIndex: 'contractType',
            key: 'contractType',
            width: 150,
        },
        {
            title: 'Người giám sát',
            dataIndex: 'supervisorName',
            key: 'supervisorName',
            width: 150,
        }, ,
        {
            title: 'Thao tác',
            fixed: 'right',
            width: 130,
            render: (_, record) => (
                <>
                    <SmartButton
                        type="primary"
                        icon={<EditOutlined />}
                        buttonWidth={50}
                        onClick={() => handleEditUser(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined />}
                        buttonWidth={50}
                        onClick={() => handleDeleteUser(record)}
                        style={{ marginLeft: '8px' }}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        // {
        //     label: 'Mã nhân viên',
        //     name: 'employeeId',
        //     type: 'text',
        //     rules: [{ required: true, message: 'Vui lòng nhập mã nhân viên!' }],
        // },
        {
            label: 'Tên công ty',
            name: 'companyName',
            type: 'text',
        },
        {
            label: 'Chức vụ',
            name: 'position',
            type: 'text',
        },
        {
            label: 'Ngày bắt đầu',
            name: 'startDate',
            type: 'date',
            render: () => <DatePicker format="YYYY-MM" picker="month" style={{ width: '100%' }} />,
        },
        {
            label: 'Ngày kết thúc',
            name: 'endDate',
            type: 'date',
            render: () => <DatePicker format="YYYY-MM" picker="month" style={{ width: '100%' }} />,
        },
        {
            label: 'Mô tả công việc',
            name: 'description',
            type: 'text',
        },
        {
            label: 'Địa chỉ công ty',
            name: 'companyAddress',
            type: 'text',
        },
        {
            label: 'Lý do nghỉ việc',
            name: 'reasonForLeaving',
            type: 'text',
        },
        {
            label: 'Mức lương',
            name: 'salary',
            type: 'text',
        },
        {
            label: 'Loại hợp đồng',
            name: 'contractType',
            type: 'text',
        },
        {
            label: 'Tên người quản lý trực tiếp',
            name: 'supervisorName',
            type: 'text',
        },
    ];

    const handleGetAllUsers = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllByEmployeePaged(employeeId, { page: page - 1, pageSize });
            const userList = response.content;

            if (response && Array.isArray(userList)) {
                const transformedUsers = userList.map((user) => ({
                    ...user,
                    keyDisplay: user.key,
                }));

                setWorkHistorySource(transformedUsers);
                setPagination((prev) => ({
                    ...prev,
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                }));
            } else {
                console.error('Invalid data users: ', response);
                setWorkHistorySource([]);
            }
        } catch (error) {
            console.error('Error fetching user', error);
            setWorkHistorySource([]);
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
        try {
            await createUser(formData);
            message.success('Tạo người dùng thành công!');
            handleGetAllUsers();
        } catch (error) {
            message.error(`Lỗi khi tạo người dùng: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditUser = (record) => {
        setSelectedUser(record);
        setModalMode('edit');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdateUser = async (formData) => {
        try {
            await updateUser(selectedUser.id, formData);
            message.success('Cập nhật người dùng thành công!');
            handleGetAllUsers();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật người dùng: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleDeleteUser = (record) => {
        setModalMode('delete');
        setSelectedRowKeys([record.id]);
        setSelectedRows([record]); // Lưu dòng được chọn để xử lý xóa
        setIsModalOpen(true);
    };

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('user');
            if (
                !response.headers['content-type'].includes(
                    'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                )
            ) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `user_${new Date().toISOString().replace(/[-:]/g, '')}.xlsx`);
            link.click();
            window.URL.revokeObjectURL(url);
            message.success('Tải file Excel thành công!');
        } catch (error) {
            console.error('Lỗi khi xuất file Excel:', error);
            message.error('Không thể tải file Excel');
        }
    };

    const handleCallDeleteUser = async () => {
        try {
            await deleteUser(selectedRowKeys);
            message.success('Xóa người dùng thành công!');
            handleGetAllUsers();
            setIsModalOpen(false);
            setSelectedRowKeys([]);
            setSelectedRows([]);
        } catch (error) {
            message.error(`Lỗi khi xóa người dùng: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleSelectChange = (newSelectedRowKeys, newSelectedRows) => {
        setSelectedRowKeys(newSelectedRowKeys);
        setSelectedRows(newSelectedRows);
    };

    useEffect(() => {
        handleGetAllUsers();
    }, []);

    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreateUser(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateUser(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteUser();
        }
    };

    const handleTableChange = (pagination, filters, sorter) => {
        handleGetAllUsers(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm lịch sử công việc';
            case 'edit':
                return 'Chỉnh sửa lịch sử công việc';
            case 'delete':
                return 'Xóa lịch sử công việc';
            default:
                return 'Chi Tiết lịch sử công việc';
        }
    };

    return (
        <div className={cx('trailer-wrapper')}>
            <div className={cx('sub_header')}>
                <SmartInput size="large" placeholder="Tìm kiếm" icon={<SearchOutlined />} />
                <div className={cx('features')}>
                    <SmartButton title="Thêm mới" icon={<PlusOutlined />} type="primary" onClick={handleAddUser} />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined />} />
                    <SmartButton title="Excel" icon={<CloudUploadOutlined />} onClick={handleExportFile} />
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={baseColumns}
                    dataSources={workHistory}
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

export default WorkHistory;