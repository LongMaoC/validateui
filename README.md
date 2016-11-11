# validateUI
## 一个表单验证的lib

# **功能**
* 验证非空
* 多次确认密码
* 正则验证
* 索引顺序验证
* 最大、最小长度验证


# 特点
* 简单！简单！简单！
* 按照顺序验证
* 验证失败可自定义动画
* 以注解的方式进行配置
* 可以自定义实现提示方式

## 使用
onCreate中注册
```
Validate.check(MainActivity.this, MainActivity.this);
```

implements IValidateResult
```
    @Override
    public void onValidateSuccess() {
        Toast.makeText(this, "验证成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidateError(EditText editText, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //需要让输入错误的edittext实现动画，需要重新这个方法，并返回一个Animation
    //没有需要则返回null
    @Override
    public Animation onValidateErrorAnno() {
        return ValidateAnimation.horizontalTranslate();
    }
```

onDestroy解注册
```
 Validate.unreg(this);
```

以下请参照app->MainActivity.java . 更用以理解

| 注解 |说明|方法|
|:--:|:--|:--|
|@Index|索引 |value: 索引标记，用来标记验证的先后顺序，必须有这个注解|
|@NotNull|非空验证|msg: 提示信息|
|@Repeat|分组<br>Repeat: 多个edittext关联时，非最后一个|flag: 标记组，当一个界面的两个或多个edittext需要关联验证时，可以设置flag分组，flag值相同为一组|
|@RepeatLast|分组<br>多个edittext关联时，最后一个|msg: 提示信息<br>flag:标记|
|@RE| 正则验证|msg: 提示信息<br>re:正则表达式|
|@MinLength|最低长度|msg: 提示信息<br>length:最低长度(包含)|
|@MaxLength|最大长度|msg: 提示信息<br>length:最大长度(包含)|
