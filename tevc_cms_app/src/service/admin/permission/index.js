import API_ENDPOINTS from '../../../constants/endpoints';
import { message } from 'antd';
import apiClient from '~/service/api/api';

export const getAllPermissions = async ({ page = 0, pageSize = 5 }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.PERMISSION.GET_ALL, {
            params: { page, pageSize },
        });
        
        return response.data.result; 
    } catch (error) {
        console.log('Error when fetching all permission ! Error: ', error);
        message.error('Error get all permission: ');
        return null;
    }
};

export const getAllPermissionsNoPaging = async () => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.PERMISSION.GET_ALL_NO_PAGING);

        return response.data.result; 
    } catch (error) {
        message.error('Error get all permission: ');
        return null;
    }
};

export const createPermission = async (formData) => {
    try {
        const response = await apiClient.post(
            API_ENDPOINTS.PERMISSION.CREATE,   
            formData, 
        );
        message.success('Permission created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating permission: ', error); 
    }
};
