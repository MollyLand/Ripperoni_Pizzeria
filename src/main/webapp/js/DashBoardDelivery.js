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
                    $("#FirstLast").append(first + " " + last);

                    $.getJSON(`/ripperonipizza-1.02/rest/DeliveryQueue/username/${username}`, function(data) {

                        var cont = 0;
                        var maxDelivery = 3;
                        var htmlContainer = "<div class='mt-3 container-fluid'>" // we can set here also number of max deliveries per row
                        htmlContainer += "<div class='mb-3 row'>";
                        $.each(data, function(index, element) {

                            htmlContainer += "<div class='col-md-" + (Math.floor(12 / (maxDelivery + 1))) + "'>" +
                                "<div class='container-fluid'>" +
                                "<div class='mb-2 card bg-warning'>" +
                                "<div class='card-body'>" +
                                "<h4 class='card-title'>" + element.customer_address + "</h4>" +
                                "<h3 class='card-title'>" + (element.delivery_time).substr(1,15) + "</h3>" +
                                "<h4 class='card-title text-uppercase'>" + element.customer_lastname + "</h3>" +
                                "<p class='card-text'>#Pizzas: " + element.number_pizzas + ", <span class='idorder'>" + element.id_order + "</span></p>" +
                                "<div class='custom-control custom-checkbox'>" +
                                "<input type='checkbox'>" +
                                "<label>Delivered</label>" +
                                "</div>" +
                                "</div>" +
                                "</div>" +
                                "</div>" +
                                "</div>";
                            cont++;
                            if (cont == maxDelivery) {
                                cont = 0;
                                // alert("Finita la riga");
                                htmlContainer += "</div>";
                                htmlContainer += "<div class='mb-4 row'>";
                            }
                        });
                        htmlContainer += "</div></div>"; // close the mt-4 container div and row
                        $(htmlContainer).appendTo("#deliver-cards");

                    });
                }

            });
        })

        // after delivering delete the delivery info by checking the checkbox
        $(document).on("click", "input[type='checkbox']", function() {

            var idorder = $(this).closest("div.container-fluid").find("div > div.card-body > p > span").text();
            // alert(idorder);  // to see if it gets the correct idorder

            $(this).closest("div.container-fluid").remove();

            $.ajax({
                type: "POST",
                url: "/ripperonipizza-1.02/rest/DeliveryQueue/username/" + username, // can be used also "/ripperonipizza-1.02/rest/order/id/${idorder}"
                data: JSON.stringify({
                    "idorder": idorder,
                    "username": username,
                    "status": "Delivered"
                }),
                contentType: "application/json; charset=utf-8",
                success: function() {
                    //alert("Updated order #" + idorder + " status, now is delivered!");
                },
                error: function() {
                    alert("Some error occurs! Contact us immediately!!");
                },
                dataType: "json",
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
