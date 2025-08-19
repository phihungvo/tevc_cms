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
        
        return response.data;
    } catch (error) {
        message.error('Error get all employees: ');
        return null;
    }
};

export const getAllEmployeesNoPaging = async () => {
    try {
        const response = await axios.get(API_ENDPOINTS.EMPLOYEE.NO_PAGING, {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
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
        return response.data;
    } catch (error) {
        console.error('Error when getting employee by position type: ', error); 
    }
};

export const updateEmployee = async (employeeId, formData) => {
    try {
        const response = await axios.patch(
            API_ENDPOINTS.EMPLOYEE.UPDATE(employeeId),   
            formData, 
        );
        message.success('Employee updated successfully');
        return response.data;
    } catch (error) {
        console.error('Error when updatting employee: ', error); 
    }
};

export const deleteEmployee = async (employeeId) => {
    try {
        const response = await axios.delete(
            API_ENDPOINTS.EMPLOYEE.DELETE(employeeId),  
        );
        message.success('Employee created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating employee: ', error); 
    }
};