## PingCAP 小作业 - SQL 查询生成器

开始时间 | 北京时间：2020 年 12 月 17 日晚，

截止时间 | 北京时间：2019 年 12 月 23 日，


题目:

在数据库中，如果我们能有一个 SQL 查询生成器，就可以很方便的用来测试我们的数据库了。

假设有这么一张表：
CREATE TABLE t (
    
   a int,
    
   b int,
   
   c varchar(10)

);

需要编写程序，实现一个 SQL 查询生成器，生成关于表 t 的查询，如：
1. select * from t;
2. select a+b from t where a > 10 and b < 20;
3. select a, c from t order by a limit 10;
4. select * from t t1, t t2 where t1.a = t2.a;
5. select * from (select a from t where a < 10) tx where tx.a > 10;

为了简单，只需要考虑加减乘除，大于小于等于这几种表达式。

请尽量让生成的 SQL 空间越大越好，如包含子查询，链接，聚合等算子。

### 题目分析
在测试数据库时，通常选择查询的方式来测试，同时也能测试数据库的性能。而编写SQL的查询语句是乏味，无聊，低效的，所以快速的生成SQL语句就能
方便测试数据库性能。因此本项目的目标是针对一个既定的表的结构，能够完成SQL查询语句的快速生成，以方便测试数据库的性能。

### 解题思路
通常一条查询语句的结构可以表示为：SELECT -> FROM -> (WHERE) -> (GROUP BY) -> (HAVING) -> (ORDER BY) -> (LIMIT)，
而自动生成SQL查询语句的过程其实就是随机生成一棵语法树的过程。

![image](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/Images/SQL%E2%80%94%E2%80%94%E8%AF%AD%E6%B3%95%E6%A0%91.png)

（PS：图中由一个点出发的表示只能生成其中一个模块）

所以解题思路就是如何随机生成一棵类似的语法树，并将其组成一条SQL查询语句。同时在生成的过程中，也要注意条件的合理性，例如数据类型是否对应等。

### 结构分析
本节将详细描述实现的逻辑。整体的思路就是为上图中每一中可能出现的模块构造一个类。

#### 基础：

[Node](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Node/Node.java) 类为项目中最基础的类，是其他
模块的父类，主要负责承担树上每个结点的基础，例如记录它的父亲结点，记录当前的层数，记录scope。除Scope类，其余所有可能出现在语法树上的类
都会继承这一个类。

[Scope](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Node/Scope.java)类负责记录当前结点可以选择的Table及Column的范围，以及记录生成table的别名的编号。

[Query](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Query/Query.java)类，承担一个查询入口的功能，是查询语句生成的根结点。其会生成三个子结点：SelectList，FromClause，WhereCondition，分别对应查询语句的
三个部分。也在这一类中将各个子结点生成的语句整合。

[SelectList](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Query/SelectList.java)类，负责一个查询语句中select到from之间的生成，即搜索哪些列出来。同时也会将搜索出来的列名记录在一个list中。

[FromClause](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Query/FromClause.java)类，负责一个查询语句中from到select之间的生成，即从哪些表中搜索，包含单独的表，join及子查询，

WhereCondition，负责一个查询语句where后的生成。

#### 目录[Element](https://github.com/PHISSTOOD/PingCAP_Assignment/tree/master/src/Generator/Element)中的类：

枚举类[SQLType](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Element/SQLType.java)，负责记录可能出现的数据类型，包括：int，string，bool，聚合。

枚举类[AggregateType](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Element/AggregateType.java)，[CompareType](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Element/CompareType.java)，[ComputeType](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Element/ComputeType.java)枚举了可能出现的聚合函数，比较操作符，运算操作符以及他们对应的SQLType。

[Operator](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Element/Operator.java)类和[Column](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Element/Column.java)类对应的是操作符和一个数据库中列。

[Table](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Element/Table.java)类负责表示一个表结构，包含表名，表的别名，表中的列。之后的子查询的结构也会被表示为一个Table，是一个比较重要的类。

#### 目录[Table](https://github.com/PHISSTOOD/PingCAP_Assignment/tree/master/src/Generator/Table)中的类：

[TableRef](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Table/TableRef.java)类，代表表名的基础的类，在Node类的基础上添加储存Table的List。[TableOrQuery](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Table/TableOrQuery.java)代表单个的表，[JoinTable](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Table/JoinTable.java)类代表多个表的Join，[SubQuery](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Table/SubQuery.java)
代表子查询。

[Join](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Table/Join.java)：[JoinTable](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Table/JoinTable.java)主要负责储存多个表的信息，join的类型和[JoinCondition](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Table/JoinCondition.java)。[JoinCondition](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Table/JoinCondition.java)类负责记录两个表Join的条件。

#### 目录[Expression](https://github.com/PHISSTOOD/PingCAP_Assignment/tree/master/src/Generator/Expression)中的类：

这其中最基础的类是[Expression](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Expression/Expression.java)类，它继承类Node类，但也会被其他的该目录下的类继承。其添加了属性 SQLType，负责记录这一结点对应的SQL中的值的属性。
同时这一个类中也有随机生成其他子类的方法generate。

[ColumnRef](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Expression/ColumnRef.java)类代表那些会在select后会出现的列名，其在父类的基础上多了一个reference的属性，负责记录列名。

[AggregateExpression](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Expression/AggregateExpression.java)类负责表示聚合函数，其中会记录聚合函数的类型，以及以那一列作为聚合函数的根据。

[BinExpression](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Expression/BinExpression.java)类，在Expression的基础上添加了两个子Expression，负责记录操作左右的两个Expression。其继承了Expression，被ComputeExpression，
CompareExpression，ChildExpression类继承。

[ComputeExpression](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Expression/ComputeExpression.java)类，表示一个计算结果，例如：a+b。其有一个计算操作符和两个子Expression构成，通过计算符规定了两个子Expression的SQLType以及
结果的SQLType。

[CompareExpression](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Expression/CompareExpression.java)类，表示一个计算结果，例如：a<56。其有一个比较操作符和两个子Expression构成，通过比较运算符规定了两个子Expression的SQLType。

[ChildExpression](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Expression/ChildExpression.java)类，主要出现在whereClause后，负责扩大条件的数量。

[ConstExpression](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Expression/ConstExpression.java)类，负责表示一个常量，在本项目中主要包含Int型和String型，对应数据库中Int和Varchar。常量的生成由随机生成函数RandomGenerate完成。

#### 目录[Random](https://github.com/PHISSTOOD/PingCAP_Assignment/tree/master/src/Generator/Random)中的类：

[RandomGenerate](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Random/RandomGenerate.java)类主要负责生成随机数和随机制定数据类型的值，[RandomPick](https://github.com/PHISSTOOD/PingCAP_Assignment/blob/master/src/Generator/Random/RandomPick.java)主要负责从指定的List中随机抽取。

#### 难点实现：

在完成的过程中，我认为值得注意的点主要包括：获取可以查询的范围、join及其他运算对应的数据类型的对称、找不到列的情况。

获取可以查询的范围：

这个点的实现主要通过Scope类及SelectList中的derivedColumns。我通过Scope的设定，在Scope中记录当前结点可以访问到的表和列，并且子结点可以对Scope的
值进行继承，这样就实现类在生成from后语句时，可以在Scope中的表的List中随机产生，而生成select后的列名时可以通过在Scope中的列名中随机产生。而
SelectList中derivedColumns主要负责记录一个子查询的查找出来的列，然后记录在其父结点的Scope中。这其中在FromClause的生成中，需要对Scope进行区分，
防止FromClause影响父结点的Scope。

join及其他运算对应的数据类型的对称：

这一点主要通过Expression中SQLType的记录来实现。首先在枚举类中例举了每一个操作符支持的数据类型，并且在表的传入过程中传入对应的数据类型。所以当涉及到
操作符时，会从枚举类中获取目标数据类型，然后挑选满足该数据类型的列。

找不列的情况：

因为查询语句的生成过程为随机的，所以可能会出现找不到列的情况。例如在一个子查询出来的列为varchar类型，在外层与一个表join的条件，另一个表先随机出了
一个int型的列，那么从子查询中就无法找到满足条件的列，因此无法完成生成完整的SQL语句。例：

SELECT t_1.a FROM t as t_1 inner join （SELECT c FROM t as t_2 where a > 10) as sub_3 on a = ? where t_1/b < 20

语句中？的地方就无法找到对应的列。因此，遇见类似无法执行下去的情况，解决办法是抛出错误及错误的信息，在最终执行完成生成一个执行报告。

### 延伸
#### 分布式
在对分布式数据库及TiDB有一定了解后，做了一定的延伸。
基于blog：[三篇文章了解TiDB技术内幕-说计算](https://pingcap.com/blog-cn/tidb-internal-2/) 中提到基于KV查询时，需要根据table和key值
来构造左开右闭的区间，从这个区间中扫描每一行数据。因此创建Request类，其中保存key的起始值，key的终止值，要执行的SQL语句，以及alias
（alias的用途后文会提到）。但因为本身的实现不依赖数据库，所以目前所有Request的key的区间设为【0，0）。

但这种request是针对单表的，因为在TiDB中key的组成是由table id 和 row id组成的。所以在查询语句涉及到多表的情况时，上述的结构无法支持，
因此需要对涉及到多表的SQL查询语句进行处理。同时因为之前博客中的提到的为了减少RPC调用（RPC调用，网络传播，IO会导致大量的开销），
避免无意义的网络传输，所以要将计算尽量靠近结点，将Filter，聚合函数，Group By下推。这不仅更高效，在实现上也更简单。

RPC类，意为实现远程调用针对单表的SQL语句的功能，其主要的方法为rpc()，旨在执行request返回Result。

Result类，代表针对单表执行以此SQL语句后的结果集，在本项目中对单表执行一次的结果集由 < Execute (SQL查询语句) >表示。

Select类，保存由上层传来的一条完整的查询语句（没有针对多表进行处理，一开始由查询语句生成器直接生成的），并且还保存这条语句是否针对多表，
如果是，保存被划分为针对每一个表的语句。Select类完成调用RPC的操作。

Executor类，意为执行SQL查询语句。里面主要分为两个部分，一个是根据传进的Query类，判断其是针对单表还是多表，然后生成对应的Request，进行执行。
另一部分是如果Query类对应的是一个针对多表的查询语句，那么进行查询语句的拆分，生成子Query，并生成多个Request类。

这里在实现时做了一定的假设：

1. 假设在生成SQL语句时，列名都会有所属的表的信息。型如（a.id)，表示为表a里的属性。
2. 假设在生成SQL语句时，表都被赋予了别名，这主要是因为本题的设定中只有一个表，所以当SQL语句如果进行Join时，需要赋予别名。之前提到的Request类
中的alas就是为了保存这个信息，在最终的结果中有所显示。
3. 不包含子查询。

这样，这对一条查询语句：

```SELECT a.id, b.code, c.name FROM t a, t b cross join t c WHERE a.id = 1 AND b.code > 8 AND c.name = 'jack'```

其结果被表示为：

```
Execute (SELECT id FROM t WHERE id = 1) As a
Result Union Execute (SELECT code FROM t WHERE code > 8) As b
Result Union Execute (SELECT name FROM t WHERE name = 'jack') As c
With the oringinal sql is (SELECT a.id, b.code, c.name FROM t a, t b cross join t c WHERE a.id = 1 AND b.code > 8 AND c.name = 'jack')
```

Execute意为针对子语句进行查询，Result Union意为针对子结果进行Join的实现，其实现原理可以与
[TiDB源码阅读系列文章](https://pingcap.com/blog-cn/tidb-source-code-reading-9/) 提到的相符，但本项目没有涉及，最后附上原查询语句。


### 实际测试

#### 测试环境
系统： Mac OS 10.15.6/ Jdk 1.8

#### 测试设计


#### 测试结果
执行结果

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E9%94%99%E8%AF%AF%E8%BE%93%E5%85%A5.png)

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E9%94%99%E8%AF%AF%E8%BE%93%E5%85%A5.png)

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E9%94%99%E8%AF%AF%E8%BE%93%E5%85%A5.png)

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E9%94%99%E8%AF%AF%E8%BE%93%E5%85%A5.png)

### 说明
1. 设计中的子句只包含了SELECT，FROM，JOIN（JOIN，CROSS JOIN，INNER JOIN，LEFT JOIN，RIGHT JOIN），ON，USING，WHERE，
GROUP BY，HAVING，ORDER BY，LIMIT，及五个聚合函数（COUNT，AVG，SUM，MIN，MAX）其余的子句类似BETWEEN，EXIST,IN暂无涉及。
2. 生成的SQL查询语句除了Join type，order type，聚合函数因为人为输入可能为小写，其余子句自动生成为大写。
3. 设计中，默认每一个join对应的on只有一组，主要原因是为了在终端输入时更好表达。
4. 测试中，终端输入暂不支持子查询，因为会导致输入时过于繁琐。生成带有子查询的SQL语句在查询的第一部分有被涵盖。



## 参考资料
[三篇文章了解TiDB技术内幕-说计算](https://pingcap.com/blog-cn/tidb-internal-2/)

[TiDB源码阅读系列文章](https://pingcap.com/blog-cn/tidb-source-code-reading-9/)

[TPC-H query plan](http://www.qdpma.com/tpch/TPCH100_Query_plans.html)
