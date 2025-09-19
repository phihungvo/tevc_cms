import API_ENDPOINTS from '../../../constants/endpoints';
import { message } from 'antd';
import apiClient from '~/service/api/api';

export const getAllPositions = async () => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.POSITION.GET_ALL);
        
        return response.data; 
    } catch (error) {
        message.error('Error get all positions: ');
        return null;
    }
};

export const getAllPositionsNoPaging = async () => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.POSITION.GET_ALL_NO_PAGING);
        
        return response.data; 
    } catch (error) {
        message.error('Error get all positions: ');
        return null;
    }
};

export const getAllByEmployeePaged = async (employeeId) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.POSITION.ALL_BY_EMPLOYEE_PAGED(employeeId));

        return response.data.result;
    } catch (error) {
        return null;
    }
};

export const createPosition = async (formData) => {
    try {
        const response = await apiClient.post(
            API_ENDPOINTS.POSITION.CREATE,   
            formData, 
        );
        message.success('Position created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating Positions: ', error); 
    }
};

export const getAllByTitle = async (type) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.EMPLOYEE.GET_EMPLOYEE_BY_POSITION_TYPE, {
            params: { type }
        });
        console.log('employee data:', response.data);
        return response.data;
    } catch (error) {
        console.error('Failed to fetch positions:', error);
        message.error('Không thể tải danh sách vị trí');
        return [];
    }
};

export const updatePosition = async (positionId, formData) => {
    try {
        const response = await apiClient.patch(
            API_ENDPOINTS.POSITION.UPDATE(positionId),   
            formData, 
        );
        message.success('Position updated successfully');
        return response.data;
    } catch (error) {
        console.error('Error when updating positions: ', error); 
    }
};

export const deletePosition = async (positionId) => {
    try {
        const response = await apiClient.delete(
            API_ENDPOINTS.POSITION.DELETE(positionId),   
        );
        message.success('Position deleted successfully');
        return response.data;
    } catch (error) {
        console.error('Error when deleting positions: ', error); 
    }
};