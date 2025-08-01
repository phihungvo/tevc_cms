import HomeDashboard from '~/pages/AdminDashboard/HomeDashboard';
import Login from '../pages/General/Login';
import Register from '../pages/General/Register';
import User from '~/pages/AdminDashboard/User';

const publicRoutes = [
    { path: '/login', component: Login, title: 'Login' },
    { path: '/register', component: Register, title: 'Register' },
];

const privateRoutes = [
    {
        path: '/admin/dashboard',
        component: HomeDashboard,
        title: 'HomeDashboard',
        role: 'admin',
    },
    {
        path: '/admin/user',
        component: User,
        title: 'User Management',
        role: 'admin',
    },
];

export { publicRoutes, privateRoutes };
