import React, {useState, useEffect} from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/User/User.module.scss';
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
import {getAllUser, createUser, updateUser, deleteUser} from '~/service/admin/user';
import {getAllByEmployeePaged} from "~/service/admin/contract";
import {exportExcelFile} from '~/service/admin/export_service';

const cx = classNames.bind(styles);

function Contract({employeeId}) {
    const [contractSource, setContractSource] = useState([]);
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
            title: 'Loại hợp đồng',
            dataIndex: 'contractType',
            key: 'contractType',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'Thời hạn hợp đồng',
            dataIndex: 'contractDuration',
            key: 'contractDuration',
            width: 175,
            render: (_, record) => {
                const start = record.startDate ? new Date(record.startDate).toLocaleDateString('vi-VN') : 'N/A';
                const end = record.endDate ? new Date(record.endDate).toLocaleDateString('vi-VN') : 'N/A';
                return `${start} ⇒ ${end}`;
            }
        },
        {
            title: 'Lương cơ bản',
            dataIndex: 'basicSalary',
            key: 'basicSalary',
            width: 110,
            render: (value) =>
                value != null
                    ? value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
                    : 'N/A',
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
            width: 100,
        },
        {
            title: 'Ngày ký',
            dataIndex: 'signedDate',
            key: 'signedDate',
            width: 100,
            render: (date) => (date ? new Date(date).toLocaleDateString('vi-VN') : 'N/A'),
        },
        {
            title: 'Thời gian thử việc (tháng)',
            dataIndex: 'probationPeriod',
            key: 'probationPeriod',
            width: 80,
        },
        {
            title: 'Lý do chấm dứt',
            dataIndex: 'terminationReason',
            key: 'terminationReason',
            width: 150,
        },
        {
            title: 'Ngày chấm dứt',
            dataIndex: 'terminationDate',
            key: 'terminationDate',
            width: 100,
            render: (date) => (date ? new Date(date).toLocaleDateString('vi-VN') : 'N/A'),
        },
        {
            title: 'Tệp đính kèm',
            dataIndex: 'fileId',
            key: 'fileId',
            width: 150,
            render: (value) => (value ? `File ID: ${value}` : 'N/A'),
        },
        {
            title: 'Người tạo',
            dataIndex: 'createdById',
            key: 'createdById',
            width: 150,
        },
        {
            title: 'Hành động',
            fixed: 'right',
            width: 130,
            render: (_, record) => (
                <>
                    <SmartButton
                        type="primary"
                        icon={<EditOutlined/>}
                        buttonWidth={50}
                        onClick={() => handleEditUser(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined/>}
                        buttonWidth={50}
                        onClick={() => handleDeleteUser(record)}
                        style={{marginLeft: '8px'}}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'contractType',
            name: 'contractType',
            type: 'text',
            rules: [{required: true, message: 'Skill name is required!'}],
        },
        {
            label: 'startDate',
            name: 'startDate',
            type: 'date',
        },
        {
            label: 'endDate',
            name: 'endDate',
            type: 'date',
        },
        {
            label: 'basicSalary',
            name: 'basicSalary',
            type: 'text',
        },
        {
            label: 'positionId',
            name: 'positionId',
            type: 'text',
        },
        {
            label: 'ContractStatus',
            name: 'status',
            type: 'text',
        },
        {
            label: 'endDate',
            name: 'signedDate',
            type: 'date',
        },
        {
            label: 'probationPeriod',
            name: 'probationPeriod',
            type: 'text',
        },
        {
            label: 'terminationReason',
            name: 'terminationReason',
            type: 'text',
        },
        {
            label: 'endDate',
            name: 'terminationDate',
            type: 'text',
        },
        {
            label: 'file',
            name: 'fileIds',
            type: 'text',
        }
    ];

    const handleGetAllContracts = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllByEmployeePaged(employeeId, { page: page - 1, pageSize });
            const userList = response.content;

            if (response && Array.isArray(userList)) {
                const transformedUsers = userList.map((user) => ({
                    ...user,
                    keyDisplay: user.key,
                }));

                setContractSource(transformedUsers);
                setPagination((prev) => ({
                    ...prev,
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                }));
            } else {
                console.error('Invalid data users: ', response);
                setContractSource([]);
            }
        } catch (error) {
            console.error('Error fetching user', error);
            setContractSource([]);
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
            handleGetAllContracts();
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
            handleGetAllContracts();
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
            handleGetAllContracts();
            setIsModalOpen(false);
            setSelectedRowKeys([]); // Xóa các dòng đã chọn
            setSelectedRows([]); // Xóa các dòng đã chọn
        } catch (error) {
            message.error(`Lỗi khi xóa người dùng: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleSelectChange = (newSelectedRowKeys, newSelectedRows) => {
        setSelectedRowKeys(newSelectedRowKeys);
        setSelectedRows(newSelectedRows); // Lưu các dòng được chọn
    };

    useEffect(() => {
        handleGetAllContracts();
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
        handleGetAllContracts(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Người Dùng Mới';
            case 'edit':
                return 'Chỉnh Sửa Người Dùng';
            case 'delete':
                return 'Xóa Người Dùng';
            default:
                return 'Chi Tiết Người Dùng';
        }
    };

    return (
        <div className={cx('trailer-wrapper')}>
            <div className={cx('sub_header')}>
                <SmartInput size="large" placeholder="Tìm kiếm" icon={<SearchOutlined/>}/>
                <div className={cx('features')}>
                    <SmartButton title="Thêm mới" icon={<PlusOutlined/>} type="primary" onClick={handleAddUser}/>
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined/>}/>
                    <SmartButton title="Excel" icon={<CloudUploadOutlined/>} onClick={handleExportFile}/>
                </div>
            </div>
            <div className={cx('trailer-container')}>
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

export default Contract;