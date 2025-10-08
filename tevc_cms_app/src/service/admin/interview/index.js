import API_ENDPOINTS from '../../../constants/endpoints';
import { message } from 'antd';
import apiClient from '~/service/api/api';

export const getAllInterviews = async ({ page = 0, size = 10 }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.INTERVIEW.GET_ALL, {
            params: { page, size },
        });

        return response.data;
    } catch (error) {
        message.error('Error get all interviews: ');
        return null;
    }
};

export const getInterviewsByCandidate = async (candidateId, { page = 0, size = 10 }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.INTERVIEW.GET_BY_CANDIDATE(candidateId), {
            params: { page, size },
        });

        return response.data;
    } catch (error) {
        message.error('Error get all interviews: ');
        return null;
    }
};

export const getInterviewsByJobPosting = async (jobPostingId, { page = 0, size = 10 } = {}) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.INTERVIEW.GET_BY_JOB_POSTING(jobPostingId), {
            params: {
                page,
                size,
            },
        });

        return response.data;
    } catch (error) {
        message.error('Error getting interviews by job posting');
        return null;
    }
};

export const getInterviewsByCandidateList = async (candidateIds = [], { page = 0, size = 10 } = {}) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.INTERVIEW.GET_BY_LIST_CANDIDATE, {
            params: {
                candidateIds: candidateIds.join(','),
                page,
                size,
            },
        });

        return response.data;
    } catch (error) {
        message.error('Error getting interviews by candidates');
        return null;
    }
};
