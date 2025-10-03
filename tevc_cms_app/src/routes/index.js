import HomeDashboard from '~/pages/AdminDashboard/HomeDashboard';
import Login from '../pages/General/Login';
import Register from '../pages/General/Register';
import User from '~/pages/AdminDashboard/User';
import Role from '~/pages/AdminDashboard/Role';
import Permission from '~/pages/AdminDashboard/Permission';
import Employee from '~/pages/AdminDashboard/Employee';
import Department from '~/pages/AdminDashboard/Department';
import Position from '~/pages/AdminDashboard/Position';
import Leave from '~/pages/AdminDashboard/Leave';
import PayRoll from '~/pages/AdminDashboard/PayRoll';
import RootRedirect from '~/components/RootRedirect';
import EmployeeDetail from '~/pages/AdminDashboard/Employee/detail';
import Team from "~/pages/AdminDashboard/Team";
import UserManagement from "~/pages/AdminDashboard/User/UserManagement";
import RecruitmentCMS from "src/pages/AdminDashboard/Recruitment";
import TabRecruitment from "~/pages/AdminDashboard/Recruitment/TabRecruitment";

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
        path: '/admin/user-management',
        component: UserManagement,
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
        path: '/admin/employee',
        component: Employee,
        title: 'Employee Management',
        role: 'admin',
    },
    {
        path: '/admin/employee/:id',
        component: EmployeeDetail,
        title: 'Employee Detail',
        role: 'admin',
      },
    {
        path: '/admin/department',
        component: Department,
        title: 'Department Management',
        role: 'admin',
    },
    {
        path: '/admin/position',
        component: Position,
        title: 'Position Management',
        role: 'admin',
    },
    {
        path: '/admin/leave',
        component: Leave,
        title: 'Leave Management',
        role: 'admin',
    },
    {
        path: '/admin/payroll',
        component: PayRoll,
        title: 'PayRoll Management',
        role: 'admin',
    },
    {
        path: '/admin/team',
        component: Team,
        title: 'Team Management',
        role: 'admin',
    },
    {
        path: '/admin/recruitment',
        component: TabRecruitment,
        title: 'Recruitment Management',
        role: 'admin',
    },
    // {
    //     path: '/admin/recruitment',
    //     component: RecruitmentCMS,
    //     title: 'Recruitment Management',
    //     role: 'admin',
    // },

    {
        path: '/',
        component: RootRedirect,
        title: 'Home'
    }
];

export { publicRoutes, privateRoutes };
