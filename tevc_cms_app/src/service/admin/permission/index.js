import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';

export const getAllPermissions = async () => {
    try {
        const response = await axios.get(API_ENDPOINTS.PERMISSION.GET_ALL_NO_PAGING, {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        return Array.isArray(response.data.result) ? response.data.result : [];
    } catch (error) {
        console.error('Error fetching permissions:', error);
        return [];
    }
};

export const getAllPermissionsNoPaging = async () => {
    try {
        const response = await axios.get(API_ENDPOINTS.PERMISSION.GET_ALL_NO_PAGING, {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data.result; 
    } catch (error) {
        message.error('Error get all permission: ');
        return null;
    }
};

export const createPermission = async (formData) => {
    try {
        const response = await axios.post(
            API_ENDPOINTS.PERMISSION.CREATE,   
            formData, 
        );
        message.success('Permission created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating permission: ', error); 
    }
};

export const getAllRoles = async () => {
    try {
        const response = await axios.get(API_ENDPOINTS.ROLE.GET_ALL, {
            headers: { Authorization: `Bearer ${getToken()}` },
        });
        return Array.isArray(response.data.result) ? response.data.result : [];
    } catch (error) {
        console.error('Error fetching roles:', error);
        return [];
    }
};

export const createRole = async (formData) => {
    try {
        const response = await axios.post(API_ENDPOINTS.ROLE.CREATE, formData, {
            headers: { Authorization: `Bearer ${getToken()}` },
        });
        return response.data;
    } catch (error) {
        console.error('Error creating role:', error);
        throw error;
    }
};

export const assignPermissionsToRole = async (roleId, permissionIds) => {
    try {
        await axios.post(`${API_ENDPOINTS.ROLE.ASSIGN_PERMISSIONS}/${roleId}/permissions`, permissionIds, {
            headers: { Authorization: `Bearer ${getToken()}` },
        });
    } catch (error) {
        console.error('Error assigning permissions to role:', error);
        throw error;
    }
};
