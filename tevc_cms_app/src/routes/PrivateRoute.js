import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from '~/routes/AuthContext';

const PrivateRoute = ({ element, role, title }) => {
    const { user } = useAuth();
    const location = useLocation();

    const elementWithTitle = React.cloneElement(element, { pageTitle: title });

    // Check if user is logged in and token is valid
    if (!user || !user.token) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    // Check token expiration
    const currentTime = Date.now() / 1000;
    if (user.tokenExpiration && user.tokenExpiration < currentTime) {
        return <Navigate to="/login" state={{ from: location }} replace />;
    }

    // Check permissions for admin routes
    if (role === 'admin') {
        const hasAdminAccess = 
            user.roles?.includes('ADMIN') || 
            user.permissions?.includes('ADMIN:MANAGE');
            
        if (!hasAdminAccess) {
            return <Navigate to="/" replace />;
        }
    }

    return elementWithTitle;
};

export default PrivateRoute;
