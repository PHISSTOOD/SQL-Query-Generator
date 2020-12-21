## PingCAP 小作业 - SQL 查询生成器

开始时间 | 北京时间：2020 年 12 月 9 日，

截止时间 | 北京时间：2019 年 12 月 16 日，


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
方便测试数据库性能。其次，在生成一条SQL语句时，针对库表结构的不同，管理数据的多样性已经不同的应用场景，需要人为的有所干预，例如选择哪些
列进行搜索，通过哪些表的链接，或者使用怎样的聚合。但我认为本题的主要考察的不是这一部分人为选择查询条件的交互的部分，所以在测试类中提前
设置的生成条件。本项目主要针对查询语句的生成，以及其扩展。

### 解题思路
因为一条查询语句的结构有其特定的顺序，可以表示为：SELECT -> FROM -> (WHERE) -> (GROUP BY) -> (HAVING) -> (ORDER BY) -> (LIMIT)，
括号中意为该子句可以不存在。在完成项目的时候主要有两种思路：
1. 通过判断接下来的语句来不断的更新已有的SQL语句，举个例子：

   例如此时有方法：select()：表示添加SQL查询语句中"SELECT"的部分,columns()：表示添加SQL查询语句中要查询出的属性的部分（即列名）,
   from()：表示添加SQL查询语句中"FROM"的部分..
   
   通常一个SQL查询语句生成时都先调用select()，然后通过判断其后跟着的条件，例如如果跟了列名，则调用columns()来添加列名的部分，
   但如果没有类似列名的条件，则添加"*"然后点用from()。以此类推，通过不断的对条件的判断来调用不同的函数，进行添加、完善SQL查询语句。
 
2. 收集各个部分的条件，然后统一的拼接起来。本项目选用的是第二种思路，具体的思路将在下文中详细叙述。

两种思路的比较：

这样做的优点我认为有以下几点（下文中的模块意为查询语句中每一个部分，例如列名，表名，JOIN）：
1. 每一个模块设为一个类，条理更加清晰，并且能够记录表述关于这一模块更多的属性。
2. 每个模块通过其构造函数能够尽可能少的需要人为的输入。
3. 最终的每一个查询对应一个Query对象，因此在人输入条件时不必强制完全按照查询语句本来的顺序，例如在设置条件是可以先设置FROM的表名，
然后再设置列名，但不提倡。

### 结构分析
本节将详细描述实现的逻辑。

#### 整体结构
项目中为除SELECT，FROM之外的所有模块创建了独自的类，Column（列名），Condition（WHERE语句的条件），Group（GROUP BY），Having（HAVING），
Join（各种类型Join），Limit（Limit），On（Join后面的On语句），Order（ORDER BY），Table（FROM后跟的表名）。

Column 和 Table主要包含属性：本身的列名（表名），是否使用AS，别名。后两者可以为空。

Condition 主要包含三个字符串类型，分别表示操作符前的条件，操作符，操作符后的条件。

Group内为两个List，List<Column> 和 List<Having>, 表示group by的组，以及可能有的聚合的条件。

Having 内包含四个字符串类型，aggregate（聚合函数名），column（列名），operator（操作符），comparator（操作符后跟的数）。

Join 包含joinType，table（表名），on（是否使用on语句，及on的条件，本质上on与condition相同），using（是否使用using，using和on只能2选1）。

Limit 包含offset，size。offset如果没设置默认为0。

Order orderName及orderType，分表表示根据哪一列进行排序以及排序的类型（默认为ASC）。

#### Query主体结构
SQL查询语句的生成主要在Query类中生成，一个Query就对应一条完整的查询语句。Query中根据SELECT，FROM，WHERE，GROUP BY，HAVING，ORDER BY，LIMIT
对每一个小模块进行实现，并且具有添加各个模块内的条件的方法。其中列名，表名，JOIN，WHERE Condition，Order为List，可以储存多个该类的条件。

而生成查询语句对应的是generate方法，里面主要调用针对各个模块的list或实体转换成字符串的方法，然后统一拼接起来。

Query类还有一些校验的方法，例如校验JoinType，OrderType是否书写正确。

这样的结构也能够支持创建子查询，例如题目中5所示，就将addTable方法的入参传为(new Query().add(..各类条件..)，true（意为是通过SQL语句
生成的表）)。

### 延伸
#### 分布式
在对分布式数据库及TiDB有一定了解后，做了一定的延伸。
基于blog：[三篇文章了解TiDB技术内幕-说计算](https://pingcap.com/blog-cn/tidb-internal-2/) 中提到基于KV查询时，需要根据table和key值
来构造左开右闭的区间，从这个区间中扫描每一行数据。因此创建Request类，其中保存key的起始值，key的终止值，要执行的SQL语句，以及alias
（alias的用途后文会提到）。但因为本身的实现不依赖数据库，所以目前所有Request的key的区间设为【0，0）。

但这种request是针对单表的，因为在TiDB中key的组成是由table id 和 row id组成的。所以在查询语句涉及到多表的情况时，上述的结构无法支持，
因此需要对涉及到多表的SQL查询语句进行处理。同时因为之前博客中的提到的为了减少RPC调用（RPC调用，网络传播，IO会导致大量的开销），
避免无意义的网络传输，所以要将计算尽量靠近节点，将Filter，聚合函数，Group By下推。这不仅更高效，在实现上也更简单。

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
测试主要分为两部分，一部分是提前预设好添加的条件，然后与应该生成的SQL查询语句进行对比，另一部分是可以在终端交互，
可以由人工根据提示输入的条件，生成SQL查询语句，同时也可以选择是类MySQL端还是类分布式数据库端的。类MySQL端的会输出一条SQL查询语句，
类分布式数据库端的会输出如前一节描述的结果。

#### 测试结果
第一部分（提前设定）：

该部分由test.test中的GenerateTest实现，预先设定了题目中出现的SQL语句，自编的SQL查询语句，及有错误输入的SQL语句。

自编的SQL查询语句前三条是基于表t，给三列赋予一定的属性产生的查询语句，另外一条是基于TPCH改编的查询语句。

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E9%A2%98%E7%9B%AE%E4%B8%AD%E7%9A%84%E8%BE%93%E5%85%A5.png)

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E5%85%B6%E4%BB%96%E8%BE%93%E5%85%A5.png)

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E9%94%99%E8%AF%AF%E8%BE%93%E5%85%A5.png)


第二部分（终端输入）：

该部分由test.test中的TerminalTest实现，可以在终端选择模式并输入条件生成想生成的SQL查询语句。

手动输入时请参照说明及用例。

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E6%89%8B%E5%8A%A8%E8%BE%93%E5%85%A5SQL%201.png)

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E6%89%8B%E5%8A%A8%E8%BE%93%E5%85%A5SQL%202.png)

终端输入生成类分布式 SQL查询语句

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E6%89%8B%E5%8A%A8%E8%BE%93%E5%85%A5SQL%EF%BC%88%E5%88%86%E5%B8%83%E5%BC%8F%EF%BC%891.png)

![image](https://github.com/PHISSTOOD/PingCAP-HomeWork/blob/main/Images/%E6%89%8B%E5%8A%A8%E8%BE%93%E5%85%A5SQL%EF%BC%88%E5%88%86%E5%B8%83%E5%BC%8F%EF%BC%892.png)

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
