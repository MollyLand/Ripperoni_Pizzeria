// array of all selected ingredients
var list_selected_ingredients = [];

var username;

// initial price is 3.5 euro
var total_cost_selected_ingredients = 3.5;
$("#total-price-ingredients").text(total_cost_selected_ingredients+" \u20AC ");

// after the document is loaded
$(document).ready(function() {

    $.ajax({
        type: "GET",
        url: "/ripperonipizza-1.02/rest/whoami",
        dataType: "json",
        contentType: "application/json",
        success: function(data) {
            username = data[0].username;
            $("#userAccount").append("&nbsp;"+username);
            var dashpath = findDashboardType(data[0].role);
            $("#dashboardType").attr("href", dashpath);

            $.getJSON("http://localhost:8080/ripperonipizza-1.02/rest/ingredient", function(data) {
                $.each(data, function(index, element) {
                    var data = "<div class='col-md-3'>"+
                    "<div class='container-fluid'>"+
                    "<div class='media d-block text-center block-6 services ingredient-icon'>"+
                    "<div class='icon d-flex justify-content-center align-items-center mb-5'>"+
                    `<span class="${element.icon}"></span>`+
                    "</div>"+
                    "<div class='media-body'>"+
                    "<h3 class='heading'><span>"+"<span class='label-ingredient'>"+element.ingredientname+"</span> &euro; <span class='label-price'>"+element.ingredientprice+"</span></span></h3>"+
                    "</div>"+
                    "</div>"+
                    "</div>"+
                    "</div>"

                    var data_id = "#" + element.category + "-ingredients";
                    $(data).appendTo(data_id);
                });
            })

            // add event listeners to all icons added dynamically
            $(document).on("click", "div.ingredient-icon", function(){
                handle_ingredient(this);
            })
        }
    })
})

function handle_ingredient(icon){
    var ingredient_name = $(icon).find("div.media-body > h3 > span > span.label-ingredient").text();
    var ingredient_price = parseFloat($(icon).find("div.media-body > h3 > span > span.label-price").text());

    if(list_selected_ingredients.includes(ingredient_name)){
        remove_ingredient(ingredient_name, ingredient_price);
    }
    else{
        add_ingredient(ingredient_name, ingredient_price);
    }

    $("#chosen-ingredients").text(list_selected_ingredients);
    $("#total-price-ingredients").text(total_cost_selected_ingredients+" \u20AC ");

}

function add_ingredient(ingredient, price){
    list_selected_ingredients.push(ingredient);
    total_cost_selected_ingredients += price;
}

function remove_ingredient(ingredient, price){
    list_selected_ingredients.splice (list_selected_ingredients.indexOf(ingredient), 1);
    total_cost_selected_ingredients -= price;
}

var modal=document.getElementById("create-modal");
$("#done-button").on("click", function () {
    modal.classList.remove("hide-modal");
    modal.classList.add("display-modal");
})

window.onclick = function(event) {
    if (event.target == modal) {
        // modal.style.display = "none";
        modal.classList.remove("display-modal");
        modal.classList.add("hide-modal");
    }
}


    var pizzaName;
    var ingredient_list = list_selected_ingredients;
    var price = total_cost_selected_ingredients;    

$(document).on("click", "#done-button-pizzaname", function(){

    pizzaName = document.getElementById("name-pizza-text").value;
    var newPizza = {pizzaname: pizzaName, username: username, price:price, ingredients:ingredient_list};

    // server update
    $.ajax({
        type: "POST",
        url: "/ripperonipizza-1.02/rest/pizza/",  //see the POST case in RestManagerServlet.java
        dataType: "json",
        data: JSON.stringify(newPizza),
        contentType: "application/json",
        success: function(e) {
            if(e.message.message == "If you see this message, then everything is GOOOOOD!!"){

                alert("Thank you! You have created a new pizza! You will be redirected to the dashboard.");

                location.assign("./dashboard.html");

            }else{
                alert(e.message.message);
            }
            },
        error: function(e) {
            alert(e);
        },
        dataType: "json"
    });
})


$(document).on("click", "#logout", function() {
        // since the user wants to log out, we alert the webserver to invalidate the session
        $.ajax({
            type: "POST",
            url: `/ripperonipizza-1.02/rest/logout/${username}`,
            data: JSON.stringify({
                "username": username
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
    }

)
