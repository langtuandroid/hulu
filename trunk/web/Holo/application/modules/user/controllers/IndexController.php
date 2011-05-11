<?php
class IndexController extends Zendvn_Controller_Action
{
	public function init()
	{	
		$template_path=TEMPLATE_PATH."/user/system/html";
//		$this->loadTemplate($template_path);	
		 $option = array('layoutPath'=>$template_path,'layout'=> 'header','viewSuffix'=>'php');
		Zend_Layout::startMvc($option);
	}
	public function preDispatch(){
		$auth  = Zend_Auth::getInstance(); 	
		if(!$auth->hasIdentity()){
			if($this->_request->getActionName() != 'login'){
		   		$this->_redirect('/index/login');
			}
		}				
	}
	public function indexAction()
	{
		$auth  = Zend_Auth::getInstance(); 
		$infoUser = $auth->getIdentity();
		$this->view->fullName  = $infoUser->full_name;
	}
	public function loginAction() {
		if($this->_request->isPost()){
			
			//1.Goi ket noi voi Zend Db
			$db = Zend_Registry::get('connectDB');
			//$db = Zend_Db::factory($dbOption['adapter'],$dbOption['params']);
			
			//2. Khoi tao Zend Autho
			$auth = Zend_Auth::getInstance ();
			
			//3. Khai bao bang va 2 cot se su dung so sanh trong qua tronh login
			$authAdapter = new Zend_Auth_Adapter_DbTable ( $db );
			$authAdapter->setTableName('users')
					    ->setIdentityColumn('username')
					    ->setCredentialColumn('password');

			//4. Lay gia tri duoc gui qua tu FORM
			$uname = $this->_request->getParam ( 'username' );
			$paswd = $this->_request->getParam ( 'password' );
			if($uname==null && $paswd==null){
				echo "ko hop le";
				$this->_redirect('/index/login');
			}
			//5. Dua vao so sanh voi du lieu khai bao o muc 3
			$authAdapter->setIdentity ( $uname );
			$authAdapter->setCredential ( md5 ( $paswd ) );
			
			//6. Kiem tra trang thai cua user neu status = 1 moi duoc login
			$select = $authAdapter->getDbSelect();
			$select->where('status = 1');
			
			//7. Lay ket qua truy van
			$result = $auth->authenticate ( $authAdapter );
			$flag = false;
			if($result->isValid()){
				//8. Lay nhung du lieu can thiet trong bang users neu login thanh cong				
				$data = $authAdapter->getResultRowObject (null,array('password'));			
			
				//9. Luu  nhung du lieu cua member vao session
				$auth->getStorage()->write ( $data );
				$flag = true;
				
			}
			if($flag == true){
				$this->_redirect('/index/index');
			}
			
		}
	}
	
	public function logoutAction() {
		$auth = Zend_Auth::getInstance(); 
		$auth->clearIdentity();
		
	}
	
}