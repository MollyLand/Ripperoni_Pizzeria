<!DOCTYPE html>
<html lang="en">
<head>

    <title>About Us - Ripperoni </title>

    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Josefin+Sans" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Nothing+You+Could+Do" rel="stylesheet">

    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/open-iconic-bootstrap.min.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="css/owl.theme.default.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/icomoon.css">
    <link rel="stylesheet" href="css/flaticon.css">
    <link rel="stylesheet" href="css/flaticon_about.css">
    <link rel="stylesheet" href="css/styleAbout.css">

    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>

      	<!-- ****** favicon ******-->
	<link rel="shortcut icon" href="/favicon.ico">
	<link rel="icon" sizes="16x16 32x32 64x64" href="media/favicon/favicon.ico">
	<link rel="icon" type="image/png" sizes="196x196" href="media/favicon/favicon-192.png">
	<link rel="icon" type="image/png" sizes="160x160" href="media/favicon/favicon-160.png">
	<link rel="icon" type="image/png" sizes="96x96" href="media/favicon/favicon-96.png">
	<link rel="icon" type="image/png" sizes="64x64" href="media/favicon/favicon-64.png">
	<link rel="icon" type="image/png" sizes="32x32" href="media/favicon/favicon-32.png">
	<link rel="icon" type="image/png" sizes="16x16" href="media/favicon/favicon-16.png">
	<link rel="apple-touch-icon" href="/favicon-57.png">
	<link rel="apple-touch-icon" sizes="114x114" href="media/favicon/favicon-114.png">
	<link rel="apple-touch-icon" sizes="72x72" href="media/favicon/favicon-72.png">
	<link rel="apple-touch-icon" sizes="144x144" href="media/favicon/favicon-144.png">
	<link rel="apple-touch-icon" sizes="60x60" href="media/favicon/favicon-60.png">
	<link rel="apple-touch-icon" sizes="120x120" href="media/favicon/favicon-120.png">
	<link rel="apple-touch-icon" sizes="76x76" href="media/favicon/favicon-76.png">
	<link rel="apple-touch-icon" sizes="152x152" href="media/favicon/favicon-152.png">
	<link rel="apple-touch-icon" sizes="180x180" href="media/favicon/favicon-180.png">
	<meta name="msapplication-TileColor" content="#FFFFFF">
	<meta name="msapplication-TileImage" content="media/favicon/favicon-144.png">
	<meta name="msapplication-config" content="media/favicon/browserconfig.xml">
	<!-- ****** favicon ****** -->

</head>


<body>

    <!-- Modal -->
    <div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-body ftco-animate">
                    <div role="tabpanel">
                        <h1 class="text-center">Ripperoni Pizzeria!</h1>
                        <!-- Nav tabs -->
                        <ul class="nav nav-tabs" role="tablist">
                            <li role="presentation" class="active"><a href="#uploadTab" aria-controls="uploadTab" role="tab" data-toggle="tab">Log In</a>

                            </li> &nbsp; | &nbsp;
                            <li role="presentation"><a href="#browseTab" aria-controls="browseTab" role="tab" data-toggle="tab">Sign Up</a>

                            </li>
                        </ul>
                        <!-- Tab panes -->
                        <div class="tab-content">
                            <div role="tabpanel" class="tab-pane active" id="uploadTab">
                                <br>
                                <form>
                                    <div class="form-group">
                                        <label for="inputEmail">Email address or Username</label>
                                        <input type="text" class="form-control" id="inputEmail">
                                        <small class="form-text text-muted"></small>
                                        <small class="form-text text-muted">We'll never share your email with anyone else.</small>
                                    </div>
                                    <div class="form-group">
                                        <label for="inputPassword">Password</label>
                                        <input type="password" class="form-control" id="inputPassword">
                                        <small class="form-text text-muted"></small>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                        <button type="submit" class="btn btn-primary save">Go!</button>
                                    </div>
                                </form>
                            </div>
                            <div role="tabpanel" class="tab-pane" id="browseTab">
                                <br>
                                <form class="needs-validation" novalidate>
                                <div class="form-group">
                                    <label for="newInputFirst">First Name</label>
                                    <input type="text" name="First" class="form-control" id="newInputFirst" aria-describedby="emailHelp">
                                    <small class="form-text text-muted"></small>
                                </div>
                                <div class="form-group">
                                    <label for="newInputLast">Last Name</label>
                                    <input type="text" name="Last" class="form-control" id="newInputLast">
                                    <small class="form-text text-muted"></small>
                                </div>
                                <div class="form-group">
                                    <label for="newInputUsername">Username</label>
                                    <input type="email" name="Username" class="form-control" id="newInputUsername" aria-describedby="emailHelp">
                                    <small class="form-text text-muted"></small>
                                </div>
                                <div class="form-group">
                                    <label for="newInputEmail">Email</label>
                                    <input type="email" name="Email" class="form-control" id="newInputEmail">
                                    <small class="form-text text-muted"></small>
                                    <small class="form-text text-muted">We'll never share your email with anyone else.</small>
                                </div>
                                <div class="form-group">
                                    <label for="newInputPhoneNumber">Phone Number</label>
                                    <input type="text" name="Phone" class="form-control" id="newInputPhoneNumber" aria-describedby="emailHelp">
                                    <small class="form-text text-muted"></small>
                                </div>
                                <div class="form-group">
                                    <label for="newInputAddress">Address</label>
                                    <input type="text" name="Address" class="form-control" id="newInputAddress" aria-describedby="emailHelp">
                                    <small class="form-text text-muted"></small>
                                </div>
                                <div class="form-group">
                                    <label for="newInputPassword">Password</label>
                                    <input type="password" name="Password" class="form-control" id="newInputPassword">
                                    <small class="form-text text-muted"></small>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary save">Go!</button>
                                </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>



    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light" id="ftco-navbar">
        <div class="container">
            <a class="navbar-brand" href="index.html"><span><img src="media/logoNew.png" height="40px"
                                                                 width="40px"></span>Pizza<br><small>Ripperoni</small></a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#ftco-nav"
                    aria-controls="ftco-nav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="oi oi-menu"></span> Menu
            </button>
            <div class="collapse navbar-collapse" id="ftco-nav">
<%--                <ul class="navbar-nav ml-auto">--%>
<%--                    <li class="nav-item"><a href="index.html" class="nav-link">Home</a></li>--%>
<%--                    <li class="nav-item"><a href="index.html#servicesSection" class="nav-link">Services</a></li>--%>
<%--                    <li class="nav-item active"><a href="#" class="nav-link">About</a></li>--%>
<%--                    <li class="nav-item"><a href="index.html#contactSection" class="nav-link">Contact</a></li>--%>
<%--                    <li class="nav-item"><a href="#" data-toggle="modal" data-target="#loginModal" class="nav-link">Sign In</a></li>--%>
<%--                </ul>--%>
            </div>
        </div>
    </nav>
    <section class="ftco-animate text-center mt-4 mb-4">
        <h2>${message}</h2>
            <a href="index.html" class="btn btn-white btn-outline-white p-3 px-xl-4 py-xl-3">Back to HomePage</a>
            <a href=${destinationPage} class="btn btn-white btn-outline-white p-3 px-xl-4 py-xl-3 ${login}">Go to Dashboard!</a>
            <br>
            <br>
            <br>
            <br>
    </section>






    <!-- Footer of the page -->
    <footer class="ftco-footer ftco-section img">
        <div class="overlay"></div>
        <div class="container">
            <div class="row mb-5 justify-content-center">
                <div class="col-lg-3 col-md-6 mb-6 mb-md-6">
                    <div class="ftco-footer-widget mb-4">
                        <h2 class="ftco-heading-2">About Us</h2>
                        <p>A team of engineers with years of experience in the pizza delivery field designed Ripperoni Pizza and its services. We do not use any kind of marsupial meat in our recipes.</p>
                        <ul class="ftco-footer-social list-unstyled float-md-left float-lft mt-5">
                            <li class="ftco-animate"><a href="https://www.twitter.com" target="_blank"><span
                                    class="icon-twitter"></span></a></li>
                            <li class="ftco-animate"><a href="https://www.facebook.com" target="_blank"><span
                                    class="icon-facebook"></span></a></li>
                            <li class="ftco-animate"><a href="https://www.instagram.com" target="_blank"><span
                                    class="icon-instagram"></span></a></li>
                        </ul>
                    </div>
                </div>

                <div class="col-lg-3 col-md-6 mb-6 mb-md-6">
                    <div class="ftco-footer-widget mb-4">
                        <h2 class="ftco-heading-2">Have a Questions?</h2>
                        <div class="block-23 mb-3">
                            <ul>
                                <li><span class="icon icon-map-marker"></span><span class="text">Via dei Nicolas Cage in a Cage, 69 ,
                    Orgiano (VI) 36043, ITALY</span></li>
                                <li><a href="#"><span class="icon icon-phone"></span><span class="text">(+39) 0444 - 473829 -
                      83847</span></a></li>
                                <li><a href="#"><span class="icon icon-envelope"></span><span
                                        class="text">staff@pizzeriarip.com</span></a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 text-center">

                    <p>
                        <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
                        Copyright &copy;
                        <script>document.write(new Date().getFullYear());</script> All rights reserved | Thanks to ColorLib by
                        RipperoniStaff
                    </p>
                </div>
            </div>
        </div>
    </footer>



    <!-- Cookie bar -->
    <div id="cb" class="cookie-bar">
        <span> We don't use &#x1F36A to track your experience. We prefer pizzas to cookies  </span>
        <button type="button" class="close-cb">x</button>
    </div>
    <!-- loader -->
    <div id="ftco-loader" class="show fullscreen"><svg class="circular" width="48px" height="48px">
        <circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee" />
        <circle cla ss="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10"
                stroke="#F96D00" /></svg></div>



    <script src="js/advco.js"></script>
    <script src="js/jquery.min.js"></script>
    <script src="js/jquery-migrate-3.0.1.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/jquery.easing.1.3.js"></script>
    <script src="js/jquery.waypoints.min.js"></script>
    <script src="js/jquery.stellar.min.js"></script>
    <script src="js/owl.carousel.min.js"></script>
    <script src="js/jquery.magnific-popup.min.js"></script>
    <script src="js/aos.js"></script>
    <script src="js/jquery.animateNumber.min.js"></script>
    <script src="js/bootstrap-datepicker.js"></script>
    <script src="js/jquery.timepicker.min.js"></script>
    <script src="js/scrollax.min.js"></script>
    <script src="js/google-map.js"></script>
    <script src="js/main.js"></script>
    <script src="js/validation.js"></script>
    <script src="js/cookiebar.js"></script>

</body>



</html>
