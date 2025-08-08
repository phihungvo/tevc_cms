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

        return response.data;
    } catch (error) {
        console.log('Error when fetching all user ! Error: ', error);
        message.error('Error get all user: ');
    }
};

export const createUser = async (formData) => {
    console.log('form data: ', formData)
    try {
        console.log('form data user: ', formData);
        const processedData = {
            ...formData,
            roles:  [formData.roles],
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
        
        updateData.roles = Array.isArray(formData.roles) ? formData.roles : [formData.roles];
        updateData.permissions = Array.isArray(formData.permissions) ? formData.permissions : [formData.permissions];
        updateData.enabled = formData.enabled === "Yes";
       
        const response = await axios.patch(
            API_ENDPOINTS.USER.UPDATE(userId),
            updateData,
            {
                headers: {
                    Authorization: `Bearer ${getToken()}`,
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
              Authorization: `Bearer ${getToken()}`,
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