import { jwtDecode } from 'jwt-decode';
import { createContext, useContext, useEffect, useState } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);

    const initializeAuth = () => {
        const token = localStorage.getItem('token');
        if (token) {
            try {
                const decodedToken = jwtDecode(token);
                const currentTime = Date.now() / 1000;
                
                if (decodedToken.exp && decodedToken.exp < currentTime) {
                    clearAuthData();
                } else {
                    const role = localStorage.getItem('role');
                    setUser({
                        token,
                        role: role || 'user',
                        userId: decodedToken.userId,
                        roles: decodedToken.role,
                        username: decodedToken.username,
                        email: decodedToken.sub
                    });
                }
            } catch (error) {
                console.log('Error when decoding token: ', error);
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
    }, []);

    const login = (userData) => {
        console.log('user data: ', userData)
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
