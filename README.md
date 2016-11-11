# validateUI
## 一个表单验证的lib

# **功能**
* 验证非空
* 多次确认密码
* 正则验证

# 特点
* 简单！简单！简单！
* 验证失败可自定义动画
* 以注解的方式进行配置
* 可以自定义实现提示方式

## 使用
onCreate中注册
```
 Validate.reg(this);
```

实现IValidateResult接口，并重写三个方法
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

| 注解 |例子|备注|
|:--:|:--|:--|
|@NotNull<br>非空验证|@NotNull(msg = "不能为空！")<br>EditText etNotnull;|msg: 提示信息|
|@Repeat<br>@RepeatLast<br>分组|@NotNull(msg = "两次密码验证->密码一不为能空！")<br>@Repeat(flag = "AA")<br>private EditText etPw1;<br><br>@NotNull(msg = "两次密码验证->密码二不为能空！")<br>@RepeatLast(flag = "AA", msg = "两次密码不一致！！！")<br>private EditText etPw2;|msg: 提示信息<br>flag: 标记组，当一个界面的两个或多个edittext需要关联时，可以设置flag分组，相同为一组<br>Repeat: 多个edittext关联时，非最后一个<br>Repeat: 多个edittext关联时，最后一个，需要配置两次文本不同时的提示信息|
|@RE<br>正则验证| @NotNull(msg = "请填写邮箱")<br>@RE(re="\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}"msg = "格式错误！")<br>private EditText etRe;|re:正则表达式|
