/*
 * For error handling on UI use something like:
 * $.ajax({
 * // ...
 *  success: function (response) { ... },
 *  error: function (xhr, ajaxOptions, thrownError) {
 *  // your code goes here
 *  }
 * });
 * 
 * Sample JSON error object
 * {"error":"can't call something : /127.0.0.1:27017/test; nested exception is com.mongodb.MongoException$Network: 
 * can't call something : /127.0.0.1:27017/test"}
 */



var curDb="";
var curCollection="";
var globalUri="http://localhost:8080/services/mongo"
var currFields=new Array();
var currFirstIds=new Array();
var currLastIds=new Array();
$(document).ready(function() {


    $('#selectLimit').change(function() {
    	clearForm();
        $('#myForm').submit();
    });


    // set effect from select menu value
    $( "#button" ).click(function() {
        var options = {};
        $( "#myForm" ).toggle( "slide", options, 500 );
        return false;
    });

    $( "#rewind" ).click(function() {
        $("#formBrowseDir").val("prev");
        $( "#myForm" ).submit();

    });

    $( "#forward" ).click(function() {
        $("#formBrowseDir").val("next");
        $( "#myForm" ).submit();
    });
    $( "#searchBtn" ).click(function() {
    	clearForm();
        $( "#myForm" ).submit();
    });

var options = {
    //target:        '#tabs-2',   // target element(s) to be updated with server response
    //beforeSubmit:  showRequest,  // pre-submit callback
    //success:       showResponse,  // post-submit callback
    dataType:'json',
    success:   processJson,
    // other available options:
    //url:       url         // override for form's 'action' attribute
    //type:      type        // 'get' or 'post', override for form's 'method' attribute
    //dataType:  null        // 'xml', 'script', or 'json' (expected server response type)
    //clearForm: true        // clear all form fields after successful submit
    //resetForm: true        // reset the form after successful submit
    // $.ajax options can be used here too, for example:
    timeout:   3000
};

    $('#myForm').ajaxForm(options);

// bind to the form's submit event
/*$('#myForm').submit(function() {
    // inside event callbacks 'this' is the DOM element so we first
    // wrap it in a jQuery object and then invoke ajaxSubmit
    $(this).ajaxSubmit(options);

    // !!! Important !!!
    // always return false to prevent standard browser submit and page navigation
    return false;
});*/
    /*$('#myForm').ajaxForm({
        // dataType identifies the expected content type of the server response
        dataType:  'json',

        // success identifies the function to invoke when the server response
        // has been received
        success:   processJson
    });*/
});

function processJson(data) {
    // 'data' is the json object returned from the server

    var textToInsert = [];
    var i=0;
    var items=data.items;

    $("[id^=jsonViewTab1]").remove();
    for (var key in items) {
        var obj = items[key];
        $("#container").append('<div class="item" id="jsonViewTab1' + i + '"></div>');
        $('#jsonViewTab1' + i).jsonView(obj);
        i++;
    }

    $("#formPageNumber").val(data.pageId);
    var browse_dir=$("#formBrowseDir").val();
    if(browse_dir!="prev"){
    	currFirstIds[data.pageId]=data.firstId;
        currLastIds[data.pageId]=data.lastId;
    }
    
    if(data.firstPage){
    	$("#formPrevId").val(null);
    	$( "#rewind" ).hide();
    }else{
    	$("#formPrevId").val(currFirstIds[data.pageId-1]);
    	$( "#rewind" ).show();
    }
    
    if(data.lastPage){
    	$("#formNextId").val(null);
    	$( "#forward" ).hide();
    }else{
    	$("#formNextId").val(currLastIds[data.pageId]);
    	$( "#forward" ).show();
    }
    
    var count=data.maxSize+((data.pageId)*data.size);
    $("#countLbl").html("Records found: "+count+". Displaying page "+(data.pageId+1));
    
    

}


// pre-submit callback
function showRequest(formData, jqForm, options) {
    // formData is an array; here we use $.param to convert it to a string to display it
    // but the form plugin does this for you automatically when it submits the data
    var queryString = $.param(formData);

    // jqForm is a jQuery object encapsulating the form element.  To access the
    // DOM element for the form do this:
    // var formElement = jqForm[0];

    alert('About to submit: \n\n' + queryString);

    // here we could return false to prevent the form from being submitted;
    // returning anything other than false will allow the form submit to continue
    return true;
}

// post-submit callback
function showResponse(responseText, statusText, xhr, $form)  {
    // for normal html responses, the first argument to the success callback
    // is the XMLHttpRequest object's responseText property

    // if the ajaxSubmit method was passed an Options Object with the dataType
    // property set to 'xml' then the first argument to the success callback
    // is the XMLHttpRequest object's responseXML property

    // if the ajaxSubmit method was passed an Options Object with the dataType
    // property set to 'json' then the first argument to the success callback
    // is the json data object returned by the server
    $( "#tabs" ).tabs( "option", "active", 1 );
    alert('status: ' + statusText + '\n\nresponseText: \n' + responseText +
        '\n\nThe output div should have already been updated with the responseText.');
}

function loadCollectionForm(db,col){
    var uri=globalUri+"/collection/"+db+"/"+col+"/";
    $.getJSON(uri, function(data) {
        curDb=db;
        curCollection=col;
        var colInfo=data.collectionInfo;
        var dbObject=data.dbObject;
        var uri='<a href="'+globalUri+'/export/'+curDb+'">'+curDb+'(Click to export to FreeMind)</a>'
        $("#dbValue").html(uri);
        $("#colValue").html(curCollection);
        $("#countValue").html(colInfo.count);
        $("#dbValue1").html(curDb);
        $("#colValue1").html(curCollection);
        $('#dbData').remove();

        var checkedCount=0;
        var checked="checked";
        $("#selectFields").empty();
        $("#selectCriteria").empty();
        $("#varSort").empty();
        if (dbObject!=null) {
        	$("#selectCriteria").append($("<option value=''>Select criteria</option>") );
            $.each(dbObject, function (key, val) {
                $("#selectFields").append($("<option value="+key+">"+ key+"</option>") );
                $("#selectCriteria").append($("<option value="+key+">"+ key+"</option>") );
            })
            
        }        
       
        clearForm();
        
        $("#formCriteriaValue").val(null);

        $('#myForm').submit();

       $('#jsonViewT2').jsonView(colInfo.indexInfo);
        $( "#tabs" ).tabs();
        $( "#tabs" ).tabs( "option", "heightStyle", "fill" );
    });

    $("#tabs").show();
}

function clearForm(){
	currFirstIds=[];
    currLastIds=[];
    $("#formCurDb").val(curDb);
    $("#formCurCollection").val(curCollection);
    $("#formPageNumber").val(0);
    $("#formPrevId").val(null);
    $("#formNextId").val(null);
    $("#formBrowseDir").val(null);
    $("[id^=jsonViewTab1]").remove();
    $("#countLbl").html(null);
}

function browseCollection(db,col){
    var uri=globalUri+"/collection/"+db+"/"+col+"/";
    $( "#tabs" ).tabs("enable", 1 );
    $( "#tabs" ).tabs( "option", "active", 1 );
}

$(document).ready(function () {
    $('body').layout({ applyDefaultStyles: true });
    $("#tabs").hide();
    $(".browse-collection").hide();

});

$(function() {
    $.getJSON(''+globalUri+'/dbstruct', function(data) {
        var dbs=data.dbMap;
        $.each(dbs, function(key, val) {
            var items = [];
            $( "#accordion").append('<h3>'+key+'</h3><div class="' + key + '"></div>');
            $.each(val, function (i,col) {
                var fc="loadCollectionForm('"+key+"','"+col+"');";
                items.push('<li><a href="#" onclick="'+fc+'" class="'+col+'">'+col+'</a> </li>');
            });
            $('<ul/>', {
                'class': 'my-new-list',
                html: items.join('')
            }).appendTo('.' + key );
        });
        $( "#accordion" ).accordion();
    });


});
