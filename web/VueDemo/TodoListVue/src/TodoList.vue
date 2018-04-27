<template>
<div>
  <div>
    <input type="text" v-model="inputValue">
    <button @click="handleSubmit">提交</button>
  </div>

  <ul>
    <todo-item v-for="(item, index) in list" 
        :key="index"
        :content="item"
        @delete="handleDelete(index)"></todo-item>
  </ul>

    <login></login>

    <label><input type="checkbox"><span></span></label>

</div>
  
</template>

<script>

import TodoItem from './components/TodoItem'
import Login from './components/login'

export default {
  components: {
    'todo-item': TodoItem,
    'login': Login
  },
  data: function(){
    return {
      inputValue: '',
      list: [],
    }
  },
  methods: {
    handleSubmit(){
      this.list.push(this.inputValue)
      this.inputValue = "";
    },
    handleDelete(index){
      this.list.splice(index, 1);
    }

  }
}
</script>

<style>






</style>
