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
				