import HomeDashboard from '~/pages/AdminDashboard/HomeDashboard';
import Login from '../pages/General/Login';
import Register from '../pages/General/Register';
import User from '~/pages/AdminDashboard/User';
import Role from '~/pages/AdminDashboard/Role';
import Permission from '~/pages/AdminDashboard/Permission';
import RootRedirect from '~/components/RootRedirect';

const publicRoutes = [
    { path: '/login', component: Login, title: 'Login' },
    { path: '/register', component: Register, title: 'Register' },
];

const privateRoutes = [
    {
        path: '/admin/dashboard',
        component: HomeDashboard,
        title: 'Dashboard',
        role: 'admin',
    },
    {
        path: '/admin/user',
        component: User,
        title: 'User Management',
        role: 'admin',
    },
    {
        path: '/admin/role',
        component: Role,
        title: 'Role Management',
        role: 'admin',
    },
    {
        path: '/admin/permission',
        component: Permission,
        title: 'Permission Management',
        role: 'admin',
    },
    {
        path: '/',
        component: RootRedirect,
        title: 'Home'
    }
];

export { publicRoutes, privateRoutes };
