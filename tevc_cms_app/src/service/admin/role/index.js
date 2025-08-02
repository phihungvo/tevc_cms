import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';


export const getAllRoles = async ({ page = 0, pageSize = 5 }) => {
    try {
        const response = await axios.get(API_ENDPOINTS.ROLE.GET_ALL, {
            params: { page, pageSize },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data.result; 
    } catch (error) {
        console.log('Error when fetching all role ! Error: ', error);
        message.error('Error get all role: ');
        return null;
    }
};

export const createRole = async (formData) => {
    try {
        const response = await axios.post(
            API_ENDPOINTS.ROLE.CREATE,   
            formData, 
        );
        message.success('Role created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating role: ', error); 
    }
};
