import apiClient from '~/service/api/api';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';

export const getAllAttendances = async ({page, pageSize}) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.ATTENDANCE.GET_ALL, {
            params: { page, pageSize }
        });
        
        return response.data; 
    } catch (error) {
        message.error('Error get all attandance: ');
        return null;
    }
};

export const filterAttendances = async ({ startDate, endDate, status, employeeName }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.ATTENDANCE.FILTER, {
            params: {
                startDate,
                endDate,
                status,
                employeeName,
            },
        });
        return response.data;
    } catch (error) {
        console.error('Error when filtering attendances: ', error);
        message.error('Error filter attendances');
        return null;
    }
};

export const deleteAttendance = (id) => {
    return apiClient.delete(`/api/attendances/${id}`);
};

export const createAttendance = async (formData) => {
    try {
        const response = await apiClient.post(
            API_ENDPOINTS.ATTENDANCE.CREATE,   
            formData, 
        );
        //message.success('attandance created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating attendance: ', error);
    }
};


export const updateAttendance = async (attendanceId, formData) => {
    try {
        const response = await apiClient.put(
            API_ENDPOINTS.ATTENDANCE.UPDATE(attendanceId),
            formData,
        );
        message.success('AttendanceId update successfully');
        return response.data;
    } catch (error) {
        console.error('Error when update attendance: ', error);
    }
};