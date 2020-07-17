// Here we are going to validate all the form. If all the text are valid then the Go!
// button will send the request to the backend
var form = document.getElementsByTagName("form")[1];
var formLogIn = document.getElementsByTagName("form")[0];

// Get each element of the Sign In form
var first = document.getElementById("newInputFirst");
var last = document.getElementById("newInputLast");
var username = document.getElementById("newInputUsername");
var email = document.getElementById("newInputEmail");
var phone = document.getElementById("newInputPhoneNumber");
var password = document.getElementById("newInputPassword");
var address = document.getElementById("newInputAddress");

// Get each element of the Log in form
var usernamEmail = document.getElementById("inputEmail");
var passwordLog = document.getElementById("inputPassword");

// Regular expression to check whether the input is an email address
var emailRegExp = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
var namesRegExp = /^[a-zA-Z]+(([',. -][a-zA-Z])?[a-zA-Z]*)*$/;
var phoneRegExp = /^[0-9]*$/;
var passwordRegExp = /^(?=^.{8,}$)((?=.*\d)|(?=.*\W+))(?![.\n])(?=.*[A-Z])(?=.*[a-z]).*$/;
// Explanation of RegExp : At Least 1 upper case letter, 1 lower case letter,1 number or special character
//                         8 characters in length



// This defines what happens when the user tries to submit the data
form.addEventListener("submit", function (event) {
    // Now using the Go! button, I check all the element of the form
    var counter = 0; // # of incorrect fields

    var error = first.nextElementSibling; // error message
    var test = first.value.length > 0 && namesRegExp.test(first.value);
    if (test) {
        first.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        first.className = "form-control";
        error.innerHTML = "Please, insert a correct First Name!";
        counter++;
    }

    error = last.nextElementSibling;
    test = last.value.length > 0 && namesRegExp.test(last.value);
    if (test) {
        last.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        last.className = "form-control";
        error.innerHTML = "Please, insert a correct Last Name";
        counter++;
    }

    error = username.nextElementSibling;
    test = username.value.length > 0;
    if (test) {
        username.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        username.className = "form-control";
        error.innerHTML = "You must insert an Username!";
        counter++;
    }

    error = address.nextElementSibling;
    test = address.value.length > 0;
    if (test) {
        address.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        address.className = "form-control";
        error.innerHTML = "You must insert an Username!";
        counter++;
    }

    error = email.nextElementSibling;
    test = email.value.length > 0 && emailRegExp.test(email.value);
    if (test) {
        email.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        email.className = "form-control";
        error.innerHTML = "I expect an e-mail!";
        counter++;
    }

    error = phone.nextElementSibling;
    test = phone.value.length > 0 && phoneRegExp.test(phone.value);
    if (test) {
        phone.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        phone.className = "form-control";
        error.innerHTML = "I expect an Phone Number!";
        counter++;
    }

    error = password.nextElementSibling;
    test = password.value.length > 0 && passwordRegExp.test(password.value);
    if (test) {
        password.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        password.className = "form-control";
        error.innerHTML = "Password should contain 8 characters where at least must be 1 upper case letter, 1 lower case letter, 1 number or special character";
        counter++;
    }

    // Something wrong? I send nothing!
    if (counter != 0)
        event.preventDefault();

});

formLogIn.addEventListener("submit", function (event) {
    var flag = true;

    error = usernamEmail.nextElementSibling;
    test = usernamEmail.value.length > 0;
    if (test) {
        usernamEmail.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        usernamEmail.className = "form-control";
        error.innerHTML = "You must insert an Username or an Email!";
        flag = false;
    }

    error = passwordLog.nextElementSibling;
    test = passwordLog.value.length > 0;
    if (test) {
        passwordLog.className = "form-control is-valid text-center";
        error.innerHTML = "";
    } else {
        passwordLog.className = "form-control";
        error.innerHTML = "You must insert a password to log in!";
        flag = false;
    }

    if (flag == false)
        event.preventDefault();


});
