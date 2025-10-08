import React, { useState } from 'react';
import CustomTabs from '~/components/Layout/components/Tab';
import Candidate from "~/pages/AdminDashboard/Candidate";
import JobPosting from "~/pages/AdminDashboard/JobPosting";
import Interview from "~/pages/AdminDashboard/Interview";

function TabRecruitment() {
    const [activeKey, setActiveKey] = useState('1');
    const [selectedJobId, setSelectedJobId] = useState(null);

    const handleApplicantClick = (jobId) => {
        console.log('Debug: Click applicant, jobId =', jobId); // Log để check
        setSelectedJobId(jobId);
        setActiveKey('3'); // Switch tab sau khi set jobId
    };

    const tabItems = [
        {
            key: '1',
            label: 'Ứng viên',
            children: <Candidate />,
        },
        {
            key: '2',
            label: 'Tin tuyển dụng',
            children: <JobPosting onApplicantClick={handleApplicantClick} />,
        },
        {
            key: '3',
            label: 'Lịch phỏng vấn',
            children: (
                <Interview
                    jobId={selectedJobId}
                    onResetFilter={() => {
                        console.log('Debug: Reset filter, set selectedJobId to null');
                        setSelectedJobId(null);
                    }}
                />
            ),
        },
    ];

    return (
        <div>
            <CustomTabs
                items={tabItems}
                activeKey={activeKey}
                onChange={(key) => setActiveKey(key)}
                tabPosition="top"
            />
        </div>
    );
}

export default TabRecruitment;