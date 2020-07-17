// after the document is loaded
$(document).ready(function() {
    
    // add event listener to the cookiebar element
    $("#cb").on("click", function() {

        // close the cookie bar "popup"
        $(this).remove();
    })
})

// /* Cookie bar function */
// function cbclose() {
//     document.getElementById("cb").style.display = "none";
// }