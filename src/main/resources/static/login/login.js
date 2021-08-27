$(document)
    .ready(function() {
        $('.ui.form')
            .form({
                fields: {
                    email: {
                        identifier  : 'email',
                        rules: [
                            {
                                type   : 'empty',
                                prompt : '请输入邮箱'
                            },
                            {
                                type   : 'email',
                                prompt : '请输入有效邮箱'
                            }
                        ]
                    },
                    password: {
                        identifier  : 'password',
                        rules: [
                            {
                                type   : 'empty',
                                prompt : '请输入密码'
                            },
                            {
                                type   : 'length[6]',
                                prompt : '请输入大于6位密码'
                            }
                        ]
                    }
                }
            })
        ;
    })
;

//登录账号
$("#login").on("click", function(){  //点击login触发行为
    $.ajax({
        url:"/user/login",
        type:"POST",
        data:{
            email: $("#email").val(), //获取email
            password: $("#password").val()
        },
        resultType:"JSON",
        success:function (result){

            alert(result.nickname + "登录成功") //返回登录成功提示框,message为UserService中的resultMap
            if(200 === result.code){     //注册成功跳转
                window.open("chat","_self");

            }
        },
        error:function (result){}

    })
});

