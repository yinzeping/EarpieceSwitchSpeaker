# EarpieceSwitchSpeaker
Use AudioManager to switch earpiece and speaker.

一级标题
=
二级标题
-

 ***

# 第一级标题 `<h1>` 
## 第二级标题 `<h2>` 
### 第三级标题 `<h3>` 
#### 第二四级标题 `<h4>` 
##### 第五级标题 `<h5>` 
###### 第六级标题 `<h6>` 

 ***

> 区块引用
> > 嵌套引用
> > >三嵌套引用
> > > > 四嵌套引用

 ***

    fun main(args: Array<String>) {
       println("Hello World!")

       println("sum = ${sum(34, 67)}")
       println("sum = ${sum(34, 67)}")
       println("sum = ${sum(34, 6, 57, 34)}")

       printSum(237, 57)
       printSum(234, 567, 8)
       vars(1, 4, 6, 78, 0, 6, 9, 8)


       val sumLambda: (Int, Int) -> Int = { x, y -> x + y }
       println("sumLambda = ${sumLambda(3, 6)}")


    //    if (args.size < 2) {
    //        println("Two integers expected")
    //        return
    //    }
       testFor()


       val a: Int = 1000
       println(a === a)//true 值相等，对象地址相等

       //经过了装箱，创建了两个不同的对象
       val boxedA: Int? = a
       val anotherBoxedA: Int? = a

       //虽然经过了装箱，但是值是相等的，都是10000
       println(boxedA === anotherBoxedA) //  false，值相等，对象地址不一样
       println(boxedA == anotherBoxedA) // true，值相等
    }
    
 ***
 
*斜体* ，_斜体_
**加粗**，__粗体__

 ***
 
-   第一项
+   第二项
-   第三项
+   第四项
-   第五项
+   第六项

 ***
 
 1. 第一项
 2. 第二项
 3. 第三项
 4. 第四项
 5. 第五项
 6. 第六项
 
 ***
---
_____ 
======

[GitHub](http://github.com)
自动生成连接  <http://www.github.com/>

---

![GitHub set up](http://zh.mweb.im/asset/img/set-up-git.gif)
格式: ![Alt Text](url)

_____ 

第一格表头 | 第二格表头
---------| -------------
内容单元格 第一列第一格 | 内容单元格第二列第一格
内容单元格 第一列第二格 多加文字 | 内容单元格第二列第二格
内容单元格 第一列第三格 多加文字 | 内容单元格第二列第三格
内容单元格 第一列第四格 多加文字 | 内容单元格第二列第四格

======

    st=>start: Start:>http://www.google.com[blank]
    e=>end:>http://www.google.com
    op1=>operation: My Operation
    sub1=>subroutine: My Subroutine
    cond=>condition: Yes
    or No?:>http://www.google.com
    io=>inputoutput: catch something...
    para=>parallel: parallel tasks

    st->op1->cond
    cond(yes)->io->e
    cond(no)->para
    para(path1, bottom)->sub1(right)->op1
    para(path2, top)->op1
