2018-03-25: 
  现象：执行npm install webpack --save-dev 报错
  解决办法：是文件夹的名字不能为 webpack，修改文件夹名字就好了

2018-03-25:
  现象：上下元素margin 重叠了
  解决办法：不让共同在一个BFC中，比如把其中一个用div包裹起来，然后加上 style="overflow:hidden;"
  总结BFC作用：
  	1. 解决margin重叠问题
  	2. 消除float
  	3. 解决侵占浮动元素的问题
2018-03-25：
	现象：手机屏幕左右滑动了，布局超过100%了
	解决办法：width: 100% 并且遇到 padding 或者 margin的时候，手机屏幕会左右滑动，解决办法就是 box-sizing:border-box 限制不能超过盒子限制  
2018-03-25：
	现象：左边图标，右边文字
	解决办法：设置文字，然后给文字加伪类 :before , 优点是 随着文字变长，图标会跟着移动，很好的解决兼容性问题
2018-03-28：
	现象：text-align失效
	解决办法：text-align是让块状里面的元素（比如文字）居中或者上下移。而这个margin是直接让块状 居中
2018-03-28:
	现象： HTML5 data-* 自定义属性及其注意点
	解决办法：我们可以将一些参数绑定到dom标签上，而不用将数据填 到input标签上然后隐藏该标签。最需要注意的一点的取值时必需全部使用小写的名字，”data-*”中名子可以用驼峰命名。
2018-03-28：
	现象：$.each 不懂
	解决办法：https://www.cnblogs.com/qq313462961/p/6111446.html
			$().each,对于这个方法，在dom处理上面用的较多。如果页面有多个input标签类型为checkbox，对于这时用$().each来处理多个checkbook，例如：
				$(“input[name=’ch’]”).each(function(i){
					if($(this).attr(‘checked’)==true) {
						//一些操作代码
					}
				})
			$.each jQuery提供的each方法是对参数一提供的对象的中所有的子元素逐一进行方法调用
2018-03-28:
	现象：image有load和 error 事件
	解决方法：
			var img = new Image();
            $(img).on('load error', function() {
                $progress.html(Math.round((count + 1) / len * 100) + '%');

                if (count + 1 >= len) {
                    $('.loading').hide();
                }
                count++;
            })

2018-03-28:
	现象：$.extend()的一些用法
	解决办法：
		$.extend(src) 该方法就是将src合并到jquery的全局对象中去  
		$.fn.extend(src) 该方法将src合并到jquery的实例对象中去  
2018-04-02：
	现象：js执行上下文
	解决办法：
		变量、函数表达式——变量声明，默认赋值为undefined；
		this——赋值；
		函数声明——赋值；
		这三种数据的准备情况我们称之为“执行上下文”或者“执行上下文环境”。
2018-04-02：
	函数在定义的时候（不是调用的时候），就已经确定了函数体内部自由变量的作用域
	函数在定义的时候（不是调用的时候），就已经确定了函数体内部自由变量的作用域
	函数在定义的时候（不是调用的时候），就已经确定了函数体内部自由变量的作用域
2018-04-02：
	在函数中this到底取何值，是在函数真正被调用执行的时候确定的，函数定义的时候确定不了
	在函数中this到底取何值，是在函数真正被调用执行的时候确定的，函数定义的时候确定不了
	在函数中this到底取何值，是在函数真正被调用执行的时候确定的，函数定义的时候确定不了
2018-04-03:
	作用域中变量的值是在执行过程中产生的确定的，而作用域却是在函数创建时就确定了
	作用域中变量的值是在执行过程中产生的确定的，而作用域却是在函数创建时就确定了
	作用域中变量的值是在执行过程中产生的确定的，而作用域却是在函数创建时就确定了	
2018-04-08:
	apply 、 call 、bind 三者都是用来改变函数的this对象的指向的；
	apply 、 call 、bind 三者第一个参数都是this要指向的对象，也就是想指定的上下文；
	apply 、 call 、bind 三者都可以利用后续参数传参；
	bind 是返回对应函数，便于稍后调用；apply 、call 则是立即调用 。
2018-04-08:
	var vegetables = ['Cabbage', 'Turnip', 'Radish', 'Carrot'];
	console.log(vegetables); 
	// ["Cabbage", "Turnip", "Radish", "Carrot"]
	var pos = 1, n = 2;
	var removedItems = vegetables.splice(pos, n);
	console.log(vegetables); 
	// ["Cabbage", "Carrot"] (the original array is changed)
	console.log(removedItems); 
	// ["Turnip", "Radish"]
	var shallowCopy = fruits.slice(); // this is how to make a copy
	// ["Strawberry", "Mango"]
2018-04-12：
	一个promise链式遇到异常就会停止，查看链式的底端，寻找catch处理程序来代替当前执行
2018-04-18:
	input 不好居中就隐藏，然后设置z-index，后面加上span，这样看起来他们俩个融合了，然后就是input添加点击事件，span添加样式	











