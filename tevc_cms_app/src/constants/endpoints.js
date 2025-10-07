const BASE_URL = process.env.REACT_APP_API_URL || '/api';

const API_ENDPOINTS = {
    AUTH: {
        LOGIN: `${BASE_URL}/auth/login`,
        REGISTER: `${BASE_URL}/auth/register`,
    },
    FILE: {
        UPLOAD: `${BASE_URL}/files/upload`,
        CHECK_EXISTED: (file) =>
            `${BASE_URL}/storage/checkFileExists/${file.name}`,
        PRESIGNED_URL: (fileId) => `${BASE_URL}/files/presigned-url/${fileId}`,
        GET_INFO: `${BASE_URL}/storage/files`,
        GET_FILE: (filePath) => `${BASE_URL}/storage/files/${filePath}`,
    },
    SERVICE: {
        EXPORT_EXCEL: `${BASE_URL}/export/excel`,
    },
    USER: {
        GET_ALL: `${BASE_URL}/user/getAll`,
        CREATE: `${BASE_URL}/user/createUser`,
        UPDATE: (userId) => `${BASE_URL}/user/${userId}`,
        DELETE: `${BASE_URL}/user`,    
    },
    ROLE: {
        GET_ALL: `${BASE_URL}/roles`,
        GET_ALL_NO_PAGING: `${BASE_URL}/roles/noPaging`,
        CREATE: `${BASE_URL}/roles`,
        UPDATE: (roleId) => `${BASE_URL}/roles/${roleId}`,
        DELETE: (roleId) => `${BASE_URL}/roles/${roleId}`,
    },
    PERMISSION: {
        GET_ALL: `${BASE_URL}/permissions`,
        GET_ALL_NO_PAGING: `${BASE_URL}/permissions/noPaging`,
        CREATE: `${BASE_URL}/permissions`,
        UPDATE: (permissionId) => `${BASE_URL}/permissions/${permissionId}`,
    },
    EMPLOYEE: {
        GET_ALL: `${BASE_URL}/employees`,
        NO_PAGING:  `${BASE_URL}/employees/no-paging`,
        DETAIL: (employeeId) => `${BASE_URL}/employees/${employeeId}`,
        CREATE: `${BASE_URL}/employees`,
        GET_EMPLOYEE_BY_POSITION_TYPE: `${BASE_URL}/employees/by-position-type`,
        UPDATE: (employeeId) => `${BASE_URL}/employees/${employeeId}`,
        DELETE: (employeeId) => `${BASE_URL}/employees/${employeeId}`,
        BY_DEPARTMENTS_BASIC_RESPONSE: `${BASE_URL}/employees/by-department/basic`,
    },
    DEPARTMENT: {
        GET_ALL: `${BASE_URL}/departments`,
        CREATE: `${BASE_URL}/departments`,
        UPDATE: (departmentId) => `${BASE_URL}/departments/${departmentId}`,
        DELETE: (departmentId) => `${BASE_URL}/departments/${departmentId}`,
        NO_PAGING: `${BASE_URL}/departments/no-paging`,
    },
    POSITION: {
        GET_ALL: `${BASE_URL}/positions`,
        GET_ALL_NO_PAGING: `${BASE_URL}/positions/no-paging`,
        ALL_BY_EMPLOYEE_PAGED: (employeeId) => `${BASE_URL}/positions/employee/${employeeId}/paged`,
        CREATE: `${BASE_URL}/positions`,
        GET_BY_TITLE: `${BASE_URL}/positions/getPositionsByTitle`,
        UPDATE: (positionId) => `${BASE_URL}/positions/${positionId}`,
        DELETE: (positionId) => `${BASE_URL}/positions/${positionId}`,
    },
    LEAVE: {
        GET_ALL: `${BASE_URL}/leaves`,
        CREATE: `${BASE_URL}/leaves`,
        ALL_BY_EMPLOYEE_PAGED: (employeeId) => `${BASE_URL}/leaves/employee/${employeeId}/paged`,
        APPROVE: (leaveId) => `${BASE_URL}/leaves/${leaveId}/approve`,
        REJECT: (leaveId) => `${BASE_URL}/leaves/${leaveId}/reject`,
        UPDATE: (leaveId) => `${BASE_URL}/leaves/${leaveId}`,
        DELETE: (leaveId) => `${BASE_URL}/leaves/${leaveId}`,
    },
    PAYROLL: {
        GET_ALL: `${BASE_URL}/payrolls`,
        CREATE: `${BASE_URL}/payrolls`,
        CALCULATE: (employeeId) => `${BASE_URL}/payrolls/calculate/${employeeId}`,
        PROCESS: `${BASE_URL}/payrolls/process`,
        UPDATE: (payRollId) => `${BASE_URL}/payrolls/${payRollId}`,
    },
    ATTENDANCE: {
        GET_ALL: `${BASE_URL}/attendances`,
        GET_ALL_NO_PAGING: `${BASE_URL}/attendances/no-pagination`,
        FILTER: `${BASE_URL}/attendances/filter`,
        CREATE: `${BASE_URL}/attendances`,
        UPDATE: (attendanceId) => `${BASE_URL}/attendances/${attendanceId}`,
    },
    TEAM: {
        GET_ALL: `${BASE_URL}/teams`,
        NO_PAGING: `${BASE_URL}/teams/no-paging`,
        CREATE: `${BASE_URL}/teams`,
        UPDATE: (teamId) => `${BASE_URL}/teams/${teamId}`,
        DELETE: (teamId) => `${BASE_URL}/teams/${teamId}`,
        ADD_MEMBER: (teamId, memberId) => `${BASE_URL}/teams/${teamId}/addMember/${memberId}`,
        REMOVE_MEMBER: (teamId, memberId) => `${BASE_URL}/teams/${teamId}/removeMember/${memberId}`,
    },
    WORK_HISTORY: {
        GET_ALL: `${BASE_URL}/work-histories`,
        ALL_BY_EMPLOYEE_PAGED: (employeeId) => `${BASE_URL}/work-histories/employee/${employeeId}/paged`,
    },
    SKILL: {
        GET_ALL: `${BASE_URL}/skills`,
        ALL_BY_EMPLOYEE_PAGED: (employeeId) => `${BASE_URL}/skills/employee/${employeeId}/paged`,
        NO_PAGING: `${BASE_URL}/skills/no-paging`,
        CREATE: `${BASE_URL}/skills`,
        UPDATE: (skillId) => `${BASE_URL}/skills/${skillId}`,
        DELETE: (skillId) => `${BASE_URL}/skills/${skillId}`,
    },
    EDUCATION: {
        GET_ALL: `${BASE_URL}/educations`,
        ALL_BY_EMPLOYEE_PAGED: (employeeId) => `${BASE_URL}/educations/employee/${employeeId}/paged`,
    },
    CONTRACT: {
        GET_ALL: `${BASE_URL}/contracts`,
        ALL_BY_EMPLOYEE_PAGED: (employeeId) => `${BASE_URL}/contracts/employee/${employeeId}/paged`,
        UPLOAD_FILE_CONTRACT: (contractId) => `${BASE_URL}/files/upload/contract/${contractId}`,
        CREATE: `${BASE_URL}/contracts`,
    },
    CANDIDATE: {
        GET_ALL: `${BASE_URL}/candidates/paginated`,
    },
    JOB_POSTING: {
        GET_ALL: `${BASE_URL}/job-postings/paginated`,
        GET_BY_CANDIDATE: (candidateId) => `${BASE_URL}/job-postings/${candidateId}/by-candidate`,
        FILTER: `${BASE_URL}/job-postings/filter`
    }
};

export default API_ENDPOINTS;
