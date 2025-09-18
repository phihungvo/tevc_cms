import apiClient from '~/service/api/api';
import API_ENDPOINTS from '../../../constants/endpoints';
import {message} from 'antd';

export const getAllSkills = async () => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.SKILL.GET_ALL);

        return response.data.result;
    } catch (error) {
        return null;
    }
};

export const getAllByEmployeePaged = async (employeeId) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.SKILL.ALL_BY_EMPLOYEE_PAGED(employeeId));

        return response.data.result;
    } catch (error) {
        return null;
    }
};