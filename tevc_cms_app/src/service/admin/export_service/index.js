import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import {getToken} from '~/constants/token';
import {message} from 'antd';

export const exportExcelFile = async (entityType) => {
    try {
        const response = await axios.get(API_ENDPOINTS.SERVICE.EXPORT_EXCEL, {
            params: {entityType},
            headers: {
                Authorization: `Bearer ${getToken()}`,
                Accept: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            },
            responseType: 'blob',
        });
        return response;
    } catch (error) {
        console.error('Error exporting Excel file:', error);
        throw error;
    }
};