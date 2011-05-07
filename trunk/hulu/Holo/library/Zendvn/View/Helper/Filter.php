<?php
class Zendvn_View_Helper_Filter extends Zend_View_Helper_Abstract
{
	public function filter()
	{
		$categoryFeature = new Default_Model_Filter();
		$name = $categoryFeature->getFeatureCategory();
		$html =
		'
		<style type="text/css">
       
        ul{list-style: none;}
        a{text-decoration: none;}
        #title:hover{
            cursor: pointer;
            background-color: #ddd;
        }
          #titleFilter{
	margin: 4px;
	padding-left: 5px;
	list-style-type: decimal;
		}
    
        
    </style>
		<table width="184" border="0" cellpadding="0" cellspacing="0" id="table">
  <tr>
    <td><img src="'.$this->view->imgUrl.'/timtheophankhuc.gif" width="184" height="30" /></td>
  </tr>
  <tr>
    <td id="infotable">
<table width="100%" border="0" cellspacing="1" cellpadding="4">';
			foreach ($name as $key=>$value)
		{
			$html.='<tr>
    <td id="leftmenu"><div id="title">'.$key.'</div>
    <div id="parent">';
			if(count($value)>1)
			{
				foreach($value as $keys=>$val)
				{
					
					$html.='<div id="titleFilter"> + <a href="'.$this->view->url(array("controller"=>"product","action"=>"filter","getId"=>$keys),null,true).'"> '.$val.'</a></div>';
				}
			}
		
  
         
        
        $html.='</div>
        </td>
  </tr>';
		}
$html.='</table>    </td>
  </tr>
</table>
<!-- End #wrapper -->
<script type="text/javascript" src="jquery.js"></script> 
    <script type="text/javascript"> // bắt đầu 1 kịch bản
        $(document).ready(function(){  //Tài liệu đã được sẵn sàng để thực thi
            $("div#parent").hide(); //Ẩn tất cả thẻ p ngoại trừ thẻ p đầu tiên<font face="Arial, Helvetica, sans-serif"></font>
            $("div#title").click(function(){  //Khi thẻ h4 được click
                $("div#parent").slideUp("fast");  // ẩn tất cả thẻ p
                if($(this).next("div#parent").is(":hidden") == true){ //tìm thẻ đứng kế thẻ vừa được click, ở đây thẻ được click là h4, thẻ đứng kế thẻ h4 là thẻ p, nếu đang ở trạng  thái ẩn... thì sẽ cho hiện ra theo hiệu ứng sổ xuống
                    $(this).next("div#parent").slideDown("fast");
                }
                
            });
        });
    </script>';
		return $html;
	}
}