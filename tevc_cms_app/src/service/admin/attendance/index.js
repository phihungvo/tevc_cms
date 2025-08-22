import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';

export const getAllAttendancesWithPagination = async ({page, pageSize}) => {
    try {
        const response = await axios.get(API_ENDPOINTS.ATTENDANCE.GET_ALL, {
            params: { page, pageSize },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data; 
    } catch (error) {
        message.error('Error get all attandance: ');
        return null;
    }
};

export const deleteAttendance = (id) => {
    return axios.delete(`/api/attendances/${id}`);
};

export const createAttandance = async (formData) => {
    try {
        const response = await axios.post(
            API_ENDPOINTS.ATTENDANCE.CREATE,   
            formData, 
        );
        message.success('attandance created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating attandance: ', error); 
    }
};


export const updateAttandance = async (attandanceId, formData) => {
    try {
        const response = await axios.patch(
            API_ENDPOINTS.ATTENDANCE.UPDATE(attandanceId),
            formData,
        );
        message.success('Attandance update successfully');
        return response.data;
    } catch (error) {
        console.error('Error when update attandance: ', error);
    }
};