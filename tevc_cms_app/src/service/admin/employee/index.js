import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';


export const getAllEmployees = async ({ page = 0, pageSize = 10 }) => {
    try {
        const response = await axios.get(API_ENDPOINTS.EMPLOYEE.GET_ALL, {
            params: { page, pageSize },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        console.log('dddd: ', response.data)
        return response.data;
    } catch (error) {
        message.error('Error get all employees: ');
        return null;
    }
};

export const createEmployee = async (formData) => {
    try {
        const response = await axios.post(
            API_ENDPOINTS.EMPLOYEE.CREATE,   
            formData, 
        );
        message.success('Employee created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating employee: ', error); 
    }
};

export const getEmployeeByPositionType = async (positionType) => {
    try {
        const response = await axios.get(API_ENDPOINTS.EMPLOYEE.GET_EMPLOYEE_BY_POSITION_TYPE, {
            params: { positionType },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        message.success('Get employee by position type successfully');
        return response.data;
    } catch (error) {
        console.error('Error when getting employee by position type: ', error); 
    }
};