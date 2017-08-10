/**
* Created by guzhenfu on 2017/8/8.
*/

<template>
    <div>
        <head-title isShowBack="true" title="重置密码"></head-title>

        <nav class="inputWrap">
            <input type="text" class="nameInput" placeholder="账号">
            <input type="text" class="nameInput" placeholder="旧密码">
            <input type="text" class="nameInput" placeholder="请输入新密码">
            <input type="text" class="nameInput" placeholder="请确认密码">

            <section class="pwdWrap">
                <input type="text" class="pwdInput" placeholder="验证码">
                <div class="codeWrap">
                    <span class="codePic">{{codeNum}}</span>
                    <div class="changeWrap" @click="changeCodePic">
                        <span class="look">看不清</span>
                        <span class="changeOne">换一张</span>
                    </div>
                </div>
            </section>

        </nav>

        <button class="loginBtn">确认修改</button>


    </div>
</template>

<script>
    import headTitle from '../../components/header/head'

    export default{
        data() {
            return {
                pwdShowType: false,
                codeNum: '111'
            }
        },
        mounted(){

        },

        computed: {},

        components: {
            headTitle
        },
        methods:{
            changePwdType(){
                this.pwdShowType = !this.pwdShowType;
            },

            changeCodePic(){
                this.codeNum = Math.ceil(Math.random()*10000)
            }
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
        @include wh(100%, 2.5rem);
    }

    .nameInput{
        @include wh(100%, 2.5rem);
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
        @include wh(15%, 50%);
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

    .trans_to_right{
        transform: translateX(40%);
    }

    .codeWrap{
        display: flex;
        flex-direction: row;
        align-items: center;
    }

    .codePic{
        margin-right: 0.5rem;
    }

    .changeWrap{
        @include wh(3rem, 2rem);
        line-height: 1rem;
    }

    .look{
        font-size: 0.5rem;
    }

    .changeOne{
        @include sc(0.5rem, $blue)
    }

    .loginBtn{
        @include wh(90%, 2rem);
        @include sc(1rem, #fff);
        margin: 5%;
        background-color: #4cd964;
        border-radius: 0.2rem;
    }

    .changePwd{
        float: right;
        margin-right: 1rem;
        @include sc(.6rem, #3b95e9);
    }


</style>
