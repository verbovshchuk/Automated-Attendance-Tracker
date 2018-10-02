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
	<a href = "ProfLoginServlet"><button type="button" name="Student"> Student</button></a>

  <div class="form">
    <form action="ProfLoginServlet" method="post">
    		<font color = "red">Attendance already taken!</font><br>
			Professor Login<br>
			Username<input type = "password" name = "professor_id"/><br>
			Password<input type = "password" name = "password"/><br>
			Course<input type = "text" name = "section_name"/><br>
			Add key<input type = "text" name = "addKey"/><br>
			<input type = "submit" value ="take attendance">
			<input type = "reset" value = "reset">
			
	</form> 
  </div>
</div>
</body>
</html>