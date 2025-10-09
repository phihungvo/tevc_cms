import API_ENDPOINTS from '../../../constants/endpoints';
import { message } from 'antd';
import apiClient from '~/service/api/api';
import axiosInstance from '~/utils/axiosInstance';

export const getAllJobPostings = async ({ page = 0, size = 10 }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.JOB_POSTING.GET_ALL, {
            params: { page, size },
        });

        return response.data;
    } catch (error) {
        message.error('Error get all employees: ');
        return null;
    }
};

export const getByCandidateId = async ({ candidateId }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.JOB_POSTING.GET_BY_CANDIDATE(candidateId));

        return response.data.content;
    } catch (error) {
        message.error('Error get all employees: ');
        return null;
    }
};

export const filterJobPostings = async ({ page = 0, size = 10, title, location, startDate, endDate, status }) => {
    try {
        const params = {
            page,
            size,
        };

        if (title) {
            params.title = title;
        }

        if (location) {
            params.location = location;
        }

        if (startDate) {
            params.startDate = startDate;
        }

        if (endDate) {
            params.endDate = endDate;
        }

        if (status && status !== 'ALL') {
            params.status = status;
        }

        const response = await apiClient.get(API_ENDPOINTS.JOB_POSTING.FILTER, {
            params,
        });

        return response.data;
    } catch (error) {
        console.error('Error when filtering job postings: ', error);
        message.error('Error filter job postings');
        return null;
    }
};