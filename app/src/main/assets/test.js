
var item=undefined;
var count=0;
function fillData(text){
    var obj=JSON.parse(text);
    if (obj.lat && obj.lon){
      item.innerText="lat="+obj.lat.toFixed(4)+", lon="+obj.lon.toFixed(4);
    }
}
function showError(etext){
    var elem=document.getElementById('errtext');
    if (! elem) return;
    if (! etext){
        elem.style.display='none';
    }
    else{
        elem.style.display='block';
        elem.innerText=etext;
    }
}
function update(){
    console.log("update");
    count++;
    var request=new XMLHttpRequest();
    request.open("GET","query");
    request.addEventListener('load',function(ev){
        if (request.status >= 200 && request.status <300){
            fillData(request.responseText);
            showError(undefined);
        }
        else{
            showError(request.statusText+": "+request.status);
            console.log("error in request "+request.statusText+": "+request.status)
        }
    });
    request.send();
}
function start(){
    item=document.getElementById('main');
    setInterval(update,2000);

}