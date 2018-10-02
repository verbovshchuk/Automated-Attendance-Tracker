<!DOCTYPE html>
<html >
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" media="screen">
  <title>Student Login</title>
      <link rel="stylesheet" href="css/login.css"> 
      
      
</head>
<body>
<div class = "login-page">
	<a href = "LoginServlet"><button type="button" name="Professor"> Professor</button></a>

  <div class="form">
    <form action="LoginServlet" method="post">
    		<font color = "red">Invalid Login</font><br>
    
			Student Login<br>
			ID<input type = "text" name = "student_id"/><br>
			Key<input type = "text" name = "key"/><br>
			Class<input type = "text" name = "section_name"/><br>
			<input type = "submit" value ="submit">
			<input type = "reset" value = "reset">
			
	</form> 
  </div>
</div>
</body>
</html>