export const getToken = () => {
    return localStorage.getItem('authToken'); // Retrieve token
};

export const setToken = (token) => {
    localStorage.setItem('authToken', token); // Save token
};
