import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';

export const getAllPositions = async ({ page = 0, pageSize = 5 }) => {
    try {
        const response = await axios.get(API_ENDPOINTS.POSITION.GET_ALL, {
            params: { page, pageSize },
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

export const getAllByTitle = async (title) => {
    console.log('title:', title);
    try {
        const response = await axios.get(API_ENDPOINTS.POSITION.GET_BY_TITLE, {
            params: { title },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        console.log('data:', response.data);
        return response.data; // Trả về danh sách PositionDTO
    } catch (error) {
        console.error('Failed to fetch positions:', error);
        message.error('Không thể tải danh sách vị trí');
        return []; // Trả về mảng rỗng nếu có lỗi
    }
};