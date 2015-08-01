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
