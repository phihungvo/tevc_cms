import axios from '~/utils/axios';

export const getAllAttendancesWithPagination = (page, size) => {
    return axios.get('/api/attendances', { params: { page, size } }).then((res) => res.data);
};

export const deleteAttendance = (id) => {
    return axios.delete(`/api/attendances/${id}`);
};
