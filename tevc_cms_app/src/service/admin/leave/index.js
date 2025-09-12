import apiClient from '~/service/api/api';
import API_ENDPOINTS from '../../../constants/endpoints';
import { message } from 'antd';


export const getAllLeaves = async ({ page = 0, pageSize = 10 }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.LEAVE.GET_ALL, {
            params: { page, pageSize },
        });
        
        return response.data; 
    } catch (error) {
        message.error('Error get all leaves: ');
        return null;
    }
};

export const createLeave = async (formData) => {
    try {
        const response = await apiClient.post(
            API_ENDPOINTS.LEAVE.CREATE,   
            formData, 
        );
        message.success('Leave created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating leave: ', error); 
    }
};

export const updateLeave = async (formData) => {
    try {
        const response = await apiClient.patch(API_ENDPOINTS.LEAVE.UPDATE, formData);
        message.success('Leave updated successfully');
        return response.data;
    } catch (error) {
        console.error('Error when updating leave: ', error);
    }
};

export const deleteLeave = async (leaveId) => {
    try {
        const response = await apiClient.delete(API_ENDPOINTS.LEAVE.DELETE(leaveId));
        message.success('Leave deleted successfully');
        return response.data;
    } catch (error) {
        console.error('Error when deleting leave: ', error);
    }
};
