import apiClient from '~/service/api/api';
import API_ENDPOINTS from '../../../constants/endpoints';
import {message} from 'antd';
import axios from "axios";
import axiosInstance from "~/utils/axiosInstance";

export const getAllWorkHistory = async () => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.CONTRACT.GET_ALL);

        return response.data.result;
    } catch (error) {
        return null;
    }
};

export const getAllByEmployeePaged = async (employeeId) => {
    try {
        const response = await apiClient.get(API_ENDPOINTS.CONTRACT.ALL_BY_EMPLOYEE_PAGED(employeeId));

        return response.data.result;
    } catch (error) {
        return null;
    }
};

export const uploadFilesForContract = async (files, contractId) => {
    const formData = new FormData();
    files.forEach((file) => {
        formData.append('files', file);
    });

    try {
        const response = await apiClient.post(
            `${API_ENDPOINTS.CONTRACT.UPLOAD_FILE_CONTRACT(contractId)}`, formData);

        return response.data;
    } catch (error) {
        throw error;
    }
};

export const createContract = async (formData) => {
    try {
        const response = await apiClient.post(
            API_ENDPOINTS.CONTRACT.CREATE,
            formData,
        );
        const result = response.data;
        if (result.status === 201) {
            message.success("Tạo hợp đồng thành công")
            return result;
        }
    } catch (error) {
        const errMsg = error.response?.data?.message || "Lỗi khi tạo hợp đồng!";
        message.error(errMsg); // ✅ hiện thông báo lỗi server trả về
        // throw error; // ✅ ném lỗi ra ngoài cho handleFormSubmit catch
    }
};
