<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
  <title>Login</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
</head>
<style>
  .toast {
    opacity: 1 !important;
  }
</style>

<body>
  <div class="container text-center">
    <form action="login" method="POST" id="loginForm">
      <h2 class="text-primary">Interview Creation Portal</h2>
      <div class="form-group text-left">
        <label for="username">Username:</label>
        <input type="text" class="form-control" id="username" placeholder="Enter username" name="username" autocomplete="nope">
      </div>
      <div class="form-group text-left">
        <label for="pwd">Password:</label>
        <input type="password" class="form-control" id="pwd" placeholder="Enter password" name="pwd">
      </div>
      <button type="submit" class="btn btn-success">Submit</button>
    </form>
  </div>
</body>
<script>
  if ("${Error}" != '') {
    toastr.options.closeButton = true;
    toastr.options.positionClass = 'toast-top-center';
    toastr.options.timeOut = 2000;
    toastr.error("${Error}", 'Error');
  }
  const form = document.getElementById('loginForm');
  form.addEventListener('submit', (event) => {
    if ($("#username").val() != '' && $("#pwd").val() != '') {
      form.submit();
    } else {
      if ($("#username").val() == '' && $("#pwd").val() == '') {
        toastr.clear()
        toastr.options.closeButton = true;
        toastr.options.positionClass = 'toast-top-center';
        toastr.options.timeOut = 2000;
        toastr.warning('Please enter username and password', ' Warning');
        event.preventDefault();
      } else if ($("#username").val() == '') {
        toastr.clear()
        toastr.options.closeButton = true;
        toastr.options.positionClass = 'toast-top-center';
        toastr.options.timeOut = 2000;
        toastr.warning('Please enter username', ' Warning');
        event.preventDefault();
      } else {
        toastr.clear()
        toastr.options.closeButton = true;
        toastr.options.positionClass = 'toast-top-center';
        toastr.options.timeOut = 2000;
        toastr.warning('Please enter password', ' Warning');
        event.preventDefault();
      }
    }
  });
</script>

</html>
