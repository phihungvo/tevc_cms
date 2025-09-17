import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Descriptions, Button, message } from 'antd';
import moment from 'moment';
import { employeeDetail } from '~/service/admin/employee';
import { useAuth } from '~/routes/AuthContext';
import 'moment/locale/vi';
moment.locale('vi');

function EmployeeDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  const [employee, setEmployee] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!user || !user.token) {
      message.error('Vui lòng đăng nhập để xem chi tiết nhân viên');
      navigate('/login');
      return;
    }

    const fetchEmployee = async () => {
      try {
        const response = await employeeDetail(id); 
        if (response) {
          setEmployee({
            ...response,
            dateOfBirth: response.dateOfBirth ? moment(response.dateOfBirth).format('YYYY-MM-DD') : null,
            hireDate: response.hireDate ? moment(response.hireDate).format('YYYY-MM-DD') : null,
          });
        } else {
          throw new Error('Không tìm thấy nhân viên');
        }
      } catch (error) {
        message.error(`Lỗi khi load chi tiết nhân viên: ${error.message}`);
        navigate('/admin/employee');
      } finally {
        setLoading(false);
      }
    };
    fetchEmployee();
  }, [id, user, navigate]);

  if (loading) return <div>Loading...</div>;
  if (!employee) return <div>Không tìm thấy nhân viên</div>;

  return (
    <Card
      title="Chi tiết Nhân viên"
      extra={<Button onClick={() => navigate('/admin/employee')}>Quay lại</Button>}
    >
      <Descriptions bordered column={1}>
        <Descriptions.Item label="Mã nhân viên">{employee.employeeCode}</Descriptions.Item>
        <Descriptions.Item label="Họ">{employee.lastName}</Descriptions.Item>
        <Descriptions.Item label="Tên">{employee.firstName}</Descriptions.Item>
        <Descriptions.Item label="Ngày sinh">
          {employee.dateOfBirth ? moment(employee.dateOfBirth).format('DD/MM/YYYY') : 'N/A'}
        </Descriptions.Item>
        <Descriptions.Item label="Giới tính">{employee.gender === 'Male' ? 'Nam' : employee.gender === 'Female' ? 'Nữ' : 'Khác'}</Descriptions.Item>
        <Descriptions.Item label="Email">{employee.email}</Descriptions.Item>
        <Descriptions.Item label="Số điện thoại">{employee.phone}</Descriptions.Item>
        <Descriptions.Item label="Địa chỉ">{employee.address}</Descriptions.Item>
        <Descriptions.Item label="Ngày vào làm">
          {employee.hireDate ? moment(employee.hireDate).format('DD/MM/YYYY') : 'N/A'}
        </Descriptions.Item>
        <Descriptions.Item label="Trạng thái">{employee.isActive ? 'Hoạt động' : 'Không hoạt động'}</Descriptions.Item>
        <Descriptions.Item label="Ngày tạo">
          {employee.createdAt ? moment(employee.createdAt).format('DD/MM/YYYY') : 'N/A'}
        </Descriptions.Item>
        <Descriptions.Item label="Ngày cập nhật">
          {employee.updatedAt ? moment(employee.updatedAt).format('DD/MM/YYYY') : 'N/A'}
        </Descriptions.Item>
      </Descriptions>
    </Card>
  );
}

export default EmployeeDetail;