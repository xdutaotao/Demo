/**
* Created by guzhenfu on 2017/8/8.
*/

<template>
    <div>
        <head-title isShowBack="true" title="密码登录"></head-title>

        <nav class="inputWrap">
            <input type="text" class="nameInput" placeholder="账号">
            <section class="pwdWrap">
                <input type="text" class="pwdInput" placeholder="密码">
                <div class="pwdLetter">
                    <span class="letter">abc</span>
                    <span class="letter">...</span>
                    <div class="pwdBtn"></div>
                </div>
            </section>

        </nav>
    </div>
</template>

<script>
  import headTitle from '../../components/header/head'

  export default{
    data() {
      return {}
    },
    mounted(){

    },

    computed: {},

    components: {
      headTitle
    }
  }
</script>

<style lang="scss" scoped>
    @import "../../style/mixin.scss";

    .inputWrap{
        margin-top: 3rem;
        background-color: white;
    }

    .pwdWrap{
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid $bc;
    }

    .nameInput{
        @include wh(100%, 2rem);
        padding: 0rem 0.5rem;
        border-bottom: 1px solid $bc;
    }

    .pwdInput{
        @include wh(100%, 2rem);
        padding: 0rem 0.5rem;
    }

    .pwdLetter{
        display: flex;
        justify-content: space-around;
        align-items: center;
        flex-direction: row;
        border-radius: 0.5rem;
        background-color: $bc;
        margin-right: 0.5rem;
        @include wh(15%, 100%);
        span:nth-of-type(2){
            margin-bottom: 0.2rem;
        }
    }

    .letter{
        @include sc(0.5rem, #fff);
    }

    .pwdBtn{
        position: absolute;
        z-index: 1;
        left: 85%;
        background-color: #f1f1f1;
        border-radius: 50%;
        @include wh(1.8rem, 1.8rem)
    }


</style>
