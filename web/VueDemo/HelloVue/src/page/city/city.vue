/**
* Created by guzhenfu on 2017/8/8.
*/

<template>
    <div>
        <head-title isShowBack="true" title="上海" menu="切换城市"></head-title>

        <section class="addWrap">
            <input type="text" placeholder="输入学校、商务楼、地址" class="address">

            <button class="commit">提交</button>
        </section>

        <p class="pois_search_history">搜索历史</p>

        <ul class="historyDataUl">
            <li v-for="(item, index) in historyData" :key="index" class="addressItem">
                <p class="name">{{item.name}}</p>
                <p class="addressDetail">{{item.address}}</p>
            </li>
        </ul>

        <footer class="clearAll">清空所有</footer>
        <div class="search_none_place" v-if="noData">很抱歉！无搜索结果</div>

    </div>
</template>

<script>
	import headTitle from '../../components/header/head'

	export default {
		data () {
			return {
			    historyData: [
			        {name: '111', address: '222'}, {name: '333', address: '444'}
			    ],
                noData: false,
            }
		},
		mounted () {

		},

		computed: {},

		components: {
			headTitle
		},
		methods: {}
	}
</script>

<style lang="scss" scoped>
    @import "../../style/mixin.scss";

    .addWrap{
        background-color: white;
        margin-top: 3rem;
        padding-bottom: 0.5rem;
        border-bottom: 1px solid $bc;
    }

    .address{
        @include wh(90%, 2rem);
        margin: 0.5rem 5% 1rem;
        border: 1px solid $bc;
        padding: 0rem 0.5rem;
    }

    .commit{
        @include wh(90%, 2rem);
        margin: 0rem 5%;
        @include sc(0.8rem, #fff);
        background-color: $blue;
    }

    .pois_search_history{
        border-bottom: 1px solid $bc;
        padding-left: 0.5rem;
        @include font(0.6rem, 1.5rem);
    }

    .clearAll{
        background-color: white;
        text-align: center;
        padding: 0.5rem;
    }

    .addressItem{
        border-bottom: 1px solid $bc;
        background-color: white;
        padding: 0.5rem;

        .name{
            @include sc(1rem, #333);
        }

        .addressDetail{
            @include sc(0.8rem, #999);
        }
    }
</style>
