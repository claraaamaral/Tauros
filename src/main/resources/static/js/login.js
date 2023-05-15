$('button[type="submit"]').click(function (event) {

    $.post("/login", {'user_name': $('#user_name').val(), 'password': $('#password').val()}, function () {

    }).fail(function () {

    });
});