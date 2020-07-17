// initialize the total cost of the order
var total = 0;

var idcoupon = "none";

var coupon_discount = 0;

var username = "";

// initialize array of objects containing all pizza price and quantity
var TableData = [];

// number of distinct pizzas in the cart order
var number_distinct_pizzas = 0;

// set the maximum quantity possible to order for a distinct pizza
var MAX_QUANTITY_DISTINCT_PIZZA = 100;

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
            initPizzaCatalogue();
        }
    }) 

})


function initPizzaCatalogue(){

    // at the loading of the document build the html table
    // using JSON-encoded data from a server using a HTTP GET request
    $.getJSON("http://localhost:8080/ripperonipizza-1.02/rest/pizza", function(data) {
        $.each(data, function(index, element) {

            var ingredient = element.ingredients.replace(/,/g,', ').slice(1,-1);

            var tblRow = "<tr>"+
                "<td class='pizza-name'>"+element.name+"</td>"+
                "<td class='ingredients'>"+ingredient+"</td>"+
                "<td class='user'>"+element.username+"</td>"+
                "<td class='score'>"+element.success+"</td>"+
                "<td class='price'><span>"+element.price+"</span>&euro;</td>"+
                "</tr>";
            $(tblRow).appendTo("#pizza-catalogue tbody");
        });
    })

    // since the elements of the table are created after an ajax call the function
    // becomes like this
    $(document).on("click", "#pizza-catalogue>tbody>tr", function(){

        // find the text of the td corresponding to the class "price" in the same row using jQuery
        var text_price = $(this).closest("tr").find(".price > span").text();

        // extract the numeric value as float variable and find the new price after removing the pizza
        var pizza_price = parseFloat(text_price);

        var pizza_name = $(this).closest("tr").find("td.pizza-name").text();

        var index = findIndexFromPizzaNameOrder(pizza_name);

        if(index == -1){  // new pizza

            // clone the selected row
            var newTr = $(this).closest("tr").clone();

            // create the new html tag for input buttons
            // var newButtonsHTML = "<input type='button' class='edit-pizza btn btn-warning btn-sm btn-space' value='Edit'/><input type='button' class='delete-pizza btn btn-warning btn-sm' value='Delete'/>";
            var newButtonsHTML = "<input type='button' class='delete-pizza btn btn-warning btn-sm' value='Delete'/>";

            // append the new td element with buttons
            newTr.append( $("<td>").html("").html(newButtonsHTML));

            // remove user and score from cart order, we just dont care at this poit about this two fields
            newTr.find(".user").remove();
            newTr.find(".score").remove();

            // create the html tag for the quantity
            var newQuantityHTML = "<select class='quantity'></select>";
            // append the new td element with quantity selector
            newTr.append( $("<td>").html("").html(newQuantityHTML));

            // append the new tds to the table with class pizza-order
            newTr.appendTo($("#pizza-order"));

            // build the 100 quantity choice for the selector with class quantity
            var $select = $(".quantity");
            for (i=1;i<=100;i++){
                $select.append($("<option></option>").val(i).html(i))
            }

            // increment the distinct pizza counter
            number_distinct_pizzas += 1;

            // after adding a new row in the order table i refresh the event listeners
            RefreshEventsListener();
        }

        else{  // the pizza already exists in the cart order (increase the quantity)

            var quantity = TableData[index].quantity;

            if(quantity < MAX_QUANTITY_DISTINCT_PIZZA){
                quantity += 1;

                // loop over all rows
                $("#pizza-order > tbody  > tr").each(function() {
                    $this = $(this);

                    // when it finds the corresponding pizza name
                    if( pizza_name == $this.find("td.pizza-name").text()){

                        //change his selector to the new value quantity (old quantity+1)
                        $this.find("select.quantity").val(quantity);
                        return;
                    } ;
                })
            } else{ // you can't order more pizza then MAX_QUANTITY_DISTINCT_PIZZA

                // for now an alert, can be made with a modal
                alert("You cant order more than "+MAX_QUANTITY_DISTINCT_PIZZA+" pizza of the same type");
                return;
            }
        }

        // find the new total price and visualize
        total += pizza_price;

        displayPrice();

        updateTable();
    });

    // search elements in the catalogue table
    $("#tableSearch").on("keyup", function() {
        var value = $(this).val().toLowerCase();
        $("#pizza-catalogue tr").filter(function() {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
    });
}



// when i add a new row dynamically in the order table i refresh all the events listeners involving them
function RefreshEventsListener() {
    // Remove handler from existing elements
    $(".edit-pizza").off();
    $(".delete-pizza").off();
    $(".quantity").off();

    // Re-add event handler for all matching elements - edit-pizza class buttons
    $(".edit-pizza").on("click", function() {
        edit(this);
    })

    // Re-add event handler for all matching elements - delete-pizza class buttons
    $(".delete-pizza").on("click", function() {
        deleteRow(this);
    })

    // Re-add event handler for all matching elements - quantity class selector
    $(".quantity").on("click", function() {
        changeQuantity(this);
    })
}


// remove one pizza item from the pizza-order table when pressed the input button delete
function deleteRow(button) {
    // extract the table row index corresponding to the input (button) element
    var i = button.parentNode.parentNode.rowIndex;
    document.getElementById("pizza-order").deleteRow(i);

    // find the text of the td corresponding to the class "price" in the same row using jQuery
    var text_price = $(button).closest("tr").find(".price > span").text();
    // extract the numeric value as float variable and find the new price after removing the pizza
    var pizza_price = parseFloat(text_price);

    // find the quantity of the pizza that we want to remove
    var pizza_quantity = $(button).closest("tr").find("select.quantity").val();

    // find the new total cost after removing multiple pizza copies
    total -= pizza_price*pizza_quantity;

    // display the result using jQuery
    displayPrice();

    updateTable();

    number_distinct_pizzas -= 1;
}


 function changeQuantity(selector){

    // find the text of the td corresponding to the class "price" in the same row using jQuery
    var text_price = $(selector).closest("tr").find(".price > span").text();
    // extract the numeric value as float variable and find the new price
    var pizza_price = parseFloat(text_price);

    // find th row index to be changed the quantity
    var i = selector.parentNode.parentNode.rowIndex;

    // alert("row index: "+1 +" quantity: "+TableData[i].quantity);
    var selected_pizza_quantity = TableData[i].quantity;

    var new_price = total - pizza_price*selected_pizza_quantity;

    var quantity = $(selector).closest("tr").find(".quantity").val();

    total = new_price + pizza_price*quantity;

    // display the result using jQuery
    displayPrice();

    updateTable();
 }


 // preserve data about the order table
 function updateTable(){

    var i = 0;

    var anyrows = 0
    // for each row of the pizza-order table exctract price of the pizza and quantity
    $("#pizza-order > tbody  > tr").each(function(i) {
        $this = $(this);

        var value = parseFloat($this.find("td.price > span").text());
        var quantity = parseInt($this.find("select.quantity").val());
        var pizza_name = $this.find("td.pizza-name").text();

        TableData[i]={
            name: pizza_name,
            value: value,
            quantity: quantity
        };
        anyrows = 1;
    });

    // force price to zero if there are no pizzas
    if(anyrows == 0){
        total = 0;
        $("#total-cost").text(total);

    }
 }


function findIndexFromPizzaNameOrder(name){

    for(i=0; i<number_distinct_pizzas; i++){

        if (TableData[i].name == name){
            return i;
        }

    }
    return -1;
}

function displayPrice(){
    $("#total-cost").text(total - total*coupon_discount);
}

// // edit one pizza ingredients before buying
// function edit(button) {
//     ingredients_string = $(button).closest("tr").find(".ingredients").text();

//     if(ingredients_string){ // if the string in not empty

//         // extract each ingredient of this pizza, i recall that our ingredients are divided by the ", "  (virgola and a space!)
//         var ingredients = ingredients_string.split(', ');
//     }
//     else{
//         // no ingredients selected, empty array
//         var ingredients = [];
//     }

//     // find how many ingredients are in this specific pizza
//     var num_ingredients = ingredients.length;

//     // Get the <span> element that closes the modal
//     var span = document.getElementsByClassName("close")[0];

//     // Get the modal
//     var modal = document.getElementById("myModal");

//     // display the modal empy
//     modal.classList.remove("hide-modal");
//     modal.classList.add("display-modal");

//     // load the table in the modal throght ingredients.json
//     $.getJSON("../data/ingredients.json", function(data) {

//         // variable used only for creating serial id for ingredients  (useful for later..)
//         var i = 0;

//         // loop over all elements in the json
//         $.each(data, function(index, element) {

//             // for each ingredient check if it is present in our selected pizza (loop over pizza ingredients)
//             var foundIngredient = 0;

//             // make the name of the ingredient without spaces (example mozzarella di bufala -> mozzarelladibufala)
//             // to be used for defining the price of a pizza by its ingredients
//             var id = element.ingredient.replace(/\s/g, '');

//             //loop over ingredients inside the selected pizza
//             for(j=0; j<num_ingredients; j++){

//                 // if the selected ingredient from ingredients.json is one of our selected pizza ingredients set "cheched" his checkbox
//                 if(element.ingredient == ingredients[j]){
//                     var td_checkbox =  `<td> <input type='checkbox' checked id='id_${id}' class='checkbox'></td>`;
//                     foundIngredient = 1;
//                 }
//             }
//             // if the instance of the ingredient from ingredients.json is not found in the ingredients of the pizza set the checkbox empty
//             if(foundIngredient == 0){
//                 var td_checkbox =  `<td> <input type='checkbox' id='id_${id}' class='checkbox'> </td>`;
//             }

//             // build the table row for the current ingredient from ingredients.json
//             var tblRow = "<tr>"+
//                             td_checkbox +
//                             "<td class='ingredient_name'>"+element.ingredient+"</td>"+
//                             "<td class='ingredient_cost'>"+element.additional_cost+"</td>"+
//                         "</tr>";

//             // append the row to the table containing all the ingredients
//             $(tblRow).appendTo("#ingredients-catalogue tbody");
//             i += 1;
//         });

//         var submitRow = "<tr>"+
//                             "<td><input type='submit' value='Submit'></input></td>"+
//                             "<td>Total</td>"+
//                             "<td id='ingredient_total_cost' class='ingredient_total_cost'>0</td>"+
//                         "</tr>";


//         $(submitRow).appendTo("#ingredients-catalogue tbody");
//     })

//     // When the user clicks on <span> (x), close the modal
//     span.onclick = function() {
//         $("#ingredients-catalogue tbody").empty();
//         modal.classList.remove("display-modal");
//         modal.classList.add("hide-modal");
//     }

//     // When the user clicks anywhere outside of the modal, close it
//     window.onclick = function(event) {
//         if (event.target == modal) {
//             $("#ingredients-catalogue tbody").empty();
//             modal.classList.remove("display-modal");
//             modal.classList.add("hide-modal");
//         }
//     }

//     // find the price of the selected ingredients when the mouse is over the window, so basically after the table is completely loaded
//     $(document).on("mouseenter", "#ingredients-catalogue", function(){

//         var cumulativePrice = defineCustomPizzaPriceFromSelectedIngredients(ingredients);
//             // display the new price
//         $("#ingredient_total_cost").text(cumulativePrice);

//     });

//     var has_checked = false;

//     // when a checkbox is checked, while editing a pizza ingredients
//     $(document).on("change", "input[type='checkbox']", function(){

//         // find the name of the checked ingredient
//         var checked_ingredient = $(this).closest("tr").find(".ingredient_name").text();

//         // see if it is already present in the current pizza
//         var isAlreadyUsed = ingredients.includes(checked_ingredient);

//         if( isAlreadyUsed ){
//             // if already present remove it from the array of all ingredient fot that pizza
//             ingredients.splice( ingredients.indexOf(checked_ingredient), 1 );
//         }
//         else{
//             // if its new add it to the array containing all ingredients for that pizza
//             ingredients = ingredients.concat(checked_ingredient);
//         }

//         var cumulativePrice = defineCustomPizzaPriceFromSelectedIngredients(ingredients);
//             // display the new price
//         $("#ingredient_total_cost").text(cumulativePrice);

//         // i edited the ingredients
//         has_checked = true;
//     });

//     // after you have edited the pizza, when you press the submit button
//     $(document).on("click", "input[type='submit']", function(event){
//         event.preventDefault();

//         var pricedummy = defineCustomPizzaPriceFromSelectedIngredients(ingredients);
//         $("#ingredients-catalogue tbody").empty();
//         modal.classList.remove("display-modal");
//         modal.classList.add("hide-modal");

//         var original_string_ingredient = $(button).closest("tr").find("td.ingredients").text();
//         var original_ingredients = original_string_ingredient.split(', ');

//         var copy_ingredients = ingredients.slice();

//         // ingredients are the edited ingredients
//         if( has_checked==false && (copy_ingredients.sort().join(',')=== original_ingredients.sort().join(','))){ // if no editing is done for this pizza editing

//             alert('not modified');
//             // remove event handlers for this edit button, the events will start at the next click on an edit button
//             $(document).off("click", "input[type='submit']");
//             $(document).off("change", "input[type='checkbox']");

//         }
//         else{

//             // display comma and space separated ingredients
//             var ingredient_string = copy_ingredients.toString();
//             ingredient_string = ingredient_string.replace(/,/g,', ');

//             var price_before_editing = parseFloat($(button).closest("tr").find("td.price > span").text());
//             var quantity = parseInt($(button).closest("tr").find("select.quantity").val());

//             $(button).closest("tr").find("td.ingredients").text(ingredient_string);
//             $(button).closest("tr").find("td.pizza-name").text("Edited");
//             $(button).closest("tr").find("td.price > span").text(pricedummy);

//             total -= price_before_editing*quantity;
//             total += pricedummy*quantity;

//             // display the result using jQuery
//             $("#total-cost").text(total);

//             updateTable();

//             // remove event handlers for this edit button, the events will start at the next click on an edit button
//             $(document).off("click", "input[type='submit']");
//             $(document).off("change", "input[type='checkbox']");
//         }

//         // it is checkable again
//         has_checked = false;

//     })
// }


// // it takes in input an array of strings (where there are all ingredients we want in the pizza)
// // and provide in output a number
// function defineCustomPizzaPriceFromSelectedIngredients(ingredients){
// // I CHOOSED THE FOLLOWING RULES TO BUILD THE PIZZA PRICES
// // minimum price without bot pomodoro and mozzarella  - 3.5

//     var cumulativePrice = 3.5;
//     for(i=0; i<ingredients.length; i++){

//         // to find the id of the ingredient i use the rule i defined before, concatenate "id_" with its name without spaces
//         var id_ingredient = "id_" + ingredients[i].replace(/\s/g, '');

//         cumulativePrice += parseFloat( $("#"+id_ingredient).closest("tr").find("td.ingredient_cost").text() );

//     }

//     return cumulativePrice;
// }

$(document).on("click", "#coupon-submit" , function(){
  var codiceCoupon =   $("#coupon").val();
  var coupon = { idcoupon: codiceCoupon, username: username};

  // server update
  $.ajax({
      type: "POST",
      url: "/ripperonipizza-1.02/rest/coupon",  //need to be implemented method HTTP POST INTO REST
      dataType: "json",
      data: JSON.stringify(coupon),
      contentType: "application/json",
      success: function(data) {
                                if(data[0].order == 0)
                                {
                                  //alert("You have inserted this unused coupon! " + JSON.stringify(data));
                                  $("#discount").text(data[0].percentage + "% discount");
                                  idcoupon = data[0].id;
                                  coupon_discount= data[0].percentage/100;
                                  displayPrice()
                                }
                                else
                                {
                                  //alert("You have inserted this used coupon! " + JSON.stringify(data));
                                  $("#discount").text("You have inserted an used coupon!");
                                  idcoupon = "none";
                                }

                              },
      error: function() {
                                $("#discount").text("You have inserted a wrong coupon!");
                                idcoupon = "none";
                              },
      dataType: "json"
    });
})

$(document).on("click",  "#send-order", function(){


    // var idorder = $(this).closest("tr").find(".idorder").text();

    var pizza_list = [];
    var quantity = [];

    $("#pizza-order > tbody > tr").each( function(i, element) {


        pizza_list.push($(this).find("td.pizza-name").text());

        quantity.push($(this).find("td > select.quantity").val());
    })


    if (pizza_list.length == 0)
    {
      alert("You cannot place an order with zero pizzas!!");
    }
    else
    {

      var order = { username: username, pizzas:pizza_list, quantities: quantity, coupon: idcoupon};
    // server update
      $.ajax({
          type: "POST",
          url: "/ripperonipizza-1.02/rest/order/",
          dataType: "json",
          data: JSON.stringify(order),
          contentType: "application/json",
          success: function() {$('#orderConfirmModal').modal('show'); addModalEvent(); },
          error: function() {alert("Some error occurs! Contact us immediately!!")},
          dataType: "json"
        });
    }
})
function addModalEvent(){
    $(document).click(function () {
        location.assign("./dashboard.html");
    })
}


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
