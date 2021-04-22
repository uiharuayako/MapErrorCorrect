# 地图纠错工具原型

## 概述
（作业文本本质是markdown写的，在github网页查看可能效果更佳）  
本程序复用了本人之前的JavaFx联网画板程序（Java课程的大作业程序）的许多代码，而且UI风格一脉相承，但从底层上改变了原程序的一些逻辑，而且彻底删除了网络功能。当然，从本次作业的要求来看，重写一份仅仅符合要求的程序比改造之前的程序的成本更低，难度更小，但是我还是想实现一些更多的功能，也体现了一下之前代码的复用性，就改造了一下之前的代码，做出了这个结果。  

程序较好的完成了要求的所有功能，并在许多地方有所延伸，以下为详细内容：  
1. 地图数据加载:从应用中打开PNG栅格或mec文档（MapErrorCorrect，我自定义的文档后缀名，其实就是文本文件。该文件能储存与地图一一对应的矢量数据）格式的地图矢量数据文件，并显示在应用程序窗口中。
2. 地图数据显示:其中，栅格图像会显示在主程序中，当然，矢量也能加载到主程序中，并在主程序的GUI界面中实现对矢量数据的可视化修改。文本矢量文件亦可以由一个相对独立的，用JavaFx实现的简单记事本（MyNotePad）显示并提供编辑功能。
3. 地图数据快捷显示：当鼠标移动到矢量附近，程序的状态栏会显示矢量的备注
![图 1](https://i.loli.net/2021/04/22/oTwvHnM6brWseDf.png)  
4. 错误记录:直接用画板的形式在图像中画出并纠正错误，每次绘画的详细行为都会被记录在文本文件中。可以像画画一样纠正地图。
5. 错误记录查询:地图数据加载后，在质检工具中打开已保存的错误信息文件，可通过输入关键字搜索并显示出错情况。并且可以对搜索到的错误信息进行任意修改，包括修改位置和错误备注。
6. 矢量绘制:能自动加载与地图文件同名的矢量文件，并将其显示在图上。  
当然，由于开发时间较短，任务较重，最近事比较多，可能有很多没测试出来的bug，请不要苛责这个程序QuQ。比如如果手动把点的位置改成不正确的格式，程序会直接报错，可能不会崩溃，但是没做处理。如果时间长点可能会继续完善，
## 使用指南
![图 2](https://i.loli.net/2021/04/22/NozpjAxlg86XKQC.png)  
### 功能介绍
#### 绘制功能
程序支持9种绘图工具，分别是铅笔，橡皮，打个圆点，文字，直线，圆角矩形/矩形，多边形，椭圆（用这个画圆其实也可以）。上图展示了大部分绘图的特性。绘图之后程序会以文本方式将绘制的图形储存。储存的结构是**画笔属性+点位置+错误描述文本**。这里面有两种矢量的储存比较特殊。
1. **铅笔/橡皮**：这个工具就像一种画笔，能绘制任意形状的线。当然，其实底层上是以折线的形式储存的矢量。具体操作时，视鼠标的dpi和响应速度影响，画一笔可能有几十个点。比较占用空间。
2. **多边形**：如图右边中间，位置文本框显示的，多边形会储存成多个点。原理简单，效果不错。
#### 搜索&修改功能
如图，右边的搜索框里输入，某一矢量描述字符串的，一个子串，就能找到那个矢量啦。如果存在多个矢量描述包含同一子串的情况，返回第一个结果。此时时间修改描述或者位置，并点击保存更改，就能把更改同步到本地文件中，如果在文档菜单中选择文档重绘，就能看到修改位置后的图形。  
![图 4](https://i.loli.net/2021/04/22/hl8HOQLjUmAfqZt.png)  
#### 矢量文档编辑功能
在上图的选单点击文档编辑，即可调用一个记事本编辑与当前图像对应的文档，文档在编辑后的改动会被即时反馈在图上。
![图 5](https://i.loli.net/2021/04/22/qjMNogmhE9B8JsW.png)  

## 架构分析
### 类图分析
![图 7](https://i.loli.net/2021/04/22/rWHKNfDLxyZwgdl.png)  
上图是本程序的UML类图，当然，这个.uml文件已经附在附件内，可以在idea内打开。如果用其他工具打开应该只能看到类名，看不到构造和类方法。  
我把这些类分成了4列，每列有不同的意义
1. 第一列：是JavaFx的入口类，实际意义就是把各种组件组合到一起，作用是完成了GUI的设计
2. 第二列：是GUI的主要组件，含有程序的绝大部分逻辑，完成了主要功能。但这并不是GUI的全部组件，有些功能比较简单的归入第四列。这四个重要组件是
*  菜单栏MyMenuBar，其中包含了文件IO的重要逻辑
*  画板MyCanvas，包办了画图的所有逻辑，既可以直接鼠标画，也可以从矢量文件中读取着画
*  信息搜索与编辑栏MyEditBar，实现了关键词搜索，错误备注添加等逻辑
*  简单记事本MyNotePad，实现了文档的打开
3. 第三列：是本程序的最重要特点。因为第二列各个组件之间联系很弱，但是完成功能又需要组件间大量的信息交互，因此，我实现了一个“属性”类。这是一个静态类，里面所有的变量和方法都是静态的，MyStatus类不需要也不应被实例化，  
这个类包括了绘图需要的各种信息。例如绘图工具的种类，线的粗细，是画实心图像还是空心图形等等属性信息。在绘图时，MyCanvas画板会调用这些信息，在工具切换或者其他信息改变时，调色板MyPalette以及工具栏ToolsPane会改变这些信息。以此实现了组件间的数据交换，
4. 第四列：包含了一些很简单的类，主要是添加新功能，或者组织数据，或者完成简单的模式切换。比如这个MyPoint类就是对Java自带的Point类的拓展，增加了适合本程序实际情况的几种构造方式，减少了逻辑层的代码量。挺多都是这个用法。这个IconImage的用法非常有意思，他是把字符串翻译成图片。因为工具栏的图标很多，每一个都有对应的文件，我索性写了个类来管理。还是非常有意思的。
![图 8](https://i.loli.net/2021/04/22/17hOFXIPa5k9tb6.png)  

### 架构风格
我认为本程序突出体现了如下风格，当然，Java程序除了一点点native部分都有解释器风格，就不赘述了。
#### 仓库风格
我不知道能不能这样归类，但是我感觉很像。就是各个组件都在围绕这地图图像服务，同时各个组件的耦合很低，只是共享本地的图片和文档两个文件，并对其进行操作。
##### 优缺点分析
仓库风格的主要优点包括便于模块间的数据共享，方便模块的添加、更新和删除，避免了知识源的不必要的重复存储等。而仓库风格也有一些缺点，比如对于各个模块，需要一定的同步/加锁机制以保证数据结构的完整性和一致性等。
```
摘录来自
软件架构理论与实践 (架构师书库)
李必信 等
此材料受版权保护。
```
#### 面向对象风格
Java作为一种接近或者就是纯面向对象的语言（jvm内原始类型就是对象），本程序也有很多面向对象的特征。
* 高度抽象性
* 封装：各个组件都隐藏了实现细节，仅仅对外提供方法。
* 继承：一些拓展功能的类体现了此特性，比如对Button功能拓展构建的ToolButton类
##### 优缺点分析
优点包括：  
①因为对象隐藏了其实现细节，所以可以在不影响其他对象的情况下改变对象的实现，不仅使得对象的使用变得简单、方便，而且具有很高的安全性和可靠性；  
②设计者可将一些数据存取操作的问题分解成一些交互的代理程序的集合。  
缺点是当一个对象和其他对象通过过程调用等方式进行交互时，必须知道其他对象的标识。无论何时改变对象的标识，都必须修改所有显式调用它的其他对象，并消除由此带来的一些副作用。与此相反的是管道–过滤器系统，其中过滤器之间进行交互时不需要知道系统中其他过滤器的存在。  
```
摘录来自
软件架构理论与实践 (架构师书库)
李必信 等
此材料受版权保护。
```

## 在github上获取代码并阅读readme
[访问网站](https://github.com/uiharuayako/MapErrorCorrect)  
或者
```
git clone https://github.com/uiharuayako/MapErrorCorrect.git
```