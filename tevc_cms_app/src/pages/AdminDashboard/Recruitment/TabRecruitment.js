import React from 'react';
import CustomTabs from '~/components/Layout/components/Tab';
import UserList from "~/pages/AdminDashboard/User/index";
import RoleList from "~/pages/AdminDashboard/Role";
import PermissionList from "~/pages/AdminDashboard/Permission";
import { Watermark } from 'antd';
import Candidate from "~/pages/AdminDashboard/Candidate";

function TabRecruitment() {
    const tabItems = [
        {
            key: '1',
            label: 'Ứng viên',
            children: <Candidate />,
        },
        {
            key: '2',
            label: 'Tin tuyển dụng',
            children: <RoleList />,
        },
        {
            key: '3',
            label: 'Lịch phỏng vấn',
            children: <PermissionList />,
        },
        {
            key: '4',
            label: 'Báo cáo',
            children: (
                <>
                    <Watermark content="Ant Design">
                        <div style={{ height: 500 }} />
                    </Watermark>
                </>
            ),
        },
    ];

    return (
        <div>
            <CustomTabs
                items={tabItems}
                defaultActiveKey="1"
                tabPosition="top"
                onChange={(key) => console.log('Tab changed:', key)}
            />
        </div>
    );
}

export default TabRecruitment;