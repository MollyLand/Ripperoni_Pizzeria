var number_queued_pizzas = 0;

// dummy name for now, it has to be retrievied
var username = "";
var first = "";
var last = "";

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
      $("#userAccount").append("&nbsp;"+username);
      $("#FirstLast").append(first+" "+last);

      $.getJSON(`/ripperonipizza-1.02/rest/CookQueue/username/${username}`, function (data) {
          $.each(data, function (index, element) {
              var ingredients = element.ingredients.replace(/,/g,', ').slice(1,-1);
              var tblRow = "<tr>" +
                  "<td class ='idorder' >" + element.idorder + "</td>" +
                  "<td>" + element.pizzaname + "</td>" +
                  "<td>"+ingredients+"</td>" +
                  "<td><input type='checkbox' class='checkOrder'> Done </td>" +
                  "</tr>";
              $(tblRow).appendTo("#incoming-pizza-table tbody");
          });
      })
      .done(function(){
          update_queued_pizzas();
      })

      }
  });


  /* Write First & Last Name of the user
  $.getJSON(`/ripperonipizza-1.02/rest/whoami`, function (data) {
      $.each(data, function (index, element) {
          username = element.user;
          var first = element.firstname;
          var last = element.lastname;

      });
  }); */





})


$(document).on("click", "input[type='checkbox']", function(){

    var idorder = $(this).closest("tr").find(".idorder").text();

    $(this).closest("tr").remove();
    number_queued_pizzas -= 1;
    $("#pizza-in-queue").text(number_queued_pizzas);


    var numPizzaInTheOrder = 0;
    $("#incoming-pizza-table > tbody > tr > td.idorder").each( function() {
        if( idorder == parseInt($(this).text()) ){
            numPizzaInTheOrder += 1;
        }
    })

    // if the selected pizza is the last of the order update the order status to delivering
    if( numPizzaInTheOrder == 0){

        // server update
        $.ajax({
            type: "POST",
            url: `/ripperonipizza-1.02/rest/CookQueue/username/${username}`,  //can be used also `/ripperonipizza-1.02/rest/order/id/${idorder}`
            data: JSON.stringify({
                "idorder": idorder,
                "username": username,
                "status": "Delivering"
            }),
            contentType: "application/json; charset=utf-8",
            success: function() {
                alert("Updated order #"+idorder+" status, now is in delivering" );
            },
            error: function() {
                alert("Some error occurs! Contact us immediately!!");
            },
            dataType: "json",
        });
    }
})

function update_queued_pizzas(){
    $("#incoming-pizza-table > tbody  > tr").each(function() {
        number_queued_pizzas += 1;
    });
    $("#pizza-in-queue").text(number_queued_pizzas);
}

$(document).on("click", "#logout" , function() {
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
    success: function(){
      document.location.href="/ripperonipizza-1.02";
    },
    error: function(){
      alert("SOMETHING WRONG!");
    }
  });
}

)
