import API_ENDPOINTS from '../../../constants/endpoints';
import { message } from 'antd';
import apiClient from '~/service/api/api';
import axiosInstance from '~/utils/axiosInstance';

export const getAllCandidates = async ({ page = 0, size = 10 }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.CANDIDATE.GET_ALL, {
            params: { page, size },
        });

        return response.data;
    } catch (error) {
        message.error('Error get all employees: ');
        return null;
    }
};

