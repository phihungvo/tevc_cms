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
import {getAllByEmployeePaged} from "~/service/admin/education";
import {exportExcelFile} from '~/service/admin/export_service';

const cx = classNames.bind(styles);

function Education({employeeId}) {
    const [educationSource, setEducationSource] = useState([]);
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
            title: 'Trường học',
            dataIndex: 'institutionName',
            key: 'institutionName',
            width: 150,
            fixed: 'left',
        },
        {
            title: 'Chuyên ngành',
            dataIndex: 'major',
            key: 'major',
            width: 150,
        },
        {
            title: 'Bằng cấp',
            dataIndex: 'degree',
            key: 'degree',
            width: 100,
        },
        {
            title: 'Ngày bắt đầu',
            dataIndex: 'startDate',
            key: 'startDate',
            width: 150,
        },
        {
            title: 'Ngày tốt nghiệp',
            dataIndex: 'graduationDate',
            key: 'graduationDate',
            width: 150,
        },
        {
            title: 'GPA',
            dataIndex: 'gpa',
            key: 'gpa',
            width: 150,
        },
        {
            title: 'Thao tác',
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
            label: 'Trường học',
            name: 'institutionName',
            type: 'text',
            rules: [{ required: true, message: 'Vui lòng nhập tên trường học!' }],
        },
        {
            label: 'Chuyên ngành',
            name: 'major',
            type: 'text',
            rules: [{ required: true, message: 'Vui lòng nhập chuyên ngành!' }],
        },
        {
            label: 'Bằng cấp',
            name: 'degree',
            type: 'text',
            rules: [{ required: true, message: 'Vui lòng nhập bằng cấp!' }],
        },
        {
            label: 'Ngày bắt đầu',
            name: 'startDate',
            type: 'date',
            render: () => <DatePicker format="YYYY-MM" picker="month" style={{ width: '100%' }} />,
            rules: [{ required: true, message: 'Vui lòng chọn ngày bắt đầu!' }],
        },
        {
            label: 'Ngày tốt nghiệp',
            name: 'graduationDate',
            type: 'date',
            render: () => <DatePicker format="YYYY-MM" picker="month" style={{ width: '100%' }} />,
        },
        {
            label: 'GPA',
            name: 'gpa',
            type: 'text',
        },
        {
            label: 'Mô tả',
            name: 'description',
            type: 'text',
        },
        {
            label: 'Trình độ',
            name: 'level',
            type: 'text',
        },
    ];

    const handleGetAllEducation = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllByEmployeePaged(employeeId, { page: page - 1, pageSize });
            const userList = response.content;

            if (response && Array.isArray(userList)) {
                const transformedUsers = userList.map((user) => ({
                    ...user,
                    keyDisplay: user.key,
                }));

                setEducationSource(transformedUsers);
                setPagination((prev) => ({
                    ...prev,
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                }));
            } else {
                console.error('Invalid data users: ', response);
                setEducationSource([]);
            }
        } catch (error) {
            console.error('Error fetching user', error);
            setEducationSource([]);
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
            handleGetAllEducation();
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
            handleGetAllEducation();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật người dùng: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleDeleteUser = (record) => {
        setModalMode('delete');
        setSelectedRowKeys([record.id]);
        setSelectedRows([record]);
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
            handleGetAllEducation();
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
        handleGetAllEducation();
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
        handleGetAllEducation(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm học vấn mới';
            case 'edit':
                return 'Chỉnh sửa học vấn';
            case 'delete':
                return 'Xóa học vấn';
            default:
                return 'Chi Tiết học vấn';
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
                    dataSources={educationSource}
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

export default Education;