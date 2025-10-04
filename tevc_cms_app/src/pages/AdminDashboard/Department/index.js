import React from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Department/Department.module.scss';
import {useState, useEffect} from 'react';
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
import {Form, message, Tag} from 'antd';
import {getAllDepartments, createDepartment, updateDepartment, deleteDepartment} from '~/service/admin/department';
import {getAllByTitle, getAllNoPaging} from '~/service/admin/position';
import {getEmployeeByPositionType} from '~/service/admin/employee';
import {exportExcelFile} from "~/service/admin/export_service";

const cx = classNames.bind(styles);

function Department() {
    const [employeeSource, setDepartmentSource] = useState([]);
    const [managerPositionSource, setManagerPositionSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1,
        pageSize: 5,
        total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedDepartment, setSelectedDepartment] = useState(null);
    const [form] = Form.useForm();

    const columns = [
        {
            title: 'Tên phòng ban',
            dataIndex: 'name',
            key: 'name',
            width: 200,
            fixed: 'left',
        },
        {
            title: 'Tên quản lý',
            dataIndex: 'managerName',
            key: 'managerName',
        },
        {
            title: 'Số lượng nhân viên',
            dataIndex: 'employeeCount',
            key: 'employeeCount',
            width: 100,
        },
        {
            title: 'Mô tả chi tiết',
            dataIndex: 'description',
            key: 'description',
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
                        onClick={() => handleEditDepartment(record)}
                    />
                    <SmartButton
                        type="danger"
                        icon={<DeleteOutlined/>}
                        buttonWidth={50}
                        onClick={() => handleDeleteDepartment(record)}
                        style={{marginLeft: '8px'}}
                    />
                </>
            ),
        },
    ];

    const userModalFields = [
        {
            label: 'Tên phòng ban',
            name: 'name',
            type: 'text',
            rules: [{required: true, message: 'Tên phòng ban bắt buộc!'}],
        },
        {
            label: 'Mô tả chi tiết',
            name: 'description',
            type: 'text',
        },

        {
            label: 'Nhân sự quản lý',
            name: 'managerId',
            type: 'select',
            options: managerPositionSource,
        },
        // {
        //     label: 'Số lượng nhân viên',
        //     name: 'employeeCount',
        //     type: 'number',
        // },
    ];

    useEffect(() => {
        handleGetManager('MANAGER');
        handleGetAllDepartments();
    }, []);

    const handleGetManager = async (positionType) => {
        try {
            // const response = await getAllByTitle(type);
            const response = await getEmployeeByPositionType(positionType);
            const mappedEmployee = response.map(employee => ({
                value: employee.id,
                label: employee.firstName + ' ' + employee.lastName,
            }));
            setManagerPositionSource(mappedEmployee);
        } catch (error) {
            console.error('Error fetching manager employee:', error);
            setManagerPositionSource([]);
        }
    };

    const handleGetAllDepartments = async (page = 1, pageSize = 10) => {
        setLoading(true);
        try {
            const response = await getAllDepartments({page: page - 1, pageSize});
            if (response && Array.isArray(response.content)) {
                const mappedDepartments = response.content.map(employee => ({
                    ...employee,
                    isActive: employee.active,
                }));
                setDepartmentSource(mappedDepartments);
                setPagination({
                    current: page,
                    pageSize: pageSize,
                    total: response.totalElements,
                });
            } else {
                console.error('Invalid data format:', response);
                setDepartmentSource([]);
            }
        } catch (error) {
            console.error('Error fetching employees:', error);
            setDepartmentSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddRole = () => {
        setModalMode('create');
        setSelectedDepartment(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreatePermission = async (formData) => {
        try {
            await createDepartment(formData);
            handleGetAllDepartments();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Error creating employee: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditDepartment = (record) => {
        setSelectedDepartment(record);
        setModalMode('edit');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    };

    const handleCallUpdateDepartment = async (formData) => {
        console.log('formdata: ', formData)
        await updateDepartment(selectedDepartment.id, formData);
        handleGetAllDepartments();
        setIsModalOpen(false);
    };

    const handleDeleteDepartment = (record) => {
        setSelectedDepartment(record);
        setModalMode('delete');
        form.setFieldsValue(record);
        setIsModalOpen(true);
    }

    const handleCallDeleteDepartment = async () => {
        await deleteDepartment(selectedDepartment.id);
        handleGetAllDepartments();
        setIsModalOpen(false);
    }

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('department');
            if (!response.headers['content-type'].includes('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `department_${new Date().toISOString().replace(/[-:]/g, '')}.xlsx`);
            link.click();
            window.URL.revokeObjectURL(url);
            message.success('Tải file Excel thành công!');
        } catch (error) {
            console.error('Lỗi khi xuất file Excel:', error);
            message.error('Không thể tải file Excel');
        }
    };

    // updateDepartment
    const handleFormSubmit = (formData) => {
        if (modalMode === 'create') {
            handleCallCreatePermission(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateDepartment(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteDepartment();
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllDepartments(pagination.current, pagination.pageSize);
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm phòng ban';
            case 'edit':
                return 'Chỉnh sửa phòng ban';
            case 'delete':
                return 'Xoá phòng ban';
            default:
                return 'Department Details';
        }
    };

    return (
        <div className={cx('trailer-wrapper')}>
            <div className={cx('sub_header')}>
                <SmartInput
                    size="large"
                    placeholder="Search"
                    icon={<SearchOutlined/>}
                />
                <div className={cx('features')}>
                    <SmartButton
                        title="Thêm"
                        icon={<PlusOutlined/>}
                        type="primary"
                        onClick={handleAddRole}
                    />
                    <SmartButton title="Bộ lọc" icon={<FilterOutlined/>}/>
                    <SmartButton
                        title="Excel"
                        icon={<CloudUploadOutlined/>}
                        onClick={handleExportFile}
                    />
                </div>
            </div>
            <div className={cx('trailer-container')}>
                <SmartTable
                    columns={columns}
                    dataSources={employeeSource}
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
                initialValues={selectedDepartment}
                isDeleteMode={modalMode === 'delete'}
                formInstance={form}
            />
        </div>
    );
}

export default Department;