/* eslint-disable indent,arrow-spacing */
/**
 * Created by guzhenfu on 2017/8/4.
 */
import App from '../App.vue'

export default [{
    path: '/',
    component: App,
    children: [
        {
            path: '/test',
            component: r => require.ensure([], ()=> r(require('../page/home')), 'home')
        },
        {
            path: '/test1',
            component: r => require.ensure([], ()=> r(require('../page/login/login')), 'login')
        },
        {
            path: '/forget',
            component: r => require.ensure([], ()=> r(require('../page/login/forget')), 'forget')
        },
        {
            path: '',
            component: r => require.ensure([], ()=> r(require('../page/city/city')), 'city')
        }
    ]
}]
