import React, {useState, useEffect} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {
    Card,
    Avatar,
    Descriptions,
    Button,
    Row,
    Col,
    Divider,
    message,
    Upload,
    Watermark,
    Badge,
    Space,
    Typography,
    Tooltip
} from 'antd';
import {
    UserOutlined,
    UploadOutlined,
    ArrowLeftOutlined,
    CalendarOutlined,
    PhoneOutlined,
    MailOutlined,
    HomeOutlined,
    IdcardOutlined
} from '@ant-design/icons';
import moment from 'moment';
import {employeeDetail} from '~/service/admin/employee';
import {uploadFile, getPresignedUrl} from '~/service/admin/uploadFile';
import {useAuth} from '~/routes/AuthContext';
import 'moment/locale/vi';
import CustomTabs from "~/components/Layout/components/Tab";
import WorkHistory from "~/pages/AdminDashboard/WorkHistory";
import Skill from "~/pages/AdminDashboard/Skill";
import Education from "~/pages/AdminDashboard/Education";

const { Title, Text } = Typography;

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

    if (loading) return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '50vh'
        }}>
            <Text>Đang tải...</Text>
        </div>
    );

    if (!employee) return (
        <div style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            height: '50vh'
        }}>
            <Text type="secondary">Không tìm thấy nhân viên</Text>
        </div>
    );

    const tabItems = [
        {
            key: '1',
            label: 'Lịch sử công việc',
            children: employee && employee.workHistory && employee.workHistory.length > 0 ? (
                <WorkHistory />
            ) : (
                <Card style={{ minHeight: 400 }}>
                    <Watermark content="Ant Design">
                        <div style={{ height: 400, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                            <Text type="secondary">Nội dung lịch sử công việc sẽ được hiển thị ở đây</Text>
                        </div>
                    </Watermark>
                </Card>
            ),
        },
        {
            key: '2',
            label: 'Kỹ năng',
            children: <Skill employeeId={id}/>,
        },
        {
            key: '3',
            label: 'Học vấn',
            children: <Education employeeId={id}/>,
        },
        {
            key: '4',
            label: 'Báo cáo',
            children: (
                <Card style={{ minHeight: 400 }}>
                    <Watermark content="Ant Design">
                        <div style={{ height: 400, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                            <Text type="secondary">Nội dung báo cáo sẽ được hiển thị ở đây</Text>
                        </div>
                    </Watermark>
                </Card>
            ),
        },
    ];

    return (
        <div style={{ minHeight: '100vh' }}>
            {/* Header với nút quay lại */}
            <Row style={{ marginBottom: '24px' }}>
                <Col span={24}>
                    <Button
                        type="text"
                        icon={<ArrowLeftOutlined />}
                        onClick={() => navigate('/admin/employee')}
                        style={{ fontSize: '14px' }}
                    >
                        Quay lại danh sách nhân viên
                    </Button>
                </Col>
            </Row>

            {/* Card thông tin chính */}
            <Card
                style={{
                    marginBottom: '24px',
                    borderRadius: '12px',
                    boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
                }}
                // bodyStyle={{ padding: '32px' }}
            >
                <Row gutter={[32, 24]}>
                    {/* Cột avatar */}
                    <Col xs={24} sm={8} md={6} lg={5}>
                        <div style={{ textAlign: 'center' }}>
                            <div style={{ position: 'relative', display: 'inline-block' }}>
                                <Avatar
                                    size={120}
                                    src={avatarUrl}
                                    style={{
                                        border: '4px solid #f0f0f0',
                                        boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
                                    }}
                                />
                                <Badge
                                    status={employee.active ? "success" : "default"}
                                    style={{
                                        position: 'absolute',
                                        bottom: '8px',
                                        right: '8px',
                                        transform: 'scale(1.2)'
                                    }}
                                />
                            </div>
                            <div style={{ marginTop: '16px' }}>
                                <Upload
                                    name="avatar"
                                    showUploadList={false}
                                    beforeUpload={handleAvatarUpload}
                                    accept="image/*"
                                >
                                    <Button
                                        icon={<UploadOutlined/>}
                                        size="small"
                                        style={{
                                            borderRadius: '6px',
                                            fontSize: '12px'
                                        }}
                                    >
                                        Cập nhật ảnh
                                    </Button>
                                </Upload>
                            </div>
                        </div>
                    </Col>

                    {/* Cột thông tin */}
                    <Col xs={24} sm={16} md={18} lg={19}>
                        <Row>
                            <Col span={24}>
                                <Space direction="vertical" size="small" style={{ width: '100%' }}>
                                    <div>
                                        <Title level={2} style={{ margin: 0, color: '#1a1a1a' }}>
                                            {employee.lastName} {employee.firstName}
                                        </Title>
                                        <Text type="secondary" style={{ fontSize: '16px' }}>
                                            <IdcardOutlined style={{ marginRight: '8px' }} />
                                            Mã NV: {employee.employeeCode}
                                        </Text>
                                    </div>

                                    <div style={{ marginTop: '16px' }}>
                                        <Badge
                                            status={employee.active ? "success" : "error"}
                                            text={
                                                <Text strong style={{ fontSize: '14px' }}>
                                                    {employee.active ? 'Đang hoạt động' : 'Không hoạt động'}
                                                </Text>
                                            }
                                        />
                                    </div>
                                </Space>
                            </Col>
                        </Row>

                        {/* Thông tin liên hệ nhanh */}
                        <Row style={{ marginTop: '24px' }}>
                            <Col span={24}>
                                <Space wrap size="large">
                                    <Tooltip title="Email">
                                        <Button
                                            type="text"
                                            icon={<MailOutlined />}
                                            style={{
                                                display: 'flex',
                                                alignItems: 'center',
                                                padding: '4px 12px',
                                                height: 'auto'
                                            }}
                                        >
                                            {employee.email}
                                        </Button>
                                    </Tooltip>
                                    <Tooltip title="Số điện thoại">
                                        <Button
                                            type="text"
                                            icon={<PhoneOutlined />}
                                            style={{
                                                display: 'flex',
                                                alignItems: 'center',
                                                padding: '4px 12px',
                                                height: 'auto'
                                            }}
                                        >
                                            {employee.phone}
                                        </Button>
                                    </Tooltip>
                                </Space>
                            </Col>
                        </Row>
                    </Col>
                </Row>

                <Divider style={{ margin: '32px 0 24px 0' }} />

                {/* Thông tin chi tiết */}
                <Row gutter={[24, 16]}>
                    <Col xs={24} lg={12}>
                        <Descriptions
                            column={1}
                            size="middle"
                            labelStyle={{
                                fontWeight: '500',
                                color: '#666',
                                width: '120px'
                            }}
                            contentStyle={{
                                color: '#1a1a1a'
                            }}
                        >
                            <Descriptions.Item
                                label={
                                    <Space>
                                        <CalendarOutlined />
                                        Ngày sinh
                                    </Space>
                                }
                            >
                                {employee.dateOfBirth ? moment(employee.dateOfBirth).format('DD/MM/YYYY') : 'Chưa cập nhật'}
                            </Descriptions.Item>
                            <Descriptions.Item
                                label={
                                    <Space>
                                        <UserOutlined />
                                        Giới tính
                                    </Space>
                                }
                            >
                                {employee.gender}
                            </Descriptions.Item>
                            <Descriptions.Item
                                label={
                                    <Space>
                                        <HomeOutlined />
                                        Địa chỉ
                                    </Space>
                                }
                            >
                                {employee.address || 'Chưa cập nhật'}
                            </Descriptions.Item>
                        </Descriptions>
                    </Col>

                    <Col xs={24} lg={12}>
                        <Descriptions
                            column={1}
                            size="middle"
                            labelStyle={{
                                fontWeight: '500',
                                color: '#666',
                                width: '180px'
                            }}
                            contentStyle={{
                                color: '#1a1a1a'
                            }}
                        >
                            <Descriptions.Item
                                label={
                                    <Space>
                                        <CalendarOutlined />
                                        Ngày vào làm
                                    </Space>
                                }
                            >
                                {employee.hireDate ? moment(employee.hireDate).format('DD/MM/YYYY') : 'Chưa cập nhật'}
                            </Descriptions.Item>
                            <Descriptions.Item
                                label={
                                    <Space>
                                        <IdcardOutlined />
                                        Ngày tạo
                                    </Space>
                                }
                            >
                                {employee.createdAt ? moment(employee.createdAt).format('DD/MM/YYYY HH:mm') : 'N/A'}
                            </Descriptions.Item>
                            <Descriptions.Item
                                label={
                                    <Space>
                                        <UserOutlined />
                                        Cập nhật lần cuối
                                    </Space>
                                }
                            >
                                {employee.updatedAt ? moment(employee.updatedAt).format('DD/MM/YYYY HH:mm') : 'N/A'}
                            </Descriptions.Item>
                        </Descriptions>
                    </Col>
                </Row>
            </Card>

            {/* Tab content */}
            <Card
                style={{
                    borderRadius: '12px',
                    boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
                }}
                bodyStyle={{ padding: '24px' }}
            >
                <CustomTabs
                    items={tabItems}
                    defaultActiveKey="1"
                    tabPosition="top"
                    onChange={(key) => console.log('Tab changed:', key)}
                    style={{ minHeight: '400px' }}
                />
            </Card>
        </div>
    );
}

export default EmployeeDetail;