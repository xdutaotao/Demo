/**
 * Created by guzhenfu on 2017/8/4.
 */

<template>
  <div class="homePage">
    <head-title signin-up="home">
      <span slot="logoText" class="logoText">ele.me</span>
    </head-title>
  </div>
</template>

<script>

  import headTitle from '../../components/header/head'

  export default{
    data() {
      return {

      }
    },
    components:{
      headTitle
    }
  }
</script>

<style lang="scss" scoped>

  @import "../../style/mixin.scss";

  .logoText{
    @include sc(0.8rem, #fff);
    @include ct;
    margin-left: 0.4rem;
  }

</style>
