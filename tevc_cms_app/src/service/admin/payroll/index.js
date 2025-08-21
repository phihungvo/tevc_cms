import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';

export const getAllPayroll = async ({ page = 0, pageSize = 5 }) => {
    try {
        const response = await axios.get(API_ENDPOINTS.PAYROLL.GET_ALL, {
            params: { page, pageSize },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data;
    } catch (error) {
        message.error('Error get all payrolls: ');
        return null;
    }
};

export const calculatePayroll = async (employeeId, period) => {
    try {

        const response = axios.post(API_ENDPOINTS.PAYROLL.CALCULATE(employeeId),
        {},
            { 
                params: { period }
            }
        )
        if (response.data) {
            message.success('Payroll calculated successfully');
        }
        return response.data;
    } catch (error) {
        const responseMessage = error.response?.data?.message;
        const fallbackMessage = error.message;

        // Ưu tiên message từ server, nếu không có thì dùng lỗi mặc định
        message.error(responseMessage || fallbackMessage || 'Unexpected error occurred');
        // Nếu muốn xử lý tiếp ở nơi gọi
        throw error;
    }
};


export const processPayroll = async (payrollIds) => {
    console.log("payrollIds: ", payrollIds)
    try {
        const response = await axios.patch(
            API_ENDPOINTS.PAYROLL.PROCESS,
            payrollIds,
            {
                headers: {
                    Authorization: `Bearer ${getToken()}`,
                    'Content-Type': 'application/json',
                },
            }
        );
        if (response.data) {
            message.success('Payrolls processed successfully');
        }
        return response.data;
    } catch (error) {
        const responseMessage = error.response?.data?.message;
        const fallbackMessage = error.message;
        message.error(responseMessage || fallbackMessage || 'Unexpected error occurred');
        throw error;
    }
};

export const updatePayroll = async (payrollIds, formData) => {
    try {
        const response = await axios.patch(
            API_ENDPOINTS.PAYROLL.UPDATE(payrollIds),
            formData,
            {
                headers: {
                    Authorization: `Bearer ${getToken()}`,
                    'Content-Type': 'application/json',
                },
            },
        );
        if (response.data) {
            message.success('Payrolls updated successfully');
        }
        return response.data;
    } catch (error) {
        const responseMessage = error.response?.data?.message;
        const fallbackMessage = error.message;
        message.error(responseMessage || fallbackMessage || 'Unexpected error occurred');
        throw error;
    }
};