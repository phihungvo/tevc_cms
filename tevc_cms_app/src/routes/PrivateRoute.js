import React from "react";
import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "./AuthContext";

const PrivateRoute = ({ element, role, title }) => {
  const { user } = useAuth();
      const location = useLocation();

  const elementWithTitle = React.cloneElement(element, { pageTitle: title });

  // Check user login ?
  if (!user || !user.token) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (role && user.role !== role) {
    return <Navigate to="/" replace />;
  }

  return elementWithTitle;
};

export default PrivateRoute;
