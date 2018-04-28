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

    <!-- <div class="checkboxFive">
      <input type="checkbox" value="1" id="checkboxOneInput" name="" />
      <label for="checkboxOneInput"></label>
  </div> -->

  <div class="checkboxFour">
      <input type="checkbox" id="checkboxOneInput"  disabled/>
      <label for="checkboxOneInput"></label>
  </div>

  <div class="checkboxFour">
      <input type="checkbox" id="checkboxTwoInput"  checked/>
      <label for="checkboxTwoInput"></label>
  </div>

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

<style scoped>

input[type=checkbox] {
visibility: hidden;
}

  /* .checkboxFive {
    width: 25px;
    margin: 20px 100px;
    position: relative;
  }


	.checkboxFive label {
    cursor: pointer;
    position: absolute;
    width: 25px;
    height: 25px;
    top: 0;
    left: 0;
    background: #eee;
    border:1px solid #ddd;
}


.checkboxFive label:after {
    opacity: 0.2;
    content: '';
    position: absolute;
    width: 9px;
    height: 5px;
    background: transparent;
    top: 6px;
    left: 7px;
    border: 3px solid #333;
    border-top: none;
    border-right: none;
 
    -webkit-transform: rotate(-45deg);
    -moz-transform: rotate(-45deg);
    -o-transform: rotate(-45deg);
    -ms-transform: rotate(-45deg);
    transform: rotate(-45deg);
}  

.checkboxFive label:hover::after {
    opacity: 0.5;
}

.checkboxFive input[type=checkbox]:checked + label:after {
    opacity: 1;
} */



.checkboxFour {
    width: 20px!important;
    height: 20px!important;
    background: #ddd;
	display: inline-block;
 
    border-radius: 100%;
    position: relative;
}

.checkboxFour label {
    display: block;
    width: 14px;
    height: 14px;
    border-radius: 100px;
 
    -webkit-transition: all .5s ease;
    -moz-transition: all .5s ease;
    -o-transition: all .5s ease;
    -ms-transition: all .5s ease;
    transition: all .5s ease;
    cursor: pointer;
    position: absolute;
    top: 3px;
    left: 3px;
    z-index: 1;
}

.checkboxFour input[type=checkbox]:checked + label {
    background: #26ca28;
}



</style>
