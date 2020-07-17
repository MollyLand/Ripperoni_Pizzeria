var today = new Date();
var dd = String(today.getDate()).padStart(2, '0');
var mm = String(today.getMonth() + 1).padStart(2, '0');
var yyyy = today.getFullYear();

// the db data has this format 2020-06-23
var today = yyyy + mm + dd;

//dumy username for now
var username = "";
var first = "";
var last = "";

$(document).ready(function() {

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
                        $("h1 #FirstLast").append(first + "<br>" + last);

                        $.getJSON(`/ripperonipizza-1.02/rest/pizza/username/${username}`, function(data) {
                            $.each(data, function(index, element) {
                                var ingredient = element.ingredients.replace(/,/g, ', ').slice(1, -1);
                                var tblRow = "<tr class='datarow'>" +
                                    "<td>" + element.name + "</td>" +
                                    "<td>" + element.price + "&nbsp; &euro;</td>" +
                                    "<td>" + element.success + "</td>" +
                                    "<td>" + ingredient + "</td></tr>";
                                $(tblRow).appendTo("#custom-catalogue tbody");
                            });
                        });

                        // Show the order status of your incoming order
                        $.getJSON(`/ripperonipizza-1.02/rest/order/username/${username}`, function(data) {
                            $.each(data, function(index, element) {
                                var tblRow = "<tr class='datarow'>" +
                                    "<td>" + element.idOrder + "</td>" +
                                    "<td>" + element.price + "&nbsp; &euro;</td>" +
                                    "<td>" + element.deliveryTime.split(/(\s+)/)[0] + "</td>" +
                                    // "<td>" + element.Pizzas + "</td>" +
                                    "</tr>";

                                if (element.orderStatus == "InCharge") {
                                    var progressBar = "<p>Id order: " + element.idOrder + ". <br> Estimated delivery time: " + (element.deliveryTime).substring(10, 16) +"</p>" +
                                        "<div class='container-fluid'>" +
                                        "<ol class='row progtrckr container-fluid' data-progtrckr-steps='4'>" +
                                        "<li class='progtrckr-done'>In Charge</li>" +
                                        "<li class='progtrckr-todo'>Baking</li>" +
                                        "<li class='progtrckr-todo'>Delivering</li>" +
                                        "<li class='progtrckr-todo'>Delivered</li>" +
                                        "</ol>" +
                                        "</div>" +
                                        "<br>";
                                }
                                if (element.orderStatus == "Baking") {
                                    var progressBar = "<p>Id order: " + element.idOrder + ". <br> Estimated delivery time: " + (element.deliveryTime).substring(10, 16) + "</p>" +
                                        "<div class='container-fluid'>" +
                                        "<ol class='row progtrckr'  data-progtrckr-steps='4'>" +
                                        "<li class='progtrckr-done'>In Charge</li>" +
                                        "<li class='progtrckr-done'>Baking</li>" +
                                        "<li class='progtrckr-todo'>Delivering</li>" +
                                        "<li class='progtrckr-todo'>Delivered</li>" +
                                        "</ol>" +
                                        "</div>" +
                                        "<br>";
                                }
                                if (element.orderStatus == "Delivering") {
                                    var progressBar = "<p>Id order: " + element.idOrder + ". <br> Estimated delivery time: " + (element.deliveryTime).substring(10, 16) + "</p>" +
                                        "<div class='container-fluid'>" +
                                        "<ol class='row progtrckr' data-progtrckr-steps='4'>" +
                                        "<li class='progtrckr-done'>In Charge</li>" +
                                        "<li class='progtrckr-done'>Baking</li>" +
                                        "<li class='progtrckr-done'>Delivering</li>" +
                                        "<li class='progtrckr-todo'>Delivered</li>" +
                                        "</ol>" +
                                        "</div>" +
                                        "<br>";
                                }
                                // show also the today's already delivered orders
                                if (element.orderStatus == "Delivered" && element.deliveryTime.split(/(\s+)/)[0] == today) {
                                    var progressBar = "<p>Id order: " + element.idOrder + ". <br> Estimated delivery time: " + (element.deliveryTime).substring(10, 16) + "</p>" +
                                        "<div class='container-fluid'>" +
                                        "<ol class='row progtrckr' data-progtrckr-steps='4'>" +
                                        "<li class='progtrckr-done'>In Charge</li>" +
                                        "<li class='progtrckr-done'>Baking</li>" +
                                        "<li class='progtrckr-done'>Delivering</li>" +
                                        "<li class='progtrckr-done'>Delivered</li>" +
                                        "</ol>" +
                                        "</div>" +
                                        "<br>";
                                }
                                $(progressBar).appendTo("#pending-orders");

                                $(tblRow).appendTo("#order-catalogue tbody");
                            });
                        });


                        $.getJSON(`/ripperonipizza-1.02/rest/coupon/username/${username}`, function(data) {

                            $.each(data, function(index, element) {

                                if (parseInt(element.order) == 0) {
                                    $("#couponDisplay").remove();
                                    var couponinfo = " <div class='media-body'>" +
                                        "<h3 class='heading'>ID: " + element.id + "</h3>" +
                                        "<p>" + element.percentage + "% discount</p>" +
                                        "</div>";

                                    $(couponinfo).appendTo("#coupon-info > div > div");

                                }

                            });

                        });

                    }
                });
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
            }

        )
