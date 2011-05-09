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
							<select id="s" name="price1">
								<option value=0>Account </option>
								<option value=5000>Login</option>
								<option value=10000>Logout</option>							
							</select>   							
						</div><!--End .search-->							
		            </ul>		            
	    	 </div> <!--End .wrapper-->	    	 
	     </div><!--End .top-paper-->
	     <div id="main-content">
		     <div id="banner">
				<h1><a href="#">Izwebz.com></a></h1>
	
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
		</div><!--End #content-->
     </div> 
</body>
</html>
