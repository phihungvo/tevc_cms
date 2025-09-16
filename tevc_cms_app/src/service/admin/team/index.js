import API_ENDPOINTS from '../../../constants/endpoints';
import { message } from 'antd';
import apiClient from '~/service/api/api';
import axiosInstance from '~/utils/axiosInstance';

export const getAllTeams = async ({ page = 0, pageSize = 10 }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.TEAM.GET_ALL, {
            params: { page, pageSize },
        });

        return response.data;
    } catch (error) {
        message.error('Error get all team: ');
        return null;
    }
};

export const getAllTeamsNoPaging = async () => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.TEAM.NO_PAGING);

        return response.data;
    } catch (error) {
        message.error('Error get all team: ');
        return null;
    }
};

export const createTeam = async (formData) => {
    try {
        const response = await apiClient.post(
            API_ENDPOINTS.TEAM.CREATE,
            formData,
        );
        message.success('Team created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating team: ', error);
    }
};