function findDashboardType(role){
    var dashpath = ""
    switch(role) {
        case "Customer":
            dashpath = "dashboard.html";
            break 
        case "Delivery Guy":
            dashpath = "deliver_dash.html";
            break 
        case "Cook":
            dashpath = "cook_dash.html";
            break 
        default:
            dashpath = "dashboard.html";
            break    
      }

    return dashpath;
}