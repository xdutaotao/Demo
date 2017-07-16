<template>
  <div>
    <h1>{{msg}}</h1>
    <h1>{{dataFromFather}}</h1>
    <button @click="clickMe">ClickMe</button>
  </div>
</template>

<script>
  export default{
    data() {
      return {
        msg: 'hello hello'
      }
    },
    props:['dataFromFather'],
    methods: {
      clickMe: function () {
        console.log("333333")
      }
    }
  }
</script>

<style>

</style>
