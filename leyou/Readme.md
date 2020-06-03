5.29 完成es项目的基本查询功能

1创建maven 项目，创建search服务，注册注册中心
2 远程调用 interface项目的api包下的接口
3前端项目搜索关键字，响应结果
见5.29流程图
===================================
6.1分页
获取总页数   获取总条数    判断分页范围    v-for循环回显数据加图片
图品选中回显
====================================
6.2
   1：es索引库存在我们的品牌id和三级分类id
   2：根据品牌id和分类id去聚合（term）
   3: 获取聚合的的分类和品牌id，并且放到list集合中，集合中要获取整个分类的对象及品牌的对象
   4：返回结果pageResult需要重新封装一个实体类 SearchResult  ENXEND  pageResult
   5: 设置构造方法
   6：在放回的结果中把集合返回回去
   7：把返回的数据放到vue data数据源中的filters
====================================
6.3
 后台聚合规格参数的过程思路
     1：根据cid3 查询可以搜索的规格参数列表
 	2：循环列表，用列表中的name（spec_param）作为聚合名称，聚合字段（specs.name.keyword）
 	3: 获取聚合结果 桶  进行封装  list<map<string,boject>>
 	4: 把list返回给前台
 	前台数据渲染步骤
       1：到数据源data 定义接受数据的集合	filters：[]
动态选择参数像后台传递格式
	search：{
	   key:1
	   page:
	   filter:{
	      "前置摄像头": 1500-2000万,
		  "电池容量（mAh)" 3000-4000mAh,
	   }
	}
	去common.js   250行设置   allowDots: true,
	动态选择参数像后台传递格式
    	search：{
    	   key:1
    	   page:
    	   filter:{
    	      "前置摄像头": 1500-2000万,
    		  "电池容量（mAh)" 3000-4000mAh,
    	   }
    	}
    	去common.js   250行设置   allowDots: true,
    后台接受参数使用Map<String,Object>	
    