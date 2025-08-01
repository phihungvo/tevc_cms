import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';

export const login = async (email, password) => {
    try {
        const response = await axios.post(
            API_ENDPOINTS.AUTH.LOGIN,
            { email, password },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            },
        );

        if (response.status === 200) return response.data.token;
    } catch (error) {
        console.log('Login error: ', error);
        console.error('Error in login !');
    }
};

export const register = async (username, email, password) => {
    try {
        const response = await axios.post(
            API_ENDPOINTS.AUTH.REGISTER,
            { username, email, password },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            },
        );

        if (response.status === 200) return response.data.token;
    } catch (error) {
        console.log('Register error: ', error);
    }
};

export const getAllUser = async ({ page = 0, pageSize = 5 }) => {
    try {
        const response = await axios.get(API_ENDPOINTS.USER.GET_ALL, {
            params: { page, pageSize },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });

        console.log('user response: ', response.data);
        return response.data;
    } catch (error) {
        console.log('Error when fetching all user ! Error: ', error);
        message.error('Error get all user: ');
    }
};

export const createUser = async (formData) => {
    try {
        console.log('form data user: ', formData);
        const processedData = {
            ...formData,
            roles: formData.roles && typeof formData.roles === 'string'
                ? formData.roles.split(',').map((role) => role.trim())
                : [], // Gán mảng rỗng nếu roles không tồn tại hoặc không hợp lệ
        };

        const response = await axios.post(
            API_ENDPOINTS.USER.CREATE,
            processedData,
            {
                headers: {
                    Authorization: `Bearer ${getToken()}`,
                    'Content-Type': 'application/json',
                },
            },
        );

        if (response.status === 200) {
            message.success('User created successfully!');
        }

        return response.data;
    } catch (error) {
        console.error('Error when creating user: ', error);
        message.error(error.response?.data?.message || 'Failed to create user');
        throw error; // Ném lại lỗi để xử lý tiếp nếu cần
    }
};

export const updateUser = async (userId, formData) => {

    try {
        const updateData = { ...formData };
        
        if (formData.roles) {
            updateData.roles = Array.isArray(formData.roles) ? formData.roles : [formData.roles];
            updateData.enabled = formData.enabled === "Yes";
        }
        console.log('updateData: ', updateData);
        const response = await axios.put(
            API_ENDPOINTS.USER.UPDATE(userId),
            updateData,
            {
                headers: {
                    Authorization: `Bearer ${getToken()}`,
                    'Content-Type': 'application/json',
                },
            }
        );

        if (response.status === 200) {
            message.success(response.data);
            return response.data;
        }
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Error updating user';
        console.log('Error when updating user! Error: ', errorMessage);
        message.error(errorMessage);
        throw error;
    }
};