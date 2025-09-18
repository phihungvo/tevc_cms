import React, {useState, useEffect} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {Card, Avatar, Descriptions, Button, Row, Col, Divider, message, Upload} from 'antd';
import {UserOutlined, UploadOutlined} from '@ant-design/icons';
import moment from 'moment';
import {employeeDetail} from '~/service/admin/employee';
import {uploadFile, getPresignedUrl} from '~/service/admin/uploadFile';
import {useAuth} from '~/routes/AuthContext';
import 'moment/locale/vi';

moment.locale('vi');

function EmployeeDetail() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {user} = useAuth();
    const [employee, setEmployee] = useState(null);
    const [loading, setLoading] = useState(true);
    const [avatarUrl, setAvatarUrl] = useState(null);

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

                    // Lấy presigned URL nếu có profilePictureId
                    if (response.profilePictureId) {
                        const presignedUrl = await getPresignedUrl(response.profilePictureId);
                        setAvatarUrl(presignedUrl);
                    } else {
                        setAvatarUrl('/default-avatar.png');
                    }
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

    const handleAvatarUpload = async (file) => {
        try {
            const uploadedFile = await uploadFile(file, parseInt(id));

            // const parsedFile = JSON.parse(uploadedFile);
            const presignedUrl = await getPresignedUrl(uploadedFile.result.id);
            setAvatarUrl(presignedUrl);
            message.success('Tải ảnh đại diện thành công!');

            const updatedEmployee = await employeeDetail(id);
            setEmployee(updatedEmployee);
        } catch (error) {
            message.error(`Lỗi khi tải ảnh đại diện: ${error.message}`);
        }
        return false;
    };

    if (loading) return <div>Đang tải...</div>;
    if (!employee) return <div>Không tìm thấy nhân viên</div>;

    return (
        <Card
            title={
                <Row align="middle" gutter={16}>
                    <Col>
                        <Avatar size={100} src={avatarUrl}/>
                        <Upload
                            name="avatar"
                            showUploadList={false}
                            beforeUpload={handleAvatarUpload}
                            accept="image/*"
                        >
                            <Button icon={<UploadOutlined/>} style={{marginTop: 8}}>Tải ảnh lên</Button>
                        </Upload>
                    </Col>
                    <Col>
                        <h2>Chi tiết Nhân viên</h2>
                        <span style={{color: '#1890ff'}}>Mã: {employee.employeeCode}</span>
                    </Col>
                </Row>
            }
            extra={<Button type="primary" icon={<UserOutlined/>} onClick={() => navigate('/admin/employee')}>Quay
                lại</Button>}
            style={{margin: '0 auto'}}
        >
            <Divider/>
            <Descriptions column={1} bordered size="small">
                <Descriptions.Item label="Họ">{employee.lastName}</Descriptions.Item>
                <Descriptions.Item label="Tên">{employee.firstName}</Descriptions.Item>
                <Descriptions.Item label="Ngày sinh">
                    {employee.dateOfBirth ? moment(employee.dateOfBirth).format('DD/MM/YYYY') : 'N/A'}
                </Descriptions.Item>
                <Descriptions.Item label="Giới tính">
                    {employee.gender === 'Male' ? 'Nam' : employee.gender === 'Female' ? 'Nữ' : 'Khác'}
                </Descriptions.Item>
                <Descriptions.Item label="Email">{employee.email}</Descriptions.Item>
                <Descriptions.Item label="Số điện thoại">{employee.phone}</Descriptions.Item>
                <Descriptions.Item label="Địa chỉ">{employee.address}</Descriptions.Item>
                <Descriptions.Item label="Ngày vào làm">
                    {employee.hireDate ? moment(employee.hireDate).format('DD/MM/YYYY') : 'N/A'}
                </Descriptions.Item>
                <Descriptions.Item label="Trạng thái">
                    {employee.isActive ? 'Hoạt động' : 'Không hoạt động'}
                </Descriptions.Item>
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