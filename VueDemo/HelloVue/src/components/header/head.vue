<template>
  <header id="header">

    <slot name="logoText"></slot>

    <section class="back" v-if="isShowBack">
      <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" version="1.1">
        <polyline points="12,18 4,9 12,0" style="fill:none;stroke:rgb(255,255,255);stroke-width:2"/>
      </svg>
    </section>

    <section class="titleCenter">
      <span class="titleText">{{title}}</span>
    </section>


    <router-link :to="userInfo? '/profile':'/login'" v-if='signinUp' class="head_login">
      <svg class="user_avatar" v-if="false">
        <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#user"></use>
      </svg>
      <span class="login_span" v-else>登录|注册</span>
    </router-link>

    <!--<section id="headerRight" v-if="isShowTitleRight">-->

    <!--</section>-->
  </header>


</template>

<script>
  //import {mapState, mapActions} from 'vuex'

  export default {
    data(){
      return {
        userInfo: false
      }
    },
    mounted(){
      //this.getUserInfo();
    },

    props: ['title', 'isShowBack', 'signinUp'],

//    computed:{
//      ...mapState([
//        'userInfo'
//      ])
//    },
//
//    methods:{
//      ...mapActions([
//        'getUserInfo'
//      ])
//    }

  }
</script>

<style scoped lang="scss">

  @import "../../style/mixin.scss";

  #header{
    background-color: $blue;
    @include wh(100%, 2.2rem);
    position: fixed;
    left: 0;
    top: 0;
    z-index: 100;
  }

  .back{
    @include wh(0.6rem, 1rem);
    margin-left: 0.4rem;
    line-height: 2.2rem;
  }

  .titleCenter{
    @include center;
    text-align: center;
    width: 50%;
    color: #fff;
    .titleText{
      font-weight: bold;
      @include sc(0.8rem, #fff);
    }
  }

  .head_login{
    @include ct;
    @include sc(0.65rem, #fff);
    right: 0.55rem;
    .login_span{
      color: #fff;
    }
  }

</style>
