/**
* Created by guzhenfu on 2017/8/8.
*/

<template>
    <div>
        <head-title title="...">
            <router-link to="/forget" slot="search" class="searchPic">
                <svg width="100%" height="100%" xmlns="http://www.w3.org/2000/svg" version="1.1">
                    <circle cx="8" cy="8" r="7" stroke="rgb(255,255,255)" stroke-width="1" fill="none"/>
                    <line x1="14" y1="14" x2="20" y2="20" style="stroke:rgb(255,255,255);stroke-width:2"/>
                </svg>
            </router-link>
        </head-title>

        <nav class="contained">
            <div class="swipe-wrapper">
                <mt-swipe :auto="0">
                    <mt-swipe-item class="swip-item-1 item">1</mt-swipe-item>
                    <mt-swipe-item class="swip-item-2 item">2</mt-swipe-item>
                </mt-swipe>
            </div>

        </nav>

    </div>
</template>

<script>
	import headTitle from '../../components/header/head'
    import { Swipe, SwipeItem } from 'mint-ui';


	export default {
		data () {
			return {

            }
		},
		mounted () {

		},

		computed: {},

		components: {
			headTitle,
            Swipe,
            SwipeItem
		},
		methods: {}
	}
</script>

<style lang="scss" scoped>
    @import "../../style/mixin.scss";

    .searchPic{
        left: .6rem;
        @include wh(1.5rem, 1.5rem);
        @include ct
    }

    .contained{
        padding-top: 3rem;
        @include wh(100%, 60%)
    }

    .swipe-wrapper{
        width: 100%;
        height: 300px;
    }

    .swip-item-1{
        background: red;
    }
    .swip-item-2{
        background: blue;
    }
    .swip-item-3{
        background: green;
    }
    .item{
        text-align: center;
        font-size: 40px;
        color: white;
    }
</style>
