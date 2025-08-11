import axios from 'axios';
import API_ENDPOINTS from '../../../constants/endpoints';
import { getToken } from '~/constants/token';
import { message } from 'antd';


export const getAllDepartments = async ({ page = 0, pageSize = 5 }) => {
    try {
        const response = await axios.get(API_ENDPOINTS.DEPARTMENT.GET_ALL, {
            params: { page, pageSize },
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data; 
    } catch (error) {
        message.error('Error get all departments: ');
        return null;
    }
};

export const getAllDepartmentsNoPaging = async () => {
    try {
        const response = await axios.get(API_ENDPOINTS.DEPARTMENT.NO_PAGING, {
            headers: {
                Authorization: `Bearer ${getToken()}`,
                'Content-Type': 'application/json',
            },
        });
        
        return response.data; 
    } catch (error) {
        message.error('Error get all departments: ');
        return null;
    }
};

export const createDepartment = async (formData) => {
    try {
        const response = await axios.post(
            API_ENDPOINTS.DEPARTMENT.CREATE,   
            formData, 
        );
        message.success('Departments created successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating employee: ', error); 
    }
};

export const updateDepartment = async (departmentId, formData) => {
    try {
        const response = await axios.patch(
            API_ENDPOINTS.DEPARTMENT.UPDATE(departmentId),   
            formData, 
        );
        message.success('Departments update successfully');
        return response.data;
    } catch (error) {
        console.error('Error when update employee: ', error); 
    }
};

export const deleteDepartment = async (departmentId) => {
    try {
        const response = await axios.delete(
            API_ENDPOINTS.DEPARTMENT.DELETE(departmentId),
        );
        message.success('Departments deleted successfully');
        return response.data;
    } catch (error) {
        console.error('Error when creating employee: ', error);
    }
};