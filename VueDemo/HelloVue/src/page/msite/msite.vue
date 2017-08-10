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
            <mt-swipe class="swipe">
                <mt-swipe-item class="swipeItem">1</mt-swipe-item>
                <mt-swipe-item class="swipeItem">2</mt-swipe-item>
                <mt-swipe-item class="swipeItem">3</mt-swipe-item>
            </mt-swipe>
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

    .swipeItem{
        @include wh(100%, 100%);
        background-color: #3190e8;
    }
</style>
