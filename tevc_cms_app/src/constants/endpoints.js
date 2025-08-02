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

    USER: {
        GET_ALL: `${BASE_URL}/user/getAll`,
        CREATE: `${BASE_URL}/user/createUser`,
        UPDATE: (userId) => `${BASE_URL}/user/${userId}/update`,
    },
    ROLE: {
        GET_ALL: `${BASE_URL}/roles`,
        CREATE: `${BASE_URL}/roles`,
    },
    PERMISSION: {
        GET_ALL: `${BASE_URL}/permissions`,
        CREATE: `${BASE_URL}/permissions`,
    },
    EMPLOYEE: {
        GET_ALL: `${BASE_URL}/employees`,
        CREATE: `${BASE_URL}/employees`,
    },
    DEPARTMENT: {
        GET_ALL: `${BASE_URL}/departments`,
        CREATE: `${BASE_URL}/departments`,
    },
    POSITION: {
        GET_ALL: `${BASE_URL}/positions`,
        CREATE: `${BASE_URL}/positions`,
    }
};

export default API_ENDPOINTS;
