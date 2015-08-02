function httpGet(url)
{
    var xmlHttp = null;

    xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", url, false );
    xmlHttp.send( null );
    return xmlHttp.responseText;
}


function httpPost(url, onSuccess)
{
	var xmlHttp = null;

	xmlHttp = new XMLHttpRequest();
	xmlHttp.open("POST", url, false);
	xmlHttp.onreadystatechange=function()
	{
		if (xmlHttp.readyState == 4)
			if (xmlHttp.status == 200)
				onSuccess();
	};
	xmlHttp.send( null );
	return xmlHttp.responseText;
}


function submitEmployeeQuery(){
	var selected_index = document.getElementById('employee_list').selectedIndex;

	if(selected_index > 0){
		var value = document.getElementById('employee_list').options[selected_index].value;
		
		var schedule_request = '/employee/getScheduleByEmployee/' + value;
		document.getElementById('schedule_by_employee').innerHTML = httpGet(schedule_request);

		var qualification_request = '/employee/getQualificationByEmployee/' + value;
		document.getElementById('qualification_by_employee').innerHTML = httpGet(qualification_request);


	}
	else {
		alert('Please select an employee from the drop down list');
	}
}



function submitQualificationQuery(){
		var selected_index = document.getElementById('qualification_list').selectedIndex;
		var value = document.getElementById('qualification_list').options[selected_index].value;
		var qualification_request = '/qualifications/getQualificationReport/' + value;
		document.getElementById('qualification_report').innerHTML = httpGet(qualification_request);

}


