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
    <div id="wrapper">
           <ul id="tabs">
                <li><a><span>Application</span></a></li>
                <li><a><span>Forum</span></a></li>
                <li><a><span>Social</span></a></li>
<!--                <li><a><span>Contact</span></a></li>-->
                <li>  <input type="text" value="search"  width="200" height="5" ></td></li>
            </ul>
        </div>  
</body>
</html>
