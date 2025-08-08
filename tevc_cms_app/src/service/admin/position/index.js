import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';

export const getAllPositions = async () => {
    try {
        const response = await axios.get(API_ENDPOINTS.POSITION.GET_ALL, {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data; 
    } catch (error) {
        message.error('Error get all positions: ');
        return null;
    }
};

export const createPosition = async (formData) => {
    try {
        const response = await axios.post(
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
        const response = await axios.get(API_ENDPOINTS.EMPLOYEE.GET_EMPLOYEE_BY_POSITION_TYPE, {
            params: { type },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        console.log('employee data:', response.data);
        return response.data; // Trả về danh sách PositionDTO
    } catch (error) {
        console.error('Failed to fetch positions:', error);
        message.error('Không thể tải danh sách vị trí');
        return []; // Trả về mảng rỗng nếu có lỗi
    }
};

export const updatePosition = async (positionId, formData) => {
    try {
        const response = await axios.patch(
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
        const response = await axios.delete(
            API_ENDPOINTS.POSITION.DELETE(positionId),   
        );
        message.success('Position deleted successfully');
        return response.data;
    } catch (error) {
        console.error('Error when deleting positions: ', error); 
    }
};