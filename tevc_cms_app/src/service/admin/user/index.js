import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import {getToken, setToken} from '~/constants/token';
import {message} from 'antd';
import apiClient from "~/service/api/api";
import {setAuthToken} from "~/service/api/api";
import axiosInstance from '~/utils/axiosInstance';

export const login = async (username, password) => {
    try {
        const response = await axiosInstance.post(
            '/auth/login',
            { username, password },
            {
                headers: {
                    'Content-Type': 'application/json',
                },
            }
        );

        const data = response.data;
        console.log('Login data: ', data);

        if (response.status === 200) {
            return data.result.token;
        }
    } catch (error) {
        console.error('Login error: ', error);
        throw error;
    }
};

export const logout = async () => {
    try {
        await apiClient.post('/auth/logout');
    } catch (error) {
        console.error('Logout error:', error);
    } finally {
        setAuthToken(null);
        localStorage.clear();
        sessionStorage.clear();
    }
};

export const refreshToken = async () => {
    try {
        const response = await apiClient.post('/auth/refresh');
        const { token } = response.data.result;

        if (token) {
            setAuthToken(token);
            return token;
        }
    } catch (error) {
        console.error('Refresh token error:', error);
        throw error;
    }
};

export const register = async (username, email, password) => {
    try {
        const response = await axios.post(
            API_ENDPOINTS.AUTH.REGISTER,
            {username, email, password},
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
        const response = await axiosInstance.get(API_ENDPOINTS.USER.GET_ALL, {
            params: { page, pageSize },
        });
        return response.data;
    } catch (error) {
        message.error(error.response?.data?.message || 'Lỗi lấy danh sách người dùng');
        throw error;
    }
};

export const createUser = async (formData) => {
    console.log('form data: ', formData)
    try {
        console.log('form data user: ', formData);
        const processedData = {
            ...formData,
            roles: [formData.roles],
            permissions: [formData.permissions],
            enabled:
                typeof formData.enabled === 'string'
                    ? ['true', 'yes'].includes(formData.enabled.toLowerCase())
                    : Boolean(formData.enabled),
        };


        const response = await axios.post(
            API_ENDPOINTS.USER.CREATE,
            processedData,
            {
                headers: {
                    Authorization: `Bearer ${getToken()}`, // Use token
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
        const updateData = {...formData};

        updateData.roles = Array.isArray(formData.roles) ? formData.roles : [formData.roles];
        updateData.permissions = Array.isArray(formData.permissions) ? formData.permissions : [formData.permissions];
        updateData.enabled = formData.enabled === "Yes";

        const response = await axios.patch(
            API_ENDPOINTS.USER.UPDATE(userId),
            updateData,
            {
                headers: {
                    Authorization: `Bearer ${getToken()}`, // Use token
                    'Content-Type': 'application/json',
                },
            }
        );

        if (response.data) {
            message.success("User updated successfully!");
        }
    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Error updating user';
        message.error(errorMessage);
        throw error;
    }
};

export const deleteUser = async (userIds) => {
    try {
        const response = await axios.delete(API_ENDPOINTS.USER.DELETE, {
            headers: {
                Authorization: `Bearer ${getToken()}`, // Use token
                'Content-Type': 'application/json',
            },
            data: userIds
        });

        if (response.data) {
            message.success("User deleted successfully!");
            return response.data.result;
        }

    } catch (error) {
        const errorMessage = error.response?.data?.message || 'Error deleting user';
        console.log('Error when deleting user! Error: ', errorMessage);
        message.error(errorMessage);
        throw error;
    }
}