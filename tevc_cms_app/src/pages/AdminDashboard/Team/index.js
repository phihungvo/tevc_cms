import React, {useState, useEffect} from 'react';
import classNames from 'classnames/bind';
import styles from '~/pages/AdminDashboard/Employee/Employee.module.scss';
import moment from 'moment';
import SmartTable from '~/components/Layout/components/SmartTable';
import {
    SearchOutlined, PlusOutlined, FilterOutlined, CloudUploadOutlined, EditOutlined, DeleteOutlined,
} from '@ant-design/icons';
import SmartInput from '~/components/Layout/components/SmartInput';
import SmartButton from '~/components/Layout/components/SmartButton';
import PopupModal from '~/components/Layout/components/PopupModal';
import {Form, message, Tag, DatePicker} from 'antd';
import {updateEmployee, deleteEmployee, getByDepartmentBasicInfo} from '~/service/admin/employee';
import {getAllDepartmentsNoPaging} from '~/service/admin/department';
import {exportExcelFile} from '~/service/admin/export_service';
import {createTeam, getAllTeams, getAllTeamsNoPaging} from "~/service/admin/team";

import 'moment/locale/vi';

moment.locale('vi');

const cx = classNames.bind(styles);

function Team() {
    const [teamSource, setTeamSource] = useState([]);
    const [departmentOptions, setDepartmentOptions] = useState([]);
    const [employeeOptions, setEmployeeOptions] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({
        current: 1, pageSize: 5, total: 0,
    });
    const [modalMode, setModalMode] = useState('create');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedTeam, setSelectedTeam] = useState(null);
    const [selectedDepartment, setSelectedDepartment] = useState(null);
    const [form] = Form.useForm();

    const genderStyles = {
        Male: {color: 'processing', label: 'Nam'}, Female: {color: 'error', label: 'Nữ'},
    };

    const columns = [{
        title: 'Team CODE', dataIndex: 'id', key: 'id', width: 150, fixed: 'left',
    }, {
        title: 'Team Name', dataIndex: 'name', key: 'name', width: 150,
    }, {
        title: 'Description', dataIndex: 'description', key: 'description', width: 150,
    }, {
        title: 'Actions', fixed: 'right', width: 180, render: (_, record) => (<>
            <SmartButton
                title="Sửa"
                type="primary"
                icon={<EditOutlined/>}
                buttonWidth={80}
                onClick={() => handleEditRole(record)}
            />
            <SmartButton
                title="Xóa"
                type="danger"
                icon={<DeleteOutlined/>}
                buttonWidth={80}
                onClick={() => handleDeleteEmployee(record)}
                style={{marginLeft: '8px'}}
            />
        </>),
    },];

    const userModalFields = [{
        label: 'Team Name', name: 'name', type: 'text', rules: [{required: true, message: 'Tên là bắt buộc!'}],
    }, {
        label: 'Description', name: 'description', type: 'text',
    }, {
        label: 'Department',
        name: 'departmentId',
        type: 'select',
        options: departmentOptions,
        onChange: (value) => handleDepartmentChange(value),
    },
        {
            label: 'Employee',
            name: 'employeeIds',
            type: 'select',
            options: employeeOptions,
            multiple: true,
        }];

    const fetchDepartmentOptions = async () => {
        try {
            const deptResponse = await getAllDepartmentsNoPaging();
            if (Array.isArray(deptResponse)) {
                const departments = deptResponse.map((dept) => ({
                    label: dept.name,
                    value: dept.id,
                }));
                setDepartmentOptions(departments);
            }
        } catch (error) {
            console.error('Error fetching departments:', error);
        }
    };

    const handleDepartmentChange = async (departmentId) => {
        if (!departmentId) {
            setEmployeeOptions([]);
            form.setFieldsValue({ employeeId: null });
            return;
        }

        try {
            const empResponse = await getByDepartmentBasicInfo(departmentId);

            if (empResponse && Array.isArray(empResponse.result)) {
                const employees = empResponse.result.map((emp) => ({
                    label: `${emp.lastName} ${emp.firstName}`,
                    value: emp.id,
                }));
                setEmployeeOptions(employees);

                form.setFieldsValue({ employeeId: null });
            } else {
                setEmployeeOptions([]);
            }
        } catch (error) {
            console.error("Error fetching employees:", error);
            setEmployeeOptions([]);
        }
    };

    const handleGetAllTeams = async (page = 1, size = 10) => {
        setLoading(true);
        try {
            const response = await getAllTeams({page: page - 1, size});

            if (!response?.content || !Array.isArray(response.content)) {
                console.error("Định dạng dữ liệu không hợp lệ:", response);
                setTeamSource([]);
                return;
            }

            setTeamSource(response.content);
            setPagination({
                current: page, pageSize: size, total: response.totalElements ?? 0,
            });
        } catch (error) {
            console.error("Lỗi khi lấy danh sách team:", error);
            setTeamSource([]);
        } finally {
            setLoading(false);
        }
    };

    const handleAddTeam = () => {
        setModalMode('create');
        setSelectedTeam(null);
        form.resetFields();
        setIsModalOpen(true);
    };

    const handleCallCreateTeam = async (formData) => {
        try {
            await createTeam(formData);
            handleGetAllTeams();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi tạo nhân viên: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleEditRole = (record) => {
        setSelectedTeam(record);
        setModalMode('edit');
        form.setFieldsValue({
            ...record,
            dateOfBirth: record.dateOfBirth && moment(record.dateOfBirth, moment.ISO_8601, true).isValid() ? moment(record.dateOfBirth) : null,
            hireDate: record.hireDate && moment(record.hireDate, moment.ISO_8601, true).isValid() ? moment(record.hireDate) : null,
        });
        setIsModalOpen(true);
    };

    const handleCallUpdateEmployee = async (formData) => {
        try {
            const formattedData = {
                ...formData,
                dateOfBirth: formData.dateOfBirth ? moment(formData.dateOfBirth).format('YYYY-MM-DD') : null,
                hireDate: formData.hireDate ? moment(formData.hireDate).format('YYYY-MM-DD') : null,
            };
            await updateEmployee(selectedTeam.id, formattedData);
            handleGetAllTeams();
            setIsModalOpen(false);
        } catch (error) {
            message.error(`Lỗi khi cập nhật nhân viên: ${error.response?.data?.message || error.message}`);
        }
    };

    const handleFormSubmit = (formData) => {
        console.log('Form Data:', formData);
        if (modalMode === 'create') {
            handleCallCreateTeam(formData);
        } else if (modalMode === 'edit') {
            handleCallUpdateEmployee(formData);
        } else if (modalMode === 'delete') {
            handleCallDeleteEmployee();
        }
    };

    const handleTableChange = (pagination) => {
        handleGetAllTeams(pagination.current, pagination.pageSize);
    };

    const handleDeleteEmployee = (record) => {
        setSelectedTeam(record);
        setModalMode('delete');
        form.resetFields();
        setIsModalOpen(true);
    }

    const handleCallDeleteEmployee = async () => {
        await deleteEmployee(selectedTeam.id);
        handleGetAllTeams();
        setIsModalOpen(false);
    }

    const handleExportFile = async () => {
        try {
            const response = await exportExcelFile('employee');
            if (!response.headers['content-type'].includes('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')) {
                throw new Error('Định dạng file không hợp lệ');
            }
            const url = window.URL.createObjectURL(response.data);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', `employee_${new Date().toISOString().replace(/[-:]/g, '')}.xlsx`);
            link.click();
            window.URL.revokeObjectURL(url);
            message.success('Tải file Excel thành công!');
        } catch (error) {
            console.error('Lỗi khi xuất file Excel:', error);
            message.error('Không thể tải file Excel');
        }
    };

    const getModalTitle = () => {
        switch (modalMode) {
            case 'create':
                return 'Thêm Team Mới';
            case 'edit':
                return 'Chỉnh Sửa Team';
            case 'delete':
                return 'Xóa Team';
            default:
                return 'Chi Tiết Team';
        }
    };

    useEffect(() => {
        fetchDepartmentOptions();
        handleGetAllTeams();
    }, []);

    return (<div className={cx('trailer-wrapper')}>
        <div className={cx('sub_header')}>
            <SmartInput
                size="large"
                placeholder="Search"
                icon={<SearchOutlined/>}
            />
            <div className={cx('features')}>
                <SmartButton
                    title="Add new"
                    icon={<PlusOutlined/>}
                    type="primary"
                    onClick={handleAddTeam}
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
                dataSources={teamSource}
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
            initialValues={selectedTeam}
            isDeleteMode={modalMode === 'delete'}
            formInstance={form}
        />
    </div>);
}

export default Team;
