import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';


export const getAllPermissions = async ({ page = 0, pageSize = 5 }) => {
    try {
        const response = await axios.get(API_ENDPOINTS.PERMISSION.GET_ALL, {
            params: { page, pageSize },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
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
