import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';


export const getAllLeaves = async ({ page = 0, pageSize = 5 }) => {
    try {
        const response = await axios.get(API_ENDPOINTS.LEAVE.GET_ALL, {
            params: { page, pageSize },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data; 
    } catch (error) {
        message.error('Error get all leaves: ');
        return null;
    }
};

export const createLeave = async (formData) => {
    try {
        const response = await axios.post(
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
        const response = await axios.put(API_ENDPOINTS.LEAVE.UPDATE, formData);
        message.success('Leave updated successfully');
        return response.data;
    } catch (error) {
        console.error('Error when updating leave: ', error);
    }
};
