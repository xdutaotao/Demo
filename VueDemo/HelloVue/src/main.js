import Vue from 'vue'
import VueRouter from 'vue-router'
import routes from './router/router'
import store from './store'
import Mint from 'mint-ui';

Vue.use(Mint);
Vue.use(VueRouter);

const router = new VueRouter({
  routes
});

new Vue({
  router,
  store
}).$mount('#app');
