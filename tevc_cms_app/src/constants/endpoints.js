const BASE_URL = process.env.REACT_APP_API_URL || '/api';

const API_ENDPOINTS = {
    AUTH: {
        LOGIN: `${BASE_URL}/auth/login`,
        REGISTER: `${BASE_URL}/auth/register`,
    },
    FILE: {
        UPLOAD: `${BASE_URL}/storage/upload`,
        CHECK_EXISTED: (file) =>
            `${BASE_URL}/storage/checkFileExists/${file.name}`,
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
        CREATE: `${BASE_URL}/employees`,
        GET_EMPLOYEE_BY_POSITION_TYPE: `${BASE_URL}/employees/by-position-type`,
        UPDATE: (employeeId) => `${BASE_URL}/employees/${employeeId}`,
        DELETE: (employeeId) => `${BASE_URL}/employees/${employeeId}`,
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
        CREATE: `${BASE_URL}/positions`,
        GET_BY_TITLE: `${BASE_URL}/positions/getPositionsByTitle`,
        UPDATE: (positionId) => `${BASE_URL}/positions/${positionId}`,
        DELETE: (positionId) => `${BASE_URL}/positions/${positionId}`,
    },
    LEAVE: {
        GET_ALL: `${BASE_URL}/leaves`,
        CREATE: `${BASE_URL}/leaves`,
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
        CREATE: `${BASE_URL}/attendances`,
        UPDATE: (attendanceId) => `${BASE_URL}/attendances/${attendanceId}`,
    }
};

export default API_ENDPOINTS;
