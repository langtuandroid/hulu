<?php echo $this->doctype();?>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<?php echo $this->headTitle();?>
<?php echo $this->headMeta();?>
<?php echo $this->headLink();?>
<?php echo $this->headScript();?>
<?php echo $this->headStyle();?>



</head>

<html>
<head>
    <title>Tabs Center</title>
	<link rel="stylesheet" href="../css/style.css" type="text/css" />
	<!--[if IE]>
		<style type="text/css">
			ul#tabs li a {
			padding: 0px 0px 0px 10px;
			}
		</style>
	<![endif]-->
</head>
<body>
	<div id="page-wrap">
		<div id="top-page"> 
		    <div id="wrapper">
		           <ul id="tabs">
		                <li><a><span>Application</span></a></li>
		                <li><a><span>Forum</span></a></li>
		                <li><a><span>Social</span></a></li>
<!--		                <li><a><span>Contact</span></a></li>-->						
<!--		                <li>  <input id="s" type="text" value="search"   ></td></li>-->
		                <div class="search">
							<form method="get" id="" action=" ">
								<input type="text" name="s" id="s" value="Type to search for something" />
							</form>
							<div id="cbxacount">
								<select class="select" name="price1">
									<option value=0>Account </option>
									<option value=5000>Login</option>
									<option value=10000>Logout</option>							
								</select>
							</div>   							
						</div><!--End .search-->							
		            </ul>		            
	    	 </div> <!--End .wrapper-->	    	 
	     </div><!--End .top-paper-->
	     <div id="main-content">
		     <div id="banner">
				<h1><a href="#">Izwebz.com></a></h1>
				<div id="playnow">
					<input class="button" type="submit" value="Play now">
				</div>				
				<div id="platforms">
					<select class="select" name="platforms">
						<option value=0>platforms </option>
						<option value=1>Android</option>
						<option value=2>iPhone</option>							
					</select>
				</div>
				<div id="social-platforms">
					<select class="select" name="platforms">
						<option value=0>social platforms </option>
						<option value=1>Facebook</option>
						<option value=2>yahoo</option>							
					</select>
				</div>
			</div><!--End #banner-->
			<div id="listbox">
				<div id="news">
						<h2><a href="#">News</a></h2>
						<p>
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec consequat iaculis sapien eget volutpat. Maecenas blandit pellentesque lorem vitae condimentum. Integer aliquam, est ut auctor molestie, nunc orci euismod magna, eu condimentum mi lorem at velit. Nulla non leo et tortor mattis dignissim. 
						</p>
				</div><!-- End #news -->
							
				<div id="charge">
						<h2><a href="#">Charge</a></h2>
						<p>
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec consequat iaculis sapien eget volutpat. Maecenas blandit pellentesque lorem vitae condimentum. Integer aliquam, est ut auctor molestie, nunc orci euismod magna, eu condimentum mi lorem at velit. Nulla non leo et tortor mattis dignissim. 
						</p>
				</div><!-- End #charge -->
				
				<div id="sport">
						<h2><a href="#">Sport</a></h2>
						<p>
							Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec consequat iaculis sapien eget volutpat. Maecenas blandit pellentesque lorem vitae condimentum. Integer aliquam, est ut auctor molestie, nunc orci euismod magna, eu condimentum mi lorem at velit. Nulla non leo et tortor mattis dignissim. 
						</p>
				</div><!--End #sport   --> 
			</div><!-- End listbox -->
			<div id="listboxgame">	
				 <form method="get" id="" action=" ">			 
				 	<input id="vegas" type="submit" value="">	
				 </form>			
			</div><!-- End listboxgame -->
		</div><!--End #content-->
		<div id="footer">
			<div id="footerNav">
				<p class="copyright">Copyright 20011 Holo Inc. All rights reserved.</p>
				<ul>					
					<li><a href="#">About Us</a></li>
					<li><a href="#">Privacy</a></li>
					<li><a href="#">Legas</a></li>
					<li><a href="#">Language</a></li>
				</ul>
			</div><!--End #footNav-->
<!--			<div id="footerInfo">-->
<!--				<img src="../images/author.png" alt="author" />-->
<!--				<div class="rules">-->
<!--					<div class="inside">-->
<!--						<p>-->
<!--							Hey guys! this is the place where I share most of my stuff. Sharing is always my favourite thing to do. I hope you can pick up something out of this website. I am also learning, so if there is any mistake, please leave your constructive comments. There are few rules, however, you should stick to when you are using my website.-->
<!--						</p>-->
<!--							<ol>-->
<!--							<li>Do yourself a favour. Don't be a jerk, be nice to everybody.</li>-->
<!--							<li>Read my <a href="#">Terms and Conditions</a>.</li>-->
<!--							<li>Do not copy and republish my stuff without asking.</li>-->
<!--							<li><a href="#">Email me</a> if you have any questions. I try as much as I can to answer each and every email.</li>-->
<!--							<li>Last thing, if you like my stuff and want to <a href="#">buy me a beer</a>. Go ahead.</li>-->
<!--							</ol>-->
<!--						 <p>That's it! enjoy your stay and remember to come back often.</p>-->
<!--				 	</div>End .inside-->
<!--				</div>End .rules-->
<!--			</div>End #footerInfo-->
		</div><!--End #foot-->
     </div> 
</body>
</html>
