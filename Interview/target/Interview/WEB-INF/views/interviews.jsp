<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>

<head>
  <title>Interviews</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.6.1/css/bootstrap.min.css" integrity="sha512-T584yQ/tdRR5QwOpfvDfVQUidzfgc2339Lc8uBDtcp/wYu80d7jwBgAxbyMh0a9YM9F8N3tdErpFI8iaGx6x5g==" crossorigin="anonymous"
    referrerpolicy="no-referrer" />
  <script src="//ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/popper.js@1.12.9/dist/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.6.1/js/bootstrap.min.js" integrity="sha512-UR25UO94eTnCVwjbXozyeVd6ZqpaAE9naiEUBK/A+QDbfSTQFhPGj5lOR6d8tsgbBk84Ggb5A3EkjsOgPRPcKA==" crossorigin="anonymous"
    referrerpolicy="no-referrer"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/1.1.1/js/bootstrap-multiselect.js" integrity="sha512-e6Nk3mhokFywlEPtnkGmop6rHh6maUqL0T65yOkbSsJ3/y9yiwb+LzFoSTJM/a4j/gKwh/y/pHiSLxE82ARhJA==" crossorigin="anonymous"
    referrerpolicy="no-referrer"></script>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-multiselect/1.1.1/css/bootstrap-multiselect.css" integrity="sha512-Lif7u83tKvHWTPxL0amT2QbJoyvma0s9ubOlHpcodxRxpZo4iIGFw/lDWbPwSjNlnas2PsTrVTTcOoaVfb4kwA=="
    crossorigin="anonymous" referrerpolicy="no-referrer" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/css/toastr.min.css">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/js/toastr.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js"></script>
  
</head>
<style>
  .toast {
    opacity: 1 !important;
  }
</style>
</head>

<body>
  <% response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
 %>
  <div class="container">
    <ul class="nav nav-tabs">
      <li class="nav-item">
        <a class="nav-link active" href="interviews">Interviews</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" style="cursor:pointer;" id="createInterviewBtn">Create Interview</a>
      </li>
      <li class="nav-item">
        <a class="nav-link " href="logout">Logout</a>
      </li>
    </ul>
    <div class="table-responsive">
      <table id="interviews" class="table table-bordered table-hover mt-5 text-center">
        <thead>
          <tr>
            <td style="display:none;">Participant Id</td>
            <td>Participant Name</td>
            <td style="display:none;">Interview Id</td>
            <td>Start Time</td>
            <td>End Time</td>
            <td>Edit Interview</td>
          </tr>
        </thead>
        <tbody id="tableBody">
        </tbody>
      </table>
    </div>
  </div>
  <div id="createModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-primary">Create Interview</h5>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="p">Participants:</label>
            <select id="p" class="form-select" multiple="multiple">
            </select>
          </div>
          <div class="form-group">
            <label for="startTime">Start Time:</label>
            <input type="datetime-local" class="form-control" id="startTime">
          </div>
          <div class="form-group">
            <label for="endTime">End Time:</label>
            <input type="datetime-local" class="form-control" id="endTime">
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" id="create" class="btn btn-success">Create</button>
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <div id="editModal" class="modal fade" role="dialog">
    <div class="modal-dialog">
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title text-primary">Edit Interview</h5>
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="p">Participants:</label>
            <select id="editP" class="form-select">
            </select>
          </div>
          <div class="form-group">
            <label for="editStartTime">Start Time:</label>
            <input type="datetime-local" class="form-control" id="editStartTime">
          </div>
          <div class="form-group">
            <label for="editEndTime">End Time:</label>
            <input type="datetime-local" class="form-control" id="editEndTime">
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" id="edit" class="btn btn-success">Edit</button>
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <script type="text/javascript">
    function getParticipants() {
      $.ajax({
        type: 'GET',
        url: "getParticipants",
        data: {},
        success: function(data) {
          if (data != "") {
            var options = "";
            var response = $.parseJSON(data);
            var arr = response.participants;
            for (var i = 0; i < arr.length; i++) {
              $("#p").append("<option value='" + arr[i].id + "'>" + arr[i].name + "<option>");
              $('#p').children().last().remove();
              $("#editP").append("<option value='" + arr[i].id + "'>" + arr[i].name + "<option>");
              $('#editP').children().last().remove();
            }
            $('#p').multiselect({
              includeSelectAllOption: true
            });
          }
        }
      });
    }

    function showInterviews() {
      $.ajax({
        type: 'GET',
        url: "getInterviews",
        data: {},
        success: function(data) {
          if (data != "") {
            var response = $.parseJSON(data);
            var tableContent = "";
            var arr = response.Interviews;
            for (var i = 0; i < arr.length; i++) {
              tableContent += "<tr><td style='display:none;'>" + arr[i].PId + "</td><td>" + arr[i].PName + "</td><td style='display:none;'>" + arr[i].IId + "</td><td>" + arr[i].IStartTime + "</td><td>" + arr[i].IEndTime + "</td><td>" +
                "<button class='btn btn-success' name='" + arr[i].IId + "' onclick='edit(this)'>Edit</button></td></tr>";
            }
            $("#tableBody").html(tableContent);
          }
        }
      });
    }
    var IId = 0;

    function edit(p) {
      $("#editModal").modal("show");
      IId = p.getAttribute("name");
      var startTime = p.parentElement.previousElementSibling.previousElementSibling.innerHTML;
      var endTime = p.parentElement.previousElementSibling.innerHTML;
      var participant = p.parentElement.parentElement.children[0].innerHTML;
      $("#editP").val(participant);
      $("#editStartTime").val(startTime.replace(/\s/g, 'T'));
      $("#editEndTime").val(endTime.replace(/\s/g, 'T'));
    }
    $(document).ready(function() {
      getParticipants();
      showInterviews();
      $("#create").click(function() {
        var Ids = $("#p").val();
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        if (Ids != null && startTime != '' && endTime != '') {
          if (Ids.toString().split(",").length < 2) {
            toastr.options.closeButton = true;
            toastr.options.positionClass = 'toast-top-center';
            toastr.warning("Please select atleast two participants", 'Warning');
            return;
          }
          bootbox.dialog({ message: '<div class="text-center"><span class="fas fa-spinner"></span> Creating...</div>',size:"small",closeButton:false });

          $.ajax({
            type: 'POST',
            url: "createInterview",
            data: {
              participantIds: Ids.toString(),
              startT: startTime,
              endT: endTime
            },
            success: function(data) {
              if (data != "") {
                $("#createModal").modal("hide");
                var response = $.parseJSON(data);
         	   bootbox.hideAll();
                if (response.hasOwnProperty("Success")) {
                  toastr.options.closeButton = true;
                  toastr.options.positionClass = 'toast-top-center';
                  toastr.success(response.Success, 'Success');
                }
                if (response.hasOwnProperty("Error")) {
                  toastr.options.size = "large";
                  toastr.options.closeButton = true;
                  toastr.options.positionClass = 'toast-top-center';
                  toastr.options.timeOut = 0;
                  toastr.error("Other interviews are scheduled for: " + response.Error, 'Error');
                }
                showInterviews();
              }
            }
          });
        } else {
          toastr.options.closeButton = true;
          toastr.options.positionClass = 'toast-top-center';
          toastr.options.timeOut = 2000;
          toastr.warning('Please fill all details', ' Warning');
        }
      });
      $("#createInterviewBtn").click(function() {
        $("#createModal").modal("show");
        $("#startTime").val("");
        $("#endTime").val("");
      });
      $("#edit").click(function() {
        var Id = $("#editP").val();
        var startTime = $("#editStartTime").val();
        var endTime = $("#editEndTime").val();
        if (Id != '' && startTime != '' && endTime != '') {
          $.ajax({
            type: 'POST',
            url: "editInterview",
            data: {
              InterviewId: IId,
              participantId: Id,
              startT: startTime,
              endT: endTime
            },
            success: function(data) {
              if (data != "") {
                $("#editModal").modal("hide");
                var response = $.parseJSON(data);
                if (response.hasOwnProperty("Success")) {
                  toastr.options.closeButton = true;
                  toastr.options.positionClass = 'toast-top-center';
                  toastr.success(response.Success, 'Success');
                }
                if (response.hasOwnProperty("Error")) {
                  toastr.options.size = "large";
                  toastr.options.closeButton = true;
                  toastr.options.positionClass = 'toast-top-center';
                  toastr.options.timeOut = 0;
                  toastr.error(response.Error + " has another interview in this time", 'Error');
                }
                showInterviews();
              }
            }
          });
        } else {
          toastr.options.closeButton = true;
          toastr.options.positionClass = 'toast-top-center';
          toastr.options.timeOut = 2000;
          toastr.warning('Please fill all details', ' Warning');
        }
      });
    });
  </script>
</body>

</html>
