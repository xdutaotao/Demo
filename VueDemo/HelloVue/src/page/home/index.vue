/**
 * Created by guzhenfu on 2017/8/4.
 */

<template>
  <div class="homePage">

    <head-title signin-up="home">
      <span slot="logoText" class="logoText">ele.me</span>
    </head-title>

    <nav class="city_nav">
      <div class="city_top">
        <span>当前定位城市:</span>
        <span>定位不准时,请在城市列表中选择</span>
      </div>

      <router-link :to="'/city/' + guessCityid" class="guess_city">
        <span>{{guessCity}}</span>
        <svg class="arrow_right">
          <use xmlns:xlink="http://www.w3.org/1999/xlink" xlink:href="#arrow-right"></use>
        </svg>
      </router-link>

    </nav>

    <section class="hot_city">
      <span>热门城市</span>
      <ul class="citylistul">
        <router-link  tag="li" v-for="item in hotcity" :to="'/city/' + item.id" :key="item.id">
          {{item.name}}
        </router-link>
      </ul>
    </section>

  </div>
</template>

<script>

  import headTitle from '../../components/header/head'

  export default{
    data() {
      return {
        guessCityid: 0,
        guessCity: '上海',
        hotcity: [{name:'上海'},{name:'上海'},{name:'上海'},{name:'上海'},
                      {name:'上海'},{name:'上海'},{name:'上海'},{name:'上海'}]
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

  .city_nav{
    padding-top: 2.35rem;
    border-top: 1px solid $bc;
    background-color: #fff;
    margin-bottom: 0.4rem;
    .city_top{
      @include fj;
      line-height: 1.45rem;
      padding: 0 0.45rem;
      span:nth-of-type(1){
        @include sc(0.55rem, #666);
      }
      span:nth-of-type(2){
        font-weight: 900;
        @include sc(0.475rem, #9f9f9f);
      }
    }

    .guess_city{
      @include fj;
      align-items: center;
      height: 1.8rem;
      padding: 0 0.45rem;
      border-top: 1px solid $bc;
      border-bottom: 2px solid $bc;
      @include font(0.75rem, 1.8rem);
      span:nth-of-type(1){
        color: $blue;
      }
      .arrow_right{
        @include wh(1rem, 1rem);
          fill: #999;
      }
    }


  }

  .hot_city{
    padding-bottom: 0.3rem;
    border-bottom: 1px solid $bc;
    span{
      @include sc(0.55rem, #666);
      padding-left: 0.45rem;
    }
  }

  .citylistul{
    display: flex;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
    border-top: 1px solid $bc;
    li{
      @include sc(0.55rem, #666);
      text-align: center;
      color: $blue;
      @include wh(25%, 1.75rem);
      @include font(0.6rem, 1.75rem);
      border-bottom: 0.025rem solid $bc;
      border-right: 0.025rem solid $bc;
    }
    li:nth-of-type(4n){
      border-right: none;
    }
  }

</style>
