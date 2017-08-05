/**
 * Created by guzhenfu on 2017/8/4.
 */
import App from '../App.vue'

export default [{
    path: '/',
    component: App,
    children: [{
        path: '',
        component: r => require.ensure([], ()=> r(require('../page/home')), 'home')
    }]
}]
