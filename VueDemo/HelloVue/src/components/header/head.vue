<template>
  <header id="header">
    <!--<section id="headerLeft" v-if="isShowBack">-->
      <!--<svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" version="1.1">-->
        <!--<polyline points="12,18 4,9 12,0" style="fill:none;stroke:rgb(255,255,255);stroke-width:2"/>-->
      <!--</svg>-->
    <!--</section>-->

    <section>
      <span>{{title}}</span>
    </section>


    <!--<section id="headerRight" v-if="isShowTitleRight">-->

    <!--</section>-->
  </header>


</template>

<script>
  //import {mapState, mapAction} from 'vuex'

  export default {
//    mounted(){
//      this.getUserInfo();
//    },

    props: ['title'],

//    computed:{
//      ...mapState([
//        'userInfo'
//      ])
//    },
//
//    methods:{
//      ...mapAction([
//        'getUserInfo'
//      ])
//    }

  }
</script>

<style scoped>

  #header{
    background-color: blue;
    width: 100%;
    height: 1.95rem;
  }

</style>
