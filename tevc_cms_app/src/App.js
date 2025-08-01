import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import DashboardLayout from './components/Layout/DashboardLayout';
import { publicRoutes, privateRoutes } from './routes';
import PrivateRoute from './routes/PrivateRoute';
import { AuthProvider } from './routes/AuthContext';
import Login from './pages/General/Login';
import Register from './pages/General/Register';
import { LanguageProvider } from './contexts/LanguageContext';

function App() {
    return (
        <LanguageProvider>
            <AuthProvider>
                <Router>
                    <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />

                        {/* <Route path="/user-info-management" element={<UserInfoManagement />} /> */}

                        {publicRoutes
                            .filter(
                                (route) =>
                                    route.path !== '/' 
                                // &&
                                //     route.path !== '/movie/:movieId',
                            )
                            .map(({ path, component: Page }, index) => (
                                <Route key={index} path={path} element={<Page />} />
                            ))}

                        {privateRoutes.map(
                            ({ path, component: Page, role, title}, index) => (
                                <Route
                                    key={index}
                                    path={path}
                                    element={
                                        <PrivateRoute
                                            role={role}
                                            title={title}
                                            element={
                                                <DashboardLayout>
                                                    <Page />
                                                </DashboardLayout>
                                            }
                                        />
                                    }
                                />
                            ),
                        )}
                    </Routes>
                </Router>
            </AuthProvider>
        </LanguageProvider>
    );
}

export default App;
