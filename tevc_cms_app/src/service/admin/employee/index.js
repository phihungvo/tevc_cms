import API_ENDPOINTS from '../../../constants/endpoints';
import { message } from 'antd';
import apiClient from '~/service/api/api';
import axiosInstance from '~/utils/axiosInstance';

export const getAllEmployees = async ({ page = 0, pageSize = 10 }) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.EMPLOYEE.GET_ALL, {
            params: { page, pageSize },
        });
        
        return response.data;
    } catch (error) {
        message.error('Error get all employees: ');
        return null;
    }
};

export const getAllEmployeesNoPaging = async () => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.EMPLOYEE.NO_PAGING);
        
        return response.data;
    } catch (error) {
        message.error('Error get all employees: ');
        return null;
    }
};

export const createEmployee = async (formData) => {
    try {
        const response = await axiosInstance.post(
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
        const response = await apiClient.get(API_ENDPOINTS.EMPLOYEE.GET_EMPLOYEE_BY_POSITION_TYPE, {
            params: { positionType },
        });
        return response.data;
    } catch (error) {
        console.error('Error when getting employee by position type: ', error); 
    }
};

export const getByDepartmentBasicInfo = async (departmentId) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.EMPLOYEE.BY_DEPARTMENTS_BASIC_RESPONSE, {
            params: { departmentId },
        });
        return response.data;
    } catch (error) {
        console.error('Error when getting employee by position type: ', error);
    }
};


export const updateEmployee = async (employeeId, formData) => {
    try {
        const response = await apiClient.patch(
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
        const response = await apiClient.delete(
            API_ENDPOINTS.EMPLOYEE.DELETE(employeeId),  
        );
        message.success('Employee created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating employee: ', error); 
    }
};