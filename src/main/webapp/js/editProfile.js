$(document).ready(function () {

    $.ajax({
        type: "GET",
        url: "/ripperonipizza-1.02/rest/whoami",
        dataType: "json",
        contentType: "application/json",
        success: function(data) {
            username = data[0].username;
            first = data[0].firstname;
            last = data[0].lastname;
            $("#userAccount").append("&nbsp;" + username);
            $("#newInputUsername").val(username);
            $("#newInputFirst").val(first);
            $("#newInputLast").val(last);
            var dashpath = findDashboardType(data[0].role);
            $("#dashboardType").attr("href", dashpath);
            $("#dashredirect").attr("href", dashpath);
        },
        error: function() {
            alert("SOMETHING WRONG!");
        }
    });

    $.ajax({
        type: "GET",
        url: "/ripperonipizza-1.02/rest/edit",
        dataType: "json",
        contentType: "application/json",
        success: function(data) {
            email = data[0].email;
            address = data[0].address;
            telephone = data[0].telephone;
            $("#newInputEmail").val(email);
            $("#newInputAddress").val(address);
            $("#newInputPhoneNumber").val(telephone);
        },
        error: function() {
            alert("SOMETHING WRONG!");
        }
    });

});


// This defines what happens when the user tries to submit the data
$(document).on("click", "#editbutton", function() {
    var passwordRegExp = /^(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/;

    if(passwordRegExp.test($("#newInputPassword").val())){
        $.ajax({
            type: "POST",
            url: "/ripperonipizza-1.02/rest/edit/",
            data: JSON.stringify({
                "username": username,
                "firstname": $("#newInputFirst").val(),
                "lastname": $("#newInputLast").val(),
                "password": $("#newInputPassword").val(),
                "email" :  $("#newInputEmail").val(),
                "address" : $("#newInputAddress").val(),
                "telephone" : $("#newInputPhoneNumber").val()
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function() {
                alert("Updated user profile");
            },
            error: function() {
                alert("SOMETHING WRONG!");
            }
        });
    } else {
        alert("Password should have at Least 1 upper case letter, 1 lower case letter, 1 number or special character and 8 characters in length");
    }

})


$(document).on("click", "#logout", function() {
        // since the user wants to log out, we alert the webserver to invalidate the session
        $.ajax({
            type: "POST",
            url: `/ripperonipizza-1.02/rest/logout/${username}`,
            data: JSON.stringify({
                "username": username,
                "first": first,
                "last": last
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function() {
                document.location.href = "/ripperonipizza-1.02";
            },
            error: function() {
                alert("SOMETHING WRONG!");
            }
        });
});
