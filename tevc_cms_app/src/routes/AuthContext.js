import { jwtDecode } from 'jwt-decode';
import { createContext, useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { message } from 'antd';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const navigate = useNavigate();

    const checkTokenExpiration = (token) => {
        try {
            const decodedToken = jwtDecode(token);
            const currentTime = Date.now() / 1000;
            
            if (decodedToken.exp && decodedToken.exp - 60 < currentTime) {
                clearAuthData();
                message.warning('Session expired. Please login again.');
                navigate('/login');
                return false;
            }
            return true;
        } catch (error) {
            console.error('Error checking token:', error);
            clearAuthData();
            return false;
        }
    };

    const initializeAuth = () => {
        const token = localStorage.getItem('token');
        if (token && checkTokenExpiration(token)) {
            try {
                const decodedToken = jwtDecode(token);
                const permissions = JSON.parse(localStorage.getItem('permissions') || '[]');
                const role = localStorage.getItem('role');
                
                setUser({
                    token,
                    role: role || 'USER',
                    userId: decodedToken.userId,
                    roles: decodedToken.roles || [],
                    permissions: permissions,
                    username: decodedToken.username,
                    email: decodedToken.sub,
                    tokenExpiration: decodedToken.exp
                });
            } catch (error) {
                console.error('Error initializing auth:', error);
                clearAuthData();
            }
        }
    };

    const clearAuthData = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        sessionStorage.clear();
        setUser(null);
    };

    useEffect(() => {
        initializeAuth();
        const interval = setInterval(() => {
            const token = localStorage.getItem('token');
            if (token && !checkTokenExpiration(token)) {
                console.warn('Your session has expired. Please login again.');
            }
        }, 60000);

        return () => clearInterval(interval);
    }, []);

    const login = (userData) => {
        localStorage.setItem('permissions', JSON.stringify(userData.permissions || []));
        setUser(userData);
    };
    
    const logout = () => {
        clearAuthData();
    };

    const authContextValue = {
        user,
        login,
        logout,
    };

    return (
        <AuthContext.Provider value={authContextValue}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};
