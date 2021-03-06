function getOrderApiUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderList(){
	var url = getOrderApiUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);  
	   },
	   error: handleAjaxError
	});
}

function deleteOrder(id){
	var url = getOrderApiUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		getOrderList();  
	   },
	   error: handleAjaxError
	});
}


//UI DISPLAY METHODS

function displayOrderList(data){
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-outline-danger" onclick="deleteOrder(' + e.id + ')">Delete</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.datetime + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function viewOrderList(){
	if ($(this).val() == "Hide") {
      $(this).html("View");
      $(this).val("View");
      $("#order-table").hide();
   }
   else {
      $(this).html("Hide");
      $(this).val("Hide");
      $("#order-table").show();
   }
	
}

//INITIALIZATION CODE
function init(){
	$('#view-order-data').click(viewOrderList);
    $('#refresh-order-data').click(getOrderList);

}

$(document).ready(init);
$(document).ready(getOrderList);

